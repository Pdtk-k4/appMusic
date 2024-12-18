package com.example.dahitamusic.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dahitamusic.Adapter.ViewPagerPlaySongAdapter;
import com.example.dahitamusic.Fragment.DiaNhacFragment;
import com.example.dahitamusic.Fragment.LyricsSongFragment;
import com.example.dahitamusic.Fragment.PlayListSongFragment;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Podcast;
import com.example.dahitamusic.R;
import com.example.dahitamusic.Service.MusicService;
import com.example.dahitamusic.databinding.ActivityPlayMusicBinding;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayMusicActivity extends AppCompatActivity {

    private ActivityPlayMusicBinding binding;
    public static ArrayList<BaiHat> mangbaihat = new ArrayList<>();
    public static ArrayList<Podcast> mangpodcast = new ArrayList<>();
    public static ViewPagerPlaySongAdapter viewPagerPlaySongAdapter;
    private DiaNhacFragment diaNhacFragment;
    private LyricsSongFragment lyricsSongFragment;
    int position = 0;
    private final BroadcastReceiver playbackStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;  // Kiểm tra null

            try {
                // Lấy các giá trị từ intent và kiểm tra null
                boolean isPlaying = intent.getBooleanExtra("isPlaying", false);
                long currentPosition = intent.getLongExtra("currentPosition", -1); // Sử dụng giá trị mặc định là -1
                long duration = intent.getLongExtra("duration", -1);
                boolean isShuffle = intent.getBooleanExtra("shuffle", false);
                boolean isRepeat = intent.getBooleanExtra("repeat", false);

                //bai hat
                String songTitle = intent.getStringExtra("songTitle");
                String songArtist = intent.getStringExtra("songArtist");
                String songID = intent.getStringExtra("songID");
                String songImg = intent.getStringExtra("songImg");
                String songLyrics = intent.getStringExtra("songLyrics");

                //podcast
                String songIDPodcast = intent.getStringExtra("songIDPodcast");
                String songPodcast = intent.getStringExtra("songPodcast");
                String songArtistPodcast = intent.getStringExtra("songArtistPodcast");
                String songImgPodcast = intent.getStringExtra("songImgPodcast");
//                String songLyricsPodcast = intent.getStringExtra("songLyricsPodcast");

//                updateSongInfoPodcast(songPodcast, songArtistPodcast, songIDPodcast, songImgPodcast);


                // Kiểm tra null cho các giá trị quan trọng trước khi sử dụng
                if (songTitle != null && songArtist != null) {
                    updateSongInfoBaiHat(songTitle, songArtist, songID, songImg, songLyrics);
                }

                if (currentPosition >= 0 && duration >= 0) {
                    updateSeekBarAndTime(currentPosition, duration);
                }

                // Cập nhật nút phát/tạm dừng
                updatePlayPauseButton(isPlaying);

                // Cập nhật nút Shuffle và Repeat
                updateShuffleButtonUI(isShuffle);
                updateRepeatButtonUI(isRepeat);
            } catch (Exception e) {
                e.printStackTrace(); // Ghi log nếu có lỗi
            }
        }
    };

