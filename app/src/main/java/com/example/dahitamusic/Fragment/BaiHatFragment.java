package com.example.dahitamusic.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.Adapter.BaiHatGoiY_Adapter;
import com.example.dahitamusic.Model.Album;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.R;
import com.example.dahitamusic.ViewModel.AlbumViewModel;
import com.example.dahitamusic.ViewModel.BaiHatViewModel;
import com.example.dahitamusic.databinding.FragmentBaiHatBinding;
import com.example.dahitamusic.databinding.FragmentPlayListSongBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaiHatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaiHatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<BaiHat> mbaihat;
    private ArrayList<BaiHat> mbaihathienthi;
    private BaiHatGoiY_Adapter baihatgoiy_adapter;
    private FragmentBaiHatBinding binding;
    private DatabaseReference mData;
    private BaiHatViewModel viewModel;

    public BaiHatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BaiHatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BaiHatFragment newInstance(String param1, String param2) {
        BaiHatFragment fragment = new BaiHatFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBaiHatBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(BaiHatViewModel.class);

        mbaihat = new ArrayList<>();
        mbaihathienthi = new ArrayList<>();
        baihatgoiy_adapter = new BaiHatGoiY_Adapter(mbaihathienthi, getActivity());
        binding.recyclerviewBaihatgoiy.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerviewBaihatgoiy.setAdapter(baihatgoiy_adapter);

        // Kiểm tra nếu dữ liệu đã có trong ViewModel rồi thì không tải lại từ Firebase
        if (viewModel.getBaiHats().getValue() != null && !viewModel.getBaiHats().getValue().isEmpty()) {
            // Dữ liệu đã có trong ViewModel, chỉ cần sử dụng lại
            mbaihathienthi.clear();
            mbaihathienthi.addAll(viewModel.getBaiHats().getValue());
            baihatgoiy_adapter.notifyDataSetChanged();
        } else {
            // Dữ liệu chưa có trong ViewModel, cần tải từ Firebase
            loadBaiHatGoiY();
        }

        binding.txtLammoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.shuffle(mbaihat);
                viewModel.setBaiHats(mbaihat);
                mbaihathienthi.clear();
                mbaihathienthi.addAll(mbaihat);
                baihatgoiy_adapter.notifyDataSetChanged();
            }
        });
        return binding.getRoot();
    }

    public void loadBaiHatGoiY() {
        mData = FirebaseDatabase.getInstance().getReference("BaiHat");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mbaihat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BaiHat baiHat = dataSnapshot.getValue(BaiHat.class);
                    if (baiHat != null) {
                        mbaihat.add(baiHat);
                    }
                }
                // Trộn ngẫu nhiên danh sách album
                Collections.shuffle(mbaihat);
                // Cập nhật ViewModel và danh sách hiển thị
                viewModel.setBaiHats(mbaihat);
                mbaihathienthi.clear();
                mbaihathienthi.addAll(mbaihat);
                baihatgoiy_adapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}