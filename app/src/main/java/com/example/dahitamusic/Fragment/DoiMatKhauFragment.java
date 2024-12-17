package com.example.dahitamusic.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentDoiMatKhauBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoiMatKhauFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoiMatKhauFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentDoiMatKhauBinding binding;
    private ProgressDialog progressDialog;

    public DoiMatKhauFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoiMatKhauFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoiMatKhauFragment newInstance(String param1, String param2) {
        DoiMatKhauFragment fragment = new DoiMatKhauFragment();
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDoiMatKhauBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(getActivity());
        initListener();
        onClick();
        return binding.getRoot();
    }

    private void initListener() {
        binding.txtNewpassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.linearlayoutNewpassword.setBackgroundResource(R.drawable.linear_background);
            } else {
                binding.linearlayoutNewpassword.setBackgroundResource(R.drawable.linear_background_default);
            }
        });

        binding.txtNhaplaipassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.linearlayoutNhaplaipassword.setBackgroundResource(R.drawable.linear_background);
            } else {
                binding.linearlayoutNhaplaipassword.setBackgroundResource(R.drawable.linear_background_default);
            }
        });
    }

    private void onClick() {
        binding.imgBack.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Dừng đổi mật khẩu")
                    .setMessage("Bạn có muốn dừng đổi mật khẩu không?")
                    .setPositiveButton("Dừng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requireActivity().onBackPressed();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        binding.ckbHienthimatkhau.setOnClickListener(v -> {
            if (binding.ckbHienthimatkhau.isChecked()) {
                // Hiển thị mật khẩu
                binding.txtNewpassword.setTransformationMethod(null);
                binding.txtNhaplaipassword.setTransformationMethod(null);
            } else {
                // Ẩn mật khẩu
                binding.txtNewpassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                binding.txtNhaplaipassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
            }
            // Di chuyển con trỏ về cuối trường văn bản
            binding.txtNewpassword.setSelection(binding.txtNewpassword.length());
            binding.txtNhaplaipassword.setSelection(binding.txtNhaplaipassword.length());
        });


        binding.btnDoimatkhau.setOnClickListener(v -> {
            onClickDoimatkhau();
        });
    }

    private void onClickDoimatkhau() {
        String newpassword = binding.txtNewpassword.getText().toString().trim();
        String nhaplaipassword = binding.txtNhaplaipassword.getText().toString().trim();
        if (newpassword.isEmpty()) {
            binding.txtNewpassword.setError("Vui lòng nhập mật khẩu mới");
            return;
        }
        if (nhaplaipassword.isEmpty()) {
            binding.txtNhaplaipassword.setError("Vui lòng nhập lại mật khẩu mới");
            return;
        }
        if (!newpassword.equals(nhaplaipassword)) {
            binding.txtNhaplaipassword.setError("Mật khẩu mới không khớp");
            return;
        }

        progressDialog.setMessage("Đang đổi mật khẩu...");
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(newpassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            dialogXacThuc();
                        }
                    }
                });

    }

    private void dialogXacThuc() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_xacthuc_taikhoan);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edt_Email = dialog.findViewById(R.id.txt_email);
        EditText edt_Password = dialog.findViewById(R.id.txt_password);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_XacThuc = dialog.findViewById(R.id.btn_xacthuc);
        LinearLayout linearlayout_email = dialog.findViewById(R.id.linearlayout_email);
        LinearLayout linearlayout_password = dialog.findViewById(R.id.linearlayout_passwword);

        edt_Email.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                linearlayout_email.setBackgroundResource(R.drawable.linear_background);
            } else {
                linearlayout_email.setBackgroundResource(R.drawable.linear_background_default);
            }
        });

        edt_Password.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                linearlayout_password.setBackgroundResource(R.drawable.linear_background);
            } else {
                linearlayout_password.setBackgroundResource(R.drawable.linear_background_default);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_XacThuc.setOnClickListener(v -> {
            String email = edt_Email.getText().toString().trim();
            String password = edt_Password.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            reauthenticate(email, password);
        });
        dialog.show();
    }

    private void reauthenticate(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Xác thực thành công", Toast.LENGTH_SHORT).show();
                            onClickDoimatkhau();
                        } else {
                            Toast.makeText(getActivity(), "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}