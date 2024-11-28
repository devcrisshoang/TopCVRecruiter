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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();
    }

    private void changeAvatarButton(){
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    imagePickerLauncherAvatar.launch(intent);
                    return null;
                });
    }

    private void addButton(){
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();
        String image = (uri != null) ? uri.toString() : "";

        if (!title.isEmpty() && !content.isEmpty()) {
            postArticle(title, content, image);

        } else {
            Toast.makeText(ArticleActivity.this, "Insert Information", Toast.LENGTH_SHORT).show();
        }
    }

    private void setClick(){
        change_avatar.setOnClickListener(view -> changeAvatarButton());

        addButton.setOnClickListener(v -> addButton());
    }

    private void setWidget(){
        id_Recruiter = getIntent().getIntExtra("id_Recruiter",0);

        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(view -> finish());

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        avatar = findViewById(R.id.avatar);
        change_avatar = findViewById(R.id.change_avatar);

        addButton = findViewById(R.id.add_new_article_button);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

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

    }

    private void postArticle(String title, String content, String image) {

        if (image.isEmpty()) {
            image = "";
        }

        LocalDateTime currentTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        String formattedDateTime = currentTime.format(formatter);

        Article article = new Article(title, content, formattedDateTime, image, id_Recruiter );
        ApiPostingService.apiService.postArticle(article).enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                if (response.isSuccessful()) {
                    NotificationUtils.showNotification(ArticleActivity.this, "You just posted an article !");
                    Toast.makeText(ArticleActivity.this, "Post Article Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ArticleActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                Toast.makeText(ArticleActivity.this, "Error!", Toast.LENGTH_SHORT).show();
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
