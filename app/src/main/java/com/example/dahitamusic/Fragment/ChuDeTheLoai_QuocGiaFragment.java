package com.example.dahitamusic.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.Adapter.XemThem_TheLoai_Adapter;
import com.example.dahitamusic.Model.TheLoai;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentChuDeTheLoaiQuocGiaBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChuDeTheLoai_QuocGiaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChuDeTheLoai_QuocGiaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentChuDeTheLoaiQuocGiaBinding binding;
    private XemThem_TheLoai_Adapter adapter;
    private ArrayList<TheLoai> mListTheLoai;
    private DatabaseReference mData;

    public ChuDeTheLoai_QuocGiaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChuDeTheLoai_QuocGiaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChuDeTheLoai_QuocGiaFragment newInstance(String param1, String param2) {
        ChuDeTheLoai_QuocGiaFragment fragment = new ChuDeTheLoai_QuocGiaFragment();
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
        binding = FragmentChuDeTheLoaiQuocGiaBinding.inflate(inflater, container, false);
        mListTheLoai = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        binding.rcvQuocgia.setLayoutManager(gridLayoutManager);
        adapter = new XemThem_TheLoai_Adapter(mListTheLoai);
        binding.rcvQuocgia.setAdapter(adapter);

        loadQuocGia();

        return binding.getRoot();
    }

    private void loadQuocGia(){
        mData = FirebaseDatabase.getInstance().getReference("TheLoai");
        Query query = mData.orderByChild("quocGia").startAt(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListTheLoai.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TheLoai theLoai = dataSnapshot.getValue(TheLoai.class);
                    if (theLoai != null) {
                        mListTheLoai.add(theLoai);
                    }
                }

                adapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}