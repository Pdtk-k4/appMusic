package com.example.dahitamusic.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class dialod_ds_playlist_adapter extends RecyclerView.Adapter<dialod_ds_playlist_adapter.DialodDSPlaylistViewHolder> {
    private ArrayList<Playlist> mPlaylistlist;
    private IClickListner iClickListner;

    public interface IClickListner {
        void onClickItem(Playlist playlist);
    }

    public dialod_ds_playlist_adapter(ArrayList<Playlist> mPlaylistlist, IClickListner iClickListner) {
        this.mPlaylistlist = mPlaylistlist;
        this.iClickListner = iClickListner;
    }

    @NonNull
    @Override
    public DialodDSPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_add, parent, false);
        return new DialodDSPlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DialodDSPlaylistViewHolder holder, int position) {
        Playlist playlist = mPlaylistlist.get(position);
        holder.txt_playlist.setText(playlist.getTenPlaylist());
        Picasso.get().load(playlist.getAnhPlaylist()).placeholder(R.drawable.img_default).into(holder.img_playlist);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickListner.onClickItem(playlist);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlaylistlist.size();
    }

    public static class DialodDSPlaylistViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_playlist;
        private TextView txt_playlist;

        public DialodDSPlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            img_playlist = itemView.findViewById(R.id.img_playlist);
            txt_playlist = itemView.findViewById(R.id.txt_playlist);
        }
    }
}

