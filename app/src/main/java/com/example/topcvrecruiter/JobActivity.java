package com.example.topcvrecruiter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.topcvrecruiter.API.ApiJobService;
import com.example.topcvrecruiter.Model.Job;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobActivity extends AppCompatActivity {
    private Button continue_button;
    private ImageButton back_button;
    private EditText etName, etExperience, etAddress, etCompany, etSalary;
    private ImageView avatar;
    private ImageView change_avatar;
    private ActivityResultLauncher<Intent> imagePickerLauncherAvatar;
    private Uri uri;

    private int recruiter_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recruiter_id = getIntent().getIntExtra("recruiter_id",0);
        Log.e("JobActivity","ID: " + recruiter_id);

        // Ánh xạ các thành phần giao diện
        continue_button = findViewById(R.id.continue_button);
        back_button = findViewById(R.id.back_button);

        etName = findViewById(R.id.et_name);
        etExperience = findViewById(R.id.et_workexperience);
        etAddress = findViewById(R.id.et_address);
        etCompany = findViewById(R.id.et_company);
        etSalary = findViewById(R.id.et_salary);
        avatar = findViewById(R.id.avatar);
        change_avatar = findViewById(R.id.change_avatar);

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

        imagePickerLauncherAvatar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        avatar.setImageURI(uri);
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        continue_button.setOnClickListener(view -> {
            // Lấy dữ liệu từ các trường nhập liệu
            String jobName = etName.getText().toString().trim();
            String experience = etExperience.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String companyName = etCompany.getText().toString().trim();
            int salary = Integer.parseInt(etSalary.getText().toString().trim());
            String image = (uri != null) ? uri.toString() : "";

            // Tạo một Intent để chuyển sang JobDetailsActivity
            Intent intent = new Intent(JobActivity.this, JobDetailsActivity.class);
            intent.putExtra("image", image);
            intent.putExtra("jobName", jobName);
            intent.putExtra("companyName", companyName);
            intent.putExtra("experience", experience);
            intent.putExtra("address", address);
            intent.putExtra("salary", salary);
            intent.putExtra("id_Recruiter",recruiter_id);

            // Chuyển sang JobDetailsActivity
            startActivity(intent);
        });


        back_button.setOnClickListener(view -> finish());
    }
}
