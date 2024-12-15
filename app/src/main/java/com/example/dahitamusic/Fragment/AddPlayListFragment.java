package com.example.dahitamusic.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dahitamusic.Adapter.AddPlayList_Adapter;
import com.example.dahitamusic.Adapter.TimKiemAdapter;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentAddPlayListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPlayListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPlayListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentAddPlayListBinding binding;
    private AddPlayList_Adapter addPlayListAdapter;
    private Playlist playlist;
    private ArrayList<BaiHat> mListBaiHat;
    private DatabaseReference mData;

    public AddPlayListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPlayListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPlayListFragment newInstance(String param1, String param2) {
        AddPlayListFragment fragment = new AddPlayListFragment();
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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPlayListBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            playlist = (Playlist) getArguments().getParcelable("Playlist");
        }
        mListBaiHat = new ArrayList<>();
        baiHatApdapter();
        loadBaiHatGoiY();
        searchBaiHat();
        onClick();
        return binding.getRoot();
    }

    private void onClick() {
        binding.iconBack.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void baiHatApdapter(){
        addPlayListAdapter = new AddPlayList_Adapter(mListBaiHat, new AddPlayList_Adapter.IClickListner() {
            @Override
            public void onClickHeart(BaiHat baiHat) {
                clickItem(baiHat);
            }
        });
        binding.recyclerviewBaihat.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerviewBaihat.setAdapter(addPlayListAdapter);
    }

    private void clickItem(BaiHat baiHat) {
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


    public void loadBaiHatGoiY() {
        mData = FirebaseDatabase.getInstance().getReference("BaiHat");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListBaiHat.clear();

                // Kiểm tra xem playlist đã được khởi tạo chưa, nếu chưa thì không tạo lại Playlist mới
                if (playlist != null && playlist.getDanhSachBaiHat() == null) {
                    playlist.setDanhSachBaiHat(new HashMap<>());
                }

                // Duyệt qua tất cả bài hát trong cơ sở dữ liệu
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BaiHat baiHat = dataSnapshot.getValue(BaiHat.class);
                    if (baiHat != null) {
                        // Kiểm tra xem bài hát có tồn tại trong danhSachBaiHat của Playlist trên Firebase không
                        if (playlist != null && playlist.getDanhSachBaiHat() != null &&
                                !playlist.getDanhSachBaiHat().containsKey(baiHat.getIdBaiHat())) {
                            // Nếu không có trong danh sách của Playlist, thêm vào danh sách hiển thị
                            mListBaiHat.add(baiHat);
                        }
                    }
                }

                // Cập nhật lại dữ liệu cho adapter
                addPlayListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }


    private void searchBaiHat(){
        binding.searchView.setQueryHint("Tìm kiếm bài hát");

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // Nếu không có nội dung tìm kiếm, reset lại danh sách hiển thị ban đầu
                    addPlayListAdapter.setFilteredList(mListBaiHat);  // Hiển thị lại toàn bộ danh sách
                } else {
                    // Nếu có nội dung tìm kiếm, lọc danh sách
                    filterList(newText);
                }
                return false;
            }
        });
    }

    //trả về danh sách bài hát đã lọc
    private void filterList(String text) {
        //Lọc bài hát
        ArrayList<BaiHat> filteredList = new ArrayList<>();
        for (BaiHat baiHat : mListBaiHat) {
            if (baiHat.getTenBaiHat() != null && baiHat.getTenBaiHat().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(baiHat);
            }
        }

        if (filteredList.isEmpty()) {
            showCenteredToast("Không tìm thấy kết quả nào");
            addPlayListAdapter.setFilteredList(new ArrayList<>());
        } else {
            // Cập nhật danh sách đã lọc cho adapter
            addPlayListAdapter.setFilteredList(filteredList);
        }
    }

    // Phương thức hiển thị Toast ở giữa màn hình
    private void showCenteredToast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        toast.setGravity(android.view.Gravity.CENTER, 0, 0); // Đặt Toast ở giữa màn hình
        toast.show();
    }
}