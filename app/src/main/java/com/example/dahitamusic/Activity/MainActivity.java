package com.example.dahitamusic.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.dahitamusic.Fragment.HomeFragment;
import com.example.dahitamusic.Fragment.LibaryFragment;
import com.example.dahitamusic.Fragment.ProfileFragment;
import com.example.dahitamusic.Fragment.TimKiemFragment;
import com.example.dahitamusic.R;
import com.example.dahitamusic.Fragment.RadioFragment;
import com.example.dahitamusic.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SearchView searchView;
    private String currentFragmentTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");


        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.search) {
                    replaceFragment(new TimKiemFragment(), getString(R.string.none), "Tìm Kiếm");
                    return true;
                }
                return false;
            }
        });

        replaceFragment(new HomeFragment(), getString(R.string.trang_chu), "Home");
        binding.bottomnavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment(), getString(R.string.trang_chu), "Home");
            } else if (item.getItemId() == R.id.radio) {
                replaceFragment(new RadioFragment(), getString(R.string.radio), "Radio");
            } else if (item.getItemId() == R.id.library) {
                replaceFragment(new LibaryFragment(), getString(R.string.library), "Library");
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new ProfileFragment(), getString(R.string.profile), "Profile");
            } else {
                return false;
            }
            return true;
        });

        replaceFragment(new HomeFragment(), getString(R.string.trang_chu), "Home"); // Mặc định là Home
    }

    private void replaceFragment(Fragment fragment, String title, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, fragment)
                .commit();

        binding.toolbar.setTitle(title);
        currentFragmentTag = tag; // Lưu trạng thái menu hiện tại
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý khi người dùng nhấn tìm kiếm
                Log.d("SearchQuery", "aaa: " + query);
                TimKiemFragment timKiemFragment = new TimKiemFragment();
                Bundle bundle = new Bundle();
                bundle.putString("searchQuery", query);
                timKiemFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.view_pager, timKiemFragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // Trở lại fragment trước đó dựa trên tag hiện tại
                    switch (currentFragmentTag) {
                        case "Home":
                            replaceFragment(new HomeFragment(), getString(R.string.trang_chu), "Home");
                            break;
                        case "Radio":
                            replaceFragment(new RadioFragment(), getString(R.string.radio), "Radio");
                            break;
                        case "Library":
                            replaceFragment(new LibaryFragment(), getString(R.string.library), "Library");
                            break;
                        case "Profile":
                            replaceFragment(new ProfileFragment(), getString(R.string.profile), "Profile");
                            break;
                        default:
                            replaceFragment(new HomeFragment(), getString(R.string.trang_chu), "Home");
                            break;
                    }
                } else {
                    // Nếu có từ khóa tìm kiếm, cập nhật TimKiemFragment với từ khóa mới
                    TimKiemFragment timKiemFragment = new TimKiemFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("searchQuery", newText); // Truyền query vào fragment
                    timKiemFragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.view_pager, timKiemFragment)
                            .addToBackStack(null)
                            .commit();
                }
                return true;
            }

        });


        return true;
    }

}
