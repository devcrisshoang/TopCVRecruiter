package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.topcvrecruiter.API.ApiRecruiterService;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WaitingConfirmActivity extends AppCompatActivity {

    private int user_id;
    private int id_Recruiter;

    private String recruiterName;
    private String phoneNumber;

    private Button back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_waiting_confirm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();
    }

    private void setClick(){
        back_button.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void setWidget(){
        user_id = getIntent().getIntExtra("user_id",0);
        id_Recruiter = getIntent().getIntExtra("id_Recruiter",0);
        recruiterName = getIntent().getStringExtra("recruiterName");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        back_button = findViewById(R.id.back_button);
        confirm(user_id);
    }

    private void confirm(int user_id){
        ApiRecruiterService.ApiRecruiterService.getRecruiterByUserId(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.isIs_Confirm() == true){
                        navigateToMainActivity(user_id, recruiterName, phoneNumber);
                    }
                }, throwable -> {
                    Log.e("API Error", "Error fetching applicant: " + throwable.getMessage());
                });
    }

    private void navigateToMainActivity(int id_User, String recruiterName, String phoneNumber) {
        ApiRecruiterService.ApiRecruiterService.getRecruiterByUserId(id_User)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recruiter -> {
                            if (recruiter != null) {
                                id_Recruiter = recruiter.getId();
                                Intent intent = new Intent(WaitingConfirmActivity.this, MainActivity.class);
                                intent.putExtra("user_id", id_User);
                                intent.putExtra("recruiterName", recruiterName);
                                intent.putExtra("phoneNumber", phoneNumber);
                                intent.putExtra("id_Recruiter", id_Recruiter);
                                Log.e("LoginActivity","ID: "+ id_Recruiter);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Recruiter not found", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e("AccountFragment", "Error fetching recruiter: " + throwable.getMessage());
                            Toast.makeText(this, "Failed to load recruiter", Toast.LENGTH_SHORT).show();
                        }
                );
        finish();
    }

}