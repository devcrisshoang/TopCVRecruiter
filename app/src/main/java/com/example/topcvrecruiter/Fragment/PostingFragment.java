package com.example.topcvrecruiter.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.topcvrecruiter.API.ApiJobService;
import com.example.topcvrecruiter.API.ApiPostingService;
import com.example.topcvrecruiter.API.ApiRecruiterService;
import com.example.topcvrecruiter.AllArticleActivity;
import com.example.topcvrecruiter.AllJobActivity;
import com.example.topcvrecruiter.ArticleActivity;
import com.example.topcvrecruiter.JobActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Adapter.ArticleAdapter;
import com.example.topcvrecruiter.Adapter.JobAdapter;
import com.example.topcvrecruiter.Model.Article;
import com.example.topcvrecruiter.Model.Job;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostingFragment extends Fragment {
    private int id_Recruiter ;
    private Button post_button;
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private JobAdapter jobAdapter;
    private List<Article> articleList;
    private Button articleButton, jobButton;
    private List<Job> jobList;
    private Button viewAll;
    private boolean isArticleTabSelected = true;  // Biến để kiểm tra tab hiện tại

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting, container, false);

       if (getArguments() != null) {
          id_Recruiter = getArguments().getInt("id_Recruiter", 0);
      }
        if (id_Recruiter == 0) {
            Log.e("ArticleActivity", "Recruiter ID not received or is invalid!");
        } else {
            Log.d("ArticleActivity", "Recruiter ID: " + id_Recruiter);
        }


        post_button = view.findViewById(R.id.post_button);
        recyclerView = view.findViewById(R.id.recycler_view_post);
        articleButton = view.findViewById(R.id.article);
        jobButton = view.findViewById(R.id.job);
        viewAll = view.findViewById(R.id.view_all_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Đặt LayoutManager cho RecyclerView

        // Khi chọn Tab "Article"
        articleButton.setOnClickListener(v -> {
            isArticleTabSelected = true;
            loadArticles();  // Tải bài viết
        });

        // Khi chọn Tab "Job"
        jobButton.setOnClickListener(v -> {
            isArticleTabSelected = false;
            loadJobs();  // Tải công việc
        });

        // Khi bấm "View All"
        viewAll.setOnClickListener(v -> {
            if (isArticleTabSelected) {
                Intent intent = new Intent(getActivity(), AllArticleActivity.class);
                intent.putExtra("id_Recruiter",id_Recruiter);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), AllJobActivity.class);
                intent.putExtra("id_Recruiter",id_Recruiter);
                startActivity(intent);
            }
        });
        post_button.setOnClickListener(view1 -> {
            if (getContext() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose Post Type");
                String[] options = {"Article", "Job"};

                builder.setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        Intent intent = new Intent(getContext(), ArticleActivity.class);
                        intent.putExtra("id_Recruiter",id_Recruiter);
                        startActivity(intent);
                    } else if (which == 1) {
                        Intent intent = new Intent(getContext(), JobActivity.class);
                        intent.putExtra("id_Recruiter",id_Recruiter);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builder.create().show();
            }
        });

        loadArticles();  // Mặc định sẽ tải bài viết khi mở fragment lần đầu
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Kiểm tra và xóa các bài tuyển dụng đã hết hạn khi Fragment bắt đầu
        checkAndDeleteExpiredJobs();
    }

    // Tải 10 bài viết đầu tiên
    private void loadArticles() {
        if (getContext() != null) { // Kiểm tra context
            articleButton.setTextColor(getResources().getColor(R.color.green_color));
            jobButton.setTextColor(getResources().getColor(R.color.black));

            ApiPostingService apiService = ApiPostingService.retrofit.create(ApiPostingService.class);



            // Gọi API để lấy danh sách bài viết theo recruiterId
            Call<List<Article>> call = apiService.getArticlesByRecruiter(id_Recruiter);
            call.enqueue(new Callback<List<Article>>() {
                @Override
                public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                    if (response.isSuccessful()) {
                        articleList = response.body();
                        if (articleList != null) {
                            // Lấy 10 bài viết đầu tiên (nếu có nhiều hơn 10)
                            List<Article> limitedArticles = articleList.size() > 10 ? articleList.subList(0, 10) : articleList;

                            if (!(recyclerView.getAdapter() instanceof ArticleAdapter) || articleAdapter == null) {
                                articleAdapter = new ArticleAdapter(getContext(), limitedArticles);
                                recyclerView.setAdapter(articleAdapter);
                            } else {
                                articleAdapter.updateData(limitedArticles);
                            }
                        }
                    } else {
                        showToast("Failed to load articles");
                    }
                }

                @Override
                public void onFailure(Call<List<Article>> call, Throwable t) {
                    showToast("Error loading articles: " + t.getMessage());
                }
            });
        }
    }


    // Tải 10 công việc đầu tiên
    private void loadJobs() {
        if (getContext() != null) { // Kiểm tra context
            jobButton.setTextColor(getResources().getColor(R.color.green_color));
            articleButton.setTextColor(getResources().getColor(R.color.black));



            ApiJobService apiService = ApiJobService.retrofit.create(ApiJobService.class);
            Call<List<Job>> call = apiService.getJobsByRecruiter(id_Recruiter);
            call.enqueue(new Callback<List<Job>>() {
                @Override
                public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                    if (response.isSuccessful()) {
                        jobList = response.body();
                        if (jobList != null) {
                            // Lấy 10 công việc đầu tiên
                            List<Job> limitedJobs = jobList.size() > 10 ? jobList.subList(0, 10) : jobList;

                            if (!(recyclerView.getAdapter() instanceof JobAdapter) || jobAdapter == null) {
                                jobAdapter = new JobAdapter(getContext(), limitedJobs);
                                recyclerView.setAdapter(jobAdapter);
                            } else {
                                jobAdapter.updateData(limitedJobs);
                            }
                        }
                    } else {
                        showToast("Failed to load jobs");
                    }
                }

                @Override
                public void onFailure(Call<List<Job>> call, Throwable t) {
                    showToast("Error loading jobs: " + t.getMessage());
                }
            });
        }
    }

    // Kiểm tra và xóa các bài tuyển dụng đã hết hạn
    private void checkAndDeleteExpiredJobs() {
        ApiJobService.apiService.getJobs().enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Job job : response.body()) {
                        // Lấy thời gian tạo của bài đăng và tính toán xem đã quá 30 ngày chưa
                        String createTimeString = job.getCreate_Time(); // Định dạng: yyyy-MM-dd'T'HH:mm:ss.SSS
                        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                        try {
                            LocalDateTime createTime = LocalDateTime.parse(createTimeString, formatter);

                            // Tính toán thời gian đã trôi qua từ lúc tạo
                            Duration duration = Duration.between(createTime, LocalDateTime.now());
                            long daysPassed = duration.toDays();

                            // Nếu bài đăng đã được 30 ngày, xóa bài đăng
                            if (daysPassed >= 30) {
                                deleteJob(job.getId());
                            }
                        } catch (Exception e) {
                            e.printStackTrace(); // In lỗi nếu có vấn đề khi phân tích chuỗi thời gian
                        }
                    }
                } else {
                    showToast("Error fetching job list");
                }
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                showToast("Error checking for expired jobs: " + t.getMessage());
            }
        });
    }


    // Hàm gọi API để xóa bài tuyển dụng
    private void deleteJob(int jobId) {
        ApiJobService.apiService.deleteJobById(jobId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showToast("Job " + jobId + " deleted successfully");
                } else {
                    showToast("Failed to delete job " + jobId);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showToast("Error deleting job: " + t.getMessage());
            }
        });
    }

    // Hiển thị toast
    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
