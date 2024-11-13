package com.example.topcvrecruiter.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.topcvrecruiter.R;

public class NotificationUtils {

    private static final String CHANNEL_ID = "article_notifications";
    private static final String CHANNEL_NAME = "Article Notifications";
    private static final int NOTIFICATION_ID = 1;

    // Hàm hiển thị thông báo
    public static void showNotification(Context context, String message) {
        // Tạo NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Kiểm tra phiên bản Android, vì NotificationChannel yêu cầu Android 8.0 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tạo hoặc lấy NotificationChannel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_success)  // Icon cho notification
                .setContentTitle("HEYYY")
                .setContentText(message)  // Nội dung thông báo
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);  // Tự động hủy thông báo khi người dùng nhấn vào

        // Hiển thị notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
