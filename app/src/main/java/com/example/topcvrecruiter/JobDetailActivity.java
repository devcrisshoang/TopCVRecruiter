package com.example.topcvrecruiter;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class JobDetailActivity extends AppCompatActivity {
    //private ListInformationlAdapter listInformationlAdapter;
    private ImageButton informationBackButton;
    private ScrollView scrollView;
    private boolean isImageVisible = true;
    private LinearLayout header_title;
    private ImageView company_logo;
    private ImageButton back_button;
    private int jobId;
    private int bestId;

    private Button accept_button;
    private TextView working_time;
    private TextView working_place;
    private TextView interesting;
    private TextView conditions;
    private TextView description;
    private TextView deadline;
    private TextView position;
    private TextView gender;
    private TextView number_of_people;
    private TextView work_method;
    private TextView experience_detail;
    private TextView salary;
    private TextView location;
    private TextView experience;
    private TextView company_name;
    private TextView job_name;
    private TextView work_name;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_detail);
        // Thiết lập padding cho View chính
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setWidget();

        // Lấy jobId từ Intent
    }

    private void setWidget() {
        informationBackButton = findViewById(R.id.information_back_button);
        back_button = findViewById(R.id.back_button);
        scrollView = findViewById(R.id.scrollView);
        company_logo = findViewById(R.id.company_logo);
        header_title = findViewById(R.id.header_title);
        accept_button = findViewById(R.id.recruit_button);
        working_time = findViewById(R.id.working_time);
        working_place = findViewById(R.id.working_place);
        interesting = findViewById(R.id.interesting);
        conditions = findViewById(R.id.conditions);
        description = findViewById(R.id.description);
        deadline = findViewById(R.id.deadline);
        position = findViewById(R.id.position);
        gender = findViewById(R.id.gender);
        number_of_people = findViewById(R.id.number_of_people);
        work_method = findViewById(R.id.work_method);
        experience_detail = findViewById(R.id.experience_detail);
        salary = findViewById(R.id.salary);
        location = findViewById(R.id.location);
        experience = findViewById(R.id.experience);
        company_name = findViewById(R.id.company_name);
        job_name = findViewById(R.id.job_name);

    }

}
