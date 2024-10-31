package com.example.topcvrecruiter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.topcvrecruiter.API.ApiPostingService;
import com.example.topcvrecruiter.model.Article;
import com.github.dhaval2404.imagepicker.ImagePicker;

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
    private ImageView avatar;
    private ImageView change_avatar;
    private ActivityResultLauncher<Intent> imagePickerLauncherAvatar;
    private Uri uri;

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
        avatar = findViewById(R.id.avatar);
        change_avatar = findViewById(R.id.change_avatar);

        addButton = findViewById(R.id.add_new_article_button);

        change_avatar.setOnClickListener(view13 -> {
            ImagePicker.with(this)
                    .crop()                // Cắt ảnh (tùy chọn)
                    .compress(1024)        // Nén ảnh (tùy chọn)
                    .maxResultSize(1080, 1080)  // Giới hạn kích thước ảnh (tùy chọn)
                    .createIntent(intent -> {
                        imagePickerLauncherAvatar.launch(intent);  // Sử dụng launcher thay vì onActivityResult
                        return null;
                    });
        });

        imagePickerLauncherAvatar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        avatar.setImageURI(uri);
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        addButton.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String content = editTextContent.getText().toString();
            String image = uri.toString();

            // Kiểm tra xem title và content có dữ liệu hay không
            if (!title.isEmpty() && !content.isEmpty()) {
                postArticle(title, content, image);
            } else {
                Toast.makeText(ArticleActivity.this, "Insert Information", Toast.LENGTH_SHORT).show();

            }
            finish();
        });

    }

    //Post
    private void postArticle(String title, String content,String image) {


        // Lấy thời gian hiện tại
        LocalDateTime currentTime = LocalDateTime.now();

        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        // Chuyển đổi thời gian thành chuỗi theo định dạng đã chọn
        String formattedDateTime = currentTime.format(formatter);

        Article article = new Article(title, content, formattedDateTime,image,1); // Thay đổi giá trị mặc định của iD_Recruiter tại đây
        ApiPostingService.apiService.postArticle(article).enqueue(new Callback<Article>() {

            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                Toast.makeText(ArticleActivity.this, "Post Article Successfully!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                Toast.makeText(ArticleActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}