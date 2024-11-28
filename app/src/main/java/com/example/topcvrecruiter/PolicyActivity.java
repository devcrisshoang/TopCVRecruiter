package com.example.topcvrecruiter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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

    private boolean isAgreementVisible = false;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        setWidget();

        setClick();

    }

    private void setClick(){

        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY && !isAgreementVisible) {
                linearLayoutAgreement.setVisibility(View.VISIBLE);
                isAgreementVisible = true;
            } else if (scrollY < oldScrollY && isAgreementVisible) {
                linearLayoutAgreement.setVisibility(View.GONE);
                isAgreementVisible = false;
            }
        });

        Register_Button.setOnClickListener(view -> registerButton());
    }

    private void registerButton(){
        if (!agreeCheckBox.isChecked()) {
            Toast.makeText(PolicyActivity.this, "You have not agreed to our policy.", Toast.LENGTH_SHORT).show();
            return;
        }
        createUserAndRedirect();
    }

    private void setWidget(){
        Register_Button = findViewById(R.id.Register_Button);
        iconNumber2 = findViewById(R.id.iconNumber2);
        scrollView = findViewById(R.id.scrollView);
        linearLayoutAgreement = findViewById(R.id.linearLayoutAgreement);
        agreeCheckBox = findViewById(R.id.agreeCheckBox);

        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        if (getIntent().getBooleanExtra("isSignUpButtonClicked", false)) {
            iconNumber2.setColorFilter(getResources().getColor(R.color.green_color), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    @SuppressLint("CheckResult")
    private void createUserAndRedirect() {
        User newUser = new User(username, password, "", "", null,false,true);
        ApiUserService.apiUserService.createUser(newUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    Toast.makeText(PolicyActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PolicyActivity.this, LoginActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                }, throwable -> Toast.makeText(PolicyActivity.this, "Failed to create user: " + throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
