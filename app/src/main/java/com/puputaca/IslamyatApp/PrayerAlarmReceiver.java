// PrayerAlarmReceiver.java
package com.puputaca.IslamyatApp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class PrayerAlarmReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_CHANNEL_ID = "prayer_time_channel";
    public static final String NOTIFICATION_CHANNEL_NAME = "Waktu Sholat";
    private static final String TAG = "PrayerAlarmReceiver";
    private static MediaPlayer mediaPlayer; // Menggunakan static agar bisa dikontrol

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Alarm diterima untuk waktu sholat!");

        String prayerName = intent.getStringExtra("PRAYER_NAME");
        String prayerTime = intent.getStringExtra("PRAYER_TIME");

        // Tampilkan Notifikasi
        showPrayerNotification(context, prayerName, prayerTime);

        // Putar suara Adzan
        playAdzanSound(context);
    }

    private void showPrayerNotification(Context context, String prayerName, String prayerTime) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Buat Saluran Notifikasi untuk Android O (API 26) dan di atasnya
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH // Penting untuk notifikasi yang menonjol
            );
            channel.setDescription("Notifikasi untuk waktu sholat");
            // Setel suara default untuk saluran (opsional, bisa ditimpa oleh builder)
            Uri defaultSoundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.adzan);
            channel.setSound(defaultSoundUri, null); // null untuk atribut audio default
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(context, waktusholat.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE // Tambahkan FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Ganti dengan ikon aplikasi Anda
                .setContentTitle("Waktu Sholat: " + prayerName)
                .setContentText("Sudah waktunya sholat " + prayerName + " pada jam " + prayerTime)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true) // Notifikasi akan hilang saat diketuk
                .setCategory(NotificationCompat.CATEGORY_REMINDER);

        // Setel suara Adzan untuk notifikasi ini
        Uri adzanUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.adzan);
        builder.setSound(adzanUri);

        // Atur Getaran
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}); // Getar 1 detik, jeda 1 detik, dst.

        notificationManager.notify((int) System.currentTimeMillis(), builder.build()); // Gunakan ID unik
    }

    private void playAdzanSound(Context context) {
        // Hentikan suara Adzan yang sedang berjalan jika ada
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.adzan);
            if (mediaPlayer != null) {
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.release(); // Lepaskan media player saat suara selesai
                    mediaPlayer = null; // Setel kembali ke null
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Gagal memutar suara Adzan: " + e.getMessage());
        }
    }
}