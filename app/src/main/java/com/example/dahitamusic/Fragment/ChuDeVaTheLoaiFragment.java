package com.example.dahitamusic.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.Adapter.Album_Adapter;
import com.example.dahitamusic.Adapter.Chude_Theloai_Adapter;
import com.example.dahitamusic.Model.Album;
import com.example.dahitamusic.Model.TheLoai;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentAlbumBinding;
import com.example.dahitamusic.databinding.FragmentChuDeVaTheLoaiBinding;
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
 * Use the {@link ChuDeVaTheLoaiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChuDeVaTheLoaiFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentChuDeVaTheLoaiBinding binding;
    private DatabaseReference mData;
    private Chude_Theloai_Adapter chudetheloai_adapter;
    private List<TheLoai> mListTheLoai;

    public ChuDeVaTheLoaiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChuDeVaTheLoaiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChuDeVaTheLoaiFragment newInstance(String param1, String param2) {
        ChuDeVaTheLoaiFragment fragment = new ChuDeVaTheLoaiFragment();
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
        binding = FragmentChuDeVaTheLoaiBinding.inflate(inflater, container, false);

        mListTheLoai = new ArrayList<>();
        chudetheloai_adapter = new Chude_Theloai_Adapter(getActivity(), (ArrayList<TheLoai>) mListTheLoai);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recyclerviewChudevatheloai.setLayoutManager(linearLayoutManager);
        binding.recyclerviewChudevatheloai.setAdapter(chudetheloai_adapter);

        loadChudeTheloai();
        return binding.getRoot();
    }

    public void loadChudeTheloai() {
        mData = FirebaseDatabase.getInstance().getReference("TheLoai");
        Query query = mData.orderByChild("flag").startAt(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListTheLoai.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TheLoai theLoai = dataSnapshot.getValue(TheLoai.class);
                    if (theLoai != null) {
                        mListTheLoai.add(theLoai);
                        Log.d("PlayMusicActivity", "Sending Image URL: " + theLoai.getTenTheLoai());
                    }
                }

                chudetheloai_adapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}