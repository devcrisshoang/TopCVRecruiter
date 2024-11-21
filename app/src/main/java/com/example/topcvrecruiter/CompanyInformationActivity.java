package com.example.topcvrecruiter;

import android.app.Activity;
import android.content.Intent;
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

import com.example.topcvrecruiter.API.ApiCompanyService;
import com.example.topcvrecruiter.API.ApiRecruiterService;
import com.example.topcvrecruiter.Model.Company;
import com.example.topcvrecruiter.Model.Recruiter;
import com.github.dhaval2404.imagepicker.ImagePicker;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CompanyInformationActivity extends AppCompatActivity {
    private int recruiter_id;
    private int user_id;

    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextHotline;
    private EditText editTextField;

    private ImageButton image;
    private Button Submit;
    private ActivityResultLauncher<Intent> ImagePickerLauncher;
    private Uri ImageUri;

    private ImageButton back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recruiter_id = getIntent().getIntExtra("id_Recruiter",0);
        Log.e("VerifyImageActivity", "Id Recruiter: " + recruiter_id);
        user_id = getIntent().getIntExtra("user_id",0);
        back_button = findViewById(R.id.back_button);
        setWidget();
        Submit.setOnClickListener(view -> {
            CreateCompany(recruiter_id);
        });
        image.setOnClickListener(view -> {
            selectImage();
        });
        initImagePicker();
        back_button.setOnClickListener(view -> {
            finish();
        });
    }

    private void selectImage(){
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    ImagePickerLauncher.launch(intent);
                    return null;
                });
    }

    private void initImagePicker() {
        ImagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Nhận URI của ảnh đã chọn
                        ImageUri = result.getData().getData();

                        // Cập nhật hình ảnh cho ImageView background
                        image.setImageURI(ImageUri);
                    } else {
                        Toast.makeText(this, "Chưa chọn hình ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setWidget(){
        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextHotline = findViewById(R.id.editTextHotline);
        editTextField = findViewById(R.id.editTextField);
        image = findViewById(R.id.image);
        Submit = findViewById(R.id.Submit);
    }

    private void CreateCompany(int id) {
        if (id <= 0) {
            Toast.makeText(this, "ID người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        Company company = new Company();
        company.setName(editTextName.getText().toString());
        company.setAddress(editTextAddress.getText().toString());
        company.setHotline(editTextHotline.getText().toString());
        company.setField(editTextField.getText().toString());
        company.setImage(ImageUri.toString());
        company.setGreen_Badge(false);

        ApiCompanyService.ApiCompanyService.createCompanyForRecruiter(id,company)
                .subscribeOn(Schedulers.io())  // Chạy trên luồng nền
                .observeOn(AndroidSchedulers.mainThread())  // Quan sát kết quả trên luồng chính
                .subscribe(
                        response -> {
                            // Xử lý khi thành công
                            Toast.makeText(this, "CompanyInformation đã được tạo thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, CompanyDetailActivity.class);
                            intent.putExtra("id_Recruiter", recruiter_id);
                            intent.putExtra("user_id",user_id);
                            startActivity(intent);
                            //finish();
                        },
                        throwable -> {
                            // Xử lý khi có lỗi
                            Toast.makeText(this, "Có lỗi xảy ra: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
        //finish();
    }
}