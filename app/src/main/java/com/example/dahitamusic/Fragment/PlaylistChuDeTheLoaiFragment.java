package com.example.dahitamusic.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.Adapter.DS_Album_Adapter;
import com.example.dahitamusic.Adapter.DS_Playlist_Adapter;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.ChuDe;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.Model.TheLoai;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentPlaylistChuDeTheLoaiBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistChuDeTheLoaiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistChuDeTheLoaiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentPlaylistChuDeTheLoaiBinding binding;
    private ChuDe chuDe;
    private TheLoai theLoai;
    private ArrayList<Playlist> mListPlaylist;
    private DatabaseReference mData;
    private DS_Playlist_Adapter playlist_adapter;
    private boolean isExpended = true;

    public PlaylistChuDeTheLoaiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistChuDeTheLoaiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistChuDeTheLoaiFragment newInstance(String param1, String param2) {
        PlaylistChuDeTheLoaiFragment fragment = new PlaylistChuDeTheLoaiFragment();
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
        binding = FragmentPlaylistChuDeTheLoaiBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            chuDe = getArguments().getParcelable("ChuDe");
            theLoai = getArguments().getParcelable("TheLoai");
        }

        mListPlaylist = new ArrayList<>();

        initToolbar();
        initToolbanAnimation();

        if(chuDe != null){
            loadPlaylist_ChuDe(chuDe.getIdChuDe());
        }
        if(theLoai != null){
            loadPlaylist_Theloai(theLoai.getIdTheLoai());
        }

        return binding.getRoot();
    }

    private void initToolbar(){
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(binding.toolbar);
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
                if(chuDe != null){
//                    binding.appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//                        @Override
//                        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                            if (Math.abs(verticalOffset) >= binding.appbarlayout.getTotalScrollRange()) {
//                                // Người dùng đã cuộn hết, hiển thị tiêu đề
//                                binding.collapsingtoolbar.setTitle(chuDe.getTenChuDe());
//                            } else {
//                                // Khi chưa cuộn, ẩn tiêu đề
//                                binding.collapsingtoolbar.setTitle("");
//                            }
//                        }
//                    });
                    activity.getSupportActionBar().setTitle("");
                    Picasso.get().load(chuDe.getAnhChuDe()).placeholder(R.drawable.img_default).into(binding.imgAnh);
                }
                if(theLoai != null){
//                    binding.appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//                        @Override
//                        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                            if (Math.abs(verticalOffset) >= binding.appbarlayout.getTotalScrollRange()) {
//                                // Người dùng đã cuộn hết, hiển thị tiêu đề
//                                binding.collapsingtoolbar.setTitle(theLoai.getTenTheLoai());
//                            } else {
//                                // Khi chưa cuộn, ẩn tiêu đề
//                                binding.collapsingtoolbar.setTitle("");
//                            }
//                        }
//                    });
                    activity.getSupportActionBar().setTitle("");
                    Picasso.get().load(theLoai.getAnhTheLoai()).placeholder(R.drawable.img_default).into(binding.imgAnh);
                }

            }
            binding.toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
        }
    }

    private void getData(ArrayList<Playlist> mListPlaylist){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        binding.rcv.setLayoutManager(gridLayoutManager);
        playlist_adapter  = new DS_Playlist_Adapter(mListPlaylist);
        binding.rcv.setAdapter(playlist_adapter);
    }

    private void loadPlaylist_ChuDe(String idChuDe){
        mData = FirebaseDatabase.getInstance().getReference("Playlist");
        Query query = mData.orderByChild("idChuDe").equalTo(idChuDe);
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
                getData(mListPlaylist);
//                playlist_adapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void loadPlaylist_Theloai(String idTheLoai){
        mData = FirebaseDatabase.getInstance().getReference("Playlist");
        Query query = mData.orderByChild("idTheLoai").equalTo(idTheLoai);
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
                getData(mListPlaylist);
//                playlist_adapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void initToolbanAnimation(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_default);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                binding.collapsingtoolbar.setStatusBarScrimColor(getResources().getColor(R.color.black_trans));
            }
        });

        binding.appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(Math.abs(verticalOffset) > 200 ){
                    isExpended = false;
                } else{
                    isExpended = true;
                }
                requireActivity().invalidateOptionsMenu();
            }
        });
    }
}