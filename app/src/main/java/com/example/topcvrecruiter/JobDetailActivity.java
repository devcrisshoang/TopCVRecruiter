package com.example.topcvrecruiter;

import android.content.Intent;
import android.graphics.Rect;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.topcv.adapter.ListInformationlAdapter;
import com.example.topcv.api.ApiJobDetailService;
import com.example.topcv.api.ApiJobService;
import com.example.topcv.model.Job;
import com.example.topcv.model.JobDetail;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class JobDetailActivity extends AppCompatActivity {
    private ListInformationlAdapter listInformationlAdapter;
    private ImageButton informationBackButton;
    private ScrollView scrollView;
    private boolean isImageVisible = true;
    private LinearLayout header_title;
    private ImageView company_logo;
    private ImageButton back_button;
    private int jobId;
    private int bestId;

    private Button apply_button;
    private TextView working_time;
    private TextView working_place;
    private TextView interesting;
    private TextView conditions;
    private TextView description;
    private TextView deadline;
    private TextView position;
    private TextView gender;
    private TextView number_of_people;
    private TextView work_method;
    private TextView experience_detail;
    private TextView salary;
    private TextView location;
    private TextView experience;
    private TextView company_name;
    private TextView job_name;
    private TextView work_name;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_informations);
        // Thiết lập padding cho View chính
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setWidget();

        // Lấy jobId từ Intent
        jobId = getIntent().getIntExtra("job_id", -1);
        bestId = getIntent().getIntExtra("best_id", -1);

        if (jobId != -1){
            // Gọi API để lấy job theo ID
            getJobs(jobId);

            // Gọi API để lấy chi tiết công việc
            getJobDetails(jobId);

        }
        else if(bestId != -1){
            // Gọi API để lấy job theo ID
            getJobs(bestId);

            // Gọi API để lấy chi tiết công việc
            getJobDetails(bestId);
        }

        // Thêm sự kiện onClick cho nút apply_button để chuyển sang màn hình SelectCvToApplyJobActivity
        apply_button.setOnClickListener(view -> {
            startActivity(new Intent(this, SelectCvToApplyJobActivity.class));
        });

        // Sự kiện nhấn nút quay lại
        informationBackButton.setOnClickListener(v -> finish());
        back_button.setOnClickListener(v -> finish());

        // Ẩn tiêu đề ban đầu
        header_title.setVisibility(View.GONE);
        isImageVisible = true;

        // Thêm OnScrollChangedListener vào ScrollView
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            // Kiểm tra xem company_logo có nằm trong vùng nhìn thấy không
            if (isViewVisible(company_logo)) {
                if (!isImageVisible) {
                    Log.d("ButtonVisibility", "Button is visible again!");
                    // Nút đã xuất hiện trở lại
                    header_title.setVisibility(View.GONE);
                    isImageVisible = true;
                }
            } else {
                if (isImageVisible) {
                    Log.d("ButtonVisibility", "Button is out of screen!");
                    // Nút đã bị kéo ra khỏi màn hình
                    header_title.setVisibility(View.VISIBLE);
                    isImageVisible = false;
                }
            }
        });
    }

    private void setWidget() {
        informationBackButton = findViewById(R.id.information_back_button);
        back_button = findViewById(R.id.back_button);
        scrollView = findViewById(R.id.scrollView);
        company_logo = findViewById(R.id.company_logo);
        header_title = findViewById(R.id.header_title);
        apply_button = findViewById(R.id.apply_button);
        working_time = findViewById(R.id.working_time);
        working_place = findViewById(R.id.working_place);
        interesting = findViewById(R.id.interesting);
        conditions = findViewById(R.id.conditions);
        description = findViewById(R.id.description);
        deadline = findViewById(R.id.deadline);
        position = findViewById(R.id.position);
        gender = findViewById(R.id.gender);
        number_of_people = findViewById(R.id.number_of_people);
        work_method = findViewById(R.id.work_method);
        experience_detail = findViewById(R.id.experience_detail);
        salary = findViewById(R.id.salary);
        location = findViewById(R.id.location);
        experience = findViewById(R.id.experience);
        company_name = findViewById(R.id.company_name);
        job_name = findViewById(R.id.job_name);
        work_name = findViewById(R.id.work_name);
    }

    private void getJobs(int jobId) {
        compositeDisposable.add(
                ApiJobService.ApiJobService.getJobById(jobId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Job>() {
                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Job job) {
                                bindJobDataToViews(job);
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Toast.makeText(CompanyInformationsActivity.this, "Failed to load job details", Toast.LENGTH_SHORT).show();
                                Log.e("API Error", e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                // Có thể thực hiện thêm các tác vụ khi hoàn thành
                            }
                        })
        );
    }

    private void getJobDetails(int jobId) {
        compositeDisposable.add(
                ApiJobDetailService.ApiJobDetailService.getJobDetailById(jobId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<JobDetail>>() {
                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<JobDetail> jobDetails) {
                                // Giả sử chỉ có một JobDetail, lấy phần tử đầu tiên
                                if (!jobDetails.isEmpty()) {
                                    bindJobDetailDataToViews(jobDetails.get(0));
                                } else {
                                    Toast.makeText(CompanyInformationsActivity.this, "No job details found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Toast.makeText(CompanyInformationsActivity.this, "Failed to load job details", Toast.LENGTH_SHORT).show();
                                Log.e("API Error", e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                // Có thể thực hiện thêm các tác vụ khi hoàn thành
                            }
                        })
        );
    }


    private void bindJobDetailDataToViews(JobDetail jobDetail) {
        // Gán các trường dữ liệu vào TextView
        working_time.setText(jobDetail.getWorkingTime());
        interesting.setText(jobDetail.getBenefit());
        conditions.setText(jobDetail.getSkillRequire());
        description.setText(jobDetail.getJobDescription());
        position.setText(jobDetail.getWorkingPosition());
        gender.setText(jobDetail.getGenderRequire());
        number_of_people.setText(jobDetail.getNumberOfPeople()); // Nếu số người là số nguyên
        work_method.setText(jobDetail.getWorkingMethod());
    }

    private void bindJobDataToViews(Job job) {
        // Lấy chuỗi hình ảnh từ API
        String imageId = job.getImageId(); // giả sử đây là chuỗi "R.drawable.viettel_ic"
        if (imageId != null && !imageId.isEmpty()) {
            // Tách tên tài nguyên
            String resourceName = imageId.split("\\.")[2]; // lấy phần sau dấu '.' thứ hai
            int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

            // Kiểm tra nếu resourceId hợp lệ
            if (resourceId != 0) {
                company_logo.setImageResource(resourceId);
            } else {
                // Gán hình ảnh mặc định nếu không tìm thấy
                company_logo.setImageResource(R.drawable.viettel_ic);
            }
        } else {
            // Gán hình ảnh mặc định nếu imageId là null hoặc rỗng
            company_logo.setImageResource(R.drawable.facebook_ic);
        }

        // Gán các trường dữ liệu vào TextView
        job_name.setText(job.getJobName());
        company_name.setText(job.getCompanyName());
        experience.setText(job.getExperience());
        working_place.setText(job.getLocation());
        salary.setText(job.getSalary());
        location.setText(job.getLocation());
        experience_detail.setText(job.getExperience());
        // Chuyển đổi chuỗi ngày
        String applicationDateStr = job.getApplicationDate(); // Giả sử đây là chuỗi ngày
        LocalDate applicationDate = LocalDate.parse(applicationDateStr.substring(0, 10)); // Lấy phần ngày từ chuỗi
        LocalDate deadlineDate = applicationDate.plusDays(30); // Cộng thêm 30 ngày

        // Định dạng để hiển thị
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        deadline.setText(formatter.format(deadlineDate)); // Hiển thị ngày hạn nộp

        // Tính số ngày còn lại
        LocalDate today = LocalDate.now();
        Period period = Period.between(today, deadlineDate);
        String remainingDays = String.valueOf(period.getDays());
        // Hiển thị số ngày còn lại
        deadline.setText(remainingDays + " days remaining");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear(); // Giải phóng tài nguyên
    }

    // Phương thức kiểm tra xem view có nằm trong vùng nhìn thấy không
    private boolean isViewVisible(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect.height() > 0;
    }
}
