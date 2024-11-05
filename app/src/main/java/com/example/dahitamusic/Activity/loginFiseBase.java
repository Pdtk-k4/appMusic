package com.example.dahitamusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dahitamusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginFiseBase extends AppCompatActivity {
    private EditText email_edittext, password_edittext;
    private Button login_btn;
    private TextView goto_signup_btn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_fise_base);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainchinh), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Liên kết các View với ID trong layout
        email_edittext = findViewById(R.id.email_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        login_btn = findViewById(R.id.login_btn);
        goto_signup_btn = findViewById(R.id.goto_signup_btn);

        // Xử lý sự kiện nhấn nút đăng nhập
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        // Xử lý sự kiện chuyển đến trang đăng ký
        goto_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    // Phương thức chuyển đến trang đăng ký
    private void signup() {
        Intent i = new Intent(loginFiseBase.this, MainActivity.class);
        startActivity(i);
    }

    // Phương thức xử lý đăng nhập
    private void login() {
        String email = email_edittext.getText().toString();
        String password = password_edittext.getText().toString();

        // Kiểm tra xem email và password đã được nhập chưa
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập email.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đăng nhập bằng Firebase Auth
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            // Chuyển đến MainActivity sau khi đăng nhập thành công
                            Intent intent = new Intent(loginFiseBase.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Kết thúc Activity hiện tại để không thể quay lại trang login bằng nút back
                        } else {
                            // Thông báo lỗi đăng nhập
                            Toast.makeText(getApplicationContext(), "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}