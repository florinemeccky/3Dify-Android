package com.example.a3dify.activities;

import android.os.Bundle;
import android.widget.TextView;
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

        TextView tvBack = findViewById(R.id.tv_back);
        if (tvBack != null) tvBack.setOnClickListener(v -> finish());
    }
}
