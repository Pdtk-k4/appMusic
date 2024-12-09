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

import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DS_Album_Adapter extends RecyclerView.Adapter<DS_Album_Adapter.dsAlbumViewHolder> {
    private ArrayList<Album> mListAlbum;
    public DS_Album_Adapter(ArrayList<Album> mListAlbum) {
        this.mListAlbum = mListAlbum;
    }

    @NonNull
    @Override
    public dsAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danhsach_album, parent, false);
        return new dsAlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull dsAlbumViewHolder holder, int position) {
        Album listAlbum = mListAlbum.get(position);
        Picasso.get().load(listAlbum.getAnhAlbum()).placeholder(R.drawable.img_default).into(holder.imageView);
        holder.txtTenAlbum.setText(listAlbum.getTenAlbum());
        holder.txtTenCaSi.setText(listAlbum.getTenCaSiAlbum());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DSachBaiHatActivity.class);
                intent.putExtra("Album", listAlbum);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListAlbum.size();
    }

    public class dsAlbumViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txtTenAlbum, txtTenCaSi ;
        public dsAlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_album);
            txtTenAlbum = itemView.findViewById(R.id.txt_tenAlbum);
            txtTenCaSi = itemView.findViewById(R.id.txt_tenCasi);
        }
    }
}
