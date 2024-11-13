package com.example.topcvrecruiter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.API.ApiPostingService;
import com.example.topcvrecruiter.adapter.AllArticleAdapter;
import com.example.topcvrecruiter.model.Article;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllArticleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AllArticleAdapter articleAdapter;
    private List<Article> articleList; // Dữ liệu tải từ API
    private List<Article> displayList; // Dữ liệu sẽ hiển thị trong RecyclerView
    private int currentPage = 0;
    private int pageSize = 10; // Số bài viết mỗi trang
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_article);

        recyclerView = findViewById(R.id.rcvArticle);
        backButton = findViewById(R.id.article_all_back_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleList = new ArrayList<>();
        displayList = new ArrayList<>();
        articleAdapter = new AllArticleAdapter(this, displayList);

        recyclerView.setAdapter(articleAdapter);

        loadArticles();  // Gọi API để tải dữ liệu lần đầu tiên

        // Lắng nghe sự kiện cuộn để tải thêm dữ liệu khi người dùng kéo xuống
        recyclerView.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void loadMoreItem() {
                if (!isLoading && !isLastPage) {
                    isLoading = true;
                    articleAdapter.addFooterLoading();  // Hiển thị footer loading
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

    private void loadArticles() {
        ApiPostingService apiService = ApiPostingService.retrofit.create(ApiPostingService.class);
        Call<List<Article>> call = apiService.getArticles();  // Lấy toàn bộ dữ liệu từ API (không phân trang)

        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    articleList = response.body();  // Lưu lại dữ liệu từ API

                    // Lấy các bài viết để hiển thị cho trang đầu tiên
                    loadPage(currentPage);

                    // Kiểm tra xem có phải là trang cuối cùng không
                    if (articleList.size() <= pageSize) {
                        isLastPage = true;
                    }
                } else {
                    Toast.makeText(AllArticleActivity.this, "Failed to load articles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Toast.makeText(AllArticleActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
            articleAdapter.removeFooterLoading();

            // Kiểm tra nếu đã tới trang cuối cùng
            if (currentPage * pageSize >= articleList.size()) {
                isLastPage = true;
            }
        }, 2000);  // Thời gian giả lập việc tải thêm (2 giây)
    }

    private void loadPage(int page) {
        int start = page * pageSize;  // Tính vị trí bắt đầu của trang hiện tại
        int end = Math.min(start + pageSize, articleList.size());  // Tính vị trí kết thúc (không vượt quá kích thước danh sách)

        if (start < articleList.size()) {
            displayList.addAll(articleList.subList(start, end));  // Thêm dữ liệu vào danh sách hiển thị
            articleAdapter.notifyItemRangeInserted(start, end - start);  // Cập nhật phần tử mới thêm
        }

        // Kiểm tra nếu đã tải hết dữ liệu
        if (end >= articleList.size()) {
            isLastPage = true;
        }
    }
}
