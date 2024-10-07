package com.example.topcvrecruiter.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.topcvrecruiter.AboutActicity;
//import com.example.topcvrecruiter.LoginActivity;
import com.example.topcvrecruiter.PrivatePolicyActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.TermOfServiceActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class AccountFragment extends Fragment {
    private Button about_application_button;
    private Button term_of_services_button;
    private Button privacy_policy_button;
    private Button sign_out_button;
    private Button change_background_button;
    private ImageView background;
    private ImageView avatar;
    private ImageView change_avatar;

    // Tạo một ActivityResultLauncher để thay thế cho onActivityResult
    private ActivityResultLauncher<Intent> imagePickerLauncherBackground;
    private ActivityResultLauncher<Intent> imagePickerLauncherAvatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        about_application_button = view.findViewById(R.id.about_application_button);
        term_of_services_button = view.findViewById(R.id.term_of_services_button);
        privacy_policy_button = view.findViewById(R.id.privacy_policy_button);
        sign_out_button = view.findViewById(R.id.sign_out_button);
        change_background_button = view.findViewById(R.id.change_background_button);
        background = view.findViewById(R.id.background);
        avatar = view.findViewById(R.id.avatar);
        change_avatar = view.findViewById(R.id.change_avatar);

        about_application_button.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), AboutActicity.class);
            startActivity(intent);
        });

        privacy_policy_button.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), PrivatePolicyActivity.class);
            startActivity(intent);
        });

        term_of_services_button.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), TermOfServiceActivity.class);
            startActivity(intent);
        });

        sign_out_button.setOnClickListener(view12 -> {
//            new AlertDialog.Builder(getContext())
//                    .setTitle("Confirm sign out")
//                    .setMessage("Are you sure that you want to sign out?")
//                    .setPositiveButton("Yes", (dialog, which) -> {
//                        Intent intent = new Intent(getContext(), LoginActivity.class);
//                        startActivity(intent);
//                        getActivity().finish();
//                    })
//                    .setNegativeButton("No", (dialog, which) -> {
//                        dialog.dismiss();
//                    })
//                    .show();
        });

        // Đăng ký ActivityResultLauncher để nhận kết quả từ ImagePicker
        imagePickerLauncherBackground = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        background.setImageURI(uri);
                    } else {
                        Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        imagePickerLauncherAvatar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        avatar.setImageURI(uri);
                    } else {
                        Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Xử lý sự kiện chọn ảnh nền
        change_background_button.setOnClickListener(view13 -> {
            ImagePicker.with(this)
                    .crop()                // Cắt ảnh (tùy chọn)
                    .compress(1024)        // Nén ảnh (tùy chọn)
                    .maxResultSize(1080, 1080)  // Giới hạn kích thước ảnh (tùy chọn)
                    .createIntent(intent -> {
                        imagePickerLauncherBackground.launch(intent);  // Sử dụng launcher thay vì onActivityResult
                        return null;
                    });
        });
        change_avatar.setOnClickListener(view13 -> {
            ImagePicker.with(this)
                    .crop()                // Cắt ảnh (tùy chọn)
                    .compress(1024)        // Nén ảnh (tùy chọn)
                    .maxResultSize(1080, 1080)  // Giới hạn kích thước ảnh (tùy chọn)
                    .createIntent(intent -> {
                        imagePickerLauncherAvatar.launch(intent);  // Sử dụng launcher thay vì onActivityResult
                        return null;
                    });
        });

        return view;
    }
}
