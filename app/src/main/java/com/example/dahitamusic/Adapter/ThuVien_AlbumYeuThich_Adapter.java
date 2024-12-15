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
import com.example.dahitamusic.Model.Album;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ThuVien_AlbumYeuThich_Adapter extends RecyclerView.Adapter<ThuVien_AlbumYeuThich_Adapter.thuVienAlbumYeuThichViewHolder> {
    private ArrayList<Album> mListAlbums;
    private IClickListner mClickListner;
    public ThuVien_AlbumYeuThich_Adapter(ArrayList<Album> mListAlbums, IClickListner listner) {
        this.mListAlbums = mListAlbums;
        this.mClickListner = listner;
    }
    public interface IClickListner {
        void onClickHeart(Album album);
    }

    @NonNull
    @Override
    public thuVienAlbumYeuThichViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_yeuthich, parent, false);
        return new thuVienAlbumYeuThichViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull thuVienAlbumYeuThichViewHolder holder, int position) {
        Album album = mListAlbums.get(position);
        holder.txt.setText(album.getTenAlbum());
        Picasso.get().load(album.getAnhAlbum()).placeholder(R.drawable.img_default).into(holder.img);
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListner.onClickHeart(album);
            }
        });
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
        return mListAlbums.size();
    }

    public static class thuVienAlbumYeuThichViewHolder extends RecyclerView.ViewHolder {
        private ImageView img, icon;
        private TextView txt;
        public thuVienAlbumYeuThichViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_playlist);
            icon = itemView.findViewById(R.id.icon_heart);
            txt = itemView.findViewById(R.id.txt_playlist);
        }
    }
}
