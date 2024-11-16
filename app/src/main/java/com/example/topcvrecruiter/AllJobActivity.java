package com.example.topcvrecruiter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.API.ApiJobService;
import com.example.topcvrecruiter.API.ApiPostingService;
import com.example.topcvrecruiter.adapter.AllArticleAdapter;
import com.example.topcvrecruiter.adapter.AllJobAdapter;
import com.example.topcvrecruiter.model.Article;
import com.example.topcvrecruiter.model.Job;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllJobActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AllJobAdapter jobAdapter;
    private List<Job> jobList; // Dữ liệu tải từ API
    private List<Job> displayList; // Dữ liệu sẽ hiển thị trong RecyclerView
    private int currentPage = 0;
    private int pageSize = 10; // Số bài viết mỗi trang
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_job);

        recyclerView = findViewById(R.id.rcvJob);
        backButton = findViewById(R.id.all_job_back_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobList = new ArrayList<>();
        displayList = new ArrayList<>();
        jobAdapter = new AllJobAdapter(this,displayList);
        recyclerView.setAdapter(jobAdapter);

        loadJobs();  // Gọi API để tải dữ liệu lần đầu tiên

        // Lắng nghe sự kiện cuộn để tải thêm dữ liệu khi người dùng kéo xuống
        recyclerView.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void loadMoreItem() {
                if (!isLoading && !isLastPage) {
                    isLoading = true;
                    jobAdapter.addFooterLoading();  // Hiển thị footer loading
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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức quay lại
                onBackPressed();
            }
        });
    }

    private void loadJobs() {
        ApiJobService apiService = ApiJobService.retrofit.create(ApiJobService.class);
        Call<List<Job>> call = apiService.getJobs();  // Lấy toàn bộ dữ liệu từ API (không phân trang)

        call.enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    jobList = response.body();  // Lưu lại dữ liệu từ API

                    // Lấy các bài viết để hiển thị cho trang đầu tiên
                    loadPage(currentPage);

                    // Kiểm tra xem có phải là trang cuối cùng không
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
        // Giả lập việc tải dữ liệu từ API với một delay
        new Handler().postDelayed(() -> {
            // Tăng trang sau khi tải
            currentPage++;
            loadPage(currentPage);

            // Đánh dấu không còn loading nữa
            isLoading = false;

            // Loại bỏ footer loading sau khi tải xong
            jobAdapter.removeFooterLoading();

            // Kiểm tra nếu đã tới trang cuối cùng
            if (currentPage * pageSize >= jobList.size()) {
                isLastPage = true;
            }
        }, 2000);  // Thời gian giả lập việc tải thêm (2 giây)
    }

    private void loadPage(int page) {
        int start = page * pageSize;  // Tính vị trí bắt đầu của trang hiện tại
        int end = Math.min(start + pageSize, jobList.size());  // Tính vị trí kết thúc (không vượt quá kích thước danh sách)

        if (start < jobList.size()) {
            displayList.addAll(jobList.subList(start, end));  // Thêm dữ liệu vào danh sách hiển thị
            jobAdapter.notifyItemRangeInserted(start, end - start);  // Cập nhật phần tử mới thêm
        }

        // Kiểm tra nếu đã tải hết dữ liệu
        if (end >= jobList.size()) {
            isLastPage = true;
        }
    }
}
