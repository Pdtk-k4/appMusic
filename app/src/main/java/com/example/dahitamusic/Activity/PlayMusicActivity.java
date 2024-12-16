package com.example.dahitamusic.Activity;

import android.app.Dialog;
import android.content.Intent;
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
import com.example.dahitamusic.Fragment.PlayListSongFragment;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Podcast;
import com.example.dahitamusic.R;
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
    private ExoPlayer exoPlayer;
    int position = 0;
    boolean repeat = false;
    boolean shuffle = false;
    boolean next = false;

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
        getSupportActionBar().setTitle("");
        binding.toolbarplaynhac.setTitleTextColor(Color.WHITE);
        binding.toolbarplaynhac.getNavigationIcon().setTint(Color.WHITE);
        binding.toolbarplaynhac.setNavigationOnClickListener(v -> {
            finish();
            exoPlayer.stop();
            mangbaihat.clear();
        });

        diaNhacFragment = new DiaNhacFragment();
        PlayListSongFragment playListSongFragment = new PlayListSongFragment();
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(playListSongFragment);
        fragments.add(diaNhacFragment);

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
                } else {
                    Log.e("PlayMusicActivity", "DiaNhacFragment not attached yet");
                }
            });
        } else if (!mangpodcast.isEmpty()) {
            Podcast podcast = mangpodcast.get(position);
            new Handler(Looper.getMainLooper()).post(() -> {
                if (diaNhacFragment.isAdded()) {
                    diaNhacFragment.getAnhBaiHat(podcast.getAnhPodcast());
                    diaNhacFragment.setTenBaiHat(podcast.getTenPodcast());
                    diaNhacFragment.setTenCaSi(podcast.getTacGia());
                } else {
                    Log.e("PlayMusicActivity", "DiaNhacFragment not attached yet");
                }
            });
        }


