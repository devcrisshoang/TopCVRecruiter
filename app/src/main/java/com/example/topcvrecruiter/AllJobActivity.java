package com.example.topcvrecruiter;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcvrecruiter.API.ApiJobService;
import com.example.topcvrecruiter.Utils.PaginationScrollListener;
import com.example.topcvrecruiter.Adapter.AllJobAdapter;
import com.example.topcvrecruiter.Model.Job;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllJobActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private AllJobAdapter jobAdapter;

    private List<Job> jobList;
    private List<Job> displayList;

    private int currentPage = 0;
    private int pageSize = 10;
    private int id_Recruiter;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_job);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();

        loadJobs();

    }

    private void setClick(){

        recyclerView.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void loadMoreItem() {
                if (!isLoading && !isLastPage) {
                    isLoading = true;
                    jobAdapter.addFooterLoading();
                    loadNextPage();
                }
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });

        backButton.setOnClickListener(v -> finish());
    }

    private void setWidget(){
        id_Recruiter = getIntent().getIntExtra("id_Recruiter",0);
        recyclerView = findViewById(R.id.rcvJob);
        backButton = findViewById(R.id.all_job_back_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        displayList = new ArrayList<>();
        jobAdapter = new AllJobAdapter(this,displayList);
        recyclerView.setAdapter(jobAdapter);
    }

    private void loadJobs() {
        ApiJobService apiService = ApiJobService.retrofit.create(ApiJobService.class);
        Call<List<Job>> call = apiService.getJobsByRecruiter(id_Recruiter);

        call.enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(@NonNull Call<List<Job>> call, @NonNull Response<List<Job>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    jobList = response.body();

                    loadPage(currentPage);

                    if (jobList.size() <= pageSize) {
                        isLastPage = true;
                    }
                } else {
                    Toast.makeText(AllJobActivity.this, "Failed to load articles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                Toast.makeText(AllJobActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNextPage() {
        new Handler().postDelayed(() -> {
            currentPage++;
            loadPage(currentPage);

            isLoading = false;

            jobAdapter.removeFooterLoading();

            if (currentPage * pageSize >= jobList.size()) {
                isLastPage = true;
            }
        }, 2000);
    }

    private void loadPage(int page) {
        int start = page * pageSize;
        int end = Math.min(start + pageSize, jobList.size());

        if (start < jobList.size()) {
            displayList.addAll(jobList.subList(start, end));
            jobAdapter.notifyItemRangeInserted(start, end - start);
        }

        if (end >= jobList.size()) {
            isLastPage = true;
        }
    }
}
