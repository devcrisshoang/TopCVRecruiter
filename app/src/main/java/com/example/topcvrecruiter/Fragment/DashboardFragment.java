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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.topcvrecruiter.API.ApiDashboardService;


import com.example.topcvrecruiter.Adapter.DashboardApplicantAdapter;
import com.example.topcvrecruiter.NumberApplicantActivity;
import com.example.topcvrecruiter.NumberJobOfRecruiterActivity;
import com.example.topcvrecruiter.NumberResumeActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.model.Applicant;
import com.example.topcvrecruiter.model.CV;
import com.example.topcvrecruiter.model.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {
    private static final int REQUEST_CODE = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RecruiterId = 5;

    private TextView applicantCountTextView;
    private TextView jobCountTextView;
    private TextView recruitingRateTextView;
    private TextView resumeCountTextView;

    private RecyclerView applicantsRecyclerView;
    private DashboardApplicantAdapter dashboardAdapter;
    private ApiDashboardService apiDashboardService;

    private CardView applicantCardView;
    private CardView jobCardView;
    private CardView rateCardView;
    private CardView resumeCardView;
    private int totalAccepted = 0;
    private int totalRejected = 0;
    float rate;
    private ActivityResultLauncher<Intent> applicantDetailLauncher;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        apiDashboardService = ApiDashboardService.apiDashboardService;
//        applicantDetailLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
//                        Intent data = result.getData();
//                        int success = data.getIntExtra("rateSuccess", 0);
//                        int fail = data.getIntExtra("rateFail", 0);
//
//                        rateSuccess += success;
//                        rateFail += fail;
//
//                        recruitmentRate = (float) rateSuccess / (rateSuccess + rateFail);
//                        recruitingRateTextView.setText(String.format(Locale.getDefault(), "%.2f%%", recruitmentRate * 100));
//                    }
//                }
//        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Card View TextView
        applicantCountTextView = view.findViewById(R.id.electricity_amount);
        jobCountTextView = view.findViewById(R.id.job_count);
        recruitingRateTextView = view.findViewById(R.id.recruiting_rate);
        resumeCountTextView = view.findViewById(R.id.resume_amount);

        //Card View onclick view
        applicantCardView = view.findViewById(R.id.applicant_cardView);
        jobCardView = view.findViewById(R.id.job_cardView);
        resumeCardView = view.findViewById((R.id.resume_cardView));
        rateCardView = view.findViewById(R.id.rate_cardView);

        applicantsRecyclerView = view.findViewById(R.id.aplicants_Recycler_View);
        applicantsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dashboardAdapter = new DashboardApplicantAdapter(this.getContext(), new ArrayList<>());
        applicantsRecyclerView.setAdapter(dashboardAdapter);

        fetchDashboardData(RecruiterId);

        applicantCardView.setOnClickListener(view1 -> fetchListApplicants(RecruiterId));
        //jobCardView.setOnClickListener(view1 -> fetchListJobs(RecruiterId));
        jobCardView.setOnClickListener(view1 -> fetchListAccepted(RecruiterId));
        //resumeCardView.setOnClickListener(view1 -> fetchListResumes(RecruiterId));
        resumeCardView.setOnClickListener(view1 -> fetchListRejected(RecruiterId));

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("RecruitmentRate", "onActivityResult called"); // Kiểm tra xem có vào đây không
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            int success = data.getIntExtra("rateSuccess", 0);
            int fail = data.getIntExtra("rateFail", 0);

            totalAccepted += success;
            totalRejected += fail;

            calculateAndDisplayRate();
        }
    }

    private void calculateAndDisplayRate() {
        if (totalAccepted + totalRejected > 0) { // Kiểm tra tránh chia cho 0
            float rate = (float) totalAccepted / (totalAccepted + totalRejected);
            recruitingRateTextView.setText(String.format(Locale.getDefault(), "%.2f%%", rate * 100)); // Cập nhật tỷ lệ vào TextView
        } else {
            recruitingRateTextView.setText("0%"); // Nếu không có ứng viên nào
        }
    }

    private void fetchListApplicants(int recruiterId) {
        apiDashboardService.getListApplicants(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Applicant>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Applicant> applicants) {
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

    private void fetchListAccepted(int recruiterId) {
        apiDashboardService.getAcceptedApplicants(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Applicant>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Applicant> applicants) {
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
                .subscribe(new Observer<List<Applicant>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Applicant> applicants) {
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

    private  void  fetchListJobs(int recruiterId) {
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
    private  void  fetchListResumes(int recruiterId){
        apiDashboardService.getListResume(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CV>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<CV> resumes) {
                        Intent intent = new Intent(getContext(), NumberResumeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("resumeList", new ArrayList<>(resumes));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("DashboardFragment", "Error fetching resumes list", e);
                    }

                    @Override
                    public void onComplete() {

                    }

                });
    };

    @SuppressLint("CheckResult")
    private void fetchDashboardData(int recruiterId) {

        //------------------Applicant------------------------//
        apiDashboardService.getListApplicants(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(applicants -> {
                    int applicantCount = applicants.size(); // Đếm số lượng item trong danh sách
                    applicantCountTextView.setText(String.valueOf(applicantCount)); // Hiển thị số lượng ứng viên lên TextView
                }, throwable -> {
                    // Xử lý lỗi nếu có
                    Log.e("API Error", "Error fetching applicants", throwable);
                });
//        //------------------Job------------------------//
//        apiDashboardService.getListJobs(recruiterId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(jobs -> {
//                    int jobsCount = jobs.size(); // Đếm số lượng item trong danh sách
//                    jobCountTextView.setText(String.valueOf(jobsCount)); // Hiển thị số lượng ứng viên lên TextView
//                }, throwable -> {
//                    // Xử lý lỗi nếu có
//                    Log.e("API Error", "Error fetching applicants", throwable);
//                });
//
//        //------------------Resume------------------------//
//        apiDashboardService.getListResume(recruiterId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(resumes -> {
//                    int resumesCount = resumes.size(); // Đếm số lượng item trong danh sách
//                    resumeCountTextView.setText(String.valueOf(resumesCount)); // Hiển thị số lượng ứng viên lên TextView
//                }, throwable -> {
//                    // Xử lý lỗi nếu có
//                    Log.e("API Error", "Error fetching applicants", throwable);
//                });

        //------------------Rate------------------------//
        apiDashboardService.getAcceptedApplicants(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accepted -> {
                    totalAccepted = accepted.size(); // Đếm số lượng item trong danh sách
                    jobCountTextView.setText(String.valueOf(totalAccepted));
                    calculateAndDisplayRate();
                }, throwable -> {
                    // Xử lý lỗi nếu có
                    Log.e("API Error", "Error fetching applicants", throwable);
                });

        apiDashboardService.getRejectedApplicants(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rejected -> {
                    totalRejected = rejected.size(); // Đếm số lượng item trong danh sách
                    resumeCountTextView.setText(String.valueOf(totalRejected));
                    calculateAndDisplayRate();
                }, throwable -> {
                    // Xử lý lỗi nếu có
                    Log.e("API Error", "Error fetching applicants", throwable);
                });

        //------------------Suggest------------------------//
        apiDashboardService.getListSuggestedApplicants(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Applicant>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Applicant> applicants) {
                        dashboardAdapter.setListApplicant(applicants);
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
}