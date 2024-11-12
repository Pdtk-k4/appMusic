package com.example.dahitamusic.Adapter;

import android.content.Context;
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

public class ThuVien_Playlist_Adapter extends RecyclerView.Adapter<ThuVien_Playlist_Adapter.ThuVien_PlaylistViewHolder> {
    private ArrayList<Playlist> mangPlaylist;
    private Context context;

    public ThuVien_Playlist_Adapter(ArrayList<Playlist> mangPlaylist, Context context) {
        this.mangPlaylist = mangPlaylist;
        this.context = context;
    }

    @NonNull
    @Override
    public ThuVien_PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_playlistgoiy, parent, false);
        return new ThuVien_PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThuVien_PlaylistViewHolder holder, int position) {
        Playlist playlist = mangPlaylist.get(position);
        holder.txt_tenplaylist.setText(playlist.getTenPlaylist());
        Picasso.get().load(playlist.getAnhPlaylist()).into(holder.img_playlist);
    }

    @Override
    public int getItemCount() {
        return Math.min(mangPlaylist.size(), 10);
    }

    public class ThuVien_PlaylistViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_tenplaylist;
        private ImageView img_playlist;
        public ThuVien_PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_tenplaylist = itemView.findViewById(R.id.txt_name_playlist);
            img_playlist = itemView.findViewById(R.id.img_playlist);
        }
    }
}
