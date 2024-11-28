package com.example.topcvrecruiter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcvrecruiter.Adapter.DashboardApplicantAdapter;
import com.example.topcvrecruiter.Adapter.PaginationScrollListener;
import com.example.topcvrecruiter.Model.ApplicantJob;
import java.util.ArrayList;
import java.util.List;

public class NumberApplicantActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    DashboardApplicantAdapter applicantAdapter;

    List<ApplicantJob> applicantList;

    private List<ApplicantJob> displayedList = new ArrayList<>();

    private boolean isLoading = false;
    private boolean isLastPage;

    private int totalPage;
    private int currentPage = 1;
    private int totalItemInPage = 10;
    private int id_Recruiter;

    private ImageButton backButton;

    private ActivityResultLauncher<Intent> applicantDetailLauncher;

    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_applicant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();

        setTotalPage();

        setFirstData();

    }

    private void setTotalPage(){
        if(applicantList.size() <= totalItemInPage){
            totalPage = 1;
        }
        else if(applicantList.size() % totalItemInPage == 0){
            totalPage = applicantList.size()/totalItemInPage;
        }
        else if(applicantList.size() % totalItemInPage != 0){
            totalPage = applicantList.size()/totalItemInPage +1;
        }
    }

    private void setClick() {

        backButton.setOnClickListener(v -> finish());

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
    }

    private  void setWidget(){
        backButton = findViewById(R.id.back_button);
        applicantList = (List<ApplicantJob>) getIntent().getSerializableExtra("applicantList");
        id_Recruiter = getIntent().getIntExtra("id_Recruiter", -1);
        recyclerView = findViewById(R.id.number_applicant_Recycler_View);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        applicantDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                    }
                }
        );
        applicantAdapter = new DashboardApplicantAdapter(applicantDetailLauncher, id_Recruiter, 0); //Warning
        recyclerView.setAdapter(applicantAdapter);
    }

    private void setFirstData(){

        displayedList = getList();
        applicantAdapter.setListApplicant(displayedList);

        if (currentPage < totalPage){
            applicantAdapter.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }

    private void loadNextPage(){
        new Handler().postDelayed(() -> {
            applicantAdapter.removeFooterLoading();
            List<ApplicantJob> nextPageList = getList();
            displayedList.addAll(nextPageList);
            applicantAdapter.notifyDataSetChanged();

            isLoading = false;

            if (currentPage < totalPage) {
                applicantAdapter.addFooterLoading();
            }
            else {
                isLastPage = true;
            }
        }, 2000);
    }

    private List<ApplicantJob> getList(){
        List<ApplicantJob> list = new ArrayList<>();

        int start = (currentPage - 1) * totalItemInPage;
        int end = Math.min(start + totalItemInPage, applicantList.size());

        if (start < applicantList.size()) {
            list.addAll(applicantList.subList(start, end));
        }

        return list;
    }

}
