package com.example.topcvrecruiter;
import com.bumptech.glide.Glide;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.topcvrecruiter.API.ApiJobService;
import com.example.topcvrecruiter.model.Job;
import com.example.topcvrecruiter.model.JobDetails;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.content.DialogInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobDetailActivity extends AppCompatActivity {
    private ImageButton back_button, edit_button;
    private int jobId,jobDetailsId;
    private ImageView companyLogo;
    private TextView jobName;
    private TextView workLocation;
    private TextView experienceRequire;
    private TextView experience;
    private TextView workMethod;
    private TextView genderRequire;
    private TextView workPosition;
    private TextView applyDate;
    private TextView jobDescription;
    private TextView skillRequire;
    private TextView benefit;
    private TextView workingTime;
    private TextView salary;
    private TextView numberOfPeople;
    private TextView companyName;

    private Button delete;
    private static final int REQUEST_CODE_EDIT_JOB = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_detail);

        back_button = findViewById(R.id.information_back_button);
        edit_button = findViewById(R.id.edit_button);
        companyLogo = findViewById(R.id.company_logo);
        jobName = findViewById(R.id.job_name);
        companyName = findViewById(R.id.company_name);
        salary = findViewById(R.id.salary);
        workLocation = findViewById(R.id.location);
        experienceRequire = findViewById(R.id.experience);
        experience = findViewById(R.id.experience_detail);
        workMethod = findViewById(R.id.work_method);
        genderRequire = findViewById(R.id.gender);
        workPosition = findViewById(R.id.position);
        applyDate = findViewById(R.id.deadline);
        jobDescription = findViewById(R.id.description);
        skillRequire = findViewById(R.id.conditions);
        benefit = findViewById(R.id.interesting);
        workingTime = findViewById(R.id.working_time);
        numberOfPeople = findViewById(R.id.number_of_people);
        delete = findViewById(R.id.delete_button);

        // Thiết lập sự kiện nút back
        back_button.setOnClickListener(v -> finish());

        // Lấy jobId từ Intent
        jobId = getIntent().getIntExtra("jobId", -1);
        if (jobId != -1) {
            getJobData(jobId);
            getJobDetails(jobId);
        } else {
            Toast.makeText(this, "Job ID error!", Toast.LENGTH_SHORT).show();
        }

        // Thiết lập sự kiện cho nút delete
        delete.setOnClickListener(v -> showDeleteConfirmationDialog()); // Hiển thị hộp thoại xác nhận

        // Thiết lập sự kiện cho nút edit
        edit_button.setOnClickListener(v -> openEditJobActivity());

        // Thiết lập padding cho View chính
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_JOB) { // Đảm bảo sử dụng đúng requestCode
            if (resultCode == RESULT_OK) {
                // Gọi lại dữ liệu job để refresh
                getJobData(jobId);
                getJobDetails(jobId);
            }
        }
    }
    // Phương thức mở EditJobActivity
    private void openEditJobActivity() {
        Intent intent = new Intent(JobDetailActivity.this, EditJobActivity.class);
        intent.putExtra("jobId", jobId);
        intent.putExtra("jobDetailsId", jobDetailsId); // Truyền jobDetailsId
        // Truyền thêm các thông tin cần thiết từ đối tượng Job và JobDetails
        intent.putExtra("companyLogo", companyLogo.toString());
        intent.putExtra("jobName", jobName.getText().toString());
        intent.putExtra("companyName", companyName.getText().toString());
        intent.putExtra("salary", salary.getText().toString());
        intent.putExtra("workLocation", workLocation.getText().toString());
        intent.putExtra("experienceRequire", experienceRequire.getText().toString());
        intent.putExtra("experience", experience.getText().toString());
        intent.putExtra("workMethod", workMethod.getText().toString());
        intent.putExtra("genderRequire", genderRequire.getText().toString());
        intent.putExtra("workPosition", workPosition.getText().toString());
        intent.putExtra("applyDate", applyDate.getText().toString());
        intent.putExtra("jobDescription", jobDescription.getText().toString());
        intent.putExtra("skillRequire", skillRequire.getText().toString());
        intent.putExtra("benefit", benefit.getText().toString());
        intent.putExtra("workingTime", workingTime.getText().toString());
        intent.putExtra("numberOfPeople", numberOfPeople.getText().toString());
        // Truyền thêm đường dẫn ảnh
        String imagePath = companyLogo.getTag() != null ? companyLogo.getTag().toString() : ""; // Lấy đường dẫn ảnh từ tag của ImageView
        intent.putExtra("imagePath", imagePath);
        startActivityForResult(intent, REQUEST_CODE_EDIT_JOB); // Sử dụng startActivityForResult
    }


    // Phương thức gọi API để lấy thông tin công việc
    private void getJobData(int jobId) {
        ApiJobService apiService = ApiJobService.apiService;

        Call<Job> call = apiService.getJobById(jobId);
        call.enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Job job = response.body();
                    displayJobData(job);
                } else {
                    Toast.makeText(JobDetailActivity.this, "Không thể lấy thông tin công việc!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Job> call, Throwable t) {
                Toast.makeText(JobDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    // Phương thức gọi API để lấy chi tiết công việc
    private void getJobDetails(int jobId) {
        ApiJobService apiService = ApiJobService.apiService;

        Call<List<JobDetails>> call = apiService.getJobDetailsByJobId(jobId);
        call.enqueue(new Callback<List<JobDetails>>() {
            @Override
            public void onResponse(Call<List<JobDetails>> call, Response<List<JobDetails>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<JobDetails> jobDetailsList = response.body();
                    if (!jobDetailsList.isEmpty()) {
                        displayJobDetails(jobDetailsList.get(0)); // Hiển thị đối tượng đầu tiên trong danh sách
                    }
                } else {
                    Toast.makeText(JobDetailActivity.this, "Không thể lấy chi tiết công việc!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<JobDetails>> call, Throwable t) {
                Toast.makeText(JobDetailActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    // Hiển thị hộp thoại xác nhận trước khi xóa
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this job?")
                .setPositiveButton("Yes", (dialog, which) -> deleteJob(jobId)) // Nếu đồng ý, gọi phương thức xóa
                .setNegativeButton("No", null) // Nếu không đồng ý, chỉ cần đóng hộp thoại
                .show();
    }

    // Phương thức gọi API để xóa job
    private void deleteJob(int jobId) {
        ApiJobService apiService = ApiJobService.apiService;

        Call<Void> call = apiService.deleteJobById(jobId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(JobDetailActivity.this, "Job deleted successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại Activity trước đó
                } else {
                    Toast.makeText(JobDetailActivity.this, "Cannot delete the job!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(JobDetailActivity.this, "Connection error!", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    // Hiển thị dữ liệu của đối tượng Job
    private void displayJobData(Job job) {
        jobName.setText(job.getJob_Name());
        companyName.setText(job.getCompany_Name());
        salary.setText(job.getSalary());
        workLocation.setText(job.getWorking_Address());
        experienceRequire.setText(job.getWorking_Experience_Require());
        experience.setText(job.getWorking_Experience_Require());
        applyDate.setText(job.getApplication_Date());

        // Tải hình ảnh vào ImageView bằng Glide
        String imagePath = job.getImage_Id(); // Lấy đường dẫn ảnh từ API
        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(JobDetailActivity.this)
                    .load(Uri.parse(imagePath))  // Tải ảnh từ đường dẫn file URI
                    .into(companyLogo);
            companyLogo.setTag(imagePath);
        }else {
            Glide.with(JobDetailActivity.this)
                    .load(R.drawable.google_ic)  // Thay "account_ic" bằng ID ảnh mặc định của bạn
                    .into(companyLogo);
        }
    }

    // Hiển thị dữ liệu của đối tượng JobDetails
    private void displayJobDetails(JobDetails jobDetails) {
        jobDetailsId = jobDetails.getId(); // Lưu jobDetailsId
        jobDescription.setText(jobDetails.getJob_Description());
        skillRequire.setText(jobDetails.getSkill_Require());
        benefit.setText(jobDetails.getBenefit());
        genderRequire.setText(jobDetails.getGender_Require());
        workingTime.setText(jobDetails.getWorking_Time());
        workMethod.setText(jobDetails.getWorking_Method());
        workPosition.setText(jobDetails.getWorking_Position());
        numberOfPeople.setText(jobDetails.getNumber_Of_People());
    }
}
