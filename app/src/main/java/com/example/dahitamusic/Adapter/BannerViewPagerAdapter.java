package com.example.dahitamusic.Adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Activity.DSachBaiHatActivity;
import com.example.dahitamusic.Model.QuangCao;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerViewPagerAdapter extends RecyclerView.Adapter<BannerViewPagerAdapter.BannerViewHolder> {

    private List<QuangCao> mlistQuangCao;

    public BannerViewPagerAdapter(List<QuangCao> mlistQuangCao) {
        this.mlistQuangCao = mlistQuangCao;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_quangcao, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        QuangCao quangCao = mlistQuangCao.get(position);
        if (quangCao == null) {
            return;
        }

        Picasso.get().load(quangCao.getAnhQuangCao()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DSachBaiHatActivity.class);
                intent.putExtra("QuangCao", quangCao); // Truyền QuangCao qua Intent
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
        if (mlistQuangCao != null) {
            return mlistQuangCao.size();
        }
        return 0;
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_quangcao);

        }
    }
}