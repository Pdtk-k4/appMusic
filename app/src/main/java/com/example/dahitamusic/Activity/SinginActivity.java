package com.example.dahitamusic.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.ActivityMainBinding;
import com.example.dahitamusic.databinding.ActivitySinginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SinginActivity extends AppCompatActivity {
    private ActivitySinginBinding binding;
    private ProgressDialog progressDialog;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySinginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(this);
        initListener();
        onClick();
    }

    private void onClick() {
        binding.btnSingup.setOnClickListener(v -> {
            Intent intent = new Intent(SinginActivity.this, SingupActivity.class);
            startActivity(intent);
        });

        binding.btnSingin.setOnClickListener(v -> {
            onClickSignIn();
        });

        binding.txtQuenmatkhau.setOnClickListener(v -> {
            Intent intent = new Intent(SinginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void onClickSignIn() {
        String email = binding.txtEmail.getText().toString().trim();
        String password = binding.txtPassword.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressDialog.setMessage("Đang đăng nhập...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(SinginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(SinginActivity.this, "Sai tài khoản hoặc mật khẩu",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void initListener() {
        binding.txtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.linearlayoutEmail.setBackgroundResource(R.drawable.linear_background);
            } else {
                binding.linearlayoutEmail.setBackgroundResource(R.drawable.linear_background_default);
            }
        });

        binding.imgShowpassword.setVisibility(View.INVISIBLE);

        // Xử lý sự kiện bật/tắt hiển thị mật khẩu
        binding.imgShowpassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Đổi sang chế độ ẩn mật khẩu
                binding.txtPassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                binding.imgShowpassword.setImageResource(R.drawable.eyse); // Biểu tượng mắt đóng
            } else {
                // Đổi sang chế độ hiển thị mật khẩu
                binding.txtPassword.setTransformationMethod(null);
                binding.imgShowpassword.setImageResource(R.drawable.eyes_show); // Biểu tượng mắt mở
            }
            isPasswordVisible = !isPasswordVisible;
            // Đảm bảo con trỏ luôn ở cuối văn bản sau khi thay đổi chế độ
            binding.txtPassword.setSelection(binding.txtPassword.getText().length());
        });

        binding.txtPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.relativeLayoutPassword.setBackgroundResource(R.drawable.linear_background);
                binding.imgShowpassword.setVisibility(View.VISIBLE);
            } else {
                binding.relativeLayoutPassword.setBackgroundResource(R.drawable.linear_background_default);
                if (binding.txtPassword.getText().toString().trim().isEmpty()) {
                    binding.imgShowpassword.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}