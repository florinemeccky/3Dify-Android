package com.example.a3dify.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.R;

/*
 * ContactActivity
 * Provides three contact methods using Android system intents:
 *
 *   1. Phone call intent  — dials the support number
 *   2. SMS intent         — opens the messaging app with a pre-filled number
 *   3. Email intent       — opens the email app with support address pre-filled
 *
 * All three satisfy the lecturer's SMS and Phone Call mobile feature requirements.
 */
public class ContactActivity extends AppCompatActivity {

    // Replace these with your real support contact details
    private static final String SUPPORT_PHONE = "+255614886685";
    private static final String SUPPORT_EMAIL = "florinemeccky@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        TextView tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(v -> finish());

        setupPhoneCall();
        setupSMS();
        setupEmail();
    }

    /*
     * Phone call intent.
     * Opens the dialer with the support number pre-filled.
     * The user confirms by tapping the call button themselves.
     * Requires CALL_PHONE permission in AndroidManifest.xml.
     */
    private void setupPhoneCall() {
        LinearLayout btnCall = findViewById(R.id.btn_call);
        if (btnCall == null) return;

        btnCall.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + SUPPORT_PHONE));

            if (callIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "No phone app found on this device",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * SMS intent.
     * Opens the default messaging app with the support number pre-filled.
     * The user types and sends the message themselves.
     * Requires SEND_SMS permission in AndroidManifest.xml.
     */
    private void setupSMS() {
        LinearLayout btnSms = findViewById(R.id.btn_sms);
        if (btnSms == null) return;

        btnSms.setOnClickListener(v -> {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
            smsIntent.setData(Uri.parse("smsto:" + SUPPORT_PHONE));
            smsIntent.putExtra("sms_body", "Hello 3Dify Support, I need help with: ");

            if (smsIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(smsIntent);
            } else {
                Toast.makeText(this, "No messaging app found on this device",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * Email intent.
     * Opens the default email app with the support address, subject,
     * and a body template pre-filled.
     */
    private void setupEmail() {
        LinearLayout btnEmail = findViewById(R.id.btn_email);
        if (btnEmail == null) return;

        btnEmail.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + SUPPORT_EMAIL));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "3Dify Support Request");
            emailIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hello 3Dify Support,\n\nI need help with:\n\n[Describe your issue here]");

            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(emailIntent);
            } else {
                Toast.makeText(this, "No email app found on this device",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}