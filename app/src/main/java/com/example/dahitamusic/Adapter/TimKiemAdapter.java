package com.example.dahitamusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Activity.PlayMusicActivity;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class TimKiemAdapter extends RecyclerView.Adapter<TimKiemAdapter.TimKiemViewHolder> {
    private ArrayList<BaiHat> mListBaiHat;
    private IClickListner mClickListner;

    public interface IClickListner {
        void onClick(BaiHat baiHat);
    }

    public TimKiemAdapter(ArrayList<BaiHat> mListBaiHat, IClickListner Listner) {
        this.mListBaiHat = mListBaiHat;
        this.mClickListner = Listner;
    }

    public void setFilteredList(ArrayList<BaiHat> filteredList) {
        this.mListBaiHat = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TimKiemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_baihatgoiy, parent, false);
        return new TimKiemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimKiemViewHolder holder, int position) {
        BaiHat baiHat = mListBaiHat.get(position);
        if (baiHat == null) return;

        holder.textTen.setText(baiHat.getTenBaiHat());
        holder.txtNoiDung.setText(baiHat.getCaSi());

        Picasso.get().load(baiHat.getAnhBaiHat()).into(holder.imageView);

        holder.img_iconMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListner.onClick(baiHat);
            }
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), PlayMusicActivity.class);
            intent.putExtra("BaiHat", mListBaiHat); // Truyền toàn bộ danh sách bài hát
            intent.putExtra("position", position); // Truyền vị trí của bài hát được chọn
            view.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return Math.min(mListBaiHat.size(), 10);
    }

    public static class TimKiemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textTen;
        private final TextView txtNoiDung;
        private final ImageView img_iconMore;

        public TimKiemViewHolder(View itemView) {
            super(itemView);
            textTen = itemView.findViewById(R.id.txt_name_song);
            txtNoiDung = itemView.findViewById(R.id.txt_name_casi);
            imageView = itemView.findViewById(R.id.img_song);
            img_iconMore = itemView.findViewById(R.id.icon_more);
        }
    }
}
