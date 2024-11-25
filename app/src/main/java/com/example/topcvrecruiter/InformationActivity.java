package com.example.topcvrecruiter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.topcvrecruiter.API.ApiRecruiterService;
import com.example.topcvrecruiter.Model.Recruiter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InformationActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText editTextEmail;

    private Button submitButton;

    private ImageButton back_button;

    private int id_User;
    private int id_Recruiter;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setWidget();
        setClick();
    }

    private void setClick(){
        submitButton.setOnClickListener(v -> submitApplicant());
        back_button.setOnClickListener(view -> finish());
    }

    private void setWidget(){
        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        submitButton = findViewById(R.id.Submit);
        editTextEmail = findViewById(R.id.editTextEmail);
        back_button = findViewById(R.id.back_button);

        username = getIntent().getStringExtra("username");
        id_User = getIntent().getIntExtra("user_id", -1);
        id_Recruiter = getIntent().getIntExtra("id_Recruiter", -1);
        //Log.e("InformationActivity","id_Recruiter" + id_Recruiter);
        if (username != null) {
            nameEditText.setText(username);
        }
    }

    private void submitApplicant() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập cả tên và số điện thoại.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("Applicant Info", "Name: " + name + ", Phone: " + phone);
        sendApplicantData(name, phone, email);
    }

    @SuppressLint("CheckResult")
    private void sendApplicantData(String name, String phone, String email) {
        if (id_User <= 0) {
            Toast.makeText(this, "ID người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra các trường dữ liệu
        StringBuilder errors = new StringBuilder();

        if (name == null || name.trim().isEmpty()) {
            errors.append("- Tên không được để trống\n");
        }
        if (phone == null || phone.trim().isEmpty()) {
            errors.append("- Số điện thoại không được để trống\n");
        } else if (!phone.matches("\\d{10,15}")) { // Kiểm tra số điện thoại hợp lệ
            errors.append("- Số điện thoại phải là số từ 10 đến 15 chữ số\n");
        }
        if (email == null || email.trim().isEmpty()) {
            errors.append("- Email không được để trống\n");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) { // Kiểm tra định dạng email
            errors.append("- Email không hợp lệ\n");
        }

        // Nếu có lỗi, hiển thị thông báo và dừng xử lý
        if (errors.length() > 0) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin:\n" + errors.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // Tạo đối tượng Recruiter
        Recruiter recruiter = new Recruiter();
        recruiter.setRecruiterName(name);
        recruiter.setPhoneNumber(phone);
        recruiter.setEmailAddress(email);
        recruiter.setFrontImage("");
        recruiter.setBackImage("");
        recruiter.setIs_Registered(false);
        recruiter.setIs_Confirm(false);
        recruiter.setIdUser(id_User);
        recruiter.setIdCompany(0);

        // Gọi API để gửi dữ liệu
        ApiRecruiterService.ApiRecruiterService.createRecruiter(recruiter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            int id_Recruiter = response.getId();
                            Log.d("Recruiter", "ID Recruiter: " + id_Recruiter);
                            Intent intent = new Intent(this, VerifyImageActivity.class);
                            intent.putExtra("user_id", id_User);
                            intent.putExtra("id_Recruiter", id_Recruiter);
                            intent.putExtra("name", name);
                            intent.putExtra("phone", phone);
                            intent.putExtra("email", email);
                            startActivity(intent);
                        },
                        throwable -> {
                            Toast.makeText(this, "Có lỗi xảy ra: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
    }

}