//         Đăng ký callback cho ViewPager để đảm bảo diaNhacFragment sẵn sàng trước khi set ảnh
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
                } else if (!mangpodcast.isEmpty()) {
                    Podcast podcast = mangpodcast.get(position);
                    diaNhacFragment.getAnhBaiHat(podcast.getAnhPodcast());
                    diaNhacFragment.setTenBaiHat(podcast.getTenPodcast());
                    diaNhacFragment.setTenCaSi(podcast.getTacGia());
                }
            } else {
                Log.e("PlayMusicActivity", "DiaNhacFragment not attached yet");
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
                startSeekBarUpdate();
            }
        });

        //xủ lý repeat và shuffle(lặp lại và random)
        binding.imgbtnrepeat.setOnClickListener(v -> {
            if (repeat) {
                // Nếu repeat đang bật, tắt đi và chuyển trắng
                binding.imgbtnrepeat.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                repeat = false;
            } else {
                // Nếu repeat chưa bật, bật lên và chuyển sang tím
                binding.imgbtnrepeat.setColorFilter(Color.parseColor("#8342BD"), PorterDuff.Mode.SRC_ATOP);  // Màu tím
                repeat = true;

                // Tắt shuffle nếu đang bật
                if (shuffle) {
                    shuffle = false;
                    binding.imgbtnshuffle.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        binding.imgbtnshuffle.setOnClickListener(v -> {
            if (shuffle) {
                // Nếu shuffle đang bật, tắt đi và chuyển thành màu trắng
                binding.imgbtnshuffle.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                shuffle = false;
            } else {
                // Nếu shuffle chưa bật, bật lên và chuyển màu tím
                binding.imgbtnshuffle.setColorFilter(Color.parseColor("#8342BD"), PorterDuff.Mode.SRC_ATOP);  // Màu tím
                shuffle = true;

                // Tắt repeat nếu đang bật
                if (repeat) {
                    repeat = false;
                    binding.imgbtnrepeat.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                }
            }
        });


        //xử lý seekbar(thanh chạy nhạc)
        binding.seekbarsong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && exoPlayer != null) {
                    // Cập nhật thời gian hiện tại của bài hát khi kéo SeekBar
                    binding.txtTimesong.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Tạm dừng cập nhật SeekBar khi người dùng bắt đầu kéo
                seekBarHandler.removeCallbacks(updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (exoPlayer != null) {
                    // Phát nhạc từ vị trí người dùng chọn
                    exoPlayer.seekTo(seekBar.getProgress());
                    startSeekBarUpdate(); // Bắt đầu lại cập nhật SeekBar
                }
            }
        });


        //xử lý next nhạc
        binding.imgbtnend.setOnClickListener(v -> {
            if (!mangbaihat.isEmpty() || !mangpodcast.isEmpty()) {
                if (exoPlayer.isPlaying() || exoPlayer != null) {
                    exoPlayer.stop();
                    exoPlayer.release();
                    exoPlayer = null;
                }

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
                if (exoPlayer.isPlaying() || exoPlayer != null) {
                    exoPlayer.stop();
                    exoPlayer.release();
                    exoPlayer = null;
                }
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
                // Lấy bài hát từ Intent
                BaiHat baiHat = intent.getParcelableExtra("cakhuc");
                mangbaihat.add(baiHat);
            }
            if (intent.hasExtra("BaiHat")) {
                // Lấy danh sách bài hát từ Intent
                mangbaihat = intent.getParcelableArrayListExtra("BaiHat");
            }
            if (intent.hasExtra("Podcast")) {
                mangpodcast = intent.getParcelableArrayListExtra("Podcast");
            }
            if (intent.hasExtra("position")) {
                // Lấy vị trí bài hát từ Intent
                position = intent.getIntExtra("position", 0);
            }

        }
    }


    // Khởi tạo ExoPlayer và phát bài hát
    private Handler seekBarHandler = new Handler(Looper.getMainLooper());
    private Runnable updateSeekBar;

    private void initializePlayer(String songUrl) {

        // Kiểm tra nếu ExoPlayer đã được khởi tạo
        if (exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(this).build();
        }

        MediaItem mediaItem = MediaItem.fromUri(songUrl);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    // Cập nhật SeekBar và thời gian cho bài hát hiện tại
                    long duration = exoPlayer.getDuration();
                    binding.seekbarsong.setMax((int) duration);
                    binding.txtTimesong1.setText(formatTime(duration));
                    startSeekBarUpdate();
                } else if (state == Player.STATE_ENDED) {
                    // Kiểm tra xem có bật chế độ repeat hay không
                    if (repeat) {
                        // Nếu repeat bật, phát lại bài hát hiện tại
                        exoPlayer.seekTo(0);
                        exoPlayer.play();
                    } else {
                        // Nếu repeat tắt, chuyển sang bài hát tiếp theo
                        if (shuffle) {
                            if (!mangbaihat.isEmpty()) {
                                position = new Random().nextInt(mangbaihat.size());
                            } else if (!mangpodcast.isEmpty()) {
                                position = new Random().nextInt(mangpodcast.size());
                            }
//                            // Nếu bật shuffle, phát một bài ngẫu nhiên
//                            position = new Random().nextInt(mangbaihat.size());
                        } else {
                            // Không bật shuffle, phát bài hát tiếp theo trong danh sách

                            if (!mangbaihat.isEmpty()) {
                                position++;
                                if (position >= mangbaihat.size()) {
                                    position = 0; // Quay lại bài đầu tiên nếu đến cuối danh sách
                                }
                            } else if (!mangpodcast.isEmpty()) {
                                position++;
                                if (position >= mangpodcast.size()) {
                                    position = 0; // Quay lại bài đầu tiên nếu đến cuối danh sách
                                }
                            }

                        }
                        // Cập nhật bài hát hoặc podcast và UI khi chuyển sang bài mới
                        if (!mangbaihat.isEmpty()) {
                            updateSongAndImage(position); // Cập nhật với bài hát
                        } else if (!mangpodcast.isEmpty()) {
                            updateSongAndImage(position); // Cập nhật với podcast
                        }

                    }
                }
            }
        });
    }

    private void startSeekBarUpdate() {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (exoPlayer != null && exoPlayer.isPlaying()) {
                    binding.seekbarsong.setProgress((int) exoPlayer.getCurrentPosition());
                    binding.txtTimesong.setText(formatTime(exoPlayer.getCurrentPosition()));
                    seekBarHandler.postDelayed(this, 300);
                }
            }
        };
        seekBarHandler.post(updateSeekBar);
    }


    private String formatTime(long timeMs) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(timeMs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
        seekBarHandler.removeCallbacks(updateSeekBar); // Ngừng cập nhật SeekBar
    }
}
