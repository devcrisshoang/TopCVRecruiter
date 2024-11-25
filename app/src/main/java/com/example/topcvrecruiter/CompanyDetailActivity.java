package com.example.topcvrecruiter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.topcvrecruiter.Model.CompanyInformationDetails;
import com.example.topcvrecruiter.Model.Recruiter;

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

    private Recruiter _recruiter;

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

        setWidget();

        setClick();

    }

    private void setClick(){

        back_button.setOnClickListener(view -> finish());

        finish.setOnClickListener(view -> createCompanyDetail(company_id));
    }

    private void setWidget(){
        recruiter_id = getIntent().getIntExtra("id_Recruiter",0);
        Log.e("CompanyDetailActivity", "Id Recruiter: " + recruiter_id);

        user_id = getIntent().getIntExtra("user_id",0);
        editTextWebsite = findViewById(R.id.editTextWebsite);
        editTextTaxID = findViewById(R.id.editTextTaxID);
        back_button = findViewById(R.id.back_button);
        finish = findViewById(R.id.finish);
        _recruiter = new Recruiter();
        getCompanyId(recruiter_id);
        getRecruiterById(recruiter_id);

    }

    @SuppressLint("CheckResult")
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

    @SuppressLint("CheckResult")
    private void createCompanyDetail(int id) {
        if (id <= 0) {
            Toast.makeText(this, "ID người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy dữ liệu từ các trường nhập liệu
        String website = editTextWebsite.getText().toString().trim();
        String taxIDText = editTextTaxID.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        StringBuilder errors = new StringBuilder();

        if (website.isEmpty()) {
            errors.append("- Website không được để trống\n");
        } else if (!android.util.Patterns.WEB_URL.matcher(website).matches()) { // Kiểm tra định dạng URL
            errors.append("- Website không hợp lệ\n");
        }

        if (taxIDText.isEmpty()) {
            errors.append("- Tax ID không được để trống\n");
        } else {
            try {
                int taxID = Integer.parseInt(taxIDText); // Kiểm tra xem Tax ID có phải số hợp lệ
                if (taxID <= 0) {
                    errors.append("- Tax ID phải là số dương\n");
                }
            } catch (NumberFormatException e) {
                errors.append("- Tax ID phải là số hợp lệ\n");
            }
        }

        // Nếu có lỗi, hiển thị và dừng xử lý
        if (errors.length() > 0) {
            Toast.makeText(this, "Vui lòng kiểm tra thông tin:\n" + errors.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // Tạo đối tượng CompanyInformationDetails
        CompanyInformationDetails companyInformationDetails = new CompanyInformationDetails();
        companyInformationDetails.setIdCompany(company_id);
        companyInformationDetails.setDateFounded(LocalDateTime.now().toString());
        companyInformationDetails.setWebsite(website);
        companyInformationDetails.setTaxID(Integer.parseInt(taxIDText));

        // Gọi API để tạo thông tin chi tiết công ty
        ApiCompanyDetailService.ApiCompanyDetailService.createCompanyDetail(companyInformationDetails)
                .subscribeOn(Schedulers.io())  // Chạy trên luồng nền
                .observeOn(AndroidSchedulers.mainThread())  // Quan sát kết quả trên luồng chính
                .subscribe(
                        response -> {
                            // Xử lý khi thành công
                            Toast.makeText(this, "CompanyInformation đã được tạo thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("id_Recruiter", recruiter_id);
                            intent.putExtra("user_id", user_id);
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

    @SuppressLint("CheckResult")
    private void getRecruiterById(int recruiterId) {
        ApiRecruiterService.ApiRecruiterService.getRecruiterById(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recruiter -> {
                            if (recruiter != null) {
                                _recruiter = recruiter;
                            } else {
                                Toast.makeText(this, "Recruiter not found", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e("AccountFragment", "Error fetching recruiter: " + throwable.getMessage());
                            Toast.makeText(this, "Failed to load recruiter", Toast.LENGTH_SHORT).show();
                        }
                );
    }
}