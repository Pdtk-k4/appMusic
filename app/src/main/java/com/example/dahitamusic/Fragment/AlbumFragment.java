package com.example.dahitamusic.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.Activity.DSPlaylistAndAlbumActivity;
import com.example.dahitamusic.Adapter.Album_Adapter;
import com.example.dahitamusic.Model.Album;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.ViewModel.AlbumViewModel;
import com.example.dahitamusic.ViewModel.PlayListViewModel;
import com.example.dahitamusic.databinding.FragmentAlbumBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentAlbumBinding binding;
    private DatabaseReference mData;
    private Album_Adapter album_adapter;
    private ArrayList<Album> mListAlbum;
    private ArrayList<Album> mAlbumHienThi;
    private AlbumViewModel viewModel;

    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
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
        binding = FragmentAlbumBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(AlbumViewModel.class);

        mListAlbum = new ArrayList<>();
        mAlbumHienThi = new ArrayList<>();
        album_adapter = new Album_Adapter(getActivity(), mAlbumHienThi);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recyclerviewAlbum.setLayoutManager(linearLayoutManager);
        binding.recyclerviewAlbum.setAdapter(album_adapter);

        // Kiểm tra nếu dữ liệu đã có trong ViewModel rồi thì không tải lại từ Firebase
        if (viewModel.getAlbums().getValue() != null && !viewModel.getAlbums().getValue().isEmpty()) {
            // Dữ liệu đã có trong ViewModel, chỉ cần sử dụng lại
            mAlbumHienThi.clear();
            mAlbumHienThi.addAll(viewModel.getAlbums().getValue());
            album_adapter.notifyDataSetChanged();
        } else {
            // Dữ liệu chưa có trong ViewModel, cần tải từ Firebase
            loadAlbum();
        }

        binding.txtXemthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Album> danhsach = new ArrayList<>(viewModel.getAlbums().getValue());
                Intent intent = new Intent(getActivity(), DSPlaylistAndAlbumActivity.class);
                intent.putParcelableArrayListExtra("danhsachalbum", danhsach);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    public void loadAlbum() {
        mData = FirebaseDatabase.getInstance().getReference("Album");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListAlbum.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Album album = dataSnapshot.getValue(Album.class);
                    if (album != null) {
                        mListAlbum.add(album);
                    }
                }

                // Trộn ngẫu nhiên danh sách album
                Collections.shuffle(mListAlbum);

                // Cập nhật ViewModel và danh sách hiển thị
                viewModel.setAlbums(mListAlbum);
                mAlbumHienThi.clear();
                mAlbumHienThi.addAll(mListAlbum);


                album_adapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}