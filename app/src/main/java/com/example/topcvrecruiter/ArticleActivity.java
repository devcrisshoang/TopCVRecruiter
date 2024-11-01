package com.example.topcvrecruiter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.topcvrecruiter.API.ApiPostingService;
import com.example.topcvrecruiter.model.Article;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ArticleActivity extends AppCompatActivity {
    private ImageButton back_button;
    private EditText editTextTitle;
    private EditText editTextContent;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_article);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(view -> finish());

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);

        addButton = findViewById(R.id.add_new_article_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String content = editTextContent.getText().toString();

                // Kiểm tra xem title và content có dữ liệu hay không
                if (!title.isEmpty() && !content.isEmpty()) {
                    postArticle(title, content);
                } else {
                    Toast.makeText(ArticleActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();

                }
                finish();
            }
        });

    }


    //Post
    private void postArticle(String title, String content) {


        // Lấy thời gian hiện tại
        LocalDateTime currentTime = LocalDateTime.now();

        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        // Chuyển đổi thời gian thành chuỗi theo định dạng đã chọn
        String formattedDateTime = currentTime.format(formatter);

        Article article = new Article(title, content, formattedDateTime,1); // Thay đổi giá trị mặc định của iD_Recruiter tại đây

        ApiPostingService.apiService.postArticle(article).enqueue(new Callback<Article>() {

            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                Toast.makeText(ArticleActivity.this, "Đăng bài viết thành công!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                Toast.makeText(ArticleActivity.this, "Đã xảy ra lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}