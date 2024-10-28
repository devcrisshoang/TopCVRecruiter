package com.example.topcvrecruiter.Fragment;

import android.content.Intent;
import android.os.Bundle;

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
import com.example.topcvrecruiter.model.Jobs;

import java.util.ArrayList;
import java.util.List;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
        apiDashboardService = ApiDashboardService.apiDashboardService;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_dashboard, container, false);
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

        dashboardAdapter = new DashboardApplicantAdapter(new ArrayList<>());
        applicantsRecyclerView.setAdapter(dashboardAdapter);

        fetchDashboardData(1);

        applicantCardView.setOnClickListener(view1 -> fetchListApplicants(1));
        jobCardView.setOnClickListener(view2 -> fetchListJobs(1));
        resumeCardView.setOnClickListener(view3 -> fetchListResumes(1));

        return view;
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

    private void fetchApplicantCountAndOpenActivity(int recruiterId) {
        apiDashboardService.getApplicantCount(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Integer applicantCount) {
                        // Pass applicant count to the new activity
                        Intent intent = new Intent(getContext(), NumberApplicantActivity.class);
                        intent.putExtra("applicant_count", applicantCount);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("API_ERROR", "Error fetching applicant count", e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void fetchDashboardData(int recruiterId) {
        apiDashboardService.getApplicantCount(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer applicantCount) {
                        applicantCountTextView.setText(String.valueOf(applicantCount));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("API_ERROR", "Error fetching applicant count", e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        apiDashboardService.getJobCount(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Integer jobCount) {
                        jobCountTextView.setText(String.valueOf(jobCount));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("API_ERROR", "Error fetching job count", e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        apiDashboardService.getApplicationRatio(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String ratio) {
                        recruitingRateTextView.setText(ratio);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("API_ERROR", "Error fetching recruiting rate", e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        apiDashboardService.getResumeCount(recruiterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }


                    @Override
                    public void onNext(String resumeCount) {
                        resumeCountTextView.setText(resumeCount);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("API_ERROR", "Error fetching suggested applicant count", e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
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