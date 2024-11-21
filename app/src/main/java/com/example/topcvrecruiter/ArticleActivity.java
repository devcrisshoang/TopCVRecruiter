package com.example.topcvrecruiter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.topcvrecruiter.API.ApiPostingService;
import com.example.topcvrecruiter.Model.Article;
import com.example.topcvrecruiter.Utils.NotificationUtils;
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
    private int id_Recruiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        id_Recruiter = getIntent().getIntExtra("id_Recruiter",0);

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
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
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
            String image = (uri != null) ? uri.toString() : "";

            // Kiểm tra xem title và content có dữ liệu hay không
            if (!title.isEmpty() && !content.isEmpty()) {
                postArticle(title, content, image);

            } else {
                Toast.makeText(ArticleActivity.this, "Insert Information", Toast.LENGTH_SHORT).show();
            }
        });

        // Kiểm tra quyền gửi thông báo nếu chạy trên Android 13 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    // Post
    private void postArticle(String title, String content, String image) {
        // Nếu không có ảnh, có thể sử dụng ảnh mặc định hoặc để trống
        if (image.isEmpty()) {
            image = "";  // Hoặc để trống "" nếu không cần ảnh
        }

        // Lấy thời gian hiện tại
        LocalDateTime currentTime = LocalDateTime.now();

        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        // Chuyển đổi thời gian thành chuỗi theo định dạng đã chọn
        String formattedDateTime = currentTime.format(formatter);

        Article article = new Article(title, content, formattedDateTime, image, id_Recruiter ); // Thay đổi giá trị mặc định của iD_Recruiter tại đây
        ApiPostingService.apiService.postArticle(article).enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if (response.isSuccessful()) {
                    // Sử dụng NotificationUtils để hiển thị thông báo
                    NotificationUtils.showNotification(ArticleActivity.this, "You just posted an article !");
                    // Hiển thị Toast
                    Toast.makeText(ArticleActivity.this, "Post Article Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Thông báo lỗi nếu không thành công
                    Toast.makeText(ArticleActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                // Hiển thị lỗi nếu API thất bại
                Toast.makeText(ArticleActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    // Xử lý kết quả yêu cầu quyền gửi thông báo
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Đã cấp quyền
                Toast.makeText(this, "Permission granted to post notifications", Toast.LENGTH_SHORT).show();
            } else {
                // Không cấp quyền
                Toast.makeText(this, "Permission denied to post notifications", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
