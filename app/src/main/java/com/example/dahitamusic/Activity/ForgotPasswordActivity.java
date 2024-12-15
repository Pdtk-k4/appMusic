package com.example.dahitamusic.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.ActivityForgotPasswordBinding;
import com.example.dahitamusic.databinding.ActivitySinginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        onClick();
    }

    private void onClick() {
        binding.imgBack.setOnClickListener(v -> {
            finish();
        });

        binding.txtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.linearlayoutEmail.setBackgroundResource(R.drawable.linear_background);
            } else {
                binding.linearlayoutEmail.setBackgroundResource(R.drawable.linear_background_default);
            }
        });

        binding.btnSend.setOnClickListener(v -> {
            onClickSend();
            finish();
        });
    }

    private void onClickSend() {
        String email = binding.txtEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập địa chỉ email", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Email đã được gửi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}