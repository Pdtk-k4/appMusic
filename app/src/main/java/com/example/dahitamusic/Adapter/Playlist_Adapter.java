package com.example.dahitamusic.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Activity.DSachBaiHatActivity;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Playlist_Adapter extends RecyclerView.Adapter<Playlist_Adapter.PlaylistViewHolder> {

    private final List<Playlist> mListPlaylist;
    ;

    public Playlist_Adapter(List<Playlist> mListPlaylist) {
        this.mListPlaylist = mListPlaylist;
    }


    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = mListPlaylist.get(position);
        if (playlist == null) {
            return;
        }
        Picasso.get().load(playlist.getAnhPlaylist()).into(holder.imageView);
        holder.textView.setText(playlist.getTenPlaylist());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DSachBaiHatActivity.class);
                intent.putExtra("Playlist", playlist);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(mListPlaylist.size(), 6);
    }

    public void updatePlaylists(List<Playlist> playlists) {
        this.mListPlaylist.clear();
        this.mListPlaylist.addAll(playlists);
        notifyDataSetChanged();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_playlist);
            textView = itemView.findViewById(R.id.txt_tenplaylist);
        }
    }
}
