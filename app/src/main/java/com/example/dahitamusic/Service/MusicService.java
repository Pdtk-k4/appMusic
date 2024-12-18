package com.example.dahitamusic.Service;

import static com.example.dahitamusic.MyApplication.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.dahitamusic.Activity.PlayMusicActivity;
import com.example.dahitamusic.Fragment.DiaNhacFragment;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Podcast;
import com.example.dahitamusic.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SeekParameters;
import com.google.common.collect.BiMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service {

    public static final String ACTION_PLAY_PAUSE = "com.example.dahitamusic.ACTION_PLAY_PAUSE";
    public static final String ACTION_NEXT = "com.example.dahitamusic.ACTION_NEXT";
    public static final String ACTION_PREVIOUS = "com.example.dahitamusic.ACTION_PREVIOUS";
    public static final String ACTION_SEEK = "com.example.dahitamusic.ACTION_SEEK";
    public static final String ACTION_REPEAT = "com.example.dahitamusic.ACTION_REPEAT";
    public static final String ACTION_SHUFFLE = "com.example.dahitamusic.ACTION_SHUFFLE";

    private ExoPlayer exoPlayer;
    private boolean repeat = false;
    private boolean shuffle = false;
    private String songUrl;
    private int position = 0;

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();

        // Tạo ExoPlayer khi service khởi tạo
        exoPlayer = new ExoPlayer.Builder(this).build();

        // Tạo NotificationChannel nếu thiết bị đang chạy Android 8.0 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "music_channel", "Music Service", NotificationManager.IMPORTANCE_LOW);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (intent.hasExtra("songUrl")) {
                songUrl = intent.getStringExtra("songUrl");
                if (songUrl != null){
                    playMusic(songUrl);
                    senNotification();
                }
            } else if (ACTION_PLAY_PAUSE.equals(action)) {
                togglePlayPause();
            } else if (ACTION_NEXT.equals(action)) {
                playNext();
            } else if (ACTION_PREVIOUS.equals(action)) {
                playPrevious();
            } else if (ACTION_SEEK.equals(action)) {
                int seekPosition = intent.getIntExtra("seek_position", 0);
                seekTo(seekPosition);
            } else if (ACTION_REPEAT.equals(action)) {
                toggleRepeat();
            } else if (ACTION_SHUFFLE.equals(action)) {
                toggleShuffle();
            }
        }
        return START_STICKY;
    }

    @SuppressLint("ForegroundServiceType")
    private void senNotification() {
        // Hiển thị notification khi nhạc đang phát
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Music Player")
                .setContentText("Playing music")
                .setSmallIcon(R.drawable.play2_button_icon)
                .build();

        startForeground(1, notification); // Chạy service ở chế độ foreground
    }

    private void playMusic(String url) {
        MediaItem mediaItem = MediaItem.fromUri(url);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    sendPlaybackStateUpdate(exoPlayer.isPlaying(), exoPlayer.getCurrentPosition(), exoPlayer.getDuration());
                    startTimeUpdate();
                } else if (state == Player.STATE_ENDED) {
                        if(shuffle){
                            if(!PlayMusicActivity.mangbaihat.isEmpty()){
                                position = new Random().nextInt(PlayMusicActivity.mangbaihat.size());
                                playMusic(PlayMusicActivity.mangbaihat.get(position).getLinkNhac());
                            }else if(!PlayMusicActivity.mangpodcast.isEmpty()){
                                position = new Random().nextInt(PlayMusicActivity.mangpodcast.size());
                                playMusic(PlayMusicActivity.mangpodcast.get(position).getLinkPodcast());
                            }

                        }else {
                            if(!PlayMusicActivity.mangbaihat.isEmpty()){
                                position++;
                                if(position >= PlayMusicActivity.mangbaihat.size()){
                                    position = 0;
                                }
                                playMusic(PlayMusicActivity.mangbaihat.get(position).getLinkNhac());
                            }else if(!PlayMusicActivity.mangpodcast.isEmpty()){
                                position++;
                                if(position >= PlayMusicActivity.mangpodcast.size()){
                                    position = 0;
                                }
                                playMusic(PlayMusicActivity.mangpodcast.get(position).getLinkPodcast());
                            }
                        }
                        sendPlaybackStateUpdate(exoPlayer.isPlaying(), exoPlayer.getCurrentPosition(), exoPlayer.getDuration());
                    }
                }

        });
    }

    // Gửi broadcast cập nhật trạng thái phát nhạc
    private void sendPlaybackStateUpdate(boolean isPlaying, long currentPosition, long duration) {
        Intent intent = new Intent("MUSIC_UPDATE");

        // Truyền trạng thái phát nhạc và thời gian
        intent.putExtra("isPlaying", isPlaying);
        intent.putExtra("currentPosition", currentPosition);
        intent.putExtra("duration", duration);
        intent.putExtra("shuffle", shuffle);
        intent.putExtra("repeat", repeat);
        intent.putExtra("position", position);

        // Truyền thông tin bài hát (tên, nghệ sĩ, và URL)
        if (position > 0 && position < PlayMusicActivity.mangbaihat.size()) {
            BaiHat baiHat = PlayMusicActivity.mangbaihat.get(position);
            intent.putExtra("songTitle", baiHat.getTenBaiHat());
            intent.putExtra("songArtist", baiHat.getCaSi());
            intent.putExtra("songID", baiHat.getIdBaiHat());
            intent.putExtra("songImg", baiHat.getAnhBaiHat());
            intent.putExtra("songLyrics", baiHat.getLyrics());

        }

        if (position > 0 && position < PlayMusicActivity.mangpodcast.size()) {
            Podcast podcast = PlayMusicActivity.mangpodcast.get(position);
            intent.putExtra("songPodcast", podcast.getTenPodcast());
            intent.putExtra("songArtistPodcast", podcast.getTacGia());
            intent.putExtra("songIDPodcast", podcast.getIdPodcast());
            intent.putExtra("songImgPodcast", podcast.getAnhPodcast());
//            intent.putExtra("songLyrics", podcast.getNoiDung());
        }
        sendBroadcast(intent);
    }

    private void togglePlayPause() {
        if (exoPlayer.isPlaying()) {
            exoPlayer.pause();
            sendPlaybackStateUpdate(false, exoPlayer.getCurrentPosition(), exoPlayer.getDuration());
            stopTimeUpdate();
        } else {
            exoPlayer.play();
            sendPlaybackStateUpdate(true, exoPlayer.getCurrentPosition(), exoPlayer.getDuration());
            startTimeUpdate();
        }
    }

    private void playNext() {
        exoPlayer.seekToNext();

    }

    private void playPrevious() {
        exoPlayer.seekToPrevious();
    }

    private void seekTo(int position) {
        exoPlayer.seekTo(position);
        startTimeUpdate();
        sendPlaybackStateUpdate(exoPlayer.isPlaying(), exoPlayer.getCurrentPosition(), exoPlayer.getDuration());
    }


    private void toggleShuffle() {
        if (!shuffle) {
            shuffle = true;
            repeat = false;
            exoPlayer.setShuffleModeEnabled(true);
        } else {
            shuffle = false;
            exoPlayer.setShuffleModeEnabled(false);
        }
        sendPlaybackStateUpdate(exoPlayer.isPlaying(), exoPlayer.getCurrentPosition(), exoPlayer.getDuration());
    }

    private void toggleRepeat() {
        if (!repeat) {
            repeat = true;
            shuffle = false;
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE); // Repeat 1 bài
        } else {
            repeat = false;
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);
        }
        Log.d("Repeat", "Repeat: " + repeat);
        sendPlaybackStateUpdate(exoPlayer.isPlaying(), exoPlayer.getCurrentPosition(), exoPlayer.getDuration());
    }

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable timeUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (exoPlayer != null && exoPlayer.isPlaying()) {
                long currentPosition = exoPlayer.getCurrentPosition();
                long duration = exoPlayer.getDuration();

                sendPlaybackStateUpdate(true, currentPosition, duration);

                // Lặp lại mỗi giây để cập nhật thời gian
                handler.postDelayed(this, 500);
            }
        }
    };

    private void startTimeUpdate() {
        handler.postDelayed(timeUpdateRunnable, 0); // Bắt đầu cập nhật thời gian ngay lập tức
    }

    private void stopTimeUpdate() {
        handler.removeCallbacks(timeUpdateRunnable); // Dừng việc cập nhật thời gian
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }
}
