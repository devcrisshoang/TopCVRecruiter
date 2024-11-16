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

import com.example.topcvrecruiter.API.ApiPostingService;

import com.example.topcvrecruiter.Adapter.ArticleAdapter;
import com.example.topcvrecruiter.ArticleActivity;
import com.example.topcvrecruiter.JobActivity;
import com.example.topcvrecruiter.R;

import com.example.topcvrecruiter.model.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostingFragment extends Fragment {
    private Button post_button;
    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private List<Article> articleList;
    private Button articleButton;


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting, container, false);

        post_button = view.findViewById(R.id.post_button);
        recyclerView = view.findViewById(R.id.recycler_view_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        post_button.setOnClickListener(view1 -> {
            showPostTypeDialog();
        });



        articleButton = view.findViewById(R.id.article);
        articleButton.setOnClickListener(v -> {
            // Reload the Fragment
            getParentFragmentManager().beginTransaction().detach(this).attach(this).commit();
        });


        loadArticles(); // Gọi hàm để tải dữ liệu từ API
        return view;

    }


    private void loadArticles() {
        // Khởi tạo Retrofit service
        ApiPostingService apiService = ApiPostingService.retrofit.create(ApiPostingService.class);

        Call<List<Article>> call = apiService.getArticles(); // Gọi API để lấy danh sách bài viết
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful()) {
                    articleList = response.body();
                    if (articleList != null) {
                        if (adapter == null) {
                            adapter = new ArticleAdapter(articleList);
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.updateData(articleList);
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



    //
    private void showPostTypeDialog() {
        // Create AlertDialog to ask user if they want to post Article or Job
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Post Type");

        // Define options for the dialog (Article and Job)
        String[] options = {"Article", "Job"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // If user chooses Article, navigate to ArticleActivity
                    Intent intent = new Intent(getActivity(), ArticleActivity.class);
                    startActivity(intent);
                } else if (which == 1) {
                    // If user chooses Job, navigate to JobActivity
                    Intent intent = new Intent(getActivity(), JobActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Add "Cancel" button if the user doesn't want to choose
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();  // Close the dialog if the user cancels
            }
        });

        // Show the dialog
        builder.create().show();
    }
}



