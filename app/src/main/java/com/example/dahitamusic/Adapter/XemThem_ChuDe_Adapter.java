package com.example.dahitamusic.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahitamusic.Model.ChuDe;
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class XemThem_ChuDe_Adapter extends RecyclerView.Adapter<XemThem_ChuDe_Adapter.xemThemChuDeViewHolder> {

    private ArrayList<ChuDe> mListChuDe;

    public XemThem_ChuDe_Adapter(ArrayList<ChuDe> mListChuDe) {
        this.mListChuDe = mListChuDe;
    }

    @NonNull
    @Override
    public xemThemChuDeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_xemthem_chude_theloai, parent, false);
        return new xemThemChuDeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull XemThem_ChuDe_Adapter.xemThemChuDeViewHolder holder, int position) {
        ChuDe chuDe = mListChuDe.get(position);
        Picasso.get().load(chuDe.getAnhChuDe()).placeholder(R.drawable.img_default).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mListChuDe.size();
    }

    public static class xemThemChuDeViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;

        public xemThemChuDeViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }
}
