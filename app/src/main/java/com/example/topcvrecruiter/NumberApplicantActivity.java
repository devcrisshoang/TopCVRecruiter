package com.example.topcvrecruiter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.Adapter.DashboardApplicantAdapter;
import com.example.topcvrecruiter.Adapter.PaginationScrollListener;
import com.example.topcvrecruiter.model.ApplicantJob;

import java.util.ArrayList;
import java.util.List;

public class NumberApplicantActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DashboardApplicantAdapter applicantAdapter;
    List<ApplicantJob> applicantList;
    private List<ApplicantJob> displayedList = new ArrayList<>();

    private ImageButton backButton;
    private ActivityResultLauncher<Intent> applicantDetailLauncher;

    private boolean isLoading = false;//
    private boolean isLastPage;//
    private int totalPage;//
    private int currentPage = 1;//
    private int totalItemInPage = 10;//

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_applicant); // Sửa chính tả tên layout

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) { return; }

        applicantList = (List<ApplicantJob>) getIntent().getSerializableExtra("applicantList");

        recyclerView = findViewById(R.id.number_applicant_Recycler_View);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //------------------
        if(applicantList.size() <= totalItemInPage){
            totalPage = 1;
        }
        else if(applicantList.size() % totalItemInPage == 0){
            totalPage = applicantList.size()/totalItemInPage;
        }
        else if(applicantList.size() % totalItemInPage != 0){
            totalPage = applicantList.size()/totalItemInPage +1;
        }
        //-------------------

        applicantDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                    }
                }
        );

        applicantAdapter = new DashboardApplicantAdapter(applicantDetailLauncher);
        recyclerView.setAdapter(applicantAdapter);

        setFirstData();

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
        //Toast.makeText(this, "Load data page: " + currentPage, Toast.LENGTH_SHORT).show();
        List<ApplicantJob> list = new ArrayList<>();

        int start = (currentPage - 1) * totalItemInPage; // Tính chỉ số bắt đầu
        int end = Math.min(start + totalItemInPage, applicantList.size()); // Tính chỉ số kết thúc

        if (start < applicantList.size()) {
            list.addAll(applicantList.subList(start, end)); // Thêm các phần tử từ workList vào danh sách
        }

        return list;
    }

}
