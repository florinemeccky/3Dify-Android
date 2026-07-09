package com.example.a3dify.activities;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a3dify.R;

/*
 * NotificationsActivity
 * Displays recent app notifications.
 * Currently shows a static list of in-app notifications.
 * In a future version these would be fetched from Firebase.
 */
public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ImageView ivBack = findViewById(R.id.iv_back);
        if (ivBack != null) ivBack.setOnClickListener(v -> finish());
    }
}
