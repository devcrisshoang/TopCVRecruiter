package com.example.topcvrecruiter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.topcvrecruiter.API.ApiJobService;
import com.example.topcvrecruiter.Model.Job;
import com.example.topcvrecruiter.Model.JobDetails;
import com.github.dhaval2404.imagepicker.ImagePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditJobActivity extends AppCompatActivity {
    private EditText jobName, workLocation, experienceRequire, workMethod, genderRequire, workPosition, applyDate, jobDescription, skillRequire, benefit, workingTime, Salary, numberofpeople, companyName;
    private Button saveButton;
    private int jobId, jobDetailsId;
    private ImageView avatar, change_avatar;
    private ActivityResultLauncher<Intent> imagePickerLauncherAvatar;
    private Uri uri;
    private String currentImagePath;  // To store the current image path (original one)
    private ImageButton backButton;
    private int id_Recruiter = 3;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);

//        id_Recruiter = getIntent().getIntExtra("id_Recruiter", -1);

        // Bind views
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
        backButton = findViewById(R.id.job_edit_back_button);
        avatar = findViewById(R.id.avatar);
        change_avatar = findViewById(R.id.change_avatar);

        // Get data from Intent
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

        // Get image path from Intent
        currentImagePath = getIntent().getStringExtra("imagePath");
        if (currentImagePath != null && !currentImagePath.isEmpty()) {
            Glide.with(this)
                    .load(Uri.parse(currentImagePath))  // Use Glide to load the image from the URI
                    .into(avatar);
        }

        // Set save button listener
        saveButton.setOnClickListener(v -> showConfirmationDialog());

        ImageButton backButton = findViewById(R.id.job_edit_back_button);

        // Đặt sự kiện click cho nút back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện quay lại khi nhấn nút back
                onBackPressed();  // gọi phương thức quay lại
            }
        });
        // Set change avatar listener
        change_avatar.setOnClickListener(view -> {
            ImagePicker.with(this)
                    .crop()                // Crop image (optional)
                    .compress(1024)        // Compress image (optional)
                    .maxResultSize(1080, 1080)  // Limit the image size (optional)
                    .createIntent(intent -> {
                        imagePickerLauncherAvatar.launch(intent);  // Launch the image picker
                        return null;
                    });
        });

        // Initialize image picker result launcher
        imagePickerLauncherAvatar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        avatar.setImageURI(uri);  // Display the selected image in ImageView
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
                .setPositiveButton("Yes", (dialog, which) -> updateJob())
                .setNegativeButton("No", null) // If the user clicks "No", just close the dialog
                .show();
    }

    private void updateJob() {
        // Get values from EditTexts
        String name = jobName.getText().toString();
        int salary = Integer.parseInt(Salary.getText().toString());
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

        // If no image selected, use the current image path
        String image = (uri != null) ? uri.toString() : currentImagePath;


        
        // Create Job and JobDetails objects with updated data
        Job updatedJob = new Job(image, name, company, experience, location, salary, date, id_Recruiter); // Assuming recruiterId is 1
        JobDetails updatedJobDetails = new JobDetails(description, skill, benefitText, gender, time, method, position, numberPeople, jobId);

        // Call API to update Job
        ApiJobService.apiService.updateJob(jobId, updatedJob).enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditJobActivity.this, "Updated Job Success!", Toast.LENGTH_SHORT).show();

                    // Call API to update JobDetails
                    ApiJobService.apiService.putJobDetails(jobDetailsId, updatedJobDetails).enqueue(new Callback<JobDetails>() {
                        @Override
                        public void onResponse(Call<JobDetails> call, Response<JobDetails> response) {
                            if (response.isSuccessful()) {
                                setResult(RESULT_OK);  // Return result to calling activity
                                finish();  // Close the activity
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
