package com.example.topcvrecruiter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.topcvrecruiter.API.ApiPostingService;
import com.example.topcvrecruiter.Model.Article;
import com.example.topcvrecruiter.Utils.DateTimeUtils;
import com.example.topcvrecruiter.Utils.NotificationUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostingArticleActivity extends AppCompatActivity {

    private ImageButton back_button;

    private EditText editTextTitle;
    private EditText editTextContent;

    private Button addButton;

    private int id_Recruiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();
    }

    private void addButton(){
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();


        if (!title.isEmpty() && !content.isEmpty()) {
            postArticle(title, content, "");

        } else {
            Toast.makeText(PostingArticleActivity.this, "Insert Information", Toast.LENGTH_SHORT).show();
        }
    }

    private void setClick(){

        addButton.setOnClickListener(v -> addButton());
    }

    private void setWidget(){
        id_Recruiter = getIntent().getIntExtra("id_Recruiter",0);

        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(view -> finish());

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);

        addButton = findViewById(R.id.add_new_article_button);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

    }

    private void postArticle(String title, String content, String image) {

        if (image.isEmpty()) {
            image = "";
        }

        String time = DateTimeUtils.getCurrentTime();

        Article article = new Article(title, content, time, "", id_Recruiter );
        ApiPostingService.apiService.postArticle(article).enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if (response.isSuccessful()) {
                    NotificationUtils.showNotification(PostingArticleActivity.this, "You just posted an article !");
                    Toast.makeText(PostingArticleActivity.this, "Post Article Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PostingArticleActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                Toast.makeText(PostingArticleActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted to post notifications", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied to post notifications", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
