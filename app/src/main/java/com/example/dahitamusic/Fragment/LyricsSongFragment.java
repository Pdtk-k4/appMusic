package com.example.dahitamusic.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.Activity.PlayMusicActivity;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentLyricsSongBinding;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LyricsSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LyricsSongFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentLyricsSongBinding binding;
    private DatabaseReference mData;
    private List<BaiHat> lyricsBaiHat = new ArrayList<>();
    private int currentHighlightedLyric = -1;

    public LyricsSongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LyricsSongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LyricsSongFragment newInstance(String param1, String param2) {
        LyricsSongFragment fragment = new LyricsSongFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
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
        binding = FragmentLyricsSongBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void setSongLyrics(String songLyrics) {
        if (binding != null) {
            if (songLyrics != null) {
                String[] lines = songLyrics.split("(?<=[.,!?])\\s+");
                StringBuilder formattedLyrics = new StringBuilder();
                for (String line : lines) {
                    if (!line.isEmpty()) {
                        line = line.replaceAll("[.,!?]", "");
                        line = line.substring(0, 1).toUpperCase() + line.substring(1).toLowerCase();
                        formattedLyrics.append(line).append("\n");
                    }
                }
                binding.txtLyrics.setText(formattedLyrics.toString().trim());
            }
        }
    }

}