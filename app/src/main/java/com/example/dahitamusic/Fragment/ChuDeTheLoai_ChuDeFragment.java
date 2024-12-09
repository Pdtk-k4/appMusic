package com.example.dahitamusic.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.Adapter.XemThem_ChuDe_Adapter;
import com.example.dahitamusic.Adapter.XemThem_TheLoai_Adapter;
import com.example.dahitamusic.Model.ChuDe;
import com.example.dahitamusic.Model.TheLoai;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentChuDeTheLoaiChuDeBinding;
import com.example.dahitamusic.databinding.FragmentChuDeTheLoaiTheLoaiBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChuDeTheLoai_ChuDeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChuDeTheLoai_ChuDeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentChuDeTheLoaiChuDeBinding binding;
    private XemThem_ChuDe_Adapter adapter;
    private ArrayList<ChuDe> mListChuDe;
    private DatabaseReference mData;

    public ChuDeTheLoai_ChuDeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChuDeTheLoai_ChuDeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChuDeTheLoai_ChuDeFragment newInstance(String param1, String param2) {
        ChuDeTheLoai_ChuDeFragment fragment = new ChuDeTheLoai_ChuDeFragment();
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
        binding = FragmentChuDeTheLoaiChuDeBinding.inflate(inflater, container, false);
        mListChuDe = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        binding.rcvChude.setLayoutManager(gridLayoutManager);
        adapter = new XemThem_ChuDe_Adapter(mListChuDe);
        binding.rcvChude.setAdapter(adapter);
        loadTheLoai();
        return binding.getRoot();
    }

    private void loadTheLoai(){
        mData = FirebaseDatabase.getInstance().getReference("ChuDe");
//        Query query = mData.orderByChild("flag").equalTo(0);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListChuDe.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChuDe chude = dataSnapshot.getValue(ChuDe.class);
                    if (chude != null) {
                        mListChuDe.add(chude);
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