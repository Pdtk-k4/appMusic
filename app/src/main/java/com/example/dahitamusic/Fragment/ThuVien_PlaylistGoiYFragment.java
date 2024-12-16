package com.example.dahitamusic.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dahitamusic.Adapter.ThuVien_Playlist_Adapter;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.ViewModel.PlayListViewModel;
import com.example.dahitamusic.databinding.FragmentThuVienPlaylistBinding;
import com.example.dahitamusic.databinding.FragmentThuVienPlaylistGoiYBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThuVien_PlaylistGoiYFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThuVien_PlaylistGoiYFragment extends Fragment {

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
    private FragmentThuVienPlaylistGoiYBinding binding;
    private DatabaseReference mData;
    private PlayListViewModel viewModel;

    public ThuVien_PlaylistGoiYFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThuVien_PlaylistGoiYFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThuVien_PlaylistGoiYFragment newInstance(String param1, String param2) {
        ThuVien_PlaylistGoiYFragment fragment = new ThuVien_PlaylistGoiYFragment();
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
        binding = FragmentThuVienPlaylistGoiYBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(PlayListViewModel.class);

        mangPlaylist = new ArrayList<>();
        mangPlaylistHienThi = new ArrayList<>();
        thuVien_Playlist_Adapter = new ThuVien_Playlist_Adapter(mangPlaylistHienThi, getActivity(), new ThuVien_Playlist_Adapter.IClickListner() {
            @Override
            public void onClick(Playlist playlist) {
                clickIconHeart(playlist);
            }
        });
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerview.setAdapter(thuVien_Playlist_Adapter);
        // Kiểm tra nếu dữ liệu đã có trong ViewModel rồi thì không tải lại từ Firebase
        if (viewModel.getPlaylists().getValue() != null && !viewModel.getPlaylists().getValue().isEmpty()) {
            mangPlaylistHienThi.clear();
            mangPlaylistHienThi.addAll(viewModel.getPlaylists().getValue());
            thuVien_Playlist_Adapter.notifyDataSetChanged();
        } else {
            loadPlaylistgoiy();
        }

        return binding.getRoot();
    }

    private void loadPlaylistgoiy() {
        mData = FirebaseDatabase.getInstance().getReference("Playlist");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        DatabaseReference playListYeuThichRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user.getUid())
                .child("playListYeuThich");

        // Lấy danh sách playlist yêu thích
        playListYeuThichRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> playListYeuThichIds = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String playlistId = dataSnapshot.getKey();
                    if (playlistId != null) {
                        playListYeuThichIds.add(playlistId);
                    }
                }
                // Tải playlist từ Firebase và kiểm tra nếu nó không có trong playListYeuThich
                Query query = mData;
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mangPlaylist.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Playlist playlist = dataSnapshot.getValue(Playlist.class);
                            if (playlist != null && !playListYeuThichIds.contains(playlist.getIdPlaylist())) {
                                mangPlaylist.add(playlist);
                            }
                        }
                        Collections.shuffle(mangPlaylist);

                        viewModel.setPlaylists(mangPlaylist);
                        mangPlaylistHienThi.clear();
                        mangPlaylistHienThi.addAll(mangPlaylist);

                        thuVien_Playlist_Adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void clickIconHeart(Playlist playlist) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        DatabaseReference playListYeuThichRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user.getUid())
                .child("playListYeuThich");

        playListYeuThichRef.child(playlist.getIdPlaylist()).setValue(true);

        mangPlaylistHienThi.remove(playlist);
        viewModel.setPlaylists(mangPlaylistHienThi);
        thuVien_Playlist_Adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}