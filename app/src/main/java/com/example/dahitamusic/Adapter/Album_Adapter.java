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

import com.example.dahitamusic.Activity.DSachBaiHatActivity;
import com.example.dahitamusic.Model.Album;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Album_Adapter extends RecyclerView.Adapter<Album_Adapter.AlbumViewHolder> {
    private Context context;
    private ArrayList<Album> mangAlbum;

    public Album_Adapter(Context context, ArrayList<Album> mangAlbum) {
        this.context = context;
        this.mangAlbum = mangAlbum;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = mangAlbum.get(position);
        holder.txtTenAlbum.setText(album.getTenAlbum());
        holder.txtTenCaSiAlbum.setText(album.getTenCaSiAlbum());
        Picasso.get().load(album.getAnhAlbum()).into(holder.imgAlbum);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DSachBaiHatActivity.class);
                intent.putExtra("Album", album);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Math.min(mangAlbum.size(), 6);
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgAlbum;
        private final TextView txtTenAlbum;
        private final TextView txtTenCaSiAlbum;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum = itemView.findViewById(R.id.img_album);
            txtTenAlbum = itemView.findViewById(R.id.txt_tenalbum);
            txtTenCaSiAlbum = itemView.findViewById(R.id.txt_tencasialbum);

        }
    }
}
