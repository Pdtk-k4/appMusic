package com.example.dahitamusic.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.Adapter.BannerViewPagerAdapter;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.ViewModel.BaiHatViewModel;
import com.example.dahitamusic.databinding.FragmentQuangCaoBinding;
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
 * Use the {@link QuangCaoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuangCaoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentQuangCaoBinding binding;
    private DatabaseReference mData;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private ArrayList<BaiHat> mListBaiHat;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (binding.viewPager.getCurrentItem() == mListBaiHat.size() - 1) {
                binding.viewPager.setCurrentItem(0);
            } else {
                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
            }

        }
    };

    public QuangCaoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuangCaoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuangCaoFragment newInstance(String param1, String param2) {
        QuangCaoFragment fragment = new QuangCaoFragment();
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
        binding = FragmentQuangCaoBinding.inflate(inflater, container, false);

        mListBaiHat = new ArrayList<>();
        bannerViewPagerAdapter = new BannerViewPagerAdapter(mListBaiHat);

        binding.viewPager.setAdapter(bannerViewPagerAdapter);
        binding.circleIndicator.setViewPager(binding.viewPager);

        loadImgQuangCao();

        //thời gian chuyển ảnh
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 4000);
            }
        });
        return binding.getRoot();
    }

    public void loadImgQuangCao() {
        mData = FirebaseDatabase.getInstance().getReference("BaiHat");
        Query query = mData.orderByChild("idQuangCao").equalTo("qc");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListBaiHat.clear(); // Xoá danh sách trước khi thêm mới
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BaiHat baiHat = dataSnapshot.getValue(BaiHat.class);
                    if (baiHat != null) {
                        mListBaiHat.add(baiHat); // Thêm từng đối tượng vào danh sách
                    }
                }
                bannerViewPagerAdapter.notifyDataSetChanged();
                binding.circleIndicator.setViewPager(binding.viewPager);// Cập nhật adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

//    // Phương thức để lấy ngẫu nhiên 5 bài hát mỗi ngày
//    private void loadRandomBanner() {
//        SharedPreferences prefs = requireContext().getSharedPreferences("DailySongs", Context.MODE_PRIVATE);
//        int savedDay = prefs.getInt("day", -1);
//
//        // Lấy ngày hiện tại
//        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
//
//        if (savedDay != currentDay) {
//            // Nếu ngày đã thay đổi, lấy ngẫu nhiên 5 bài hát
//            if (mListQuangCao.size() > 5) {
//                Collections.shuffle(mListQuangCao); // Trộn danh sách ngẫu nhiên
//                List<QuangCao> randomList = mListQuangCao.subList(0, 5); // Lấy 5 bài hát đầu tiên sau khi trộn
//                bannerViewPagerAdapter = new BannerViewPagerAdapter(randomList);
//            }
//
//            // Cập nhật lại adapter và indicator
//            binding.viewPager.setAdapter(bannerViewPagerAdapter);
//            binding.circleIndicator.setViewPager(binding.viewPager);
//
//            // Lưu ngày hiện tại
//            prefs.edit().putInt("day", currentDay).apply();
//        }
//    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 4000);
    }
}