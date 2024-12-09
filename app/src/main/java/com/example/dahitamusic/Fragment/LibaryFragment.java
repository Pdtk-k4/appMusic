package com.example.dahitamusic.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.dahitamusic.Adapter.ViewPager_ThuVien_Adapter;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentLibaryBinding;
import com.google.android.material.navigation.NavigationBarView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibaryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentLibaryBinding binding;

    public LibaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibaryFragment newInstance(String param1, String param2) {
        LibaryFragment fragment = new LibaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        searchItem.setOnMenuItemClickListener(item -> {
            // Thay thế Fragment hiện tại bằng SearchFragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.view_pager, new TimKiemFragment())
                    .addToBackStack(null)
                    .commit();
            return true;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLibaryBinding.inflate(inflater, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(binding.toolbar);
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("Thư viện");
            }
        }

        setUpViewpager();
        binding.bottomnavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.yeuthich) {
                binding.viewPager.setCurrentItem(0);
            } else if (item.getItemId() == R.id.playlist) {
                binding.viewPager.setCurrentItem(1);
            } else if (item.getItemId() == R.id.album) {
                binding.viewPager.setCurrentItem(2);
            }
            return true;
        });

        return binding.getRoot();
    }

    private void setUpViewpager(){
        ViewPager_ThuVien_Adapter viewPager_thuVien_adapter = new ViewPager_ThuVien_Adapter(getChildFragmentManager(),getLifecycle());
        binding.viewPager.setAdapter(viewPager_thuVien_adapter);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        binding.bottomnavigation.setSelectedItemId(R.id.yeuthich);
                        break;
                    case 1:
                        binding.bottomnavigation.setSelectedItemId(R.id.playlist);
                        break;
                    case 2:
                        binding.bottomnavigation.setSelectedItemId(R.id.album);
                        break;
                }
            }
        });
    }
}