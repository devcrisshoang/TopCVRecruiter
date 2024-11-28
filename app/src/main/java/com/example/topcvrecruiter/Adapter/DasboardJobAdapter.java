package com.example.topcvrecruiter.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcvrecruiter.API.ApiDashboardService;
import com.example.topcvrecruiter.Model.Job;
import com.example.topcvrecruiter.NumberApplicantActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Model.ApplicantJob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DasboardJobAdapter extends RecyclerView.Adapter<DasboardJobAdapter.DashboardViewHolder>{
    private List<Job> listJob;

    private final ActivityResultLauncher<Intent> applicantDetailLauncher;

    private final Map<Integer, Integer> applicantCounts;

    int id_Recruiter;

    public DasboardJobAdapter(ActivityResultLauncher<Intent> applicantDetailLauncher, List<Job> listJob, Map<Integer, Integer> applicantCounts, int id_Recruiter) {
        this.applicantDetailLauncher = applicantDetailLauncher;
        this.listJob = listJob;
        this.applicantCounts = applicantCounts;
        this.id_Recruiter = id_Recruiter;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setListJob(List<Job> listJob) {
        this.listJob = listJob;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant, parent, false);
        return new DashboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        Job job = listJob.get(position);
        if (job == null) return;

        holder.jobNameTextView.setText(job.getJob_Name());
        holder.createTimeTextView.setText(job.getCreate_Time());
        holder.companyNameTextView.setText(job.getCompany_Name());

        int applicantCount = applicantCounts.getOrDefault(job.getId(), 0);
        holder.numberJobTextView.setText(String.valueOf(applicantCount));

        holder.itemView.setOnClickListener(v -> {
            int jobId = job.getId();
            int recruiterId = job.getiD_Recruiter();

            ApiDashboardService.apiDashboardService.getListApplicantsForJob(recruiterId, jobId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<ApplicantJob>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<ApplicantJob> applicantList) {

                            Intent intent = new Intent(holder.itemView.getContext(), NumberApplicantActivity.class);
                            intent.putExtra("applicantList", new ArrayList<>(applicantList));
                            intent.putExtra("id_Recruiter", id_Recruiter);
                            applicantDetailLauncher.launch(intent);
                        }

                        @Override
                        public void onError(Throwable e) {

                            Log.e("API Error", "Error fetching applicants: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return listJob.size();
    }

    public static class DashboardViewHolder extends RecyclerView.ViewHolder{

        private final TextView jobNameTextView;
        private final TextView createTimeTextView;
        private final TextView companyNameTextView;
        private final TextView numberJobTextView;
        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            jobNameTextView = itemView.findViewById(R.id.ActionOfRecruiterTextView);
            createTimeTextView = itemView.findViewById(R.id.ActionDetailsOfEmailRecruiterTextView);
            companyNameTextView = itemView.findViewById(R.id.ActionDetailsOfRecruiterTextView);
            numberJobTextView = itemView.findViewById(R.id.TimeOfNotification);
        }
    }
}
