package com.example.dahitamusic.Fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.dahitamusic.Adapter.ThuVien_Playlist_Adapter;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentThuVienPlaylistBinding;
import com.example.dahitamusic.databinding.FragmentThuVienPlaylistGoiYBinding;
import com.example.dahitamusic.databinding.FragmentThuVienPlaylistYeuThichBinding;
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
    private ArrayList<Playlist> mangPlaylistHienThi;
    private ThuVien_Playlist_Adapter thuVien_Playlist_Adapter;
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

        mangPlaylist = new ArrayList<>();
        mangPlaylistHienThi = new ArrayList<>();
        thuVien_Playlist_Adapter = new ThuVien_Playlist_Adapter(mangPlaylistHienThi, getActivity(), new ThuVien_Playlist_Adapter.IClickListner() {
            @Override
            public void onClick(Playlist playlist) {
                clickIconHeart(playlist);
            }
        });

        clickTaoPlaylist();

        binding.recyclerviewPlaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerviewPlaylist.setAdapter(thuVien_Playlist_Adapter);

        loadPlaylistyeuthich();
        return binding.getRoot();
    }

    private void loadPlaylistyeuthich() {
        mData = FirebaseDatabase.getInstance().getReference("Playlist");
        Query query = mData.orderByChild("yeuThich").equalTo(true);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Playlist playlist = snapshot.getValue(Playlist.class);
                if (playlist != null) {
                    mangPlaylist.add(playlist);
                    mangPlaylistHienThi.add(playlist);
                    thuVien_Playlist_Adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Playlist playlist = snapshot.getValue(Playlist.class);
                if (playlist != null) {
                    for (int i = 0; i < mangPlaylist.size(); i++) {
                        if (mangPlaylist.get(i).getIdPlaylist().equals(playlist.getIdPlaylist())) {
                            mangPlaylist.set(i, playlist);
                            break;
                        }
                    }
                    thuVien_Playlist_Adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clickTaoPlaylist() {
        binding.txtTaoplaylist.setOnClickListener(new View.OnClickListener() {
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
                        String idPlaylist = generateIdPlaylist(tenPlaylist);
                        Playlist playlist = new Playlist(idPlaylist, tenPlaylist, true, true);
                        mData = FirebaseDatabase.getInstance().getReference("Playlist");
                        mData.child(idPlaylist).setValue(playlist);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

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
        mData = FirebaseDatabase.getInstance().getReference("Playlist");

        if (playlist.isCreatePlaylisYeuThich()) {
            // Xóa hoàn toàn playlist này khỏi Firebase
            mData.child(playlist.getIdPlaylist()).removeValue();

            // Xóa khỏi danh sách hiển thị
            mangPlaylistHienThi.remove(playlist);
            thuVien_Playlist_Adapter.notifyDataSetChanged();
        } else {
            // Chỉ cập nhật trạng thái yêu thích
            playlist.setYeuThich(false);
            mData.child(playlist.getIdPlaylist()).updateChildren(playlist.toMap());

            // Xóa khỏi danh sách hiển thị
            mangPlaylistHienThi.remove(playlist);
            thuVien_Playlist_Adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}