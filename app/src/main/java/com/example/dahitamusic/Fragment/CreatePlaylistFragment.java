package com.example.dahitamusic.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dahitamusic.Activity.DSachBaiHatActivity;
import com.example.dahitamusic.Activity.PlayMusicActivity;
import com.example.dahitamusic.Adapter.SongsAdapter;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentCreatePlaylistBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatePlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePlaylistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentCreatePlaylistBinding binding;
    private DatabaseReference mData;
    private SongsAdapter adapter;
    private Playlist playlist;
    private ArrayList<BaiHat> mListBaiHat;

    public CreatePlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePlaylistFragment newInstance(String param1, String param2) {
        CreatePlaylistFragment fragment = new CreatePlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            playlist = (Playlist) getArguments().getParcelable("Playlist");
        }
        if (playlist != null) {
            getDanhSachBaiHat(playlist.getIdPlaylist());
            clickHeart(playlist);
        }
        mListBaiHat = new ArrayList<>();
        adapter = new SongsAdapter(mListBaiHat, getContext());
        binding.recyclerviewDanhsachbaihat.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerviewDanhsachbaihat.setAdapter(adapter);


        initToolbar();
        onClick();
        return binding.getRoot();
    }

    private void clickHeart(Playlist playlist) {
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
                    if (isAdded() && getContext() != null) {
                        binding.imgLike.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.heart_pink));
                    }
                } else {
                    if (isAdded() && getContext() != null) {
                        binding.imgLike.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.heart));
                    }
                }

                // Xử lý sự kiện click vào tim để xóa playlist
                binding.imgLike.setOnClickListener(v -> {
                    if (isPlaylistYeuThich) {
                        new AlertDialog.Builder(getContext())
                                .setTitle(playlist.getTenPlaylist())
                                .setMessage("Xóa bài hát này khỏi thư viện?")
                                .setPositiveButton("Xóa", (dialogInterface, i) -> {
                                    // Nếu playlist đã có trong thư viện yêu thích, xóa nó khỏi playListYeuThich
                                    playListYeuThich.child(playlist.getIdPlaylist()).removeValue((error, ref) -> {
                                        if (error == null) {
                                            // Cập nhật lại UI ngay lập tức sau khi xóa playlist khỏi playListYeuThich
                                            binding.imgLike.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.heart));
                                            showCenteredToast("Đã xóa playlist khỏi thư viện yêu thích");

                                            // Xóa playlist khỏi danh sách Playlist
                                            DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference("Playlist");
                                            playlistRef.child(playlist.getIdPlaylist()).removeValue((error2, ref2) -> {
                                                if (error2 == null) {
                                                    getActivity().onBackPressed();
                                                } else {
                                                    Toast.makeText(getContext(), "Lỗi khi xóa playlist khỏi danh sách Playlist!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getContext(), "Lỗi khi xóa playlist khỏi thư viện yêu thích!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                })
                                .setNegativeButton("Hủy", null)
                                .show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi khi tải trạng thái yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Phương thức hiển thị Toast ở giữa màn hình
    private void showCenteredToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(android.view.Gravity.CENTER, 0, 0); // Đặt Toast ở giữa màn hình
        toast.show();
    }

    private void initToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(binding.toolbardanhsach);
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                if (playlist != null) {
                    activity.getSupportActionBar().setTitle(playlist.getTenPlaylist());
                    Picasso.get().load(playlist.getAnhPlaylist()).placeholder(R.drawable.img_default).into(binding.imgplaylist);
                }
            }
            binding.toolbardanhsach.setNavigationOnClickListener(v -> activity.onBackPressed());
        }
    }

    private void onClick() {
        binding.btnThembaihat.setOnClickListener(view -> {
            // Mở AddPlaylistFragment khi nhấn nút "Thêm bài hát"
            AddPlayListFragment addPlaylistFragment = new AddPlayListFragment();
            Bundle args = new Bundle();
            args.putParcelable("Playlist", playlist);
            addPlaylistFragment.setArguments(args);

            // Mở AddPlaylistFragment
            getFragmentManager().beginTransaction()
                    .replace(R.id.view_pager, addPlaylistFragment)
                    .addToBackStack(null)
                    .commit();
        });

        binding.linearlayoutAdd.setOnClickListener(view -> {
            AddPlayListFragment addPlaylistFragment = new AddPlayListFragment();
            Bundle args = new Bundle();
            args.putParcelable("Playlist", playlist);
            addPlaylistFragment.setArguments(args);

            // Mở AddPlaylistFragment
            getFragmentManager().beginTransaction()
                    .replace(R.id.view_pager, addPlaylistFragment)
                    .addToBackStack(null)
                    .commit();
        });

    }

    private void getDanhSachBaiHat(String idPlaylist) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dsBaiHat = database.child("Playlist").child(idPlaylist).child("danhSachBaiHat");

        dsBaiHat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    String idBaiHat = songSnapshot.getKey();
                    loadBaiHat(idBaiHat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }

    private void loadBaiHat(String idBaiHat) {
        mData = FirebaseDatabase.getInstance().getReference("BaiHat").child(idBaiHat);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BaiHat baiHat = snapshot.getValue(BaiHat.class);
                if (baiHat != null) {
                    // Chỉ thêm bài hát vào danh sách nếu chưa tồn tại
                    boolean isExist = false;
                    for (BaiHat item : mListBaiHat) {
                        if (item.getIdBaiHat().equals(baiHat.getIdBaiHat())) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        mListBaiHat.add(baiHat);
                    }
                }
                adapter.notifyDataSetChanged();

                updatePlaylistWithFirstSongImage();

                anThemBaihat();
                anPhatNgauNhien();
                updateNoDataMessage();
                btnClick(mListBaiHat); // Kích hoạt nút phát nhạc
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: " + error.getMessage());
            }
        });
    }

    private void updatePlaylistWithFirstSongImage() {
        // Kiểm tra nếu danh sách bài hát không rỗng
        if (mListBaiHat != null && !mListBaiHat.isEmpty()) {
            // Lấy bài hát đầu tiên trong danh sách
            BaiHat firstSong = mListBaiHat.get(0);

            // Lấy ảnh của bài hát đầu tiên
            String firstSongImage = firstSong.getAnhBaiHat();

            // Kiểm tra playlist có hợp lệ không
            if (playlist != null) {
                // Cập nhật ảnh bài hát đầu tiên vào Playlist
                playlist.setAnhPlaylist(firstSongImage);

                // Cập nhật Playlist trong Firebase
                DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference("Playlist");
                playlistRef.child(playlist.getIdPlaylist()).child("anhPlaylist")
                        .setValue(firstSongImage);
            }
        }
    }


    private void btnClick(ArrayList<BaiHat> baiHatList) {
        if (binding != null && baiHatList != null && !baiHatList.isEmpty()) {
            binding.btnPhatngaynhien.setOnClickListener(view -> {
                Collections.shuffle(baiHatList);
                Intent intent = new Intent(requireContext(), PlayMusicActivity.class);
                intent.putExtra("BaiHat", baiHatList);
                startActivity(intent);
            });
        }
    }

    private void anThemBaihat() {
        if (mListBaiHat.isEmpty()) {
            binding.btnThembaihat.setVisibility(View.VISIBLE);
        } else {
            binding.btnThembaihat.setVisibility(View.INVISIBLE);
        }
    }

    private void anPhatNgauNhien() {
        if (mListBaiHat.isEmpty()) {
            binding.btnPhatngaynhien.setVisibility(View.INVISIBLE);
        } else {
            binding.btnPhatngaynhien.setVisibility(View.VISIBLE);
        }
    }

    private void updateNoDataMessage() {
        if (mListBaiHat.isEmpty()) {
            binding.txt.setVisibility(View.VISIBLE);
        } else {
            binding.txt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Gọi lại phương thức kiểm tra và cập nhật giao diện
        updateNoDataMessage();
        anPhatNgauNhien();
        anThemBaihat();
    }
}