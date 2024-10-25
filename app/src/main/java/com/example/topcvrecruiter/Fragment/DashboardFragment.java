package com.example.topcvrecruiter.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.topcvrecruiter.API.ApiDashboardService;
import com.example.topcvrecruiter.R;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
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
    ApiDashboardService apiDashboardService;

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

        applicantCountTextView = view.findViewById(R.id.electricity_amount);
        jobCountTextView = view.findViewById(R.id.job_count);
        recruitingRateTextView = view.findViewById(R.id.recruiting_rate);
        resumeCountTextView = view.findViewById(R.id.resume_amount);
        
        fetchDashboardData();

        return view;
    }

    private void fetchDashboardData() {
        apiDashboardService.getApplicantCount(1)
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
        apiDashboardService.getJobCount(1)
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
        apiDashboardService.getApplicationRatio(1)
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
        apiDashboardService.getResumeCount(1)
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
    }
}