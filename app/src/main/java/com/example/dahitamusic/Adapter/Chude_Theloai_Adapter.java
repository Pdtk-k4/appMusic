package com.example.dahitamusic.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Fragment.PlaylistChuDeTheLoaiFragment;
import com.example.dahitamusic.Model.Album;
import com.example.dahitamusic.Model.TheLoai;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Chude_Theloai_Adapter extends RecyclerView.Adapter<Chude_Theloai_Adapter.ChuDeTheLoaiViewHolder> {
    private Context context;
    private ArrayList<TheLoai> mangChudeTheLoai;

    public Chude_Theloai_Adapter(Context context, ArrayList<TheLoai> mangChudeTheLoai) {
        this.context = context;
        this.mangChudeTheLoai = mangChudeTheLoai;
    }

    @NonNull
    @Override
    public ChuDeTheLoaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_chudevatheloai, parent, false);
        return new ChuDeTheLoaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChuDeTheLoaiViewHolder holder, int position) {
        TheLoai theLoai = mangChudeTheLoai.get(position);

        Picasso.get().load(theLoai.getAnhTheLoai()).placeholder(R.drawable.img_default).into(holder.imgChudeTheLoai);

        holder.itemView.setOnClickListener(view -> {
            FragmentActivity activity = (FragmentActivity) view.getContext();
            PlaylistChuDeTheLoaiFragment fragment = new PlaylistChuDeTheLoaiFragment();

            // Gửi dữ liệu qua Bundle
            Bundle args = new Bundle();
            args.putParcelable("TheLoai", theLoai);
            fragment.setArguments(args);

            // Thay thế hoặc thêm Fragment vào container
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.view_pager, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return mangChudeTheLoai.size();
    }

    public static class ChuDeTheLoaiViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgChudeTheLoai;

        public ChuDeTheLoaiViewHolder(@NonNull View itemView) {
            super(itemView);
            imgChudeTheLoai = itemView.findViewById(R.id.img_chudevatheloai);
        }
    }
}
