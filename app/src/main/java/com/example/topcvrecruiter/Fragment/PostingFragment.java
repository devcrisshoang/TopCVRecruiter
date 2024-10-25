package com.example.topcvrecruiter.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.topcvrecruiter.API.ApiJobService;
import com.example.topcvrecruiter.API.ApiPostingService;
import com.example.topcvrecruiter.adapter.ArticleAdapter;
import com.example.topcvrecruiter.ArticleActivity;
import com.example.topcvrecruiter.JobActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.adapter.JobAdapter;
import com.example.topcvrecruiter.model.Article;
import com.example.topcvrecruiter.model.Job;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostingFragment extends Fragment {
    private Button post_button;
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private JobAdapter jobAdapter;
    private List<Article> articleList;
    private Button articleButton, jobButton;
    private List<Job> jobList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting, container, false);

        post_button = view.findViewById(R.id.post_button);
        recyclerView = view.findViewById(R.id.recycler_view_post);
        articleButton = view.findViewById(R.id.article);
        jobButton = view.findViewById(R.id.job);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        post_button.setOnClickListener(view1 -> {
            showPostTypeDialog();
        });

        articleButton.setOnClickListener(v -> {
            loadArticles();
        });

        jobButton.setOnClickListener(v -> {
            loadJobs();
        });

        loadArticles();
        return view;
    }

    private void loadArticles() {
        ApiPostingService apiService = ApiPostingService.retrofit.create(ApiPostingService.class);
        Call<List<Article>> call = apiService.getArticles();
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful()) {
                    articleList = response.body();
                    if (articleList != null) {

                        if (!(recyclerView.getAdapter() instanceof ArticleAdapter) || articleAdapter == null) {
                            articleAdapter = new ArticleAdapter(articleList);
                            recyclerView.setAdapter(articleAdapter);
                        } else {
                            articleAdapter.updateData(articleList);
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load articles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Toast.makeText(requireContext(), "Error loading articles: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadJobs() {
        ApiJobService apiService = ApiJobService.retrofit.create(ApiJobService.class);
        Call<List<Job>> call = apiService.getJobs();
        call.enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                if (response.isSuccessful()) {
                    jobList = response.body();
                    if (jobList != null) {

                        if (!(recyclerView.getAdapter() instanceof JobAdapter) || jobAdapter == null) {
                            jobAdapter = new JobAdapter(jobList);
                            recyclerView.setAdapter(jobAdapter);
                        } else {
                            jobAdapter.updateData(jobList);
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load jobs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                Toast.makeText(requireContext(), "Error loading jobs: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPostTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Post Type");
        String[] options = {"Article", "Job"};

        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                startActivity(new Intent(getActivity(), ArticleActivity.class));
            } else if (which == 1) {
                startActivity(new Intent(getActivity(), JobActivity.class));
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
