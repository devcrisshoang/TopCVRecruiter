package com.example.topcvrecruiter.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topcvrecruiter.API.ApiDashboardService;

import com.example.topcvrecruiter.API.ApiRecruiterService;
import com.example.topcvrecruiter.Adapter.DashboardApplicantAdapter;
import com.example.topcvrecruiter.Adapter.PaginationScrollListener;
import com.example.topcvrecruiter.Model.Job;
import com.example.topcvrecruiter.NumberApplicantActivity;
import com.example.topcvrecruiter.NumberJobOfRecruiterActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Model.ApplicantJob;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DashboardFragment extends Fragment {
    private int recruiterId;
    private int _recruiterId;
    private int id_User;

    private TextView jobCountTextView;
    private TextView acceptedTextView;
    private TextView rateSuccessTextView;
    private TextView rejectedTextView;

    private RecyclerView applicantsRecyclerView;
    private DashboardApplicantAdapter dashboardAdapter;
    private ApiDashboardService apiDashboardService;
    private ActivityResultLauncher<Intent> applicantDetailLauncher;

    private CardView jobCountCardView;
    private CardView acceptedCardView;
    private CardView rejectedCardView;
    private int totalAccepted = 0;
    private int totalRejected = 0;

    private boolean isLoading = false;//
    private boolean isLastPage;//
    private int totalPage;//
    private int currentPage = 1;//
    private int totalItemInPage = 10;//

    private List<ApplicantJob> displayedList = new ArrayList<>();
    List<ApplicantJob> applicantList = new ArrayList<>();

    LinearLayoutManager linearLayoutManager;

    public DashboardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        // Inflate the layout for this fragment
        setWidget(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id_User = bundle.getInt("user_id", 0);  // 0 là giá trị mặc định
            Log.e("NewsFeedFragment", "User ID received: " + id_User);
        } else {
            Log.e("NewsFeedFragment", "Bundle is null, user ID not received " + id_User);
        }
        getRecruiterById(id_User);
        //Log.e("DashboardFragment","response1" + _recruiterId);
        apiDashboardService = ApiDashboardService.apiDashboardService;

        applicantDetailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                    }
                }
        );

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

        applicantsRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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

        return view;
    }

    private void getRecruiterById(int userId) {
        ApiRecruiterService.ApiRecruiterService.getRecruiterByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recruiter -> {
                            if (recruiter.getId() != 0) {
                                recruiterId = recruiter.getId();
                                fetchDashboardData(recruiterId);
                                jobCountCardView.setOnClickListener(view1 -> fetchListJobs(recruiterId));
                                acceptedCardView.setOnClickListener(view1 -> fetchListAccepted(recruiterId));
                                rejectedCardView.setOnClickListener(view1 -> fetchListRejected(recruiterId));
                                Log.e("DashboardFragment","response" + recruiterId);
                            } else {
                                Toast.makeText(getContext(), "Recruiter not found", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Log.e("AccountFragment", "Error fetching recruiter: " + throwable.getMessage());
                            Toast.makeText(getContext(), "Failed to load recruiter", Toast.LENGTH_SHORT).show();
                        }

                );
    }

    private void setWidget(View view){
        //Card View TextView
        jobCountTextView = view.findViewById(R.id.electricity_amount);
        acceptedTextView = view.findViewById(R.id.job_count);
        rateSuccessTextView = view.findViewById(R.id.recruiting_rate);
        rejectedTextView = view.findViewById(R.id.resume_amount);

        //Card View onclick view
        jobCountCardView = view.findViewById(R.id.applicant_cardView);
        acceptedCardView = view.findViewById(R.id.job_cardView);
        rejectedCardView = view.findViewById((R.id.resume_cardView));

        applicantsRecyclerView = view.findViewById(R.id.aplicants_Recycler_View);

        linearLayoutManager = new LinearLayoutManager(getContext());
        applicantsRecyclerView.setLayoutManager(linearLayoutManager);
        dashboardAdapter = new DashboardApplicantAdapter(applicantDetailLauncher);
        applicantsRecyclerView.setAdapter(dashboardAdapter);
    }

    private void calculateAndDisplayRate() {
        if (totalAccepted + totalRejected > 0) { // Kiểm tra tránh chia cho 0
            float rate = (float) totalAccepted / (totalAccepted + totalRejected);
            rateSuccessTextView.setText(String.format(Locale.getDefault(), "%.2f%%", rate * 100)); // Cập nhật tỷ lệ vào TextView
        } else {
            rateSuccessTextView.setText("0%"); // Nếu không có ứng viên nào
        }
    }

    private void fetchListJobs(int recruiterId) {
        apiDashboardService.getListJobs(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Job>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Job> jobs) {
                        Intent intent = new Intent(getContext(), NumberJobOfRecruiterActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("jobsList", new ArrayList<>(jobs));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("DashboardFragment", "Error fetching jobs list", e);
                    }

                    @Override
                    public void onComplete() {

                    }

                });
    };

    private void fetchListAccepted(int recruiterId) {
        apiDashboardService.getAcceptedApplicants(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ApplicantJob>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<ApplicantJob> applicants) {
                        Intent intent = new Intent(getContext(), NumberApplicantActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("applicantList", new ArrayList<>(applicants));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("DashboardFragment", "Error fetching applicant list", e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });


    }

    private void fetchListRejected(int recruiterId) {
        apiDashboardService.getRejectedApplicants(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ApplicantJob>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<ApplicantJob> applicants) {
                        Intent intent = new Intent(getContext(), NumberApplicantActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("applicantList", new ArrayList<>(applicants));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("DashboardFragment", "Error fetching applicant list", e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });


    }

    @SuppressLint("CheckResult")
    private void fetchDashboardData(int recruiterId) {
        //------------------Job------------------------//
        apiDashboardService.getListJobs(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jobs -> {
                    int jobCount = jobs.size(); // Đếm số lượng item trong danh sách
                    jobCountTextView.setText(String.valueOf(jobCount)); // Hiển thị số lượng ứng viên lên TextView
                }, throwable -> {
                    // Xử lý lỗi nếu có
                    Log.e("API Error", "Error fetching applicants", throwable);
                });


        //------------------Accepted--------------------------//
        apiDashboardService.getAcceptedApplicants(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accepted -> {
                    totalAccepted = accepted.size(); // Đếm số lượng item trong danh sách
                    acceptedTextView.setText(String.valueOf(totalAccepted));
                    calculateAndDisplayRate();
                }, throwable -> {
                    // Xử lý lỗi nếu có
                    Log.e("API Error", "Error fetching applicants", throwable);
                });
        //------------------Rejected--------------------------//
        apiDashboardService.getRejectedApplicants(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rejected -> {
                    totalRejected = rejected.size(); // Đếm số lượng item trong danh sách
                    rejectedTextView.setText(String.valueOf(totalRejected));
                    calculateAndDisplayRate();
                }, throwable -> {
                    // Xử lý lỗi nếu có
                    Log.e("API Error", "Error fetching applicants", throwable);
                });

        //------------------Suggest------------------------//
        apiDashboardService.getListSuggestedApplicants(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ApplicantJob>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<ApplicantJob> applicants) {
                        applicantList = applicants;
                        setFirstData();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("DashboardFragment", "Error fetching applicant data", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setFirstData(){

        displayedList = getList();
        dashboardAdapter.setListApplicant(displayedList);

        if (currentPage < totalPage){
            dashboardAdapter.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }

    private void loadNextPage(){
        new Handler().postDelayed(() -> {
            dashboardAdapter.removeFooterLoading();
            List<ApplicantJob> nextPageList = getList();
            displayedList.addAll(nextPageList);
            dashboardAdapter.notifyDataSetChanged();

            isLoading = false;

            if (currentPage < totalPage) {
                dashboardAdapter.addFooterLoading();
            }
            else {
                isLastPage = true;
            }
        }, 2000);
    }

    private List<ApplicantJob> getList(){
        //Toast.makeText(getContext(), "Load data page: " + currentPage, Toast.LENGTH_SHORT).show();
        List<ApplicantJob> list = new ArrayList<>();

        int start = (currentPage - 1) * totalItemInPage; // Tính chỉ số bắt đầu
        int end = Math.min(start + totalItemInPage, applicantList.size()); // Tính chỉ số kết thúc

        if (start < applicantList.size()) {
            list.addAll(applicantList.subList(start, end)); // Thêm các phần tử từ workList vào danh sách
        }

        return list;
    }

}