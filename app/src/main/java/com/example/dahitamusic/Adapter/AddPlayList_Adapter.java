package com.example.dahitamusic.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Activity.PlayMusicActivity;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class AddPlayList_Adapter extends RecyclerView.Adapter<AddPlayList_Adapter.addPlayListViewHolder> {
    private ArrayList<BaiHat> mListBaiHat;
    private IClickListner mClickListner;

    public interface IClickListner {
        void onClickHeart(BaiHat baiHat);
    }
    public AddPlayList_Adapter(ArrayList<BaiHat> mListBaiHat, IClickListner listner) {
        this.mListBaiHat = mListBaiHat;
        this.mClickListner = listner;
    }

    public void setFilteredList(ArrayList<BaiHat> filteredList) {
        this.mListBaiHat = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public addPlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_addplaylist, parent, false);
        return new addPlayListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull addPlayListViewHolder holder, int position) {
        BaiHat baiHat = mListBaiHat.get(position);
        holder.txt_name_song.setText(baiHat.getTenBaiHat());
        holder.txt_name_casi.setText(baiHat.getCaSi());
        Picasso.get().load(baiHat.getAnhBaiHat()).placeholder(R.drawable.img_default).into(holder.img_song);

        holder.iconAdd.setOnClickListener(view -> {
            mClickListner.onClickHeart(baiHat);
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), PlayMusicActivity.class);
            intent.putExtra("BaiHat", mListBaiHat);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(mListBaiHat.size(), 10);
    }

    public static class addPlayListViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_song, iconAdd;
        private TextView txt_name_song, txt_name_casi;

        public addPlayListViewHolder(@NonNull View itemView) {
            super(itemView);
            img_song = itemView.findViewById(R.id.img_song);
            txt_name_song = itemView.findViewById(R.id.txt_name_song);
            txt_name_casi = itemView.findViewById(R.id.txt_name_casi);
            iconAdd = itemView.findViewById(R.id.icon_add);
        }
    }
}

