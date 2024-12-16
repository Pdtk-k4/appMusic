package com.example.dahitamusic.Adapter;

import android.content.Context;
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

public class PlayListSongAdapter extends RecyclerView.Adapter<PlayListSongAdapter.PlayListSongViewHolder> {
    private final ArrayList<BaiHat> songs;
    private Context context;
    private IClickListner mClickListner;
    public interface IClickListner {
        void onClickItem(BaiHat baiHat);
        void onClickMore(BaiHat baiHat);
    }

    // Cập nhật constructor để nhận listener
    public PlayListSongAdapter(ArrayList<BaiHat> songs, Context context, IClickListner mClickListner) {
        this.songs = songs;
        this.context = context;
        this.mClickListner = mClickListner;
    }

    @NonNull
    @Override
    public PlayListSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_baihat, parent, false);
        return new PlayListSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListSongViewHolder holder, int position) {
        BaiHat baiHat = songs.get(position);
        holder.songTitle.setText(baiHat.getTenBaiHat());
        holder.songArtist.setText(baiHat.getCaSi());
        Picasso.get().load(baiHat.getAnhBaiHat()).placeholder(R.drawable.img_default).into(holder.songImage);

        holder.itemView.setOnClickListener(view -> {
            mClickListner.onClickItem(baiHat);

        });
        holder.img_IconMore.setOnClickListener(view -> {
            mClickListner.onClickMore(baiHat);
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(songs.size(), 10);
    }

    // ViewHolder class
    public static class PlayListSongViewHolder extends RecyclerView.ViewHolder {
        private final TextView songTitle;
        private final TextView songArtist;
        private final ImageView songImage;
        private final ImageView img_IconMore;

        public PlayListSongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.txt_name_song);
            songArtist = itemView.findViewById(R.id.txt_name_casi);
            songImage = itemView.findViewById(R.id.img_song);
            img_IconMore = itemView.findViewById(R.id.icon_more);
        }
    }
}
