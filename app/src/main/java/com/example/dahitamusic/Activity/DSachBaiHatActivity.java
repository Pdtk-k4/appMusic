package com.example.dahitamusic.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Adapter.SongsAdapter;
import com.example.dahitamusic.Adapter.dialod_ds_playlist_adapter;
import com.example.dahitamusic.Fragment.HomeFragment;
import com.example.dahitamusic.Fragment.LibaryFragment;
import com.example.dahitamusic.Fragment.ProfileFragment;
import com.example.dahitamusic.Fragment.RadioFragment;
import com.example.dahitamusic.Model.Album;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.ActivityDsachBaiHatBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Pattern;

public class DSachBaiHatActivity extends AppCompatActivity {

    private ActivityDsachBaiHatBinding binding;
    private Playlist playlist;
    private Album album;
    private ArrayList<Playlist> mListPlaylist;
    private DatabaseReference mData;
    private ValueEventListener playlistListener, albumListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDsachBaiHatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mListPlaylist = new ArrayList<>();
        dataIntent();
        init();

        if (playlist != null && !playlist.getIdPlaylist().equals("")) {
            getPlaylist(playlist.getIdPlaylist());
            clickIconHeartPlaylist(playlist);
        }
        if (album != null && !album.getIdAlbum().equals("")) {
            getAlbum(album.getIdAlbum());
            clickIconHeartAlbum(album);
        }
    }


    private void getPlaylist(String idPlaylist) {
        mData = FirebaseDatabase.getInstance().getReference("BaiHat");
        Query query = mData.orderByChild("idPlaylist").equalTo(idPlaylist);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<BaiHat> baiHatList = new ArrayList<>();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    BaiHat baiHat = datasnapshot.getValue(BaiHat.class);
                    if (baiHat != null) {
                        baiHatList.add(baiHat);
                    }
                }
                Log.d("DSachBaiHatActivity", "Số bài hát đã lấy được: " + baiHatList.size());
                displaySongs(baiHatList);
                eventClick(baiHatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi lấy dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAlbum(String idAlbum) {
        mData = FirebaseDatabase.getInstance().getReference("BaiHat");
        Query query = mData.orderByChild("idAlbum").equalTo(idAlbum);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<BaiHat> baiHatList = new ArrayList<>();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    BaiHat baiHat = datasnapshot.getValue(BaiHat.class);
                    if (baiHat != null) {
                        baiHatList.add(baiHat);
                    }
                }
                Log.d("DSachBaiHatActivity", "Số bài hát đã lấy được: " + baiHatList.size());
                displaySongs(baiHatList);
                eventClick(baiHatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi lấy dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displaySongs(ArrayList<BaiHat> baiHatList) {
        if (baiHatList.isEmpty()) {
            Toast.makeText(this, "Không có bài hát nào để hiển thị", Toast.LENGTH_SHORT).show();
        } else {
            RecyclerView recyclerView = binding.recyclerviewDanhsachbaihat;
            SongsAdapter adapter = new SongsAdapter(baiHatList, DSachBaiHatActivity.this, new SongsAdapter.IClickListner() {
                @Override
                public void onClick(BaiHat baiHat) {
                    click_iconMore(baiHat);
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(DSachBaiHatActivity.this));
            recyclerView.setAdapter(adapter);
        }
    }

    private void click_iconMore(BaiHat baiHat) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_more_baihat);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView img_close = dialog.findViewById(R.id.img_close);
        ImageView img_baiHat = dialog.findViewById(R.id.img_baiHat);
        ImageView img_iconHeart = dialog.findViewById(R.id.img_iconHeart);

        TextView txt_tenBaiHat = dialog.findViewById(R.id.txt_tenBaiHat);
        TextView txt_tenCaSi = dialog.findViewById(R.id.txt_tenCaSi);
        TextView txt_addThuvien = dialog.findViewById(R.id.txt_addThuvien);

        RelativeLayout relative_download = dialog.findViewById(R.id.relative_download);
        RelativeLayout relative_addPlaylist = dialog.findViewById(R.id.relative_addPlaylist);
        RelativeLayout relative_addThuvien = dialog.findViewById(R.id.relative_addThuvien);

        txt_tenBaiHat.setText(baiHat.getTenBaiHat());
        txt_tenCaSi.setText(baiHat.getCaSi());
        Picasso.get().load(baiHat.getAnhBaiHat()).into(img_baiHat);

        img_close.setOnClickListener(view -> dialog.dismiss());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user.getUid())
                .child("baiHatYeuThich");

        // Kiểm tra trạng thái "yêu thích" từ Firebase
        favoritesRef.child(baiHat.getIdBaiHat()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isFavorite = snapshot.exists(); // Kiểm tra bài hát đã được yêu thích chưa

                if (isFavorite) {

                    img_iconHeart.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart_pink));

                    txt_addThuvien.setText(R.string.dathemvaothuvien);
                } else {

                    img_iconHeart.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart));

                    txt_addThuvien.setText(R.string.themvaothuvien);
                }

                // Xử lý khi nhấn nút "thêm vào thư viện"
                relative_addThuvien.setOnClickListener(view -> {
                    if (isFavorite) {
                        // Xóa bài hát khỏi thư viện
                        favoritesRef.child(baiHat.getIdBaiHat()).removeValue();

                        img_iconHeart.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart));

                        txt_addThuvien.setText(R.string.themvaothuvien);
                        showCenteredToast("Đã xóa bài hát khỏi thư viện");
                    } else {
                        // Thêm bài hát vào thư viện
                        favoritesRef.child(baiHat.getIdBaiHat()).setValue(true);
                        img_iconHeart.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart_pink));

                        txt_addThuvien.setText(R.string.dathemvaothuvien);
                        showCenteredToast("Đã thêm bài hát này vào thư viện");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi tải trạng thái yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });

        relative_addPlaylist.setOnClickListener(view -> {
            dialog.dismiss();
            openDialogAddPlaylistBaiHat(baiHat);

        });

        dialog.show();
    }

    private void openDialogAddPlaylistBaiHat(BaiHat baiHat) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_addplaylist);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);


        ImageView img_close = dialog.findViewById(R.id.img_close);
        RelativeLayout relative_addPlaylist = dialog.findViewById(R.id.relativeLayout_addPlaylist);
        RecyclerView rcv = dialog.findViewById(R.id.rcv);

        img_close.setOnClickListener(view -> dialog.dismiss());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(linearLayoutManager);
        dialod_ds_playlist_adapter adapter = new dialod_ds_playlist_adapter(mListPlaylist, new dialod_ds_playlist_adapter.IClickListner() {
            @Override
            public void onClickItem(Playlist playlist) {
                onClickAddBaihat(playlist, baiHat);
            }
        });
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setAdapter(adapter);

        //load playlist trong dialog
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        mData = FirebaseDatabase.getInstance().getReference().child("Playlist");
        Query query = mData.orderByChild("userId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListPlaylist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Playlist playlist = dataSnapshot.getValue(Playlist.class);
                    if (playlist != null) {
                        mListPlaylist.add(playlist);
                    }
                }

                adapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });

        relative_addPlaylist.setOnClickListener(view -> {
            openDialogCreatePlaylist(baiHat);
        });

        dialog.show();
    }

    private void onClickAddBaihat(Playlist playlist, BaiHat baiHat) {
        if (playlist != null) {
            // Khởi tạo danhSachBaiHat nếu null
            if (playlist.getDanhSachBaiHat() == null) {
                playlist.setDanhSachBaiHat(new HashMap<>());
            }

            // Thêm idBaiHat vào danh sách và gán giá trị true
            playlist.getDanhSachBaiHat().put(baiHat.getIdBaiHat(), true);

            // Cập nhật Playlist trong Firebase
            DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference("Playlist");
            playlistRef.child(playlist.getIdPlaylist())
                    .setValue(playlist)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Thêm bài hát thành công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi khi thêm bài hát", Toast.LENGTH_SHORT).show();
                        Log.e("FirebaseError", e.getMessage());
                    });
        } else {
            Toast.makeText(this, "Playlist không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }

    private void openDialogCreatePlaylist(BaiHat baiHat) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_createplaylist);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edt_tenplaylist = dialog.findViewById(R.id.edt_tenplaylist);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_tao_playlist = dialog.findViewById(R.id.btn_tao_playlist);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_tao_playlist.setOnClickListener(view -> {
            String tenPlaylist = edt_tenplaylist.getText().toString().trim();
            if (tenPlaylist.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên playlist!", Toast.LENGTH_SHORT).show();
                return;
            }
            String idPlaylist = generateIdPlaylist(tenPlaylist);
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String userId = currentUser.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();

            // Tạo đối tượng Playlist
            Playlist playlist = new Playlist(idPlaylist, tenPlaylist, userId);

            // Lưu playlist vào node "Playlist"
            database.child("Playlist").child(idPlaylist).setValue(playlist)
                    .addOnSuccessListener(unused -> {
                        // Đồng thời lưu playlist vào "Users -> playListYeuThich"
                        database.child("Users").child(userId).child("playListYeuThich").child(idPlaylist).setValue(true)
                                .addOnSuccessListener(unused1 -> {
                                    addBaiHatToPlaylist(playlist, baiHat);
                                    dialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Không thể thêm vào danh sách yêu thích: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Không thể lưu playlist: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        dialog.show();
    }

    private void addBaiHatToPlaylist(Playlist playlist, BaiHat baiHat) {
        if (playlist != null) {
            // Khởi tạo danhSachBaiHat nếu null
            if (playlist.getDanhSachBaiHat() == null) {
                playlist.setDanhSachBaiHat(new HashMap<>());
            }
            // Thêm idBaiHat vào danh sách và gán giá trị true
            playlist.getDanhSachBaiHat().put(baiHat.getIdBaiHat(), true);

            // Cập nhật Playlist trong Firebase
            DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference("Playlist");
            playlistRef.child(playlist.getIdPlaylist())
                    .setValue(playlist)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Thêm bài hát vào playList thành công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi khi thêm bài hát", Toast.LENGTH_SHORT).show();
                        Log.e("FirebaseError", e.getMessage());
                    });
        } else {
            Toast.makeText(this, "Playlist không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức loại bỏ dấu (chuyển các ký tự có dấu thành không dấu)
    private String removeAccents(String input) {
        String nfd = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfd).replaceAll("");
    }

    private String generateIdPlaylist(String tenPlaylist) {
        if (tenPlaylist == null || tenPlaylist.trim().isEmpty()) {
            return ""; // Trả về chuỗi rỗng nếu tên playlist không hợp lệ
        }

        // Chuyển tất cả ký tự về chữ thường
        String normalized = tenPlaylist.toLowerCase();

        // Loại bỏ dấu (chuyển các ký tự có dấu thành không dấu)
        normalized = removeAccents(normalized);

        // Thay khoảng trắng thành dấu gạch dưới
        normalized = normalized.replaceAll("\\s+", "_");

        // Loại bỏ các ký tự không phải là chữ cái, chữ số, hoặc dấu gạch dưới
        normalized = normalized.replaceAll("[^a-z0-9_]", "");

        return normalized;
    }


    private void init() {
        setSupportActionBar(binding.toolbardanhsach);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (playlist != null) {
                binding.collapsingtoolbar.setTitle(playlist.getTenPlaylist());
                Picasso.get().load(playlist.getAnhPlaylist()).placeholder(R.drawable.img_default).into(binding.imgplaylist);
            }
            if (album != null) {
                binding.collapsingtoolbar.setTitle(album.getTenAlbum());
                Picasso.get().load(album.getAnhAlbum()).placeholder(R.drawable.img_default).into(binding.imgplaylist);
            }
        }
        binding.toolbardanhsach.setNavigationOnClickListener(v -> finish());

    }


    private void dataIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("Playlist")) {
                playlist = intent.getParcelableExtra("Playlist");
                if (playlist != null) {
                    Toast.makeText(this, playlist.getIdTheLoai(), Toast.LENGTH_SHORT).show();
                }
            }
            if (intent.hasExtra("Album")) {
                album = (Album) intent.getParcelableExtra("Album");
                if (album != null) {
                    Toast.makeText(this, album.getTenAlbum(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void eventClick(ArrayList<BaiHat> baiHatList) {
        if (baiHatList == null || baiHatList.isEmpty()) {
            Toast.makeText(this, "Không có bài hát nào để phát", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.btnPhatngaynhien.setOnClickListener(view -> {
            Collections.shuffle(baiHatList);
            Intent intent = new Intent(DSachBaiHatActivity.this, PlayMusicActivity.class);
            intent.putExtra("BaiHat", baiHatList);
            startActivity(intent);
        });
    }


    private void clickIconHeartPlaylist(Playlist playlist) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference playListYeuThich = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user.getUid())
                .child("playListYeuThich");

        // Lắng nghe trạng thái yêu thích của playlist ngay khi mở ứng dụng
        playListYeuThich.child(playlist.getIdPlaylist()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isPlaylistYeuThich = snapshot.exists();
                // Cập nhật giao diện theo trạng thái yêu thích
                if (isPlaylistYeuThich) {
                    binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart_pink));
                } else {
                    binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart));
                }

                // Xử lý sự kiện click vào tim để thêm/xóa playlist
                binding.imgLike.setOnClickListener(v -> {
                    if (isPlaylistYeuThich) {
                        // Nếu playlist đã có trong thư viện yêu thích, xóa nó
                        playListYeuThich.child(playlist.getIdPlaylist()).removeValue((error, ref) -> {
                            if (error == null) {
                                // Cập nhật lại UI ngay lập tức sau khi xóa playlist
                                binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart));
                                showCenteredToast("Đã xóa playlist khỏi thư viện");
                            } else {
                                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi xóa playlist!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Nếu playlist chưa có trong thư viện yêu thích, thêm vào
                        playListYeuThich.child(playlist.getIdPlaylist()).setValue(true, (error, ref) -> {
                            if (error == null) {
                                // Cập nhật lại UI ngay lập tức sau khi thêm playlist
                                binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart_pink));
                                showCenteredToast("Đã thêm playlist vào thư viện");
                            } else {
                                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi thêm playlist!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi tải trạng thái yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickIconHeartAlbum(Album album) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference albumYeuThich = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user.getUid())
                .child("albumYeuThich");

        // Lắng nghe trạng thái yêu thích của playlist ngay khi mở ứng dụng
        albumYeuThich.child(album.getIdAlbum()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isAlbumYeuThich = snapshot.exists();
                if (isAlbumYeuThich) {
                    binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart_pink));
                } else {
                    binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart));
                }

                // Xử lý sự kiện click vào tim để thêm/xóa playlist
                binding.imgLike.setOnClickListener(v -> {
                    if (isAlbumYeuThich) {
                        albumYeuThich.child(album.getIdAlbum()).removeValue((error, ref) -> {
                            if (error == null) {
                                binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart));
                                showCenteredToast("Đã xóa Album khỏi thư viện");
                            } else {
                                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi xóa Album!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        albumYeuThich.child(album.getIdAlbum()).setValue(true, (error, ref) -> {
                            if (error == null) {
                                binding.imgLike.setImageDrawable(ContextCompat.getDrawable(DSachBaiHatActivity.this, R.drawable.heart_pink));
                                showCenteredToast("Đã thêm Album vào thư viện");
                            } else {
                                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi thêm Album!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DSachBaiHatActivity.this, "Lỗi khi tải trạng thái yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Phương thức hiển thị Toast ở giữa màn hình
    private void showCenteredToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(android.view.Gravity.CENTER, 0, 0); // Đặt Toast ở giữa màn hình
        toast.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mData != null) {
            if (playlistListener != null) {
                mData.removeEventListener(playlistListener);
            }
            if (albumListener != null) {
                mData.removeEventListener(albumListener);
            }
        }
    }
}