package com.huangyuanlove.adaptationhighversion;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {


    EditText notificationTitle;
    EditText notificationContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationContent = findViewById(R.id.edit_content);
        notificationTitle = findViewById(R.id.edit_title);
        findViewById(R.id.send_notification).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_notification:

                sendNotification();

                break;
            default:
                break;
        }
    }

    private void sendNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "take_photo";
            String channelName = "拍照选择图片";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationManager
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
            if (channel == null) {
                channel = new NotificationChannel(channelId, channelName, importance);
            }
            notificationManager.createNotificationChannel(channel);
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                startActivity(intent);
                Toast.makeText(this, "请手动将通知打开", Toast.LENGTH_SHORT).show();
            } else {
                Notification nf = new Notification.Builder(this, channelId)
                        .setContentText("setContentText")
                        .setSettingsText("setSettingsText")
                        .setContentTitle("setContentTitle")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .build();
                notificationManager.notify(1, nf);
            }
        }
    }
}
