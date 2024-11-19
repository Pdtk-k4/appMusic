package com.example.dahitamusic.Adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Activity.PlayMusicActivity;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerViewPagerAdapter extends RecyclerView.Adapter<BannerViewPagerAdapter.BannerViewHolder> {

    private List<BaiHat> mListBaiHat;

    public BannerViewPagerAdapter(List<BaiHat> mListBaiHat) {
        this.mListBaiHat = mListBaiHat;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_quangcao, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        BaiHat baiHat = mListBaiHat.get(position);

        Picasso.get().load(baiHat.getAnhQuangCao()).placeholder(R.drawable.img_default).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PlayMusicActivity.class);
                intent.putExtra("cakhuc", baiHat);
                view.getContext().startActivity(intent);
            }
        });

    }

//    lấy ds bài hát theo id
//    private BaiHat findBaiHatById(String idBaiHat) {
//        for (BaiHat baiHat : mListBaiHat) {
//            if (baiHat.getIdBaiHat().equals(idBaiHat)) {
//                return baiHat;
//            }
//        }
//        return null;
//    }


    @Override
    public int getItemCount() {
        return Math.min(mListBaiHat.size(), 5);
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_quangcao);

        }
    }
}