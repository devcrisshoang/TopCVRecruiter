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
    private TextView articleName;
    private TextView content;
    private TextView createTime;
    private ImageButton backButton;
    private Disposable disposable;
    private ImageButton editButton;
    private Button deleteButton;
    private ImageView articleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        articleName = findViewById(R.id.article_Name);
        content = findViewById(R.id.content);
        createTime = findViewById(R.id.create_Time);
        backButton = findViewById(R.id.back_button);
        deleteButton = findViewById(R.id.delete_button);
        editButton = findViewById(R.id.edit_button);
        articleImage = findViewById(R.id.image);

        // Lấy article_id từ Intent
        articleId = getIntent().getIntExtra("article_id", -1);

        // Kiểm tra articleId có hợp lệ không
        if (articleId == -1) {
            Toast.makeText(this, "Invalid article ID", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            loadArticleDetail(articleId);  // Tải chi tiết bài viết từ API
        }

        // Xử lý sự kiện nút Edit
        // Xử lý sự kiện nút Edit
        editButton.setOnClickListener(v -> {
            // Truyền articleId, tên bài viết, nội dung, và đường dẫn ảnh
            String imagePath = articleImage.getTag() != null ? articleImage.getTag().toString() : "";  // Kiểm tra null trước khi gọi toString()
            Intent intent = new Intent(ArticleDetailActivity.this, EditArticleActivity.class);
            intent.putExtra("article_id", articleId);
            intent.putExtra("article_name", articleName.getText().toString());
            intent.putExtra("content", content.getText().toString());
            intent.putExtra("image_path", imagePath);  // Truyền đường dẫn ảnh từ ImageView tag
            startActivityForResult(intent, 1); // requestCode = 1
        });


        // Xử lý sự kiện nút Back
        backButton.setOnClickListener(v -> finish());

        // Xử lý sự kiện nút Delete
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
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

                        // Tải hình ảnh vào ImageView bằng Glide
                        String imagePath = article.getImage(); // Lấy đường dẫn ảnh từ API
                        if (imagePath != null && !imagePath.isEmpty()) {
                            Glide.with(ArticleDetailActivity.this)
                                    .load(Uri.parse(imagePath))  // Tải ảnh từ đường dẫn file URI
                                    .into(articleImage);
                            articleImage.setTag(imagePath);  // Lưu đường dẫn ảnh vào tag của ImageView
                        }else {
                            // Nếu không có ảnh (null hoặc rỗng), hiển thị ảnh mặc định
                            Glide.with(ArticleDetailActivity.this)
                                    .load(R.drawable.fpt_ic)  // Thay "account_ic" bằng ID ảnh mặc định của bạn
                                    .into(articleImage);
                        }

                        // Định dạng thời gian
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        try {
                            Date createDate = inputFormat.parse(article.getCreate_Time());
                            long createTimeInMillis = createDate.getTime();
                            long currentTimeInMillis = System.currentTimeMillis();

                            long timeDifference = currentTimeInMillis - createTimeInMillis;
                            long minutesDifference = timeDifference / (60 * 1000);
                            long hoursDifference = timeDifference / (60 * 60 * 1000);

                            if (minutesDifference < 60) {
                                createTime.setText(minutesDifference + " minutes ago");
                            } else if (hoursDifference < 24) {
                                createTime.setText(hoursDifference + " hours ago");
                            } else if (hoursDifference < 24 * 2) {
                                createTime.setText("Yesterday");
                            } else if (hoursDifference < 24 * 7) {
                                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                                String dayOfWeek = dayFormat.format(createDate);
                                createTime.setText(dayOfWeek);
                            } else {
                                String formattedDate = outputFormat.format(createDate);
                                createTime.setText(formattedDate);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            createTime.setText("Create Time: " + article.getCreate_Time());
                        }
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
            // Làm mới dữ liệu khi quay lại từ EditArticleActivity
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
                            finish();  // Đóng activity sau khi xóa
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
            disposable.dispose();  // Hủy subscription khi không cần dùng nữa
        }
    }
}
