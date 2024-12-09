package com.example.dahitamusic.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class BaiHatYeuThich_Adapter extends RecyclerView.Adapter<BaiHatYeuThich_Adapter.BaiHatYeuThichViewHolder> {
    private final ArrayList<BaiHat> mBaiHatlist;
    private IClickListner mClickListner;;

    public interface IClickListner {
        void onClickHeart(BaiHat baiHat);
        void onClickMore(BaiHat baiHat);
    }

    public BaiHatYeuThich_Adapter(ArrayList<BaiHat> mBaiHatlist, IClickListner listner) {
        this.mBaiHatlist = mBaiHatlist;
        this.mClickListner = listner;

    }

    @NonNull
    @Override
    public BaiHatYeuThichViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baihat_yeuthich, parent, false);
        return new BaiHatYeuThichViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaiHatYeuThichViewHolder holder, int position) {
        BaiHat baiHat = mBaiHatlist.get(position);
        holder.txt_tenBaiHat.setText(baiHat.getTenBaiHat());
        holder.txt_tenCaSi.setText(baiHat.getCaSi());
        Picasso.get().load(baiHat.getAnhBaiHat()).placeholder(R.drawable.img_default).into(holder.img_BaiHat);

        holder.img_IconHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListner.onClickHeart(baiHat);
            }
        });

        holder.img_IconMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListner.onClickMore(baiHat);
            }
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), PlayMusicActivity.class);
            intent.putExtra("BaiHat", mBaiHatlist); // Truyền toàn bộ danh sách bài hát
            intent.putExtra("position", position); // Truyền vị trí của bài hát được chọn
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mBaiHatlist.size();
    }

    public class BaiHatYeuThichViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_tenBaiHat, txt_tenCaSi;
        private ImageView img_BaiHat, img_IconHeart, img_IconMore;

        public BaiHatYeuThichViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_tenBaiHat = itemView.findViewById(R.id.txt_tenBaiHat);
            txt_tenCaSi = itemView.findViewById(R.id.txt_tenCaSi);
            img_BaiHat = itemView.findViewById(R.id.img_anhBaiHat);
            img_IconHeart = itemView.findViewById(R.id.img_IconHeart);
            img_IconMore = itemView.findViewById(R.id.img_IconMore);

        }
    }
}
