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

import java.util.ArrayList;
import java.util.List;

public class DS_Playlist_Adapter extends  RecyclerView.Adapter<DS_Playlist_Adapter.dsPlaylistViewHolder> {
    private final ArrayList<Playlist> mListPlaylist;

    public DS_Playlist_Adapter(ArrayList<Playlist> mListPlaylist) {
        this.mListPlaylist = mListPlaylist;
    }

    @NonNull
    @Override
    public dsPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dansach_playlis_album, parent, false);
        return new dsPlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull dsPlaylistViewHolder holder, int position) {
        Playlist listPlaylist = mListPlaylist.get(position);
        Picasso.get().load(listPlaylist.getAnhPlaylist()).placeholder(R.drawable.img_default).into(holder.imageView);
        holder.textView.setText(listPlaylist.getTenPlaylist());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DSachBaiHatActivity.class);
                intent.putExtra("Playlist", listPlaylist);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(mListPlaylist.size(), 10);
    }

    public class dsPlaylistViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, icon_heart;
        private TextView textView ;
        public dsPlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_playlist);
            textView = itemView.findViewById(R.id.txt_tenplaylist);
        }
    }
}
