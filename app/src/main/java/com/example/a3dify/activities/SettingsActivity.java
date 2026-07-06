package com.example.a3dify.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import com.example.a3dify.DatabaseHelper;
import com.example.a3dify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
 * SettingsActivity
 * Manages three toggles:
 *   1. Dark / Light mode
 *   2. Daily reminder notification
 *   3. Bluetooth (opens system settings)
 *
 * KEY FIX: Listeners are attached AFTER setChecked() so that
 * loading the saved value does not trigger a save overwrite.
 * All values persist in SQLite via DatabaseHelper.
 */
public class SettingsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private String uid = "default";

    // Hold references so we can set values before attaching listeners
    private SwitchCompat switchDark, switchNotif, switchBt;
    private TextView tvBtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = DatabaseHelper.getInstance(this);

        // Resolve UID before anything reads from SQLite
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) uid = user.getUid();

        // Back arrow
        TextView tvBack = findViewById(R.id.tv_back);
        if (tvBack != null) tvBack.setOnClickListener(v -> finish());

        // Get all switch references
        switchDark  = findViewById(R.id.switch_dark_mode);
        switchNotif = findViewById(R.id.switch_notifications);
        switchBt    = findViewById(R.id.switch_bluetooth);
        tvBtStatus  = findViewById(R.id.tv_bluetooth_status);

        // ── STEP 1: Set all values from SQLite FIRST, no listeners yet ──
        loadSavedValues();

        // ── STEP 2: Attach listeners AFTER values are loaded ──
        attachListeners();
    }

    /*
     * Reads all saved settings from SQLite and sets each switch visually.
     * No listeners are active yet so setChecked() does not trigger any saves.
     */
    private void loadSavedValues() {
        // Dark mode
        if (switchDark != null) {
            boolean darkOn = db.getSetting(uid, "dark_mode", "true").equals("true");
            switchDark.setChecked(darkOn);
        }

        // Notifications
        if (switchNotif != null) {
            boolean notifOn = db.getSetting(uid, "notifications", "true").equals("true");
            switchNotif.setChecked(notifOn);
        }

        // Bluetooth — read real device state, not SQLite
        if (switchBt != null) {
            BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
            if (bt == null) {
                switchBt.setEnabled(false);
                if (tvBtStatus != null) tvBtStatus.setText("Bluetooth not supported");
            } else {
                boolean btOn = bt.isEnabled();
                switchBt.setChecked(btOn);
                if (tvBtStatus != null) {
                    tvBtStatus.setText(btOn ? "Bluetooth is on" : "Bluetooth is off");
                }
            }
        }
    }

    /*
     * Attaches all listeners after initial values have been set.
     * This is the key fix — setChecked() above did not fire these
     * because they were not attached yet.
     */
    private void attachListeners() {
        attachDarkModeListener();
        attachNotificationListener();
        attachBluetoothListener();
    }

    private void attachDarkModeListener() {
        if (switchDark == null) return;

        switchDark.setOnCheckedChangeListener((btn, isChecked) -> {
            // Save to SQLite (for the settings screen to read next time)
            db.saveSetting(uid, "dark_mode", isChecked ? "true" : "false");

            // Also save to SharedPreferences (for ThreeDifyApp to read on next launch)
            getSharedPreferences("theme_prefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("dark_mode", isChecked)
                    .apply();

            // Apply globally right now
            AppCompatDelegate.setDefaultNightMode(
                    isChecked
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );

            // Recreate this activity to redraw with new theme
            new android.os.Handler(android.os.Looper.getMainLooper())
                    .postDelayed(this::recreate, 250);
        });
    }

    private void attachNotificationListener() {
        if (switchNotif == null) return;

        switchNotif.setOnCheckedChangeListener((btn, isChecked) -> {
            // Save to SQLite
            db.saveSetting(uid, "notifications", isChecked ? "true" : "false");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager nm =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (nm != null) {
                    if (isChecked) {
                        NotificationChannel ch = new NotificationChannel(
                                "daily_reminder",
                                "Daily Reminder",
                                NotificationManager.IMPORTANCE_DEFAULT
                        );
                        ch.setDescription("Daily 3D learning reminder");
                        nm.createNotificationChannel(ch);
                    } else {
                        nm.deleteNotificationChannel("daily_reminder");
                    }
                }
            }

            Toast.makeText(this,
                    isChecked ? "Notifications enabled" : "Notifications disabled",
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void attachBluetoothListener() {
        if (switchBt == null) return;

        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        if (bt == null) return;

        switchBt.setOnCheckedChangeListener((btn, isChecked) -> {
            // Open system Bluetooth settings — safe on all Android versions
            startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));

            // Reset switch to actual device state after returning
            switchBt.post(() -> {
                switchBt.setChecked(bt.isEnabled());
                if (tvBtStatus != null) {
                    tvBtStatus.setText(bt.isEnabled() ? "Bluetooth is on" : "Bluetooth is off");
                }
            });
        });
    }
}