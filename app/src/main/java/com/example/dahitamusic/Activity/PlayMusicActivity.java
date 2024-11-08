package com.example.dahitamusic.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dahitamusic.Adapter.ViewPagerPlaySongAdapter;
import com.example.dahitamusic.Fragment.DiaNhacFragment;
import com.example.dahitamusic.Fragment.PlayListSongFragment;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.ActivityPlayMusicBinding;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayMusicActivity extends AppCompatActivity {

    ActivityPlayMusicBinding binding;
    public static ArrayList<BaiHat> mangbaihat = new ArrayList<>();
    public static ViewPagerPlaySongAdapter viewPagerPlaySongAdapter;
    DiaNhacFragment diaNhacFragment;
    PlayListSongFragment playListSongFragment;
    ExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPlayMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Cấu hình padding cho các hệ thống thanh trạng thái và thanh điều hướng
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Cấu hình StrictMode để cho phép tải nội dung trong background
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getDataIntent();
        init();

        eventClick();
    }

    private void init() {
        setSupportActionBar(binding.toolbarplaynhac);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbarplaynhac.setTitleTextColor(Color.WHITE);
        binding.toolbarplaynhac.getNavigationIcon().setTint(Color.WHITE);
        binding.toolbarplaynhac.setNavigationOnClickListener(v -> finish());

        List<Fragment> fragments = new ArrayList<>();
        playListSongFragment = new PlayListSongFragment();
        diaNhacFragment = new DiaNhacFragment();

        fragments.add(playListSongFragment);
        fragments.add(diaNhacFragment);
        viewPagerPlaySongAdapter = new ViewPagerPlaySongAdapter(PlayMusicActivity.this, fragments);

        binding.viewpagerplaynhac.setAdapter(viewPagerPlaySongAdapter);
//        binding.viewpagerplaynhac.setCurrentItem(1, false);

        // Đăng ký callback cho ViewPager để đảm bảo diaNhacFragment sẵn sàng trước khi set ảnh
        binding.viewpagerplaynhac.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 1 && mangbaihat.size() > 0) {
                    BaiHat baiHat = mangbaihat.get(0);
                    Log.d("PlayMusicActivity", "Sending Image URL: " + baiHat.getAnhBaiHat());
                    Log.d("PlayMusicActivity", "Sending Image URL: " + baiHat.getTenBaiHat());
                    Log.d("PlayMusicActivity", "Sending Image URL: " + baiHat.getCaSi());

                    // Đảm bảo fragment đã sẵn sàng
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (diaNhacFragment.isAdded()) {
                            diaNhacFragment.getAnhBaiHat(baiHat.getAnhBaiHat());
                            diaNhacFragment.setTenBaiHat(baiHat.getTenBaiHat());
                            diaNhacFragment.setTenCaSi(baiHat.getCaSi());
                        } else {
                            Log.e("PlayMusicActivity", "DiaNhacFragment not attached yet");
                        }
                    });
                }
            }
        });

        if (mangbaihat.size() > 0) {
            BaiHat baiHat = mangbaihat.get(0);
            getSupportActionBar().setTitle(baiHat.getTenBaiHat());
            initializePlayer(baiHat.getLinkNhac());
            binding.imgbtncircledplay.setImageResource(R.drawable.play2_button_icon);
        }
    }

    private void eventClick() {

        // Xử lý sự kiện play/pause
        binding.imgbtncircledplay.setOnClickListener(v -> {
            if (exoPlayer != null) {
                if (exoPlayer.isPlaying()) {
                    exoPlayer.pause();
                    binding.imgbtncircledplay.setImageResource(R.drawable.play_button_icon);
                    if (diaNhacFragment != null) {
                        diaNhacFragment.pauseAnimation(); // Dừng xoay ảnh khi pause
                    }
                } else {
                    exoPlayer.play();
                    binding.imgbtncircledplay.setImageResource(R.drawable.play2_button_icon);
                    if (diaNhacFragment != null) {
                        diaNhacFragment.resumeAnimation(); // Bắt đầu xoay ảnh khi play
                    }
                }
            }
        });
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        mangbaihat.clear();
        if (intent.hasExtra("BaiHat")) {
            BaiHat baiHat = intent.getParcelableExtra("BaiHat");
            mangbaihat.add(baiHat);
        }
    }


    // Khởi tạo ExoPlayer và phát bài hát
    private void initializePlayer(String songUrl) {
        exoPlayer = new ExoPlayer.Builder(this).build();

        MediaItem mediaItem = MediaItem.fromUri(songUrl);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_ENDED) {
                    // Xử lý khi bài hát kết thúc
                    exoPlayer.seekTo(0);
                    exoPlayer.pause();
                    binding.imgbtncircledplay.setImageResource(R.drawable.play_button_icon);
                    if (diaNhacFragment != null) {
                        diaNhacFragment.pauseAnimation(); // Dừng xoay ảnh khi pause
                    }
                }
            }
        });
        timeSong();
    }

    private void timeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        binding.txtTimesong1.setText(simpleDateFormat.format(exoPlayer.getCurrentPosition()));
        binding.seekbarsong.setProgress((int) exoPlayer.getCurrentPosition());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release(); // Giải phóng tài nguyên ExoPlayer
        }
    }
}
