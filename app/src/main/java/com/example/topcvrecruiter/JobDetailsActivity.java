package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.topcvrecruiter.API.ApiJobService;
import com.example.topcvrecruiter.model.Job;
import com.example.topcvrecruiter.model.JobDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobDetailsActivity extends AppCompatActivity {
    private Button post_button;
    private ImageButton back_button;
    //private EditText genderRequire, workingTime, workingMethod, workingPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        post_button = findViewById(R.id.post_button);
        back_button = findViewById(R.id.back_button);
//        genderRequire = findViewById(R.id.et_gender);
//        workingTime = findViewById(R.id.et_working_time);
//        workingMethod = findViewById(R.id.et_working_method);
//        workingPosition = findViewById(R.id.et_working_position);

        // Nhận dữ liệu từ Intent
        String jobName = getIntent().getStringExtra("jobName");
        String experience = getIntent().getStringExtra("experience");
        String address = getIntent().getStringExtra("address");

        post_button.setOnClickListener(view -> postJobAndDetailsToApi(jobName, experience, address));
        back_button.setOnClickListener(view -> finish());
    }


    private void postJobAndDetailsToApi(String jobName, String experience, String address) {
        // Lấy thời gian hiện tại và định dạng
        LocalDateTime currentTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        String formattedDateTime = currentTime.format(formatter);

        // Tạo đối tượng Job
        Job job = new Job(jobName, experience, address, formattedDateTime, 1);

        // Gọi API để post Job
        ApiJobService.apiService.postJob(job).enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Nếu post Job thành công, lấy id của Job vừa tạo
                    int jobId = response.body().getId();

                    // Lấy dữ liệu từ các trường nhập liệu của JobDetailsActivity
                    String jobDescription = ((EditText) findViewById(R.id.et_name)).getText().toString().trim();
                    String skillRequire = ((EditText) findViewById(R.id.et_email)).getText().toString().trim();
                    String benefit = ((EditText) findViewById(R.id.et_education)).getText().toString().trim();
                    String genderRequire = ((EditText) findViewById(R.id.et_gender)).getText().toString().trim();
                    String workingTime = ((EditText) findViewById(R.id.et_working_time)).getText().toString().trim();
                    String workingMethod = ((EditText) findViewById(R.id.et_working_method)).getText().toString().trim();
                    String workingPosition = ((EditText) findViewById(R.id.et_working_position)).getText().toString().trim();

                    // Tạo đối tượng JobDetails
                    JobDetails jobDetails = new JobDetails(jobDescription, skillRequire, benefit, genderRequire, workingTime, workingMethod, workingPosition, jobId);

                    // Gọi API để post JobDetails
                    ApiJobService.apiService.postJobDetails(jobDetails).enqueue(new Callback<JobDetails>() {
                        @Override
                        public void onResponse(Call<JobDetails> call, Response<JobDetails> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(JobDetailsActivity.this, "Đăng tin tuyển dụng thành công!", Toast.LENGTH_SHORT).show();

                                // Chuyển về MainActivity
                                Intent intent = new Intent(JobDetailsActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(JobDetailsActivity.this, "Failed to post job details!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JobDetails> call, Throwable t) {
                            Log.e("API_ERROR", t.getMessage());
                            Toast.makeText(JobDetailsActivity.this, "An error occurred while posting job details!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(JobDetailsActivity.this, "Failed to post job!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Job> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(JobDetailsActivity.this, "An error occurred while posting job!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}