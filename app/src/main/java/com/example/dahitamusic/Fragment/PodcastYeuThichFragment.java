package com.example.dahitamusic.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.Adapter.Podcast_Adapter;
import com.example.dahitamusic.Model.BaiHat;
import com.example.dahitamusic.Model.Podcast;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentPodcastYeuThichBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PodcastYeuThichFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PodcastYeuThichFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PodcastYeuThichFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PodcastYeuThichFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PodcastYeuThichFragment newInstance(String param1, String param2) {
        PodcastYeuThichFragment fragment = new PodcastYeuThichFragment();
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

    private FragmentPodcastYeuThichBinding binding;
    private DatabaseReference mData;
    private ArrayList<Podcast> mListPodcast;
    private Podcast_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPodcastYeuThichBinding.inflate(inflater, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mListPodcast = new ArrayList<>();
        getData();
        if(user != null){
            getIDPodcast(user.getUid());
        }
        return binding.getRoot();
    }

    private void getData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        binding.rcvPodcast.setLayoutManager(gridLayoutManager);
        adapter = new Podcast_Adapter(mListPodcast);
        binding.rcvPodcast.setAdapter(adapter);
    }

    private void getIDPodcast(String userId) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userFavoritesRef = database.child("Users").child(userId).child("podcastYeuThich");

        userFavoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                    String idPodcast = songSnapshot.getKey();
                    getListPodcast(idPodcast);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }

    private void getListPodcast(String idPodcast) {
        mData = FirebaseDatabase.getInstance().getReference("Podcast").child(idPodcast); // Chỉ lấy dữ liệu bài hát theo id
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Podcast podcast = snapshot.getValue(Podcast.class);
                if (podcast != null) {
                    // Chỉ thêm bài hát vào danh sách nếu chưa tồn tại
                    boolean isExist = false;
                    for (Podcast item : mListPodcast) {
                        if (item.getIdPodcast().equals(podcast.getIdPodcast())) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        mListPodcast.add(podcast);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: " + error.getMessage());
            }
        });
    }
}