package com.example.topcvrecruiter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.topcvrecruiter.API.ApiPostingService;
import com.example.topcvrecruiter.model.Article;
import com.github.dhaval2404.imagepicker.ImagePicker;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditArticleActivity extends AppCompatActivity {
    private EditText articleNameEdit;
    private EditText contentEdit;
    private Button saveButton;
    private int articleId;
    private ImageView avatar;
    private ImageView change_avatar;
    private ActivityResultLauncher<Intent> imagePickerLauncherAvatar;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        // Lấy các phần tử từ layout
        articleNameEdit = findViewById(R.id.article_name_edit);
        contentEdit = findViewById(R.id.content_edit);
        saveButton = findViewById(R.id.save_button);
        avatar = findViewById(R.id.avatar);
        change_avatar = findViewById(R.id.change_avatar);

        // Nhận articleId và dữ liệu từ Intent
        articleId = getIntent().getIntExtra("article_id", -1);
        String articleName = getIntent().getStringExtra("article_name");
        String content = getIntent().getStringExtra("content");
        String imagePath = getIntent().getStringExtra("image_path");

        if (articleId == -1) {
            Toast.makeText(this, "Invalid article ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        articleNameEdit.setText(articleName);
        contentEdit.setText(content);

        // Hiển thị ảnh nếu có đường dẫn
        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(EditArticleActivity.this)
                    .load(Uri.parse(imagePath))  // Tải ảnh từ đường dẫn
                    .into(avatar);
        }

        // Thiết lập sự kiện cho nút đổi avatar
        change_avatar.setOnClickListener(view -> {
            ImagePicker.with(this)
                    .crop() // Cắt ảnh
                    .compress(1024) // Nén ảnh tối đa 1024KB
                    .maxResultSize(1080, 1080) // Kích thước tối đa của ảnh
                    .createIntent(intent -> {
                        imagePickerLauncherAvatar.launch(intent);
                        return null;
                    });
        });

        // Khởi tạo launcher cho việc chọn ảnh từ ImagePicker
        imagePickerLauncherAvatar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();  // Lấy URI ảnh đã chọn
                        avatar.setImageURI(uri);  // Hiển thị ảnh lên ImageView
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Sự kiện khi nhấn nút lưu
        saveButton.setOnClickListener(v -> {
            new AlertDialog.Builder(EditArticleActivity.this)
                    .setTitle("Confirm Edit")
                    .setMessage("Are you sure you want to edit this article?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Thực hiện cập nhật bài viết
                        LocalDateTime currentTime = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        String formattedDateTime = currentTime.format(formatter);
                        String image = uri != null ? uri.toString() : ""; // Nếu không có ảnh, sử dụng chuỗi rỗng
                        // Tạo đối tượng Article mới
                        Article updatedArticle = new Article(
                                articleNameEdit.getText().toString(),
                                contentEdit.getText().toString(),
                                formattedDateTime, image,
                                1);  // Chuyển vào ID của recruiter (1)

                        // Gọi API để cập nhật bài viết
                        updateArticle(articleId, updatedArticle);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    // Phương thức cập nhật bài viết lên API
    private void updateArticle(int id, Article article) {
        ApiPostingService.apiService.updateArticle(id, article)
                .enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(Call<Article> call, Response<Article> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EditArticleActivity.this, "Article updated successfully", Toast.LENGTH_SHORT).show();

                            // Trả kết quả về Activity trước và quay lại
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish(); // Quay lại Activity trước
                        } else {
                            Toast.makeText(EditArticleActivity.this, "Failed to update article", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Article> call, Throwable t) {
                        Toast.makeText(EditArticleActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
