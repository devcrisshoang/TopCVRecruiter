package com.example.topcvrecruiter;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcvrecruiter.API.ApiPostingService;
import com.example.topcvrecruiter.Adapter.AllArticleAdapter;
import com.example.topcvrecruiter.Utils.PaginationScrollListener;
import com.example.topcvrecruiter.Model.Article;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllArticleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private AllArticleAdapter articleAdapter;

    private List<Article> articleList;
    private List<Article> displayList;

    private int id_Recruiter;
    private int currentPage = 0;
    private final int pageSize = 10;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    private ImageButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_article);
        setWidget();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleList = new ArrayList<>();
        displayList = new ArrayList<>();
        articleAdapter = new AllArticleAdapter(this, displayList);

        recyclerView.setAdapter(articleAdapter);

        loadArticles();

        recyclerView.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void loadMoreItem() {
                if (!isLoading && !isLastPage) {
                    isLoading = true;
                    articleAdapter.addFooterLoading();
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

    private void setWidget() {
        id_Recruiter = getIntent().getIntExtra("id_Recruiter", 0);
        recyclerView = findViewById(R.id.rcvArticle);
        backButton = findViewById(R.id.article_all_back_button);
    }

    private void loadArticles() {

        ApiPostingService apiService = ApiPostingService.retrofit.create(ApiPostingService.class);

        Call<List<Article>> call = apiService.getArticlesByRecruiter(id_Recruiter);
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    articleList = response.body();

                    loadPage(currentPage);

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
