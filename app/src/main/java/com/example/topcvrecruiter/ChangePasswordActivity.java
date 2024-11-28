package com.example.topcvrecruiter;

import android.annotation.SuppressLint;
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
import com.example.topcvrecruiter.API.ApiUserService;
import com.example.topcvrecruiter.Model.User;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmPassword;

    private ImageButton back_button;

    private Button change_password_button;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
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

        change_password_button.setOnClickListener(view -> changePassword());
    }

    private void setWidget(){
        back_button = findViewById(R.id.back_button);
        currentPassword = findViewById(R.id.current_password);
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);
        userId = getIntent().getIntExtra("user_id", 0);
        change_password_button = findViewById(R.id.change_password_button);
    }

    @SuppressLint("CheckResult")
    private void changePassword() {
        String currentPasswordText = currentPassword.getText().toString().trim();
        String newPasswordText = newPassword.getText().toString().trim();
        String confirmPasswordText = confirmPassword.getText().toString().trim();

        if (currentPasswordText.isEmpty() || newPasswordText.isEmpty() || confirmPasswordText.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPasswordText.equals(confirmPasswordText)) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiUserService.apiUserService.getUserById(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(user -> {
            // Compare the entered current password with the user's password
            if (user != null && user.getPassword().equals(currentPasswordText)) {
                // If current password matches, proceed to change password
                updatePassword(newPasswordText);
            } else {
                // If current password is incorrect, show an error
                Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
            }
        }, throwable -> {
            // Handle any errors in fetching the user
            Toast.makeText(this, "Error retrieving user details: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("CheckResult")
    private void updatePassword(String newPasswordText) {

        ApiUserService.apiUserService.getUserById(userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(user -> {

            User updatedUser = new User();
            updatedUser.setUsername(user.getUsername());
            updatedUser.setPassword(newPasswordText);
            updatedUser.setImageBackground(user.getImageBackground());
            updatedUser.setAvatar(user.getAvatar());
            updatedUser.setUid(user.getUid());
            updatedUser.setApplicant(true);
            updatedUser.setRecruiter(false);

            ApiUserService.apiUserService.updateUserById(userId, updatedUser).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {

                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                finish();
            }, throwable -> {
                Toast.makeText(this, "Error changing password: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }, throwable -> {
            Toast.makeText(this, "Error fetching user details: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}


