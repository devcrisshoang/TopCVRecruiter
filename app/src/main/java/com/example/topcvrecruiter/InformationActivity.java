package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.topcvrecruiter.API.ApiRecruiterService;
import com.example.topcvrecruiter.Model.Recruiter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InformationActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private Button submitButton;
    private EditText editTextEmail;

    private int id_User; // Biến lưu ID người dùng
    private int id_Recruiter;
    private String username; // Biến lưu tên người dùng

    private ImageButton back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        submitButton = findViewById(R.id.Submit);
        editTextEmail = findViewById(R.id.editTextEmail);
        back_button = findViewById(R.id.back_button);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username"); // Tên tài khoản
        id_User = intent.getIntExtra("user_id", -1); // Nhận ID người dùng

        // Điền dữ liệu vào EditText nếu cần
        if (username != null) {
            nameEditText.setText(username);
        }

        submitButton.setOnClickListener(v -> submitApplicant());
        back_button.setOnClickListener(view -> finish());
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

        // Gửi thông tin đến server và chuyển Activity khi thành công
        sendApplicantData(name, phone, email);
    }

    private void sendApplicantData(String name, String phone, String email) {
        if (id_User <= 0) {
            Toast.makeText(this, "ID người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

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
        ApiRecruiterService.ApiRecruiterService.createRecruiter(recruiter)
                .subscribeOn(Schedulers.io())  // Chạy trên luồng nền
                .observeOn(AndroidSchedulers.mainThread())  // Quan sát kết quả trên luồng chính
                // Giả sử response trả về là một đối tượng JSON có trường id_Recruiter
                .subscribe(
                        response -> {
                            int id_Recruiter = response.getId(); // Nhận id_Recruiter từ response
                            Log.d("Recruiter", "ID Recruiter: " + id_Recruiter);

                            // Chuyển sang VerifyImageActivity với id_Recruiter
                            Intent intent = new Intent(this, VerifyImageActivity.class);
                            intent.putExtra("user_id", id_User);
                            intent.putExtra("id_Recruiter", id_Recruiter); // Truyền id_Recruiter
                            intent.putExtra("name", name);
                            intent.putExtra("phone", phone);
                            intent.putExtra("email", email);
                            startActivity(intent);
                        },
                        throwable -> {
                            Toast.makeText(this, "Có lỗi xảy ra: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );

        //finish();
    }
}
