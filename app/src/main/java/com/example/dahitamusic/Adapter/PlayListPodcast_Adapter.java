package com.example.dahitamusic.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Podcast;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayListPodcast_Adapter extends RecyclerView.Adapter<PlayListPodcast_Adapter.PlayListPodcastViewHolder> {
    private final ArrayList<Podcast> mListPodcast;
    private IClickListner mClickListner;

    public interface  IClickListner{
        void onClickItem(Podcast podcast);
        void onClickMore(Podcast podcast);
    }
    public PlayListPodcast_Adapter(ArrayList<Podcast> mListPodcast, IClickListner mClickListner) {
        this.mListPodcast = mListPodcast;
        this.mClickListner = mClickListner;
    }

    @NonNull
    @Override
    public PlayListPodcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_baihat, parent, false);
        return new PlayListPodcastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListPodcastViewHolder holder, int position) {
        Podcast podcast = mListPodcast.get(position);
        holder.txt_ten.setText(podcast.getTenPodcast());
        holder.txt_tacgia.setText(podcast.getTacGia());

        Picasso.get().load(podcast.getAnhPodcast()).placeholder(R.drawable.img_default).into(holder.img);

        holder.itemView.setOnClickListener(view -> {
            mClickListner.onClickItem(podcast);
        });

        holder.img_more.setOnClickListener(view -> {
            mClickListner.onClickMore(podcast);
        });

    }

    @Override
    public int getItemCount() {
        return Math.min(mListPodcast.size(), 10);
    }

    public static class PlayListPodcastViewHolder extends RecyclerView.ViewHolder{
        private final ImageView img;
        private final ImageView img_more;
        private final TextView txt_ten;
        private final TextView txt_tacgia;
        public PlayListPodcastViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_song);
            img_more = itemView.findViewById(R.id.icon_more);
            txt_ten = itemView.findViewById(R.id.txt_name_song);
            txt_tacgia = itemView.findViewById(R.id.txt_name_casi);
        }
    }
}
