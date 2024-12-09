package com.example.dahitamusic.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dahitamusic.Fragment.ThuVien_AlbumFragment;
import com.example.dahitamusic.Fragment.ThuVien_PlaylistFragment;
import com.example.dahitamusic.Fragment.ThuVien_YeuthichFragment;

public class ViewPager_ThuVien_Adapter extends FragmentStateAdapter {


    public ViewPager_ThuVien_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ThuVien_YeuthichFragment();
            case 1:
                return new ThuVien_PlaylistFragment();
            case 2:
                return new ThuVien_AlbumFragment();
            default:
                return new ThuVien_YeuthichFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
