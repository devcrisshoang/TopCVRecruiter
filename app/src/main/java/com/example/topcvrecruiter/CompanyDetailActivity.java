package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.topcvrecruiter.API.ApiCompanyDetailService;
import com.example.topcvrecruiter.API.ApiCompanyService;
import com.example.topcvrecruiter.API.ApiRecruiterService;
import com.example.topcvrecruiter.Model.Company;
import com.example.topcvrecruiter.Model.CompanyInformationDetails;
import com.google.type.DateTime;

import java.time.LocalDateTime;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CompanyDetailActivity extends AppCompatActivity {
    private ImageButton back_button;

    private EditText editTextWebsite;
    private EditText editTextTaxID;

    private int recruiter_id;
    private int user_id;
    private int company_id;

    private Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recruiter_id = getIntent().getIntExtra("recruiter_id",0);
        user_id = getIntent().getIntExtra("user_id",0);
        editTextWebsite = findViewById(R.id.editTextWebsite);
        editTextTaxID = findViewById(R.id.editTextTaxID);
        back_button = findViewById(R.id.back_button);
        finish = findViewById(R.id.finish);
        back_button.setOnClickListener(view -> {
            finish();
        });
        getCompanyId(recruiter_id);
        finish.setOnClickListener(view -> {
            createCompanyDetail(company_id);
        });
    }
    private void getCompanyId(int id){
        ApiCompanyService.ApiCompanyService.getCompanyByRecruiterId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    company_id = response.getId();
                }, throwable -> {
                    Log.e("API Error", "Error fetching applicant: " + throwable.getMessage());
                    Toast.makeText(this, "Không tìm thấy Applicant, chuyển đến trang Information.", Toast.LENGTH_SHORT).show();
                });
    }
    private void createCompanyDetail(int id) {
        if (id <= 0) {
            Toast.makeText(this, "ID người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        CompanyInformationDetails companyInformationDetails = new CompanyInformationDetails();
        companyInformationDetails.setIdCompany(company_id);
        LocalDateTime currentTime = LocalDateTime.now();
        companyInformationDetails.setDateFounded(currentTime.toString());
        companyInformationDetails.setWebsite(editTextWebsite.getText().toString());
        companyInformationDetails.setTaxID(Integer.parseInt(editTextTaxID.getText().toString()));

        ApiCompanyDetailService.ApiCompanyDetailService.createCompanyDetail(companyInformationDetails)
                .subscribeOn(Schedulers.io())  // Chạy trên luồng nền
                .observeOn(AndroidSchedulers.mainThread())  // Quan sát kết quả trên luồng chính
                .subscribe(
                        response -> {
                            // Xử lý khi thành công
                            Toast.makeText(this, "CompanyInformation đã được tạo thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("recruiter_id", recruiter_id);
                            intent.putExtra("user_id",user_id);
                            startActivity(intent);
                            finish();
                        },
                        throwable -> {
                            // Xử lý khi có lỗi
                            Toast.makeText(this, "Có lỗi xảy ra: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
        finish();
    }
}