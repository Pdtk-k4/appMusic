package com.example.dahitamusic.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dahitamusic.Adapter.ThuVien_Playlist_Adapter;
import com.example.dahitamusic.Adapter.TimKiemAdapter;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentTimKiemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimKiemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimKiemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentTimKiemBinding binding;
    private TimKiemAdapter timKiemAdapter;
    private ArrayList<BaiHat> mListBaiHat;
    private DatabaseReference mData;

    public TimKiemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimKiemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimKiemFragment newInstance(String param1, String param2) {
        TimKiemFragment fragment = new TimKiemFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTimKiemBinding.inflate(inflater, container, false);

        mListBaiHat = new ArrayList<>();
        baiHatApdapter();
        loadBaiHatGoiY();
        searchBaiHat();
        return binding.getRoot();
    }

    //Bài hát
    private void baiHatApdapter(){
        timKiemAdapter = new TimKiemAdapter(mListBaiHat, new TimKiemAdapter.IClickListner() {
            @Override
            public void onClick(BaiHat baiHat) {
                clickIconMoreOnpenDialog(baiHat);
            }
        });
        binding.recyclerviewBaihat.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerviewBaihat.setAdapter(timKiemAdapter);
    }

    //firebase bài hát
    public void loadBaiHatGoiY() {
        mData = FirebaseDatabase.getInstance().getReference("BaiHat");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListBaiHat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BaiHat baiHat = dataSnapshot.getValue(BaiHat.class);
                    if (baiHat != null) {
                        mListBaiHat.add(baiHat);
                    }
                }
                Log.d("FirebaseData", "Tải " + mListBaiHat.size() + " bài hát từ Firebase");
                //timKiemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    //Search
    private void searchBaiHat(){
        binding.searchView.setQueryHint("Tìm kiếm bài hát, playlist...");
        binding.searchView.requestFocus();

        binding.txtHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    timKiemAdapter.setFilteredList(new ArrayList<>());
                } else {
                    filterList(newText);
                }
                return false;
            }
        });
    }

    //trả về danh sách bài hát đã lọc
    private void filterList(String text) {
        //Lọc bài hát
        ArrayList<BaiHat> filteredList = new ArrayList<>();
        for (BaiHat baiHat : mListBaiHat) {
            if (baiHat.getTenBaiHat() != null && baiHat.getTenBaiHat().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(baiHat);
            }
        }

        if (filteredList.isEmpty()) {
            showCenteredToast("Không tìm thấy kết quả nào");
            timKiemAdapter.setFilteredList(new ArrayList<>());
        } else {
            // Cập nhật danh sách đã lọc cho adapter
            timKiemAdapter.setFilteredList(filteredList);
        }
    }

    private void clickIconMoreOnpenDialog(BaiHat baiHat) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_more_baihat);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView img_close = dialog.findViewById(R.id.img_close);
        ImageView img_baiHat = dialog.findViewById(R.id.img_baiHat);
        ImageView img_iconHeart = dialog.findViewById(R.id.img_iconHeart);
        ImageView img_IconAddPlaylist = dialog.findViewById(R.id.img_IconAddPlaylist);
        ImageView img_IconDownload = dialog.findViewById(R.id.img_IconDownload);

        TextView txt_tenBaiHat = dialog.findViewById(R.id.txt_tenBaiHat);
        TextView txt_tenCaSi = dialog.findViewById(R.id.txt_tenCaSi);
        TextView txt_addThuvien = dialog.findViewById(R.id.txt_addThuvien);
        TextView txt_addPlaylist = dialog.findViewById(R.id.txt_addPlaylist);
        TextView txt_download = dialog.findViewById(R.id.txt_download);

        txt_tenBaiHat.setText(baiHat.getTenBaiHat());
        txt_tenCaSi.setText(baiHat.getCaSi());
        Picasso.get().load(baiHat.getAnhBaiHat()).into(img_baiHat);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        mData = FirebaseDatabase.getInstance().getReference("BaiHat");
        if (baiHat != null) {
            if (baiHat.isYeuThich()) {
                img_iconHeart.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart_pink));
                img_iconHeart.setColorFilter(Color.parseColor("#F05080"), PorterDuff.Mode.SRC_ATOP);
            } else {
                img_iconHeart.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart));
                img_iconHeart.clearColorFilter();
            }
        }
        txt_addThuvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean currentState = baiHat.isYeuThich();
                baiHat.setYeuThich(!currentState);

                if (baiHat.isYeuThich()) {
                    mData.child(baiHat.getIdBaiHat()).updateChildren(baiHat.toMap());
                    img_iconHeart.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart_pink));
                    img_iconHeart.setColorFilter(Color.parseColor("#F05080"), PorterDuff.Mode.SRC_ATOP);
                    txt_addThuvien.setText(R.string.dathemvaothuvien);

                    // Hiển thị thông báo: Đã thêm playlist vào thư viện
                    showCenteredToast("Đã thêm bài hát này vào thư viện");
                }else {
                    mData.child(baiHat.getIdBaiHat()).updateChildren(baiHat.toMap());
                    img_iconHeart.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart));
                    img_iconHeart.clearColorFilter();
                    txt_addThuvien.setText(R.string.themvaothuvien);

                    // Hiển thị thông báo: Đã xóa playlist khỏi thư viện
                    showCenteredToast("Đã xóa bài hát khỏi thư viện");
                }
            }

        });

        dialog.show();
    }

    // Phương thức hiển thị Toast ở giữa màn hình
    private void showCenteredToast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        toast.setGravity(android.view.Gravity.CENTER, 0, 0); // Đặt Toast ở giữa màn hình
        toast.show();
    }
}