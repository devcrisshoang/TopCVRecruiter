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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobActivity extends AppCompatActivity {
    private Button continue_button;
    private ImageButton back_button;
    private EditText etName, etExperience, etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các thành phần giao diện
        continue_button = findViewById(R.id.continue_button);
        back_button = findViewById(R.id.back_button);
        etName = findViewById(R.id.et_name);
        etExperience = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_education);

        //continue_button.setOnClickListener(view -> postJobToApi());
        continue_button.setOnClickListener(view -> {
            // Lấy dữ liệu từ các trường nhập liệu
            String jobName = etName.getText().toString().trim();
            String experience = etExperience.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            // Tạo một Intent để chuyển sang JobDetailsActivity
            Intent intent = new Intent(JobActivity.this, JobDetailsActivity.class);
            intent.putExtra("jobName", jobName);
            intent.putExtra("experience", experience);
            intent.putExtra("address", address);

            // Chuyển sang JobDetailsActivity
            startActivity(intent);
        });


        back_button.setOnClickListener(view -> finish());
    }

    private void postJobToApi() {
        // Lấy dữ liệu từ các trường nhập liệu
        String jobName = etName.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Lấy thời gian hiện tại
        LocalDateTime currentTime = LocalDateTime.now();

        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        // Chuyển đổi thời gian thành chuỗi theo định dạng đã chọn
        String formattedDateTime = currentTime.format(formatter);



        // Tạo đối tượng Job từ dữ liệu nhập vào
        Job job = new Job(jobName,experience,address,formattedDateTime, 1);
        job.setJob_Name(jobName);
        job.setWorking_Experience_Require(experience);
        job.setWorking_Address(address);
        job.setApplication_Status(false); // có thể thiết lập giá trị mặc định nếu cần

        // Gọi API để post job
        ApiJobService.apiService.postJob(job).enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(JobActivity.this, "Job posted successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(JobActivity.this, "Failed to post job!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Job> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(JobActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
