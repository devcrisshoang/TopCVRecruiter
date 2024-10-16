package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class InformationActivity extends AppCompatActivity {

    // Các biến thành viên
    private EditText editTextName, editTextPhone;
    private Button submitButton;
    private RadioGroup radioGroupRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_information);

        // Khởi tạo các view
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        submitButton = findViewById(R.id.Submit);

        // Nút submit
        submitButton.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(InformationActivity.this, "Vui lòng nhập tên của bạn", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(InformationActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }

            // Định dạng số điện thoại
            String formattedPhone = formatPhoneNumber(phone);
            if (formattedPhone == null) {
                Toast.makeText(InformationActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Điều hướng sang VerifyPhoneActivity
            Intent intent = new Intent(InformationActivity.this, VerifyPhoneActivity.class);
            intent.putExtra("phone", formattedPhone); // Truyền số điện thoại đã định dạng
            startActivity(intent);
            finish(); // Đóng Activity hiện tại
        });
    }

    private String formatPhoneNumber(String rawPhoneNumber) {
        // Kiểm tra và định dạng số điện thoại
        if (rawPhoneNumber != null) {
            rawPhoneNumber = rawPhoneNumber.trim();
            if (rawPhoneNumber.startsWith("0")) {
                return "+84" + rawPhoneNumber.substring(1); // Chuyển đổi từ 0 sang +84
            } else if (rawPhoneNumber.startsWith("+84")) {
                return rawPhoneNumber; // Nếu đã có mã quốc gia
            }
        }
        return null; // Trả về null nếu không hợp lệ
    }
}
