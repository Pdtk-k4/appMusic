package com.example.dahitamusic.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dahitamusic.Activity.DSachBaiHatActivity;
import com.example.dahitamusic.Activity.PlayMusicActivity;
import com.example.dahitamusic.Adapter.BaiHatYeuThich_Adapter;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentBaiHatBinding;
import com.example.dahitamusic.databinding.FragmentThuVienYeuthichBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThuVien_YeuthichFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThuVien_YeuthichFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<BaiHat> mangBaiHatYeuThich;
    private BaiHatYeuThich_Adapter baiHatYeuThich_Adapter;
    private FragmentThuVienYeuthichBinding binding;
    private DatabaseReference mData;

    public ThuVien_YeuthichFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThuVien_YeuthichFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThuVien_YeuthichFragment newInstance(String param1, String param2) {
        ThuVien_YeuthichFragment fragment = new ThuVien_YeuthichFragment();
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
        binding = FragmentThuVienYeuthichBinding.inflate(inflater, container, false);
        mangBaiHatYeuThich = new ArrayList<>();
        baiHatYeuThich_Adapter = new BaiHatYeuThich_Adapter(mangBaiHatYeuThich, new BaiHatYeuThich_Adapter.IClickListner() {
            @Override
            public void onClickHeart(BaiHat baiHat) {
                clickHeart(baiHat);
            }

            @Override
            public void onClickMore(BaiHat baiHat) {
                clickMore(baiHat);
            }
        });
        binding.rcvBaihaiyeuthich.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcvBaihaiyeuthich.setAdapter(baiHatYeuThich_Adapter);

        loadBaiHatYeuThich();
        return binding.getRoot();
    }

    private void clickMore(BaiHat baiHat) {
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

        img_iconHeart.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart_pink));
        img_iconHeart.setColorFilter(Color.parseColor("#F05080"), PorterDuff.Mode.SRC_ATOP);
        txt_addThuvien.setText(R.string.dathemvaothuvien);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        mData = FirebaseDatabase.getInstance().getReference("BaiHat");
        txt_addThuvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHeart(baiHat);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void clickHeart(BaiHat baiHat) {
        new AlertDialog.Builder(getContext())
                .setTitle(baiHat.getTenBaiHat())
                .setMessage("Xóa bài hát này khỏi thư viện?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mData = FirebaseDatabase.getInstance().getReference("BaiHat");
                        mData.child(baiHat.getIdBaiHat()).child("yeuThich").setValue(!baiHat.isYeuThich(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(getContext(), "Đã xóa bài hát khỏi thư viện", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void loadBaiHatYeuThich() {
        mData = FirebaseDatabase.getInstance().getReference("BaiHat");
        Query query = mData.orderByChild("yeuThich").equalTo(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mangBaiHatYeuThich.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BaiHat baiHat = dataSnapshot.getValue(BaiHat.class);
                    if (baiHat != null) {
                        mangBaiHatYeuThich.add(baiHat);
                    }
                }
                baiHatYeuThich_Adapter.notifyDataSetChanged();
                btnClick(mangBaiHatYeuThich);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void btnClick(ArrayList<BaiHat> baiHatList) {
        if (baiHatList == null || baiHatList.isEmpty()) {
            Toast.makeText(getContext(), "Không có bài hát nào để phát", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.btnPhatngaynhien.setOnClickListener(view -> {
            Collections.shuffle(baiHatList);
            Intent intent = new Intent(getContext(), PlayMusicActivity.class);
            intent.putExtra("BaiHat", baiHatList);
            startActivity(intent);
        });

    }
}