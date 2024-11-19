package com.example.dahitamusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Activity.DSachBaiHatActivity;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ThuVien_Playlist_Adapter extends RecyclerView.Adapter<ThuVien_Playlist_Adapter.ThuVien_PlaylistViewHolder> {
    private ArrayList<Playlist> mangPlaylist;
    private Context context;
    private IClickListner mClickListner;

    public interface IClickListner {
        void onClick(Playlist playlist);
    }

    public ThuVien_Playlist_Adapter(ArrayList<Playlist> mangPlaylist, Context context, IClickListner Listner) {
        this.mangPlaylist = mangPlaylist;
        this.context = context;
        this.mClickListner = Listner;
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
        Picasso.get().load(playlist.getAnhPlaylist()).placeholder(R.drawable.img_default).into(holder.img_playlist);
        if(playlist.getYeuThich()){
            holder.icon_heart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heart_pink));
            holder.icon_heart.setColorFilter(Color.parseColor("#F05080"), PorterDuff.Mode.SRC_ATOP);
        }
        holder.icon_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListner.onClick(playlist);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        return Math.min(mangPlaylist.size(), 5);
    }

    public class ThuVien_PlaylistViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_tenplaylist;
        private ImageView img_playlist, icon_heart;

        public ThuVien_PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_tenplaylist = itemView.findViewById(R.id.txt_name_playlist);
            img_playlist = itemView.findViewById(R.id.img_playlist);
            icon_heart = itemView.findViewById(R.id.icon_heart);

        }
    }
}
