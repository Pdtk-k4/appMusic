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
import com.example.dahitamusic.Model.Podcast;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Podcast_Adapter extends RecyclerView.Adapter<Podcast_Adapter.PodcastViewHolder> {
    private ArrayList<Podcast> mlistPodcast;
    public Podcast_Adapter(ArrayList<Podcast> listPodcast) {
        this.mlistPodcast = listPodcast;
    }
    @NonNull
    @Override
    public PodcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_podcast, parent, false);
        return new PodcastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PodcastViewHolder holder, int position) {
        Podcast podcast = mlistPodcast.get(position);
        holder.ten_podcast.setText(podcast.getTenPodcast());
        holder.tacGia_podcast.setText(podcast.getTacGia());
        Picasso.get().load(podcast.getAnhPodcast()).into(holder.img_podcast);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(),PlayMusicActivity.class);
            intent.putExtra("Podcast", mlistPodcast); // Truyền toàn bộ danh sách bài hát
            intent.putExtra("position", position); // Truyền vị trí của bài hát được chọn
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mlistPodcast.size();
    }

    public static class PodcastViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_podcast;
        private TextView ten_podcast;
        private TextView tacGia_podcast;

        public PodcastViewHolder(@NonNull View itemView) {
            super(itemView);
            img_podcast = itemView.findViewById(R.id.img_podcast);
            ten_podcast = itemView.findViewById(R.id.txt_tenPodcast);
            tacGia_podcast = itemView.findViewById(R.id.txt_tenTacGia);

        }
    }
}
