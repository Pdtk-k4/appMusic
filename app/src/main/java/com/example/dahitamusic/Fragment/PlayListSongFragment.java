package com.example.dahitamusic.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.Activity.PlayMusicActivity;
import com.example.dahitamusic.Adapter.PlayListSongAdapter;
import com.example.dahitamusic.Adapter.SongsAdapter;
import com.example.dahitamusic.databinding.FragmentPlayListSongBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayListSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayListSongFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentPlayListSongBinding binding;
    private PlayListSongAdapter playListSongAdapter;

    public PlayListSongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayListSongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayListSongFragment newInstance(String param1, String param2) {
        PlayListSongFragment fragment = new PlayListSongFragment();
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
        binding = FragmentPlayListSongBinding.inflate(inflater, container, false);

        if(!PlayMusicActivity.mangbaihat.isEmpty()){
            playListSongAdapter = new PlayListSongAdapter(PlayMusicActivity.mangbaihat, getActivity());
            binding.recyclerViewPlaybaihat.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyclerViewPlaybaihat.setAdapter(playListSongAdapter);
        };
//        if(!PlayMusicActivity.mangpodcast.isEmpty()){
//            playListSongAdapter = new PlayListSongAdapter(PlayMusicActivity.mangpodcast, getActivity());
//            binding.recyclerViewPlaybaihat.setLayoutManager(new LinearLayoutManager(getActivity()));
//            binding.recyclerViewPlaybaihat.setAdapter(playListSongAdapter);
//        }


        return binding.getRoot();
    }
}