package com.example.topcvrecruiter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.topcvrecruiter.API.ApiCompanyService;
import com.example.topcvrecruiter.Model.Company;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CompanyInformationActivity extends AppCompatActivity {

    private int recruiter_id;
    private int user_id;

    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextHotline;
    private EditText editTextField;

    private ImageButton back_button;

    private Button Submit;


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

        setWidget();

        setClick();

    }

    private void setClick(){
        Submit.setOnClickListener(view -> {
            CreateCompany(recruiter_id);
        });
        back_button.setOnClickListener(view -> {
            finish();
        });
    }

    private void setWidget(){
        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextHotline = findViewById(R.id.editTextHotline);
        editTextField = findViewById(R.id.editTextField);
        Submit = findViewById(R.id.Submit);

        recruiter_id = getIntent().getIntExtra("id_Recruiter",0);
        user_id = getIntent().getIntExtra("user_id",0);
        back_button = findViewById(R.id.back_button);
    }

    @SuppressLint("CheckResult")
    private void CreateCompany(int id) {
        if (id <= 0) {
            Toast.makeText(this, "ID người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy dữ liệu từ các trường nhập liệu
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String hotline = editTextHotline.getText().toString().trim();
        String field = editTextField.getText().toString().trim();

        // Danh sách lỗi
        StringBuilder errors = new StringBuilder();

        if (name.isEmpty()) {
            errors.append("- Tên công ty không được để trống\n");
        }
        if (address.isEmpty()) {
            errors.append("- Địa chỉ không được để trống\n");
        }
        if (hotline.isEmpty()) {
            errors.append("- Số hotline không được để trống\n");
        } else if (!hotline.matches("\\d{8,15}")) { // Kiểm tra số điện thoại
            errors.append("- Số hotline phải là số từ 10 đến 15 chữ số\n");
        }
        if (field.isEmpty()) {
            errors.append("- Lĩnh vực hoạt động không được để trống\n");
        }

        // Nếu có lỗi, hiển thị Toast và dừng thực hiện
        if (errors.length() > 0) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin:\n" + errors.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // Nếu không có lỗi, tiếp tục tạo đối tượng Company
        Company company = new Company();
        company.setName(name);
        company.setAddress(address);
        company.setHotline(hotline);
        company.setField(field);
        company.setImage("");
        company.setGreen_Badge(false);

        // Gọi API để tạo công ty
        ApiCompanyService.ApiCompanyService.createCompanyForRecruiter(id, company)
                .subscribeOn(Schedulers.io())  // Chạy trên luồng nền
                .observeOn(AndroidSchedulers.mainThread())  // Quan sát kết quả trên luồng chính
                .subscribe(
                        response -> {
                            // Xử lý khi thành công
                            //Toast.makeText(this, "CompanyInformation đã được tạo thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, CompanyDetailActivity.class);
                            intent.putExtra("id_Recruiter", recruiter_id);
                            intent.putExtra("user_id", user_id);
                            startActivity(intent);
                        },
                        throwable -> {
                            // Xử lý khi có lỗi
                            Toast.makeText(this, "Có lỗi xảy ra: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
    }
}