package com.example.dahitamusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BaiHatGoiY_Adapter extends RecyclerView.Adapter<BaiHatGoiY_Adapter.BaiHatGoiYViewHolder> {
    private ArrayList<BaiHat> mangbaihat;
    private Context context;

    public BaiHatGoiY_Adapter(ArrayList<BaiHat> mangbaihat, Context context) {
        this.mangbaihat = mangbaihat;
        this.context = context;
    }

    @NonNull
    @Override
    public BaiHatGoiYViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_baihatgoiy, parent, false);
        return new BaiHatGoiYViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaiHatGoiYViewHolder holder, int position) {
        BaiHat baiHat = mangbaihat.get(position);
        holder.txt_tenbaihat.setText(baiHat.getTenBaiHat());
        holder.txt_casi.setText(baiHat.getCaSi());
        Picasso.get().load(baiHat.getAnhBaiHat()).into(holder.img_baihat);
    }

    @Override
    public int getItemCount() {
        return Math.min(mangbaihat.size(), 5);
    }

    public class BaiHatGoiYViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_tenbaihat, txt_casi;
        private ImageView img_baihat;
        public BaiHatGoiYViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_tenbaihat = itemView.findViewById(R.id.txt_name_song);
            txt_casi = itemView.findViewById(R.id.txt_name_casi);
            img_baihat = itemView.findViewById(R.id.img_song);
        }
    }
}
