package com.example.topcvrecruiter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

import com.example.topcvrecruiter.API.ApiRecruiterService;
import com.example.topcvrecruiter.Model.Recruiter;
import com.github.dhaval2404.imagepicker.ImagePicker;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class VerifyImageActivity extends AppCompatActivity {

    private ImageView front_image;
    private ImageView back_image;

    private Button select_front_image;
    private Button select_back_image;
    private Button apply;

    private int user_id;
    private int recruiter_id;

    private Uri backImageUri;
    private Uri frontImageUri;

    private ImageButton back_button;

    private ActivityResultLauncher<Intent> backImagePickerLauncher;
    private ActivityResultLauncher<Intent> frontImagePickerLauncher;

    private String name;
    private String phone;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setWidget();

        getRecruiterId(user_id);

        setClick();

        initImagePicker();
    }

    private void setClick(){
        select_front_image.setOnClickListener(view -> {
            selectFrontImage();
        });
        select_back_image.setOnClickListener(view -> selectBackImage());
        back_button.setOnClickListener(view -> finish());
        apply.setOnClickListener(view -> {
            updateRecruiter();
        });
    }

    @SuppressLint("CheckResult")
    private void updateRecruiter() {
        if (recruiter_id <= 0 || user_id <= 0) {
            Toast.makeText(this, "ID người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        String frontImagePath = (frontImageUri != null) ? frontImageUri.toString().trim() : "";
        String backImagePath = (backImageUri != null) ? backImageUri.toString().trim() : "";

        // Biến lưu trữ danh sách các trường bị thiếu
        StringBuilder missingFields = new StringBuilder();

        if (name.isEmpty()) {
            missingFields.append("- Tên\n");
        }
        if (email.isEmpty()) {
            missingFields.append("- Email\n");
        }
        if (phone.isEmpty()) {
            missingFields.append("- Số điện thoại\n");
        }
        if (frontImagePath.isEmpty()) {
            missingFields.append("- Hình ảnh mặt trước\n");
        }
        if (backImagePath.isEmpty()) {
            missingFields.append("- Hình ảnh mặt sau\n");
        }

        // Nếu có trường bị thiếu, hiển thị thông báo và dừng xử lý
        if (missingFields.length() > 0) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin:\n" + missingFields.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // Nếu thông tin đầy đủ, tiếp tục xử lý cập nhật
        Recruiter recruiter = new Recruiter();
        recruiter.setRecruiterName(name);
        recruiter.setEmailAddress(email);
        recruiter.setPhoneNumber(phone);
        recruiter.setIdUser(user_id);
        recruiter.setIdCompany(0);
        recruiter.setIs_Registered(true);
        recruiter.setIs_Confirm(false);
        recruiter.setFrontImage(frontImagePath);
        recruiter.setBackImage(backImagePath);

        // Gọi API cập nhật thông tin
        ApiRecruiterService.ApiRecruiterService.updateRecruiterById(recruiter_id, recruiter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    // Xử lý thành công
                    Log.d("VerifyImageActivity", "Updated successfully");
                    //Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, CompanyInformationActivity.class);
                    intent.putExtra("id_Recruiter", recruiter_id);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }, throwable -> {
                    // Xử lý lỗi
                    Log.e("VerifyImageActivity", "Failed to update: " + throwable.getMessage());
                    //Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show();
                });
    }


    private void selectFrontImage(){
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    frontImagePickerLauncher.launch(intent);
                    return null;
                });
    }

    private void selectBackImage(){
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    backImagePickerLauncher.launch(intent);
                    return null;
                });
    }

    private void setWidget(){
        front_image = findViewById(R.id.front_image);
        back_image = findViewById(R.id.back_image);
        select_front_image = findViewById(R.id.select_front_image);
        select_back_image = findViewById(R.id.select_back_image);
        apply = findViewById(R.id.apply);
        back_button = findViewById(R.id.back_button);

        user_id = getIntent().getIntExtra("user_id",0);
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
    }

    @SuppressLint("CheckResult")
    private void getRecruiterId(int userId){
            ApiRecruiterService.ApiRecruiterService.getRecruiterByUserId(userId)
                    .subscribeOn(Schedulers.io()) // Xử lý ở luồng IO
                    .observeOn(AndroidSchedulers.mainThread()) // Quan sát trên luồng chính
                    .subscribe(recruiter -> {
                        // Thành công: Lấy ID của Recruiter
                        if (recruiter != null){
                            recruiter_id = recruiter.getId();
                            Log.e("RecruiterID", "Get successfully Recruiter ID: " + recruiter.getId());
                        }
                        else {
                            Log.e("VerifyImageActivity", "Failed Null: " + recruiter_id);
                        }

                        // Sử dụng recruiterId trong logic tiếp theo của bạn
                    }, throwable -> {
                        // Thất bại: Ghi log lỗi
                        Log.e("API Error", "Failed to fetch Recruiter: " + throwable.getMessage());
                    });
    }

    private void initImagePicker() {
        frontImagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Nhận URI của ảnh đã chọn
                        frontImageUri = result.getData().getData();

                        // Cập nhật hình ảnh cho ImageView background
                        front_image.setImageURI(frontImageUri);
                    } else {
                        Toast.makeText(this, "Chưa chọn hình ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        backImagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        backImageUri = result.getData().getData();
                        back_image.setImageURI(backImageUri);
                    } else {
                        Toast.makeText(this, "Chưa chọn hình ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}