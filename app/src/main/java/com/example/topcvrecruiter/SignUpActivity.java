package com.example.topcvrecruiter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.topcvrecruiter.API.ApiUserService;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpActivity extends AppCompatActivity {

    private Button Next_Button;

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;

    private ImageButton see_password1;
    private ImageButton see_password2;

    private boolean isPasswordVisible = false;

    private ApiUserService apiUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();

    }

    private void createAccount(){
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Passwords do not match. Please try again!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(SignUpActivity.this, "Your password is too short, try again!", Toast.LENGTH_SHORT).show();
            return;
        }

        checkUsernameExists(username);
    }

    private void setClick(){

        see_password1.setOnClickListener(v -> togglePasswordVisibility(passwordInput));

        see_password2.setOnClickListener(v -> togglePasswordVisibility(confirmPasswordInput));

        Next_Button.setOnClickListener(view -> createAccount());

    }

    private void setWidget(){
        Next_Button = findViewById(R.id.Next_Button);
        usernameInput = findViewById(R.id.txtDangkymail);
        passwordInput = findViewById(R.id.txtDangkymatkhau);
        confirmPasswordInput = findViewById(R.id.txtConfirmPassword);
        ImageView iconNumber1 = findViewById(R.id.iconNumber1);
        see_password1 = findViewById(R.id.see_password1);
        see_password2 = findViewById(R.id.see_password2);
        apiUserService = ApiUserService.apiUserService;
        Intent intent = getIntent();
        if (intent.getBooleanExtra("isSignUpButtonClicked", false)) {
            iconNumber1.setColorFilter(getResources().getColor(R.color.green_color), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    private void togglePasswordVisibility(EditText editText) {
        if (isPasswordVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        editText.setSelection(editText.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    @SuppressLint("CheckResult")
    private void checkUsernameExists(String username) {
        apiUserService.getAllUsernames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(usernames -> {
                    if (usernames.contains(username)) {
                        Toast.makeText(SignUpActivity.this, "Account name already exists. Please choose another name!", Toast.LENGTH_SHORT).show();
                        usernameInput.setError("Username already exists.");
                    } else {
                        Intent policyIntent = new Intent(SignUpActivity.this, PolicyActivity.class);
                        policyIntent.putExtra("isSignUpButtonClicked", true);
                        policyIntent.putExtra("username", username);
                        policyIntent.putExtra("password", passwordInput.getText().toString());
                        startActivity(policyIntent);
                        finish();
                    }
                }, throwable -> Toast.makeText(SignUpActivity.this, "Failed to get usernames: " + throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
