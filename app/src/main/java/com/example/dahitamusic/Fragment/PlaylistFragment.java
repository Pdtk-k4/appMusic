package com.example.dahitamusic.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.Adapter.Playlist_Adapter;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.ViewModel.PlayListViewModel;
import com.example.dahitamusic.databinding.FragmentPlaylistBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentPlaylistBinding binding;
    private DatabaseReference mData;
    private Playlist_Adapter playlist_adapter;
    private ArrayList<Playlist> mListPlaylist;
    private ArrayList<Playlist> mangPlaylistHienThi;
    private PlayListViewModel viewModel;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment();
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
        binding = FragmentPlaylistBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(PlayListViewModel.class);

        mListPlaylist = new ArrayList<>();
        mangPlaylistHienThi = new ArrayList<>();
        playlist_adapter = new Playlist_Adapter(mangPlaylistHienThi);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recyclerviewPlaylist.setLayoutManager(linearLayoutManager);
        binding.recyclerviewPlaylist.setAdapter(playlist_adapter);

        // Kiểm tra nếu dữ liệu đã có trong ViewModel rồi thì không tải lại từ Firebase
        if (viewModel.getPlaylists().getValue() != null && !viewModel.getPlaylists().getValue().isEmpty()) {
            // Dữ liệu đã có trong ViewModel, chỉ cần sử dụng lại
            mangPlaylistHienThi.clear();
            mangPlaylistHienThi.addAll(viewModel.getPlaylists().getValue());
            playlist_adapter.notifyDataSetChanged();
        } else {
            // Dữ liệu chưa có trong ViewModel, cần tải từ Firebase
            loadImgPlaylist("chill");
        }

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void loadImgPlaylist(String idTheLoai) {
        mData = FirebaseDatabase.getInstance().getReference("Playlist");
        Query query = mData.orderByChild("idTheLoai").equalTo(idTheLoai); // Lấy playlist có cùng idTheLoai
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

                Collections.shuffle(mListPlaylist);

                // Cập nhật ViewModel và danh sách hiển thị
                viewModel.setPlaylists(mListPlaylist);
                mangPlaylistHienThi.clear();
                mangPlaylistHienThi.addAll(mListPlaylist);


                playlist_adapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}