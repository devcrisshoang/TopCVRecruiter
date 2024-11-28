package com.example.topcvrecruiter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

    private Uri uri;

    private String imagePath;

    private int id_Recruiter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        setWidget();

        setClick();

    }

    private void setClick(){
        change_avatar.setOnClickListener(view -> ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    imagePickerLauncherAvatar.launch(intent);
                    return null;
                }));

        saveButton.setOnClickListener(v -> saveButton());

        ImageButton backButton = findViewById(R.id.post_edit_back_button);

        backButton.setOnClickListener(v -> finish());
    }

    private void saveButton(){
        new AlertDialog.Builder(EditArticleActivity.this)
                .setTitle("Confirm Edit")
                .setMessage("Are you sure you want to edit this article?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    LocalDateTime currentTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    String formattedDateTime = currentTime.format(formatter);

                    String image = uri != null ? uri.toString() : imagePath;

                    Article updatedArticle = new Article(
                            articleNameEdit.getText().toString(),
                            contentEdit.getText().toString(),
                            formattedDateTime, image,id_Recruiter);

                    updateArticle(articleId, updatedArticle);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void setWidget(){
        articleNameEdit = findViewById(R.id.article_name_edit);
        contentEdit = findViewById(R.id.content_edit);
        saveButton = findViewById(R.id.save_button);

        avatar = findViewById(R.id.avatar);
        change_avatar = findViewById(R.id.change_avatar);

        id_Recruiter = getIntent().getIntExtra("id_Recruiter", -1);
        Log.e("EditArticleActivity","ID recruiter: " + id_Recruiter);

        articleId = getIntent().getIntExtra("article_id", -1);
        String articleName = getIntent().getStringExtra("article_name");
        String content = getIntent().getStringExtra("content");
        imagePath = getIntent().getStringExtra("image_path");

        articleNameEdit.setText(articleName);
        contentEdit.setText(content);

        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(EditArticleActivity.this)
                    .load(Uri.parse(imagePath))
                    .into(avatar);
        } else {
            Glide.with(EditArticleActivity.this)
                    .load(R.drawable.account_ic)
                    .into(avatar);
        }

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
    }

    private void updateArticle(int id, Article article) {
        ApiPostingService.apiService.updateArticle(id, article)
                .enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(Call<Article> call, Response<Article> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EditArticleActivity.this, "Article updated successfully", Toast.LENGTH_SHORT).show();

                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
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
