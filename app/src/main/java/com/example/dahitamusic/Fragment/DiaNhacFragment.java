package com.example.dahitamusic.Fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentDiaNhacBinding;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiaNhacFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaNhacFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CircleImageView circleImageView;
    private TextView txttenbaihat, txttencasi;
    private ObjectAnimator objectAnimator;
    private BaiHat baiHat;

    public DiaNhacFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiaNhacFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiaNhacFragment newInstance(String param1, String param2) {
        DiaNhacFragment fragment = new DiaNhacFragment();
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
        // Inflate the layout for this fragment and initialize binding
        View view = inflater.inflate(R.layout.fragment_dia_nhac, container, false);
        circleImageView = view.findViewById(R.id.imgdianhac);
        txttenbaihat = view.findViewById(R.id.id_tenbaihat);
        txttencasi = view.findViewById(R.id.id_tencasi);

        objectAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0f, 360f);
        objectAnimator.setDuration(25000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        return view;
    }

    public void getAnhBaiHat(String anhBaiHat) {
        Picasso.get().load(anhBaiHat).into(circleImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                // Khi ảnh được tải thành công, bắt đầu hiệu ứng xoay
                if (!objectAnimator.isRunning()) {
                    objectAnimator.start();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("DiaNhacFragment", "Lỗi khi tải ảnh: " + e.getMessage());
            }
        });
    }


    // Hàm nhập tên bài hát
    public void setTenBaiHat(String tenBaiHat) {
        txttenbaihat.setText(tenBaiHat);
    }

    // Hàm nhập tên ca sĩ
    public void setTenCaSi(String tenCaSi) {
        txttencasi.setText(tenCaSi);
    }


    // Dừng xoay ảnh
    public void pauseAnimation() {
        if (objectAnimator.isRunning()) {
            objectAnimator.pause();
        }
    }

    // Tiếp tục xoay ảnh từ vị trí đã dừng
    public void resumeAnimation() {
        if (objectAnimator.isPaused()) {
            objectAnimator.resume();
        }
    }
}