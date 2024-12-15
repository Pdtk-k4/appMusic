package com.example.dahitamusic.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Activity.DSachBaiHatActivity;
import com.example.dahitamusic.Fragment.CreatePlaylistFragment;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class ThuVien_PlaylistYeuThich_Adapter extends RecyclerView.Adapter<ThuVien_PlaylistYeuThich_Adapter.thuVienPlayListYeuThichViewHolder> {
    private ArrayList<Playlist> mPlaylistlist;
    private IClickListner mClickListner;
    public interface IClickListner {
        void onClickHeart(Playlist playlist);
    }
    public ThuVien_PlaylistYeuThich_Adapter(ArrayList<Playlist> mPlaylistlist, IClickListner listner) {
        this.mPlaylistlist = mPlaylistlist;
        this.mClickListner = listner;
    }

    @NonNull
    @Override
    public thuVienPlayListYeuThichViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_yeuthich, parent, false);
        return new thuVienPlayListYeuThichViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull thuVienPlayListYeuThichViewHolder holder, int position) {
        Playlist playlist = mPlaylistlist.get(position);
        holder.txt_playlist.setText(playlist.getTenPlaylist());
        Picasso.get().load(playlist.getAnhPlaylist()).placeholder(R.drawable.img_default).into(holder.img_playlist);

        holder.icon_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListner.onClickHeart(playlist);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playlist.getUserId() != null && !playlist.getUserId().isEmpty()) {
                    // Nếu có userId, mở CreatePlaylistFragment
                    CreatePlaylistFragment createPlaylistFragment = new CreatePlaylistFragment();
                    Bundle args = new Bundle();
                    args.putParcelable("Playlist", playlist);
                    createPlaylistFragment.setArguments(args);

                    // Thay đổi fragment
                    if (view.getContext() instanceof AppCompatActivity) {
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.view_pager, createPlaylistFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                } else {
                    // Nếu không có userId, mở DSachBaiHatActivity
                    Intent intent = new Intent(view.getContext(), DSachBaiHatActivity.class);
                    intent.putExtra("Playlist", playlist);
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlaylistlist.size();
    }

    public static class thuVienPlayListYeuThichViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_playlist;
        private ImageView icon_heart;
        private TextView txt_playlist;
        public thuVienPlayListYeuThichViewHolder(@NonNull View itemView) {
            super(itemView);
            img_playlist = itemView.findViewById(R.id.img_playlist);
            icon_heart = itemView.findViewById(R.id.icon_heart);
            txt_playlist = itemView.findViewById(R.id.txt_playlist);
        }
    }
}
