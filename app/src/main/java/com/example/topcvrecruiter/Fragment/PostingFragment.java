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
import com.example.topcvrecruiter.AllArticleActivity;
import com.example.topcvrecruiter.AllJobActivity;
import com.example.topcvrecruiter.PostingArticleActivity;
import com.example.topcvrecruiter.PostingJobActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Adapter.ArticleAdapter;
import com.example.topcvrecruiter.Adapter.JobAdapter;
import com.example.topcvrecruiter.Model.Article;
import com.example.topcvrecruiter.Model.Job;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostingFragment extends Fragment {

    private int id_Recruiter;
    private int user_id;

    private Button post_button;
    private Button articleButton;
    private Button jobButton;
    private Button viewAll;

    private RecyclerView recyclerView;

    private ArticleAdapter articleAdapter;
    private JobAdapter jobAdapter;

    private List<Article> articleList;
    private List<Job> jobList;

    private boolean isArticleTabSelected = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting, container, false);
        setWidget(view);

        setClick();

        loadArticles();

        return view;
    }

    private void setClick(){

        articleButton.setOnClickListener(v -> articleButtonClick());

        jobButton.setOnClickListener(v -> jobButtonClick());

        viewAll.setOnClickListener(v -> viewAllButtonClick());

        post_button.setOnClickListener(view1 -> postButtonClick());
    }

    private void setWidget(View view){
        post_button = view.findViewById(R.id.post_button);
        recyclerView = view.findViewById(R.id.recycler_view_post);
        articleButton = view.findViewById(R.id.article);
        jobButton = view.findViewById(R.id.job);
        viewAll = view.findViewById(R.id.view_all_button);

        if (getArguments() != null) {
            id_Recruiter = getArguments().getInt("id_Recruiter", 0);
            user_id = getArguments().getInt("user_id", 0);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Đặt LayoutManager cho RecyclerView
    }

    private void articleButtonClick(){
        isArticleTabSelected = true;
        loadArticles();
    }

    private void jobButtonClick(){
        isArticleTabSelected = false;
        loadJobs();
    }

    private void viewAllButtonClick(){
        Intent intent;
        if (isArticleTabSelected) {
            intent = new Intent(getActivity(), AllArticleActivity.class);
        } else {
            intent = new Intent(getActivity(), AllJobActivity.class);
        }
        intent.putExtra("id_Recruiter", id_Recruiter);
        startActivity(intent);
    }

    private void postButtonClick(){
        if (getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Choose Post Type");
            String[] options = {"Article", "Job"};

            builder.setItems(options, (dialog, which) -> {
                if (which == 0) {
                    Intent intent = new Intent(getContext(), PostingArticleActivity.class);
                    intent.putExtra("id_Recruiter", id_Recruiter);
                    intent.putExtra("user_id",user_id);
                    startActivity(intent);
                } else if (which == 1) {
                    Intent intent = new Intent(getContext(), PostingJobActivity.class);
                    intent.putExtra("id_Recruiter", id_Recruiter);
                    intent.putExtra("user_id",user_id);
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkAndDeleteExpiredJobs();
    }

    private void loadArticles() {
        if (getContext() != null) {
            articleButton.setTextColor(getResources().getColor(R.color.green_color));
            jobButton.setTextColor(getResources().getColor(R.color.black));

            ApiPostingService apiService = ApiPostingService.retrofit.create(ApiPostingService.class);

            Call<List<Article>> call = apiService.getArticlesByRecruiter(id_Recruiter);
            call.enqueue(new Callback<List<Article>>() {
                @Override
                public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                    if (response.isSuccessful()) {
                        articleList = response.body();
                        if (articleList != null) {
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

    private void loadJobs() {
        if (getContext() != null) {
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

    private void checkAndDeleteExpiredJobs() {
        ApiJobService.apiService.getJobs().enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Job job : response.body()) {
                        String createTimeString = job.getCreate_Time();
                        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                        try {
                            LocalDateTime createTime = LocalDateTime.parse(createTimeString, formatter);

                            Duration duration = Duration.between(createTime, LocalDateTime.now());
                            long daysPassed = duration.toDays();

                            if (daysPassed >= 30) {
                                deleteJob(job.getId());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

}
