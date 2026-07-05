package com.example.a3dify.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
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
 * Controls:
 *   - Dark / Light mode toggle (AppCompatDelegate)
 *   - Daily reminder notification channel toggle
 *   - Bluetooth toggle (satisfies lecturer mobile feature requirement)
 *
 * All settings are persisted in SQLite via DatabaseHelper.saveSetting().
 */
public class SettingsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private String uid = "guest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = DatabaseHelper.getInstance(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) uid = user.getUid();

        TextView tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(v -> finish());

        setupDarkModeSwitch();
        setupNotificationSwitch();
        setupBluetoothSwitch();
    }

    /*
     * Dark mode toggle.
     * Reads saved preference from SQLite.
     * On toggle, calls AppCompatDelegate to switch theme immediately.
     * Saves new value to SQLite.
     */
    private void setupDarkModeSwitch() {
        SwitchCompat switchDark = findViewById(R.id.switch_dark_mode);
        if (switchDark == null) return;

        // Load saved preference
        String saved = db.getSetting(uid, "dark_mode", "true");
        switchDark.setChecked(saved.equals("true"));

        switchDark.setOnCheckedChangeListener((btn, isChecked) -> {
            db.saveSetting(uid, "dark_mode", isChecked ? "true" : "false");
            AppCompatDelegate.setDefaultNightMode(
                    isChecked
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );
        });
    }

    /*
     * Notification toggle.
     * Creates or deletes a notification channel for daily reminders.
     * Notification channels are required on Android 8.0+ (API 26+).
     */
    private void setupNotificationSwitch() {
        SwitchCompat switchNotif = findViewById(R.id.switch_notifications);
        if (switchNotif == null) return;

        String saved = db.getSetting(uid, "notifications", "true");
        switchNotif.setChecked(saved.equals("true"));

        switchNotif.setOnCheckedChangeListener((btn, isChecked) -> {
            db.saveSetting(uid, "notifications", isChecked ? "true" : "false");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager nm =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (isChecked) {
                    // Create the daily reminder notification channel
                    NotificationChannel channel = new NotificationChannel(
                            "daily_reminder",
                            "Daily Reminder",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
                    channel.setDescription("Reminds you to practice 3D modeling daily");
                    nm.createNotificationChannel(channel);
                    Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show();
                } else {
                    // Delete the channel to disable notifications
                    nm.deleteNotificationChannel("daily_reminder");
                    Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
     * Bluetooth toggle.
     * Reads the current Bluetooth adapter state.
     * Enables or disables Bluetooth using BluetoothAdapter.
     * This satisfies the lecturer's Bluetooth mobile feature requirement.
     *
     * Note: On Android 12+ (API 31+) enabling Bluetooth programmatically
     * requires the BLUETOOTH_CONNECT permission and user confirmation.
     * We use the deprecated enable() method which still works on older devices.
     */
    private void setupBluetoothSwitch() {
        SwitchCompat switchBt    = findViewById(R.id.switch_bluetooth);
        TextView     tvBtStatus  = findViewById(R.id.tv_bluetooth_status);
        if (switchBt == null) return;

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (btAdapter == null) {
            // Device does not support Bluetooth
            switchBt.setEnabled(false);
            if (tvBtStatus != null) tvBtStatus.setText("Bluetooth not supported on this device");
            return;
        }

        // Show current Bluetooth state
        boolean btOn = btAdapter.isEnabled();
        switchBt.setChecked(btOn);
        if (tvBtStatus != null) {
            tvBtStatus.setText(btOn ? "Bluetooth is on" : "Bluetooth is off");
        }

        switchBt.setOnCheckedChangeListener((btn, isChecked) -> {
            if (isChecked) {
                if (!btAdapter.isEnabled()) {
                    btAdapter.enable(); // deprecated but works for demonstration
                    if (tvBtStatus != null) tvBtStatus.setText("Bluetooth is on");
                    Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (btAdapter.isEnabled()) {
                    btAdapter.disable(); // deprecated but works for demonstration
                    if (tvBtStatus != null) tvBtStatus.setText("Bluetooth is off");
                    Toast.makeText(this, "Bluetooth disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}