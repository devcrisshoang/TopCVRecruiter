package com.example.topcvrecruiter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.topcvrecruiter.API.ApiPostingService;
import com.example.topcvrecruiter.Model.Article;
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
    private Uri uri;  // Uri for selected image
    private String imagePath; // Store the original image path to prevent losing it
    private ImageButton backButton;

    private int id_Recruiter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        // Initialize views
        articleNameEdit = findViewById(R.id.article_name_edit);
        contentEdit = findViewById(R.id.content_edit);
        saveButton = findViewById(R.id.save_button);

        avatar = findViewById(R.id.avatar);
        change_avatar = findViewById(R.id.change_avatar);

        // Retrieve article data from the Intent
        id_Recruiter = getIntent().getIntExtra("id_Recruiter", -1);
        Log.e("EditArticleActivity","ID recruiter: " + id_Recruiter);

        articleId = getIntent().getIntExtra("article_id", -1);
        String articleName = getIntent().getStringExtra("article_name");
        String content = getIntent().getStringExtra("content");
        imagePath = getIntent().getStringExtra("image_path"); // Save the original image path

        if (articleId == -1) {
            Toast.makeText(this, "Invalid article ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set the existing article data into EditText views
        articleNameEdit.setText(articleName);
        contentEdit.setText(content);

        // Kiểm tra xem imagePath có null hoặc rỗng không
        if (imagePath != null && !imagePath.isEmpty()) {
            // Nếu có đường dẫn ảnh hợp lệ, tải ảnh từ URL
            Glide.with(EditArticleActivity.this)
                    .load(Uri.parse(imagePath))  // Load image from the original path
                    .into(avatar);
        } else {
            // Nếu không có ảnh (null hoặc rỗng), hiển thị ảnh mặc định
            Glide.with(EditArticleActivity.this)
                    .load(R.drawable.account_ic)  // Thay "account_ic" bằng ID ảnh mặc định của bạn
                    .into(avatar);
        }


        // Set click listener for the avatar change button
        change_avatar.setOnClickListener(view -> {
            ImagePicker.with(this)
                    .crop() // Enable crop
                    .compress(1024) // Compress image to a maximum size of 1024KB
                    .maxResultSize(1080, 1080) // Set maximum image size
                    .createIntent(intent -> {
                        imagePickerLauncherAvatar.launch(intent); // Launch the image picker
                        return null;
                    });
        });

        // Initialize the image picker launcher for selecting images
        imagePickerLauncherAvatar = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();  // Get the URI of the selected image
                        avatar.setImageURI(uri);  // Display the selected image in ImageView
                    } else {
                        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Set click listener for the save button
        saveButton.setOnClickListener(v -> {
            new AlertDialog.Builder(EditArticleActivity.this)
                    .setTitle("Confirm Edit")
                    .setMessage("Are you sure you want to edit this article?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Get current time and format it
                        LocalDateTime currentTime = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        String formattedDateTime = currentTime.format(formatter);

                        // Use the selected image URI if available, otherwise keep the original image
                        String image = uri != null ? uri.toString() : imagePath; // If no new image, use the old one

                        // Create the updated article object
                        Article updatedArticle = new Article(
                                articleNameEdit.getText().toString(),
                                contentEdit.getText().toString(),
                                formattedDateTime, image,id_Recruiter);

                        // Call the API to update the article
                        updateArticle(articleId, updatedArticle);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
        ImageButton backButton = findViewById(R.id.post_edit_back_button);

        // Đặt sự kiện click cho nút back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức quay lại
                onBackPressed();
            }
        });
    }

    // Method to update the article on the server via API
    private void updateArticle(int id, Article article) {
        ApiPostingService.apiService.updateArticle(id, article)
                .enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(Call<Article> call, Response<Article> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EditArticleActivity.this, "Article updated successfully", Toast.LENGTH_SHORT).show();

                            // Return result to previous activity and finish
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish(); // Close this activity and return
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
