package com.example.dahitamusic.Fragment;

import static com.example.dahitamusic.Activity.MainActivity.MY_REQUEST_CODE;

import android.Manifest;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dahitamusic.Activity.MainActivity;
import com.example.dahitamusic.Activity.SinginActivity;
import com.example.dahitamusic.R;
import com.example.dahitamusic.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentProfileBinding binding;
    private DatabaseReference mData;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(binding.toolbar);
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("Cá nhân");
            }
        }
        setUserInfo();
        onClick();

        return binding.getRoot();
    }

    private void setUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        binding.txtTennguoidung.setText(user.getDisplayName());
        Picasso.get().load(user.getPhotoUrl()).placeholder(R.drawable.avt_user).into(binding.imgAvt);
    }

    private void onClick() {
        binding.imgAvt.setOnClickListener(v -> {
            onClickRequestPrermission();
        });

        binding.linearlayoutTennguoidung.setOnClickListener(v -> {
            onClickName();
        });

        binding.relativeDoiMatKhau.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.view_pager, new DoiMatKhauFragment())
                    .addToBackStack(null)
                    .commit();
        });

        binding.relativeSignout.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(requireContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_dialog_logout);
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(false);

            Button btn_DangXuat = dialog.findViewById(R.id.btn_dangxuat);
            TextView txt_huy = dialog.findViewById(R.id.txt_huy);

            btn_DangXuat.setOnClickListener(v1 -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), SinginActivity.class);
                startActivity(intent);
                getActivity().finish();
            });

            txt_huy.setOnClickListener(v12 -> {
                dialog.dismiss();
            });

            dialog.show();

        });
    }

    private void onClickRequestPrermission() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mainActivity.openGallery();
            return;
        }
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mainActivity.openGallery();
        } else {
            String[] premission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(premission, MY_REQUEST_CODE);
        }
    }

    public void setBitmapImageView(Bitmap bitmapImageView) {
        binding.imgAvt.setImageBitmap(bitmapImageView);
    }

    private void onClickName() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_edit_name);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edt_tenNguoiDung = dialog.findViewById(R.id.txt_tennguoidung);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_capNhat = dialog.findViewById(R.id.btn_capnhat);
        LinearLayout linearlayout_tennguoidung = dialog.findViewById(R.id.linearlayout_tennguoidung);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        edt_tenNguoiDung.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                linearlayout_tennguoidung.setBackgroundResource(R.drawable.linear_background);
            } else {
                linearlayout_tennguoidung.setBackgroundResource(R.drawable.linear_background_default);
            }
        });

        btn_capNhat.setOnClickListener(v -> {
            String tenNguoiDung = edt_tenNguoiDung.getText().toString().trim();
            if (tenNguoiDung.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập tên người dùng", Toast.LENGTH_SHORT).show();
                return;
            }
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
                            if (task.isSuccessful()) {
                                Toast.makeText(requireContext(), "Cập nhật dùng thành công", Toast.LENGTH_SHORT).show();
                                setUserInfo();
                            }
                        }
                    });
            dialog.dismiss();
        });
        dialog.show();
    }
}