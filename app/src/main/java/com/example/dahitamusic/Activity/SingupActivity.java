package com.example.dahitamusic.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dahitamusic.Model.User;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.ActivitySinginBinding;
import com.example.dahitamusic.databinding.ActivitySingupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SingupActivity extends AppCompatActivity {
    private ActivitySingupBinding binding;
    private ProgressDialog progressDialog;
    private DatabaseReference mData;
    private boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySingupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(this);
        initListener();
    }

    private void initListener() {
        binding.imgBack.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Dừng tạo tài khoản")
                    .setMessage("Bạn có muốn dừng tạo tài khoản không?")
                    .setPositiveButton("Dừng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        binding.txtTennguoidung.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.linearlayoutTennguoidung.setBackgroundResource(R.drawable.linear_background);
            } else {
                binding.linearlayoutTennguoidung.setBackgroundResource(R.drawable.linear_background_default);
            }
        });

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

        binding.btnSingup.setOnClickListener(v -> {
            onClickSingUp();
        });
    }

    private void onClickSingUp() {
        String email = binding.txtEmail.getText().toString().trim();
        String password = binding.txtPassword.getText().toString().trim();
        String tenNguoiDung = binding.txtTennguoidung.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty() || tenNguoiDung.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressDialog.setMessage("Đang tạo tài khoản...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userId = user.getUid();

                                // Lưu thông tin vào Realtime Database
                                saveUserToDatabase(userId, tenNguoiDung, email);
                                seveNameUser(tenNguoiDung);
                            }

                            Intent intent = new Intent(SingupActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(SingupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserToDatabase(String userId, String tenNguoiDung, String email) {
        mData = FirebaseDatabase.getInstance().getReference("Users");
        User user = new User(tenNguoiDung, email);
        mData.child(userId).setValue(user);;
    }

    private void seveNameUser(String tenNguoiDung) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(tenNguoiDung)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
}