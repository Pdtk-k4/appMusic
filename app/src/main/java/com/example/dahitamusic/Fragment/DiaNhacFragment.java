package com.example.dahitamusic.Fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentDiaNhacBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private ObjectAnimator objectAnimator;
    private FragmentDiaNhacBinding binding;

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
        binding = FragmentDiaNhacBinding.inflate(inflater, container, false);

        objectAnimator = ObjectAnimator.ofFloat(binding.imgdianhac, "rotation", 0f, 360f);
        objectAnimator.setDuration(25000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());

        return binding.getRoot();
    }

    public void clickIconHeart(String idBaiHat) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user.getUid())
                .child("baiHatYeuThich");

        favoritesRef.child(idBaiHat).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isFavorite = snapshot.exists(); // Kiểm tra bài hát đã được yêu thích chưa

                if (isFavorite) {
                    if (isAdded() && getContext() != null) {
                        binding.imgbtnheart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.heart_pink));
                    }
                } else {
                    if (isAdded() && getContext() != null) {
                        binding.imgbtnheart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_heart_white));
                    }
                }

                // Xử lý khi nhấn nút "thêm vào thư viện"
                binding.imgbtnheart.setOnClickListener(view -> {
                    if (isFavorite) {
                        // Xóa bài hát khỏi thư viện
                        favoritesRef.child(idBaiHat).removeValue();
                        if (isAdded() && getContext() != null) {
                            binding.imgbtnheart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_heart_white));
                        }
                        showCenteredToast("Đã xóa bài hát khỏi thư viện");
                    } else {
                        // Thêm bài hát vào thư viện
                        favoritesRef.child(idBaiHat).setValue(true);
                        if (isAdded() && getContext() != null) {
                            binding.imgbtnheart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.heart_pink));
                        }
                        showCenteredToast("Đã thêm bài hát này vào thư viện");
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Lỗi khi tải trạng thái yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCenteredToast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        toast.setGravity(android.view.Gravity.CENTER, 0, 0); // Đặt Toast ở giữa màn hình
        toast.show();
    }


    public void getAnhBaiHat(String anhBaiHat) {
        Picasso.get().load(anhBaiHat).into(binding.imgdianhac, new com.squareup.picasso.Callback() {
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
        binding.idTenbaihat.setText(tenBaiHat);
    }

    // Hàm nhập tên ca sĩ
    public void setTenCaSi(String tenCaSi) {
        binding.idTencasi.setText(tenCaSi);
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