package com.example.dahitamusic.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.dahitamusic.Fragment.HomeFragment;
import com.example.dahitamusic.Fragment.LibaryFragment;
import com.example.dahitamusic.Fragment.ProfileFragment;
import com.example.dahitamusic.R;
import com.example.dahitamusic.Fragment.RadioFragment;
import com.example.dahitamusic.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        replaceFragment(new HomeFragment());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        Menu_Adapter menuAdapter = new Menu_Adapter(this);
//        binding.viewPager.setAdapter(menuAdapter);
//        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                switch (position) {
//                    case 0:
//                        binding.bottomnavigation.getMenu().findItem(R.id.home).setChecked(true);
//                        break;
//                    case 1:
//                        binding.bottomnavigation.getMenu().findItem(R.id.radio).setChecked(true);
//                        break;
//                    case 2:
//                        binding.bottomnavigation.getMenu().findItem(R.id.library).setChecked(true);
//                        break;
//                    case 3:
//                        binding.bottomnavigation.getMenu().findItem(R.id.profile).setChecked(true);
//                        break;
//                }
//            }
//        });
//
//        binding.bottomnavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int itemId = item.getItemId();
//                if (itemId == R.id.home) {
//                    binding.viewPager.setCurrentItem(0);
//                } else if (itemId == R.id.radio) {
//                    binding.viewPager.setCurrentItem(1);
//                } else if (itemId == R.id.library) {
//                    binding.viewPager.setCurrentItem(2);
//                } else if (itemId == R.id.profile) {
//                    binding.viewPager.setCurrentItem(3);
//                } else {
//                    return false;
//                }
//                return true;
//            }
//        });
//    }
//    // Mặc định hiển thị Fragment Home
        replaceFragment(new HomeFragment());

        binding.bottomnavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.radio) {
                replaceFragment(new RadioFragment());
            } else if (item.getItemId() == R.id.library) {
                replaceFragment(new LibaryFragment());
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new ProfileFragment());
            } else {
                return false; // Trả về false nếu không có item nào khớp
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, fragment)
                .commit();
    }
}
