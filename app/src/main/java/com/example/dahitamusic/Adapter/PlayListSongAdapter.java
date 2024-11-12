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

    // Cập nhật constructor để nhận listener
    public PlayListSongAdapter(ArrayList<BaiHat> songs, Context context) {
        this.songs = songs;
        this.context = context;
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
        Picasso.get().load(baiHat.getAnhBaiHat()).into(holder.songImage);

//        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(context, PlayMusicActivity.class);
//            intent.putExtra("cakhuc", songs); // Truyền toàn bộ danh sách bài hát
//            intent.putExtra("position", position); // Truyền vị trí của bài hát được chọn
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return Math.min(songs.size(), 10);
    }

    // ViewHolder class
    public class PlayListSongViewHolder extends RecyclerView.ViewHolder {
        private final TextView songTitle;
        private final TextView songArtist;
        private final ImageView songImage;

        public PlayListSongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.txt_name_song);
            songArtist = itemView.findViewById(R.id.txt_name_casi);
            songImage = itemView.findViewById(R.id.img_song);
        }
    }
}
