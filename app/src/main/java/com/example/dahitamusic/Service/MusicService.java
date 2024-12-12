package com.example.dahitamusic.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.dahitamusic.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

public class MusicService extends Service {
    private static final String CHANNEL_ID = "MusicServiceChannel";
    private ExoPlayer exoPlayer;

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(1, createNotification("Playing music..."));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if ("ACTION_PLAY".equals(action)) {
                if (exoPlayer != null && !exoPlayer.isPlaying()) {
                    exoPlayer.play();
                }
            } else if ("ACTION_PAUSE".equals(action)) {
                if (exoPlayer != null && exoPlayer.isPlaying()) {
                    exoPlayer.pause();
                }
            } else if (intent.hasExtra("songUrl")) {
                String songUrl = intent.getStringExtra("songUrl");
                playMusic(songUrl);
            }
        }
        return START_STICKY;
    }


    private void playMusic(String songUrl) {
        if (exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(this).build();
        }
        exoPlayer.setMediaItem(MediaItem.fromUri(songUrl));
        exoPlayer.prepare();
        exoPlayer.play();
    }

    private Notification createNotification(String text) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Music Player")
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_back_white)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
