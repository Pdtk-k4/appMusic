package com.example.dahitamusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dahitamusic.Model.Playlist;
import com.example.dahitamusic.Model.QuangCao;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.ActivityDsachBaiHatBinding;
import com.example.dahitamusic.databinding.ActivityMainBinding;

public class DSachBaiHatActivity extends AppCompatActivity {

    ActivityDsachBaiHatBinding binding;
    QuangCao quangCao;
    Playlist playlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDsachBaiHatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dataIntent();
    }

    private void dataIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("QuangCao")) {
                quangCao = (QuangCao) intent.getSerializableExtra("QuangCao");
                Toast.makeText(this, quangCao.getIdBaiHat(), Toast.LENGTH_SHORT).show();

            }
        }
    }
}