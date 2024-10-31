package com.example.topcvrecruiter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.topcvrecruiter.API.ApiJobService;
import com.example.topcvrecruiter.model.Job;
import com.example.topcvrecruiter.model.JobDetails;
import com.github.dhaval2404.imagepicker.ImagePicker;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.topcvrecruiter.model.Job;

public class EditJobActivity extends AppCompatActivity {
    private EditText jobName;
    private EditText workLocation;
    private EditText experienceRequire;
    private EditText workMethod;
    private EditText genderRequire;
    private EditText workPosition;
    private EditText applyDate;
    private EditText jobDescription;
    private EditText skillRequire;
    private EditText benefit;
    private EditText workingTime;
    private EditText Salary;
    private EditText numberofpeople;
    private EditText companyName;
    private Button saveButton;
    private int jobId;
    private int jobDetailsId;
    private ImageView avatar;
    private ImageView change_avatar;
    private ActivityResultLauncher<Intent> imagePickerLauncherAvatar;
    private Uri uri;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);

        // Ánh xạ các view
        jobName = findViewById(R.id.job_name);
        Salary = findViewById(R.id.et_salary);
        companyName = findViewById(R.id.et_companyName);
        workLocation = findViewById(R.id.location);
        experienceRequire = findViewById(R.id.experience);
        workMethod = findViewById(R.id.work_method);
        genderRequire = findViewById(R.id.gender);
        workPosition = findViewById(R.id.position);
        applyDate = findViewById(R.id.deadline);
        jobDescription = findViewById(R.id.description);
        skillRequire = findViewById(R.id.conditions);
        benefit = findViewById(R.id.interesting);
        workingTime = findViewById(R.id.working_time);
        numberofpeople = findViewById(R.id.et_numberpeople);

        saveButton = findViewById(R.id.save_button);
        avatar = findViewById(R.id.avatar);
        change_avatar = findViewById(R.id.change_avatar);

        // Nhận dữ liệu từ Intent
        jobId = getIntent().getIntExtra("jobId", -1);
        jobDetailsId = getIntent().getIntExtra("jobDetailsId", -1);
        jobName.setText(getIntent().getStringExtra("jobName"));
        Salary.setText(getIntent().getStringExtra("salary"));
        companyName.setText(getIntent().getStringExtra("companyName"));
        workLocation.setText(getIntent().getStringExtra("workLocation"));
        experienceRequire.setText(getIntent().getStringExtra("experienceRequire"));
        workMethod.setText(getIntent().getStringExtra("workMethod"));
        genderRequire.setText(getIntent().getStringExtra("genderRequire"));
        workPosition.setText(getIntent().getStringExtra("workPosition"));
        applyDate.setText(getIntent().getStringExtra("applyDate"));
        jobDescription.setText(getIntent().getStringExtra("jobDescription"));
        skillRequire.setText(getIntent().getStringExtra("skillRequire"));
        benefit.setText(getIntent().getStringExtra("benefit"));
        workingTime.setText(getIntent().getStringExtra("workingTime"));
        numberofpeople.setText(getIntent().getStringExtra("numberOfPeople"));

        // Nhận đường dẫn ảnh từ Intent
        String imagePath = getIntent().getStringExtra("imagePath");

        if (imagePath != null && !imagePath.isEmpty()) {
            // Sử dụng Glide để tải và hiển thị ảnh vào ImageView
            Glide.with(this)
                    .load(Uri.parse(imagePath)) // Nếu đường dẫn ảnh là URI
                    .into(avatar);
        }

        // Thiết lập sự kiện cho nút save
        saveButton.setOnClickListener(v -> showConfirmationDialog());



        change_avatar.setOnClickListener(view13 -> {
            ImagePicker.with(this)
                    .crop()                // Cắt ảnh (tùy chọn)
                    .compress(1024)        // Nén ảnh (tùy chọn)
                    .maxResultSize(1080, 1080)  // Giới hạn kích thước ảnh (tùy chọn)
                    .createIntent(intent -> {
                        imagePickerLauncherAvatar.launch(intent);  // Sử dụng launcher thay vì onActivityResult
                        return null;
                    });
        });

        imagePickerLauncherAvatar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        avatar.setImageURI(uri);
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
    private void showConfirmationDialog() {
        // Create a confirmation dialog
        new AlertDialog.Builder(EditJobActivity.this)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to update this job?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // If the user clicks "Yes", call the updateJob method to update the job
                        updateJob();
                    }
                })
                .setNegativeButton("No", null) // If the user clicks "No", just close the dialog
                .show();
    }


    private void updateJob() {
        // Lấy các giá trị từ EditText
        String name = jobName.getText().toString();
        String salary = Salary.getText().toString();
        String company = companyName.getText().toString();
        String location = workLocation.getText().toString();
        String experience = experienceRequire.getText().toString();
        String method = workMethod.getText().toString();
        String gender = genderRequire.getText().toString();
        String position = workPosition.getText().toString();
        String date = applyDate.getText().toString();
        String description = jobDescription.getText().toString();
        String skill = skillRequire.getText().toString();
        String benefitText = benefit.getText().toString();
        String time = workingTime.getText().toString();
        String numberPeople = numberofpeople.getText().toString();
        String image = (uri != null) ? uri.toString() : "";  // Hoặc URL mặc định


        // Tạo đối tượng Job và JobDetails với dữ liệu đã cập nhật
        Job updatedJob = new Job(image, name, company, experience, location, salary, date, 1);
        JobDetails updatedJobDetails = new JobDetails(description, skill, benefitText, gender, time, method, position, numberPeople, jobId);

        // Gọi API để cập nhật Job
        ApiJobService.apiService.updateJob(jobId, updatedJob).enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditJobActivity.this, "Updated Job Success!", Toast.LENGTH_SHORT).show();

                    // Gọi API để cập nhật JobDetails
                    ApiJobService.apiService.putJobDetails(jobDetailsId, updatedJobDetails).enqueue(new Callback<JobDetails>() {
                        @Override
                        public void onResponse(Call<JobDetails> call, Response<JobDetails> response) {
                            if (response.isSuccessful()) {

                                setResult(RESULT_OK);  // Trả kết quả cho Activity gọi
                                finish();  // Kết thúc Activity
                            } else {
                                Toast.makeText(EditJobActivity.this, "Error Update JobDetails", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JobDetails> call, Throwable t) {
                            Toast.makeText(EditJobActivity.this, "Error Connection: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(EditJobActivity.this, "Error Update Job", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Job> call, Throwable t) {
                Toast.makeText(EditJobActivity.this, "Error Connection: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}