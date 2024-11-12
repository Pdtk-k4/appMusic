package com.example.dahitamusic.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.Adapter.BaiHatGoiY_Adapter;
import com.example.dahitamusic.Adapter.ThuVien_Playlist_Adapter;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentBaiHatBinding;
import com.example.dahitamusic.databinding.FragmentThuVienPlaylistBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThuVien_PlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThuVien_PlaylistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Playlist> mangPlaylist;
    private ThuVien_Playlist_Adapter thuVien_Playlist_Adapter;
    private FragmentThuVienPlaylistBinding binding;
    private DatabaseReference mData;

    public ThuVien_PlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThuVien_PlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThuVien_PlaylistFragment newInstance(String param1, String param2) {
        ThuVien_PlaylistFragment fragment = new ThuVien_PlaylistFragment();
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
        binding = FragmentThuVienPlaylistBinding.inflate(inflater, container, false);

        mangPlaylist = new ArrayList<>();
        thuVien_Playlist_Adapter = new ThuVien_Playlist_Adapter(mangPlaylist, getActivity());
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerview.setAdapter(thuVien_Playlist_Adapter);

        loadPlaylistgoiy();
        return binding.getRoot();

    }

    public void loadPlaylistgoiy() {
        mData = FirebaseDatabase.getInstance().getReference("Playlist");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mangPlaylist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Playlist playlist = dataSnapshot.getValue(Playlist.class);
                    if (playlist != null) {
                        mangPlaylist.add(playlist);
                    }
                }

                // Trộn ngẫu nhiên danh sách album
                Collections.shuffle(mangPlaylist);

                thuVien_Playlist_Adapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}