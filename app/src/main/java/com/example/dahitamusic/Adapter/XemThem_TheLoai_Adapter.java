package com.example.dahitamusic.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Model.TheLoai;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class XemThem_TheLoai_Adapter extends RecyclerView.Adapter<XemThem_TheLoai_Adapter.xemThemTheLoaiViewHolder> {
    private ArrayList<TheLoai> mListTheLoai;

    public XemThem_TheLoai_Adapter(ArrayList<TheLoai> mListTheLoai) {
        this.mListTheLoai = mListTheLoai;
    }

    @NonNull
    @Override
    public xemThemTheLoaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_xemthem_chude_theloai, parent, false);
        return new xemThemTheLoaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull xemThemTheLoaiViewHolder holder, int position) {
        TheLoai theLoai = mListTheLoai.get(position);
        Picasso.get().load(theLoai.getAnhTheLoai()).placeholder(R.drawable.img_default).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mListTheLoai.size();
    }

    public static class xemThemTheLoaiViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public xemThemTheLoaiViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }
}
