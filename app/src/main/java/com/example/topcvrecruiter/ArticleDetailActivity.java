package com.example.topcvrecruiter;

import com.bumptech.glide.Glide;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.topcvrecruiter.API.ApiPostingService;
import com.example.topcvrecruiter.Model.Article;
import com.example.topcvrecruiter.Utils.DateTimeUtils;
import com.example.topcvrecruiter.Utils.NotificationUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ArticleDetailActivity extends AppCompatActivity {

    private int articleId;
    private int id_Recruiter;

    private TextView articleName;
    private TextView content;
    private TextView createTime;

    private ImageButton backButton;
    private ImageButton editButton;

    private Disposable disposable;

    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        setWidget();

        setClick();

    }

    private void editButton(){
        Intent intent = new Intent(ArticleDetailActivity.this, EditArticleActivity.class);
        intent.putExtra("article_id", articleId);
        intent.putExtra("id_Recruiter", id_Recruiter);
        intent.putExtra("article_name", articleName.getText().toString());
        intent.putExtra("content", content.getText().toString());
        startActivity(intent);
    }

    private void setClick(){

        editButton.setOnClickListener(v -> editButton());

        backButton.setOnClickListener(v -> finish());

        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void setWidget(){
        articleName = findViewById(R.id.article_Name);
        content = findViewById(R.id.content);
        createTime = findViewById(R.id.create_Time);
        backButton = findViewById(R.id.back_button);
        deleteButton = findViewById(R.id.delete_button);
        editButton = findViewById(R.id.edit_button);

        articleId = getIntent().getIntExtra("article_id", -1);
        id_Recruiter = getIntent().getIntExtra("id_Recruiter", -1);
        if (articleId == -1) {
            Toast.makeText(this, "Invalid article ID", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            loadArticleDetail(articleId);
        }
    }

    private void loadArticleDetail(int articleId) {
        ApiPostingService.apiService.getArticleById(articleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Article>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Article article) {
                        articleName.setText(article.getArticle_Name());
                        content.setText(article.getContent());
                        createTime.setText(DateTimeUtils.formatTimeAgo(article.getCreate_Time()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ArticleDetailActivity.this, "Failed to load article details", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadArticleDetail(articleId);
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this article?");
        builder.setPositiveButton("Yes", (dialog, which) -> deleteArticle(articleId));
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteArticle(int articleId) {
        ApiPostingService.apiService.deleteArticle(articleId)
                .enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ArticleDetailActivity.this, "Article deleted successfully", Toast.LENGTH_SHORT).show();
                            NotificationUtils.showNotification(ArticleDetailActivity.this, "You just deleted an article !");
                            finish();
                        } else {
                            Toast.makeText(ArticleDetailActivity.this, "Failed to delete article", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                        Toast.makeText(ArticleDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
