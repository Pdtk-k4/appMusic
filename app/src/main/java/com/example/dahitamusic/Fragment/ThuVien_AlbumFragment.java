package com.example.dahitamusic.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dahitamusic.Adapter.ThuVien_AlbumYeuThich_Adapter;
import com.example.dahitamusic.Model.Album;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentThuVienAlbumBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThuVien_AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThuVien_AlbumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentThuVienAlbumBinding binding;
    private ArrayList<Album> mListAlbums;
    private ThuVien_AlbumYeuThich_Adapter adapter;
    private DatabaseReference mData;

    public ThuVien_AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThuVien_AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThuVien_AlbumFragment newInstance(String param1, String param2) {
        ThuVien_AlbumFragment fragment = new ThuVien_AlbumFragment();
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
        binding = FragmentThuVienAlbumBinding.inflate(inflater, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mListAlbums = new ArrayList<>();
        adapter = new ThuVien_AlbumYeuThich_Adapter(mListAlbums, new ThuVien_AlbumYeuThich_Adapter.IClickListner() {
            @Override
            public void onClickHeart(Album album) {
                clickHeart(album);
            }
        });
        binding.rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcv.setAdapter(adapter);

        if (user != null){
            getFavoriteAlbums(user.getUid());
        }
        return binding.getRoot();

    }

    private void clickHeart(Album album) {
        DatabaseReference albumYeuThich = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("albumYeuThich");

        new AlertDialog.Builder(getContext())
                .setTitle(album.getTenAlbum())
                .setMessage("Xóa album này khỏi thư viện?")
                .setPositiveButton("Xóa", (dialogInterface, i) -> {
                    albumYeuThich.child(album.getIdAlbum()).removeValue((error, ref) -> {
                        if (error == null) {
                            Toast.makeText(getContext(), "Đã xóa khỏi thư viện yêu thích", Toast.LENGTH_SHORT).show();
                            mListAlbums.remove(album);
                            adapter.notifyDataSetChanged();
                            updateNoDataMessage();
                        } else {
                            Toast.makeText(getContext(), "Lỗi: Không thể xóa", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void getFavoriteAlbums(String userId) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userFavoritesRef = database.child("Users").child(userId).child("albumYeuThich");

        userFavoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    String idAlbum = songSnapshot.getKey();
                    loadAlbumYeuThich(idAlbum);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }

    private void loadAlbumYeuThich(String idAlbum) {
        mData = FirebaseDatabase.getInstance().getReference("Album").child(idAlbum);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Album album = snapshot.getValue(Album.class);
                if (album != null) {
                    // Chỉ thêm bài hát vào danh sách nếu chưa tồn tại
                    boolean isExist = false;
                    for (Album item : mListAlbums) {
                        if (item.getIdAlbum().equals(album.getIdAlbum())) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        mListAlbums.add(album);
                    }
                }
                adapter.notifyDataSetChanged();
                updateNoDataMessage();// Cập nhật giao diện
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: " + error.getMessage());
            }
        });
    }

    private void updateNoDataMessage() {
        if (mListAlbums.isEmpty()) {
            binding.txt.setVisibility(View.VISIBLE);  // Hiển thị thông báo nếu không có album
        } else {
            binding.txt.setVisibility(View.GONE);  // Ẩn thông báo nếu có album
        }
    }
}