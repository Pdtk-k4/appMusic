package com.example.dahitamusic.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dahitamusic.Adapter.BannerViewPagerAdapter;
import com.example.dahitamusic.Adapter.Playlist_Adapter;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.Model.QuangCao;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentPlaylist2Binding;
import com.example.dahitamusic.databinding.FragmentPlaylistBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Playlist2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Playlist2Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentPlaylist2Binding binding;
    private DatabaseReference mData;
    private Playlist_Adapter playlist_adapter;
    private List<Playlist> mListPlaylist;

    public Playlist2Fragment() {
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
    public static Playlist2Fragment newInstance(String param1, String param2) {
        Playlist2Fragment fragment = new Playlist2Fragment();
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
        binding = FragmentPlaylist2Binding.inflate(inflater, container, false);

        binding.viewPagerPlaylist2.setOffscreenPageLimit(10);
        binding.viewPagerPlaylist2.setClipToPadding(true);
        binding.viewPagerPlaylist2.setClipChildren(true);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        binding.viewPagerPlaylist2.setPageTransformer(compositePageTransformer);


        mListPlaylist = new ArrayList<>();
        playlist_adapter = new Playlist_Adapter(mListPlaylist);
        binding.viewPagerPlaylist2.setAdapter(playlist_adapter);

        loadImgPlaylist();
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void loadImgPlaylist() {
        mData = FirebaseDatabase.getInstance().getReference("Playlist");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListPlaylist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Playlist playlist = dataSnapshot.getValue(Playlist.class);
                    if (playlist != null) {
                        mListPlaylist.add(playlist);
                    }
                }
                //loadRandomPlaylist(); // Gọi phương thức lấy playlist ngẫu nhiên
                playlist_adapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }


//    // Phương thức để lấy ngẫu nhiên 5 bài hát mỗi ngày
//    private void loadRandomPlaylist() {
//        SharedPreferences prefs = requireContext().getSharedPreferences("DailySongs", Context.MODE_PRIVATE);
//        int savedDay = prefs.getInt("day", -1);
//        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
//
//        if (savedDay != currentDay) {
//            // Nếu ngày đã thay đổi, lấy ngẫu nhiên 6 playlist
//            if (mListPlaylist.size() >= 6) { // Kiểm tra có ít nhất 6 playlist
//                Collections.shuffle(mListPlaylist); // Xáo trộn danh sách
//                List<Playlist> randomList = mListPlaylist.subList(0, 6); // Lấy 6 playlist đầu tiên
//                playlist_adapter.updatePlaylists(randomList); // Cập nhật adapter với danh sách ngẫu nhiên
//            } else {
//                // Nếu không đủ 6 playlist, có thể cập nhật toàn bộ danh sách
//                playlist_adapter.updatePlaylists(mListPlaylist);
//            }
//
//            prefs.edit().putInt("day", currentDay).apply();
//        } else {
//            // Nếu ngày không thay đổi, giữ nguyên adapter hiện tại
//            playlist_adapter.notifyDataSetChanged(); // Cập nhật UI nếu có sự thay đổi nào
//        }
//    }


    @Override
    public void onPause() {
        super.onPause();
    }
}