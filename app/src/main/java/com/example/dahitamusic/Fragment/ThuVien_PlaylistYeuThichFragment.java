package com.example.dahitamusic.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dahitamusic.Adapter.ThuVien_PlaylistYeuThich_Adapter;
import com.example.dahitamusic.Adapter.ThuVien_Playlist_Adapter;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentThuVienPlaylistBinding;
import com.example.dahitamusic.databinding.FragmentThuVienPlaylistGoiYBinding;
import com.example.dahitamusic.databinding.FragmentThuVienPlaylistYeuThichBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThuVien_PlaylistYeuThichFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThuVien_PlaylistYeuThichFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Playlist> mangPlaylist;
    private ThuVien_PlaylistYeuThich_Adapter thuVien_playlistYeuThich_adapter;
    private FragmentThuVienPlaylistYeuThichBinding binding;
    private DatabaseReference mData;


    public ThuVien_PlaylistYeuThichFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThuVien_PlaylistYeuThichFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThuVien_PlaylistYeuThichFragment newInstance(String param1, String param2) {
        ThuVien_PlaylistYeuThichFragment fragment = new ThuVien_PlaylistYeuThichFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentThuVienPlaylistYeuThichBinding.inflate(inflater, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mangPlaylist = new ArrayList<>();
        thuVien_playlistYeuThich_adapter = new ThuVien_PlaylistYeuThich_Adapter(mangPlaylist, new ThuVien_PlaylistYeuThich_Adapter.IClickListner() {
            @Override
            public void onClickHeart(Playlist playlist) {
                clickIconHeart(playlist);
            }
        });

        clickTaoPlaylist();

        binding.recyclerviewPlaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerviewPlaylist.setAdapter(thuVien_playlistYeuThich_adapter);

        assert user != null;
        getPlayListYeuThich(user.getUid());
        return binding.getRoot();
    }

    private void getPlayListYeuThich(String userId) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userFavoritesRef = database.child("Users").child(userId).child("playListYeuThich");

        userFavoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mangPlaylist.clear();
                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    String idPlayList = songSnapshot.getKey();
                    loadPlaylistyeuthich(idPlayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }

    private void loadPlaylistyeuthich(String idPlayList) {
        mData = FirebaseDatabase.getInstance().getReference("Playlist").child(idPlayList); // Chỉ lấy dữ liệu bài hát theo id
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Playlist playlist = snapshot.getValue(Playlist.class);
                if (playlist != null) {
                    // Chỉ thêm bài hát vào danh sách nếu chưa tồn tại
                    boolean isExist = false;
                    for (Playlist item : mangPlaylist) {
                        if (item.getIdPlaylist().equals(playlist.getIdPlaylist())) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        mangPlaylist.add(playlist);
                    }
                }
                thuVien_playlistYeuThich_adapter.notifyDataSetChanged(); // Cập nhật giao diện
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: " + error.getMessage());
            }
        });

    }

    private void clickTaoPlaylist() {
        binding.btnTaoPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(requireContext());
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

                btn_tao_playlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String tenPlaylist = edt_tenplaylist.getText().toString().trim();
                        if (tenPlaylist.isEmpty()) {
                            Toast.makeText(getContext(), "Vui lòng nhập tên playlist!", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(getContext(), "Tạo playlist thành công!", Toast.LENGTH_SHORT).show();
                                                getPlayListYeuThich(userId);
                                                dialog.dismiss();
                                                openPlaylist(playlist);
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(getContext(), "Không thể thêm vào danh sách yêu thích: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Không thể lưu playlist: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                });
                dialog.show();
            }
        });

    }

    private void openPlaylist(Playlist playlist) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("Playlist", playlist);
        CreatePlaylistFragment fragment = new CreatePlaylistFragment();
        fragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.view_pager, fragment)
                .addToBackStack(null)
                .commit();
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

    // Phương thức loại bỏ dấu (chuyển các ký tự có dấu thành không dấu)
    private String removeAccents(String input) {
        String nfd = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfd).replaceAll("");
    }


    private void clickIconHeart(Playlist playlist) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        DatabaseReference playListYeuThichRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId)
                .child("playListYeuThich");

        DatabaseReference playlistRef = FirebaseDatabase.getInstance()
                .getReference("Playlist");

        // Hiển thị hộp thoại xác nhận
        new AlertDialog.Builder(getContext())
                .setTitle(playlist.getTenPlaylist())
                .setMessage("Xóa playlist này khỏi thư viện?")
                .setPositiveButton("Xóa", (dialogInterface, i) -> {
                    // Xóa khỏi playListYeuThich
                    playListYeuThichRef.child(playlist.getIdPlaylist()).removeValue((error, ref) -> {
                        if (error == null) {
                            Log.d("ThuVien_PlaylistYeuThichFragment", playlist.getIdPlaylist());
                            Toast.makeText(getContext(), "Đã xóa playlist khỏi thư viện", Toast.LENGTH_SHORT).show();
                            mangPlaylist.remove(playlist);
                            thuVien_playlistYeuThich_adapter.notifyDataSetChanged();

                            // Nếu playlist được tạo bởi người dùng, xóa khỏi node Playlist
                            if (playlist.getUserId() != null && playlist.getUserId().equals(userId)) {
                                playlistRef.child(playlist.getIdPlaylist()).removeValue((error1, ref1) -> {
                                    if (error1 == null) {
                                        Toast.makeText(getContext(), "Đã xóa playlist khỏi thư viện", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Không thể xóa playlist khỏi hệ thống: " + error1.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getContext(), "Lỗi: Không thể xóa khỏi thư viện yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}