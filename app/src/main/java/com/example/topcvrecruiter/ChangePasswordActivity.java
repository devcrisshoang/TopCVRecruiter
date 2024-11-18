package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
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

// Inside ChangePasswordActivity.java

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmPassword;
    private ImageButton back_button;
    private int userId; // To store the user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        // Apply window insets for padding adjustment
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the user_id from the Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1); // Get user_id, default to -1 if not found
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish(); // Exit if user_id is not passed correctly
            return;
        }

        // Initialize views
        back_button = findViewById(R.id.back_button);
        currentPassword = findViewById(R.id.current_password);
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);

        // Set the back button listener
        back_button.setOnClickListener(view -> finish());

        // Set the change password action when clicked (this is where network call happens)
        findViewById(R.id.change_password_button).setOnClickListener(view -> changePassword());
    }

    private void changePassword() {
        String currentPasswordText = currentPassword.getText().toString().trim();
        String newPasswordText = newPassword.getText().toString().trim();
        String confirmPasswordText = confirmPassword.getText().toString().trim();

        // Validate the input fields
        if (currentPasswordText.isEmpty() || newPasswordText.isEmpty() || confirmPasswordText.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPasswordText.equals(confirmPasswordText)) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch the user details to compare current password
        ApiUserService.apiUserService.getUserById(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            // Compare the entered current password with the user's password
                            if (user != null && user.getPassword().equals(currentPasswordText)) {
                                // If current password matches, proceed to change password
                                updatePassword(newPasswordText);
                            } else {
                                // If current password is incorrect, show an error
                                Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            // Handle any errors in fetching the user
                            Toast.makeText(this, "Error retrieving user details: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
    }

    private void updatePassword(String newPasswordText) {
        // Lấy thông tin người dùng từ backend hoặc từ nơi khác (ví dụ, Intent, SharedPreferences,...)
        // Nếu bạn đã có thông tin người dùng trong ứng dụng, bạn chỉ cần lấy `username` và các trường khác

        ApiUserService.apiUserService.getUserById(userId)  // Lấy thông tin người dùng hiện tại
                .subscribeOn(Schedulers.io()) // Thực hiện trên thread nền
                .observeOn(AndroidSchedulers.mainThread()) // Cập nhật UI trên thread chính
                .subscribe(
                        user -> {
                            // Tạo đối tượng User mới và giữ nguyên các trường cần thiết (username, avatar, ...), chỉ thay đổi password
                            User updatedUser = new User();
                            updatedUser.setId(userId);  // Giữ nguyên ID
                            updatedUser.setUsername(user.getUsername());  // Giữ nguyên username
                            updatedUser.setPassword(newPasswordText);  // Cập nhật mật khẩu mới
                            updatedUser.setImageBackground(user.getImageBackground());  // Giữ nguyên ảnh nền
                            updatedUser.setAvatar(user.getAvatar());  // Giữ nguyên avatar
                            updatedUser.setUid(user.getUid());  // Giữ nguyên UID

                            // Gửi yêu cầu PUT để thay đổi mật khẩu, giữ nguyên username
                            ApiUserService.apiUserService.updateUserById(userId, updatedUser)
                                    .subscribeOn(Schedulers.io()) // Thực hiện trên thread nền
                                    .observeOn(AndroidSchedulers.mainThread()) // Cập nhật UI trên thread chính
                                    .subscribe(
                                            () -> {
                                                // Thông báo thành công
                                                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                finish(); // Kết thúc activity sau khi đổi mật khẩu thành công
                                            },
                                            throwable -> {
                                                // Thông báo lỗi nếu có
                                                Toast.makeText(this, "Error changing password: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                    );
                        },
                        throwable -> {
                            // Lỗi khi không lấy được thông tin người dùng
                            Toast.makeText(this, "Error fetching user details: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
    }


}


