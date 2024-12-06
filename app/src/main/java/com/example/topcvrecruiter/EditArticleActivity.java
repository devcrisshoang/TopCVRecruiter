package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.topcvrecruiter.API.ApiPostingService;
import com.example.topcvrecruiter.Model.Article;
import com.example.topcvrecruiter.Utils.DateTimeUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditArticleActivity extends AppCompatActivity {

    private EditText articleNameEdit;
    private EditText contentEdit;
    private Button saveButton;

    private int articleId;
    private int id_Recruiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        setWidget();

        // Nếu không có dữ liệu cần thiết, thoát khỏi Activity
        if (!validateInputData()) {
            finish();
            return;
        }

        setClick();
    }

    private void setClick() {
        saveButton.setOnClickListener(v -> saveButton());

        ImageButton backButton = findViewById(R.id.post_edit_back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void saveButton() {
        new AlertDialog.Builder(EditArticleActivity.this)
                .setTitle("Confirm Edit")
                .setMessage("Are you sure you want to edit this article?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    String time = DateTimeUtils.getCurrentTime();

                    // Tạo một đối tượng Article mới với dữ liệu đã sửa
                    Article updatedArticle = new Article(
                            articleNameEdit.getText().toString(),
                            contentEdit.getText().toString(),
                            time, "", id_Recruiter);

                    updateArticle(articleId, updatedArticle);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void setWidget() {
        articleNameEdit = findViewById(R.id.article_name_edit);
        contentEdit = findViewById(R.id.content_edit);
        saveButton = findViewById(R.id.save_button);

        // Lấy dữ liệu từ Intent
        id_Recruiter = getIntent().getIntExtra("id_Recruiter", -1);
        articleId = getIntent().getIntExtra("article_id", -1);

        String articleName = getIntent().getStringExtra("article_name");
        String content = getIntent().getStringExtra("content");

        // Kiểm tra null và thiết lập giá trị cho EditText
        if (articleName != null) {
            articleNameEdit.setText(articleName);
        } else {
            Log.e("EditArticleActivity", "articleName is null");
        }

        if (content != null) {
            contentEdit.setText(content);
        } else {
            Log.e("EditArticleActivity", "content is null");
        }
    }

    private boolean validateInputData() {
        if (id_Recruiter == -1) {
            Toast.makeText(this, "Missing recruiter ID!", Toast.LENGTH_SHORT).show();
            Log.e("EditArticleActivity", "Recruiter ID is missing or invalid.");
            return false;
        }

        if (articleId == -1) {
            Toast.makeText(this, "Missing article ID!", Toast.LENGTH_SHORT).show();
            Log.e("EditArticleActivity", "Article ID is missing or invalid.");
            return false;
        }

        return true;
    }

    private void updateArticle(int id, Article article) {
        ApiPostingService.apiService.updateArticle(id, article)
                .enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(Call<Article> call, Response<Article> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EditArticleActivity.this, "Article updated successfully", Toast.LENGTH_SHORT).show();

                            // Trả kết quả về Activity gọi nó
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
                        Log.e("EditArticleActivity", "Update failed: " + t.getMessage());
                    }
                });
    }
}
