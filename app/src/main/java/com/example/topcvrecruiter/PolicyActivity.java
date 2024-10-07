package com.example.topcvrecruiter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PolicyActivity extends AppCompatActivity {
    private Button Register_Button;
    private ImageView iconNumber1;
    private ImageView iconNumber2;
    private ScrollView scrollView;
    private View linearLayoutAgreement;

    private boolean isAgreementVisible = false; // Biến để theo dõi trạng thái hiển thị

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_policy);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Register_Button = findViewById(R.id.Register_Button);
        iconNumber1 = findViewById(R.id.iconNumber1);
        iconNumber2 = findViewById(R.id.iconNumber2);
        scrollView = findViewById(R.id.scrollView);
        linearLayoutAgreement = findViewById(R.id.linearLayoutAgreement);

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isPolicyButtonClicked", false)) {
            iconNumber2.setColorFilter(getResources().getColor(R.color.green_color), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        Register_Button.setOnClickListener(view -> {
//            startActivity(new Intent(PolicyActivity.this, LoginActivity.class));
//            finish();
        });

        // Thiết lập listener cho ScrollView
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Kiểm tra nếu đang cuộn xuống
                if (scrollY > oldScrollY && !isAgreementVisible) {
                    linearLayoutAgreement.setVisibility(View.VISIBLE);
                    isAgreementVisible = true; // Đặt trạng thái hiển thị là true
                }
                // Kiểm tra nếu đang cuộn lên
                else if (scrollY < oldScrollY && isAgreementVisible) {
                    linearLayoutAgreement.setVisibility(View.GONE);
                    isAgreementVisible = false; // Đặt trạng thái hiển thị là false
                }
            }
        });
    }
}
