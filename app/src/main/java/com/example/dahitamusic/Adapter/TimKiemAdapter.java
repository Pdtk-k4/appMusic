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
import com.example.dahitamusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TimKiemAdapter extends RecyclerView.Adapter<TimKiemAdapter.TimKiemViewHolder> implements Filterable {
    private ArrayList<BaiHat> mBaiHatArrayList; // Danh sách đang hiển thị
    private final ArrayList<BaiHat> mBaiHatArrayListHienThi; // Danh sách gốc
    private final Context context;

    public TimKiemAdapter(ArrayList<BaiHat> baiHatArrayList, Context context) {
        this.mBaiHatArrayListHienThi = baiHatArrayList; // Lưu danh sách gốc
        this.mBaiHatArrayList = new ArrayList<>(baiHatArrayList); // Khởi tạo danh sách hiển thị
        this.context = context;
    }

    @NonNull
    @Override
    public TimKiemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_baihatgoiy, parent, false);
        return new TimKiemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimKiemViewHolder holder, int position) {
        BaiHat baiHat = mBaiHatArrayList.get(position);
        if (baiHat == null) return;

        holder.textTen.setText(baiHat.getTenBaiHat());
        holder.txtNoiDung.setText(baiHat.getCaSi());

        Picasso.get().load(baiHat.getAnhBaiHat()).into(holder.imageView);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlayMusicActivity.class);
            intent.putExtra("cakhuc", baiHat); // Truyền toàn bộ danh sách bài hát
            intent.putExtra("position", position); // Truyền vị trí của bài hát được chọn
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mBaiHatArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence != null ? charSequence.toString().trim() : "";
                ArrayList<BaiHat> filteredList = new ArrayList<>();

                if (query.isEmpty()) {
                    // Nếu không có query, hiển thị toàn bộ danh sách
                    filteredList.addAll(mBaiHatArrayListHienThi);
                } else {
                    // Lọc danh sách theo query
                    for (BaiHat baiHat : mBaiHatArrayListHienThi) {
                        if (baiHat.getTenBaiHat().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(baiHat);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList; // Kết quả sau khi lọc
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mBaiHatArrayList.clear();
                if (filterResults.values != null) {
                    mBaiHatArrayList.addAll((ArrayList<BaiHat>) filterResults.values);
                }
                notifyDataSetChanged(); // Cập nhật hiển thị
            }
        };
    }

    public static class TimKiemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textTen;
        private final TextView txtNoiDung;

        public TimKiemViewHolder(View itemView) {
            super(itemView);
            textTen = itemView.findViewById(R.id.txt_name_song);
            txtNoiDung = itemView.findViewById(R.id.txt_name_casi);
            imageView = itemView.findViewById(R.id.img_song);
        }
    }
}
