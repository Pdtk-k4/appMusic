package com.example.dahitamusic.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dahitamusic.Adapter.BaiHatGoiY_Adapter;
import com.example.dahitamusic.Adapter.dialod_ds_playlist_adapter;
import com.example.dahitamusic.Model.Album;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.ViewModel.AlbumViewModel;
import com.example.dahitamusic.ViewModel.BaiHatViewModel;
import com.example.dahitamusic.databinding.FragmentBaiHatBinding;
import com.example.dahitamusic.databinding.FragmentPlayListSongBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaiHatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BaiHatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<BaiHat> mbaihat;
    private ArrayList<BaiHat> mbaihathienthi;
    private ArrayList<Playlist> mListPlaylist;
    private BaiHatGoiY_Adapter baihatgoiy_adapter;
    private FragmentBaiHatBinding binding;
    private DatabaseReference mData;
    private BaiHatViewModel viewModel;

    public BaiHatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BaiHatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BaiHatFragment newInstance(String param1, String param2) {
        BaiHatFragment fragment = new BaiHatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel = new ViewModelProvider(requireActivity()).get(BaiHatViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBaiHatBinding.inflate(inflater, container, false);
        mListPlaylist = new ArrayList<>();
        mbaihat = new ArrayList<>();
        mbaihathienthi = new ArrayList<>();
        baihatgoiy_adapter = new BaiHatGoiY_Adapter(mbaihathienthi, getActivity(), new BaiHatGoiY_Adapter.IClickListner() {
            @Override
            public void onClick(BaiHat baiHat) {
                clickIconMoreOnpenDialog(baiHat);
            }
        });
        binding.recyclerviewBaihatgoiy.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerviewBaihatgoiy.setAdapter(baihatgoiy_adapter);

        // Quan sát dữ liệu từ ViewModel
        viewModel.getBaiHats().observe(getViewLifecycleOwner(), new Observer<ArrayList<BaiHat>>() {
            @Override
            public void onChanged(ArrayList<BaiHat> baiHats) {
                if (baiHats != null && !baiHats.isEmpty()) {
                    mbaihathienthi.clear();
                    mbaihathienthi.addAll(baiHats);
                    baihatgoiy_adapter.notifyDataSetChanged();
                } else {
                    loadBaiHatGoiY();
                }
            }
        });

        binding.relativeLammoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getBaiHats().getValue() != null && !viewModel.getBaiHats().getValue().isEmpty()) {
                    ArrayList<BaiHat> shuffledList = new ArrayList<>(viewModel.getBaiHats().getValue());
                    Collections.shuffle(shuffledList); // Shuffle dữ liệu
                    mbaihathienthi.clear();
                    mbaihathienthi.addAll(shuffledList);
                    baihatgoiy_adapter.notifyDataSetChanged();

                    // Lưu danh sách đã làm mới vào ViewModel
                    viewModel.setBaiHats(new ArrayList<>(shuffledList)); // Cập nhật danh sách mới
                } else {
                    Toast.makeText(getActivity(), "Danh sách bài hát rỗng!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return binding.getRoot();
    }

    public void loadBaiHatGoiY() {
        mData = FirebaseDatabase.getInstance().getReference("BaiHat");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded()) {
                    return; // Tránh thao tác khi Fragment chưa được gắn vào Activity
                }
                ArrayList<BaiHat> tempList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BaiHat baiHat = dataSnapshot.getValue(BaiHat.class);
                    if (baiHat != null) {
                        tempList.add(baiHat);
                    }
                }
                if (!tempList.isEmpty()) {
                    mbaihat.clear();
                    mbaihat.addAll(tempList); // Đồng bộ hóa `mbaihat`
                    Collections.shuffle(mbaihat); // Shuffle dữ liệu để hiển thị ngẫu nhiên

                    viewModel.setBaiHats(mbaihat); // Cập nhật ViewModel

                    // Cập nhật danh sách hiển thị
                    mbaihathienthi.clear();
                    mbaihathienthi.addAll(mbaihat);
                    baihatgoiy_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void clickIconMoreOnpenDialog(BaiHat baiHat) {
        if (!isAdded()) {
            return; // Tránh thao tác khi Fragment chưa được gắn vào Activity
        }
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_more_baihat);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView img_close = dialog.findViewById(R.id.img_close);
        ImageView img_baiHat = dialog.findViewById(R.id.img_baiHat);
        ImageView img_iconHeart = dialog.findViewById(R.id.img_iconHeart);

        TextView txt_tenBaiHat = dialog.findViewById(R.id.txt_tenBaiHat);
        TextView txt_tenCaSi = dialog.findViewById(R.id.txt_tenCaSi);
        TextView txt_addThuvien = dialog.findViewById(R.id.txt_addThuvien);

        RelativeLayout relative_download = dialog.findViewById(R.id.relative_download);
        RelativeLayout relative_addPlaylist = dialog.findViewById(R.id.relative_addPlaylist);
        RelativeLayout relative_addThuvien = dialog.findViewById(R.id.relative_addThuvien);

        txt_tenBaiHat.setText(baiHat.getTenBaiHat());
        txt_tenCaSi.setText(baiHat.getCaSi());
        Picasso.get().load(baiHat.getAnhBaiHat()).into(img_baiHat);

        img_close.setOnClickListener(view -> dialog.dismiss());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user.getUid())
                .child("baiHatYeuThich");

        // Kiểm tra trạng thái "yêu thích" từ Firebase
        favoritesRef.child(baiHat.getIdBaiHat()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isFavorite = snapshot.exists(); // Kiểm tra bài hát đã được yêu thích chưa

                if (isFavorite) {
                    if (isAdded() && getContext() != null) {
                        img_iconHeart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.heart_pink));
                    }
                    txt_addThuvien.setText(R.string.dathemvaothuvien);
                } else {
                    if (isAdded() && getContext() != null) {
                        img_iconHeart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.heart));
                    }
                    txt_addThuvien.setText(R.string.themvaothuvien);
                }

                // Xử lý khi nhấn nút "thêm vào thư viện"
                relative_addThuvien.setOnClickListener(view -> {
                    if (isFavorite) {
                        // Xóa bài hát khỏi thư viện
                        favoritesRef.child(baiHat.getIdBaiHat()).removeValue();
                        if (isAdded() && getContext() != null) {
                            img_iconHeart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.heart));
                        }
                        txt_addThuvien.setText(R.string.themvaothuvien);
                        showCenteredToast("Đã xóa bài hát khỏi thư viện");
                    } else {
                        // Thêm bài hát vào thư viện
                        favoritesRef.child(baiHat.getIdBaiHat()).setValue(true);
                        if (isAdded() && getContext() != null) {
                            img_iconHeart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.heart_pink));
                        }

                        txt_addThuvien.setText(R.string.dathemvaothuvien);
                        showCenteredToast("Đã thêm bài hát này vào thư viện");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Lỗi khi tải trạng thái yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });

        relative_addPlaylist.setOnClickListener(view -> {
            dialog.dismiss();
            openDialogAddPlaylistBaiHat(baiHat);

        });

        dialog.show();
    }

    private void openDialogAddPlaylistBaiHat(BaiHat baiHat) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_addplaylist);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);


        ImageView img_close = dialog.findViewById(R.id.img_close);
        RelativeLayout relative_addPlaylist = dialog.findViewById(R.id.relativeLayout_addPlaylist);
        RecyclerView rcv = dialog.findViewById(R.id.rcv);

        img_close.setOnClickListener(view -> dialog.dismiss());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcv.setLayoutManager(linearLayoutManager);
        dialod_ds_playlist_adapter adapter = new dialod_ds_playlist_adapter(mListPlaylist, new dialod_ds_playlist_adapter.IClickListner() {
            @Override
            public void onClickItem(Playlist playlist) {
                onClickAddBaihat(playlist, baiHat);
            }
        });
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcv.setAdapter(adapter);

        //load playlist trong dialog
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        mData = FirebaseDatabase.getInstance().getReference().child("Playlist");
        Query query = mData.orderByChild("userId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListPlaylist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Playlist playlist = dataSnapshot.getValue(Playlist.class);
                    if (playlist != null) {
                        mListPlaylist.add(playlist);
                    }
                }

                adapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });

        relative_addPlaylist.setOnClickListener(view -> {
            openDialogCreatePlaylist(baiHat);
        });

        dialog.show();
    }

    private void openDialogCreatePlaylist(BaiHat baiHat) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_createplaylist);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edt_tenplaylist = dialog.findViewById(R.id.edt_tenplaylist);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_tao_playlist = dialog.findViewById(R.id.btn_tao_playlist);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_tao_playlist.setOnClickListener(view -> {
            String tenPlaylist = edt_tenplaylist.getText().toString().trim();
            if (tenPlaylist.isEmpty()) {
                Toast.makeText(getActivity(), "Vui lòng nhập tên playlist!", Toast.LENGTH_SHORT).show();
                return;
            }
            String idPlaylist = generateIdPlaylist(tenPlaylist);
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String userId = currentUser.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();

            // Tạo đối tượng Playlist
            Playlist playlist = new Playlist(idPlaylist, tenPlaylist, userId);

            // Lưu playlist vào node "Playlist"
            database.child("Playlist").child(idPlaylist).setValue(playlist)
                    .addOnSuccessListener(unused -> {
                        // Đồng thời lưu playlist vào "Users -> playListYeuThich"
                        database.child("Users").child(userId).child("playListYeuThich").child(idPlaylist).setValue(true)
                                .addOnSuccessListener(unused1 -> {
                                    addBaiHatToPlaylist(playlist, baiHat);
                                    dialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Không thể thêm vào danh sách yêu thích: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Không thể lưu playlist: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        dialog.show();

    }

    private void addBaiHatToPlaylist(Playlist playlist, BaiHat baiHat) {
        if (playlist != null) {
            // Khởi tạo danhSachBaiHat nếu null
            if (playlist.getDanhSachBaiHat() == null) {
                playlist.setDanhSachBaiHat(new HashMap<>());
            }
            // Thêm idBaiHat vào danh sách và gán giá trị true
            playlist.getDanhSachBaiHat().put(baiHat.getIdBaiHat(), true);

            // Cập nhật Playlist trong Firebase
            DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference("Playlist");
            playlistRef.child(playlist.getIdPlaylist())
                    .setValue(playlist)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getActivity(), "Thêm bài hát vào playList thành công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Lỗi khi thêm bài hát", Toast.LENGTH_SHORT).show();
                        Log.e("FirebaseError", e.getMessage());
                    });
        } else {
            Toast.makeText(getActivity(), "Playlist không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức loại bỏ dấu (chuyển các ký tự có dấu thành không dấu)
    private String removeAccents(String input) {
        String nfd = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfd).replaceAll("");
    }

    private String generateIdPlaylist(String tenPlaylist) {
        if (tenPlaylist == null || tenPlaylist.trim().isEmpty()) {
            return ""; // Trả về chuỗi rỗng nếu tên playlist không hợp lệ
        }

        // Chuyển tất cả ký tự về chữ thường
        String normalized = tenPlaylist.toLowerCase();

        // Loại bỏ dấu (chuyển các ký tự có dấu thành không dấu)
        normalized = removeAccents(normalized);

        // Thay khoảng trắng thành dấu gạch dưới
        normalized = normalized.replaceAll("\\s+", "_");

        // Loại bỏ các ký tự không phải là chữ cái, chữ số, hoặc dấu gạch dưới
        normalized = normalized.replaceAll("[^a-z0-9_]", "");

        return normalized;
    }

    private void onClickAddBaihat(Playlist playlist, BaiHat baiHat) {
        if (playlist != null) {
            // Khởi tạo danhSachBaiHat nếu null
            if (playlist.getDanhSachBaiHat() == null) {
                playlist.setDanhSachBaiHat(new HashMap<>());
            }

            // Thêm idBaiHat vào danh sách và gán giá trị true
            playlist.getDanhSachBaiHat().put(baiHat.getIdBaiHat(), true);

            // Cập nhật Playlist trong Firebase
            DatabaseReference playlistRef = FirebaseDatabase.getInstance().getReference("Playlist");
            playlistRef.child(playlist.getIdPlaylist())
                    .setValue(playlist)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getActivity(), "Thêm bài hát thành công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Lỗi khi thêm bài hát", Toast.LENGTH_SHORT).show();
                        Log.e("FirebaseError", e.getMessage());
                    });
        } else {
            Toast.makeText(getActivity(), "Playlist không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức hiển thị Toast ở giữa màn hình
    private void showCenteredToast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        toast.setGravity(android.view.Gravity.CENTER, 0, 0); // Đặt Toast ở giữa màn hình
        toast.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Đảm bảo rằng dữ liệu đã được tải vào ViewModel và hiển thị lại nếu có dữ liệu
        if (viewModel.getBaiHats().getValue() != null && !viewModel.getBaiHats().getValue().isEmpty()) {
            mbaihathienthi.clear();
            mbaihathienthi.addAll(viewModel.getBaiHats().getValue());
            baihatgoiy_adapter.notifyDataSetChanged();
        } else {
            loadBaiHatGoiY(); // Tải lại dữ liệu từ Firebase nếu không có dữ liệu trong ViewModel
        }
    }
}