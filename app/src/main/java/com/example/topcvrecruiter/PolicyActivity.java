package com.example.topcvrecruiter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.topcvrecruiter.API.ApiUserService;
import com.example.topcvrecruiter.Model.User;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PolicyActivity extends AppCompatActivity {
    private Button Register_Button;
    private ImageView iconNumber2;
    private ScrollView scrollView;
    private View linearLayoutAgreement;
    private CheckBox agreeCheckBox;
    private ApiUserService apiUserService;

    private boolean isAgreementVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_policy);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Register_Button = findViewById(R.id.Register_Button);
        iconNumber2 = findViewById(R.id.iconNumber2);
        scrollView = findViewById(R.id.scrollView);
        linearLayoutAgreement = findViewById(R.id.linearLayoutAgreement);
        agreeCheckBox = findViewById(R.id.agreeCheckBox);

        // Nhận username và password từ Intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        if (intent.getBooleanExtra("isSignUpButtonClicked", false)) {
            iconNumber2.setColorFilter(getResources().getColor(R.color.green_color), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        // Tạo đối tượng User mới
        User newUser = new User(username, password, 0, 0, 0); // Giả định các giá trị khác

        // Gọi API POST để tạo người dùng
        apiUserService = ApiUserService.apiUserService;
        apiUserService.createUser(newUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    // Xử lý khi tạo người dùng thành công
                    Toast.makeText(PolicyActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                }, throwable -> {
                    // Xử lý lỗi khi tạo người dùng
                    Toast.makeText(PolicyActivity.this, "Failed to create user: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Kiểm tra và hiển thị debug thông tin username và password
        Toast.makeText(this, "Username: " + username + ", Password: " + password, Toast.LENGTH_SHORT).show();

        Register_Button.setOnClickListener(view -> {
            if (!agreeCheckBox.isChecked()){
                Toast.makeText(PolicyActivity.this, "You have not agreed to our policy.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Chuyển đến màn hình đăng nhập sau khi đăng ký thành công
            startActivity(new Intent(PolicyActivity.this, LoginActivity.class));
            finish();
        });

        // Lắng nghe sự kiện cuộn của ScrollView
        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY && !isAgreementVisible) {
                linearLayoutAgreement.setVisibility(View.VISIBLE);
                isAgreementVisible = true;
            } else if (scrollY < oldScrollY && isAgreementVisible) {
                linearLayoutAgreement.setVisibility(View.GONE);
                isAgreementVisible = false;
            }
        });
    }
}
