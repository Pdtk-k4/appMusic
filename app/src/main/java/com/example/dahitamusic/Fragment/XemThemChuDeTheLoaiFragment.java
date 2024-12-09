package com.example.dahitamusic.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentXemThemChuDeTheLoaiBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link XemThemChuDeTheLoaiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class XemThemChuDeTheLoaiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentXemThemChuDeTheLoaiBinding binding;

    public XemThemChuDeTheLoaiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment XemThemChuDeTheLoaiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static XemThemChuDeTheLoaiFragment newInstance(String param1, String param2) {
        XemThemChuDeTheLoaiFragment fragment = new XemThemChuDeTheLoaiFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentXemThemChuDeTheLoaiBinding.inflate(inflater, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(binding.toolbar);
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setTitle("Chủ đề và thể loại");
            }
            binding.toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
        }
        return binding.getRoot();
    }
}