//    private void updateSongInfoPodcast(String songPodcast, String songArtistPodcast, String songIDPodcast, String songImgPodcast) {
//        binding.toolbarTitle.setText(songPodcast);
//        diaNhacFragment.setTenCaSi(songArtistPodcast);
//        diaNhacFragment.setTenBaiHat(songPodcast);
//        diaNhacFragment.getAnhBaiHat(songImgPodcast);
//        diaNhacFragment.clickIconPodcast(songIDPodcast);
//    }


    private void updateSongInfoBaiHat(String songTitle, String songArtist, String songUrl, String songImg, String songLyrics) {
        binding.toolbarTitle.setText(songTitle);
        diaNhacFragment.setTenCaSi(songArtist);
        diaNhacFragment.setTenBaiHat(songTitle);
        diaNhacFragment.getAnhBaiHat(songImg);
        diaNhacFragment.clickIconHeart(songUrl);
        lyricsSongFragment.setSongLyrics(songLyrics);
    }

    // Đăng ký BroadcastReceiver trong onStart()
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("MUSIC_UPDATE");
        registerReceiver(playbackStateReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(playbackStateReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPlayMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        getSupportActionBar().setTitle("");
        binding.toolbarplaynhac.setTitleTextColor(Color.WHITE);
        binding.toolbarplaynhac.getNavigationIcon().setTint(Color.WHITE);
        binding.toolbarplaynhac.setNavigationOnClickListener(v -> {
            finish();
            mangbaihat.clear();
        });

        diaNhacFragment = new DiaNhacFragment();
        lyricsSongFragment = new LyricsSongFragment();
        PlayListSongFragment playListSongFragment = new PlayListSongFragment();
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(playListSongFragment);
        fragments.add(diaNhacFragment);
        fragments.add(lyricsSongFragment);

        viewPagerPlaySongAdapter = new ViewPagerPlaySongAdapter(PlayMusicActivity.this, fragments);

        // Sau khi thiết lập ViewPager2
        binding.viewpagerplaynhac.setAdapter(viewPagerPlaySongAdapter);
        binding.viewpagerplaynhac.setCurrentItem(1, false);

        updateDiaNhacFragment();
        // Cập nhật trực tiếp DiaNhacFragment nếu dữ liệu sẵn sàng
        if (!mangbaihat.isEmpty()) {
            BaiHat baiHat = mangbaihat.get(position);
            new Handler(Looper.getMainLooper()).post(() -> {
                if (diaNhacFragment.isAdded()) {
                    diaNhacFragment.getAnhBaiHat(baiHat.getAnhBaiHat());
                    diaNhacFragment.setTenBaiHat(baiHat.getTenBaiHat());
                    diaNhacFragment.setTenCaSi(baiHat.getCaSi());
                    diaNhacFragment.clickIconHeart(baiHat.getIdBaiHat());
                    lyricsSongFragment.setSongLyrics(baiHat.getLyrics());
                }
            });
        } else if (!mangpodcast.isEmpty()) {
            Podcast podcast = mangpodcast.get(position);
            new Handler(Looper.getMainLooper()).post(() -> {
                if (diaNhacFragment.isAdded()) {
                    diaNhacFragment.getAnhBaiHat(podcast.getAnhPodcast());
                    diaNhacFragment.setTenBaiHat(podcast.getTenPodcast());
                    diaNhacFragment.setTenCaSi(podcast.getTacGia());
                    diaNhacFragment.clickIconPodcast(podcast.getIdPodcast());
                }
            });
        }
        //Đăng ký callback cho ViewPager để đảm bảo diaNhacFragment sẵn sàng trước khi set ảnh
        binding.viewpagerplaynhac.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int positionviewpager) {
                super.onPageSelected(positionviewpager);
                if (positionviewpager == 1) { // Khi chuyển tới DiaNhacFragment
                    updateDiaNhacFragment();
                }
            }
        });

        if (!mangbaihat.isEmpty()) {
            BaiHat baiHat = mangbaihat.get(position);
            binding.toolbarTitle.setText(baiHat.getTenBaiHat());
            initializePlayer(baiHat.getLinkNhac());
//            getDataIntentBaiHat(baiHat);
            binding.imgbtncircledplay.setImageResource(R.drawable.play2_button_icon);
        } else if (!mangpodcast.isEmpty()) {
            Podcast podcast = mangpodcast.get(position);
            binding.toolbarTitle.setText(podcast.getTenPodcast());
            initializePlayer(podcast.getLinkPodcast());
            binding.imgbtncircledplay.setImageResource(R.drawable.play2_button_icon);
        }
    }

    private void updateDiaNhacFragment() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (diaNhacFragment.isAdded()) {
                if (!mangbaihat.isEmpty()) {
                    BaiHat baiHat = mangbaihat.get(position);
                    diaNhacFragment.getAnhBaiHat(baiHat.getAnhBaiHat());
                    diaNhacFragment.setTenBaiHat(baiHat.getTenBaiHat());
                    diaNhacFragment.setTenCaSi(baiHat.getCaSi());
                    diaNhacFragment.clickIconHeart(baiHat.getIdBaiHat());
                    lyricsSongFragment.setSongLyrics(baiHat.getLyrics());
                } else if (!mangpodcast.isEmpty()) {
                    Podcast podcast = mangpodcast.get(position);
                    diaNhacFragment.getAnhBaiHat(podcast.getAnhPodcast());
                    diaNhacFragment.setTenBaiHat(podcast.getTenPodcast());
                    diaNhacFragment.setTenCaSi(podcast.getTacGia());
                }
            }
        });
    }

    public void playSong(BaiHat baiHat) {
        // Cập nhật tên bài hát và ca sĩ trên giao diện
        binding.toolbarTitle.setText(baiHat.getTenBaiHat());
        diaNhacFragment.getAnhBaiHat(baiHat.getAnhBaiHat());
        diaNhacFragment.setTenBaiHat(baiHat.getTenBaiHat());
        diaNhacFragment.setTenCaSi(baiHat.getCaSi());
        diaNhacFragment.clickIconHeart(baiHat.getIdBaiHat());
        lyricsSongFragment.setSongLyrics(baiHat.getLyrics());
//        getDataIntentBaiHat(baiHat);

        // Khởi tạo lại ExoPlayer với bài hát mới
        initializePlayer(baiHat.getLinkNhac());

        // Cập nhật vị trí hiện tại của bài hát trong danh sách
        position = mangbaihat.indexOf(baiHat);
        binding.imgbtncircledplay.setImageResource(R.drawable.play2_button_icon); // Đổi biểu tượng play
    }

    public void playPodcast(Podcast podcast) {
        binding.toolbarTitle.setText(podcast.getTenPodcast());
        diaNhacFragment.getAnhBaiHat(podcast.getAnhPodcast());
        diaNhacFragment.setTenBaiHat(podcast.getTenPodcast());
        diaNhacFragment.setTenCaSi(podcast.getTacGia());
        initializePlayer(podcast.getLinkPodcast());
        position = mangpodcast.indexOf(podcast);
        binding.imgbtncircledplay.setImageResource(R.drawable.play2_button_icon);
    }


    public void updateSongAndImage(int position) {
        if (position >= 0 && position < mangbaihat.size()) {
            BaiHat baiHat = mangbaihat.get(position);
            // Cập nhật lại tên bài hát và ca sĩ
            binding.toolbarTitle.setText(baiHat.getTenBaiHat());

            // Cập nhật ảnh trong ViewPager
            diaNhacFragment.getAnhBaiHat(baiHat.getAnhBaiHat());
            diaNhacFragment.setTenBaiHat(baiHat.getTenBaiHat());
            diaNhacFragment.setTenCaSi(baiHat.getCaSi());
            diaNhacFragment.clickIconHeart(baiHat.getIdBaiHat());
            lyricsSongFragment.setSongLyrics(baiHat.getLyrics());
//            getDataIntentBaiHat(baiHat);

            // Khởi tạo lại ExoPlayer với bài hát mới
            initializePlayer(baiHat.getLinkNhac());
        } else if (position >= 0 && position < mangpodcast.size()) {
            Podcast podcast = mangpodcast.get(position);
            binding.toolbarTitle.setText(podcast.getTenPodcast());
            diaNhacFragment.setTenCaSi(podcast.getTacGia());
            diaNhacFragment.getAnhBaiHat(podcast.getAnhPodcast());
            diaNhacFragment.setTenBaiHat(podcast.getTenPodcast());
            initializePlayer(podcast.getLinkPodcast());

        }
    }

    private void updateRepeatButtonUI(boolean isRepeat) {
        if (isRepeat) {
            binding.imgbtnrepeat.setColorFilter(Color.parseColor("#8342BD"), PorterDuff.Mode.SRC_ATOP);
        } else {
            binding.imgbtnrepeat.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void updateShuffleButtonUI(boolean isShuffle) {
        if (isShuffle) {
            binding.imgbtnshuffle.setColorFilter(Color.parseColor("#8342BD"), PorterDuff.Mode.SRC_ATOP);

        } else {
            binding.imgbtnshuffle.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
    }

//    private void handleNextSong() {
//        if (!mangbaihat.isEmpty()) {
//            position++;
//            if (position >= mangbaihat.size()) {
//                position = 0;
//            }
//            updateSongAndImage(position);
//        } else if (!mangpodcast.isEmpty()) {
//            position++;
//            if (position >= mangpodcast.size()) {
//                position = 0;
//            }
//            updateSongAndImage(position);
//        }
//    }

    private void updatePlayPauseButton(boolean isPlaying) {
        if (isPlaying) {
            binding.imgbtncircledplay.setImageResource(R.drawable.play2_button_icon);
            if (diaNhacFragment != null) {
                diaNhacFragment.resumeAnimation();
            }
        } else {
            binding.imgbtncircledplay.setImageResource(R.drawable.play_button_icon);
            if (diaNhacFragment != null) {
                diaNhacFragment.pauseAnimation();
            }
        }
    }

    private void updateSeekBarAndTime(long currentPosition, long duration) {
        // Cập nhật SeekBar
        binding.seekbarsong.setMax((int) duration);
        binding.seekbarsong.setProgress((int) currentPosition);

        // Cập nhật thời gian bài hát
        String currentTime = formatTime(currentPosition);
        String totalTime = formatTime(duration);
        binding.txtTimesong.setText(currentTime);
        binding.txtTimesong1.setText(totalTime);
    }

    private void eventClick() {
        binding.imgbtncircledplay.setOnClickListener(v -> {
            Intent intent = new Intent(this, MusicService.class);
            intent.setAction(MusicService.ACTION_PLAY_PAUSE);
            startService(intent);
        });

        //xủ lý repeat và shuffle(lặp lại và random)
        binding.imgbtnrepeat.setOnClickListener(v -> {
            Intent intent = new Intent(PlayMusicActivity.this, MusicService.class);
            intent.setAction(MusicService.ACTION_REPEAT);
            startService(intent);
        });

        binding.imgbtnshuffle.setOnClickListener(v -> {
            Intent intent = new Intent(PlayMusicActivity.this, MusicService.class);
            intent.setAction(MusicService.ACTION_SHUFFLE);
            startService(intent);
        });

        //xử lý seekbar(thanh chạy nhạc)
        binding.seekbarsong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Intent intent = new Intent(PlayMusicActivity.this, MusicService.class);
                    intent.setAction(MusicService.ACTION_SEEK);
                    intent.putExtra("seek_position", progress);
                    startService(intent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Intent startTimeUpdateIntent = new Intent(MusicService.ACTION_SEEK);
//                sendBroadcast(startTimeUpdateIntent);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                int seekPosition = seekBar.getProgress();
//                Intent intent = new Intent(MusicService.ACTION_SEEK);
//                intent.putExtra("seek_position", seekPosition);
//                sendBroadcast(intent);

            }
        });

        //xử lý next nhạc
        binding.imgbtnend.setOnClickListener(v -> {
            if (!mangbaihat.isEmpty() || !mangpodcast.isEmpty()) {
//                if (exoPlayer.isPlaying() || exoPlayer != null) {
//                    exoPlayer.stop();
//                    exoPlayer.release();
//                    exoPlayer = null;
//                }

                if (!mangbaihat.isEmpty()) {
                    position++;
                    if (position >= mangbaihat.size()) {
                        position = 0;
                    }
                    updateSongAndImage(position);
                } else if (!mangpodcast.isEmpty()) {
                    position++;
                    if (position >= mangpodcast.size()) {
                        position = 0;
                    }
                    updateSongAndImage(position);
                }

            }
            binding.imgbtnskiptostart.setClickable(false);
            binding.imgbtnend.setClickable(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.imgbtnskiptostart.setClickable(true);
                    binding.imgbtnend.setClickable(true);
                }
            }, 2000);
        });

        binding.imgbtnskiptostart.setOnClickListener(v -> {
            if (!mangbaihat.isEmpty() || !mangpodcast.isEmpty()) {
//                if (exoPlayer.isPlaying() || exoPlayer != null) {
//                    exoPlayer.stop();
//                    exoPlayer.release();
//                    exoPlayer = null;
//                }
                if (!mangbaihat.isEmpty()) {
                    position--;
                    if (position < 0) {
                        position = mangbaihat.size() - 1; // Nếu đến đầu danh sách, quay lại cuối danh sách
                    }
                    updateSongAndImage(position);
                } else if (!mangpodcast.isEmpty()) {
                    position--;
                    if (position < 0) {
                        position = mangpodcast.size() - 1; // Nếu đến đầu danh sách, quay lại cuối danh sách
                    }
                    updateSongAndImage(position);
                }
            }
            binding.imgbtnskiptostart.setClickable(false);
            binding.imgbtnend.setClickable(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.imgbtnskiptostart.setClickable(true);
                    binding.imgbtnend.setClickable(true);
                }
            }, 2000);
        });
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("cakhuc")) {
                BaiHat baiHat = intent.getParcelableExtra("cakhuc");
                mangbaihat.add(baiHat);
            }
            if (intent.hasExtra("BaiHat")) {
                mangbaihat = intent.getParcelableArrayListExtra("BaiHat");
            }
            if (intent.hasExtra("Podcast")) {
                mangpodcast = intent.getParcelableArrayListExtra("Podcast");
            }
            if (intent.hasExtra("position")) {
                position = intent.getIntExtra("position", 0);
            }

        }
    }
//    private void getDataIntentBaiHat(BaiHat baiHat) {
//        Intent intent = new Intent(this, PlayMusicActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("cakhuc", baiHat);
//        intent.putExtras(bundle);
//        startService(intent);
//    }


    private void initializePlayer(String songUrl) {
        Intent serviceIntent = new Intent(PlayMusicActivity.this, MusicService.class);
        serviceIntent.putExtra("songUrl", songUrl);
        startService(serviceIntent);
    }

    private String formatTime(long timeMs) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(timeMs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
