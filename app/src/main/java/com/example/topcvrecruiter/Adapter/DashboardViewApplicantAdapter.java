package com.example.topcvrecruiter.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.API.ApiApplicantService;
import com.example.topcvrecruiter.Model.ApplicantJob;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.ResumeActivity;
import com.example.topcvrecruiter.Utils.DateTimeUtils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DashboardViewApplicantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    private List<ApplicantJob> listApplicant;

    private final ActivityResultLauncher<Intent> applicantDetailLauncher;

    private boolean isLoadingAdd;

    private final int recruiterId;
    private final int userId;
    private int applicantUserId;

    private String jobName;

    public DashboardViewApplicantAdapter(ActivityResultLauncher<Intent> applicantDetailLauncher, int recruiterId, int userId, String jobName){
        this.applicantDetailLauncher = applicantDetailLauncher;
        this.recruiterId = recruiterId;
        this.userId = userId;
        this.jobName = jobName;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListApplicant(List<ApplicantJob> listApplicant) {
        this.listApplicant = listApplicant;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (listApplicant != null && position == listApplicant.size()-1 && isLoadingAdd){
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_applicant, parent, false);
            return new DashboardViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM){

        ApplicantJob applicant = listApplicant.get(position);
        if (applicant == null) return;

        DashboardViewHolder dashboardViewHolder = (DashboardViewHolder) holder;
        dashboardViewHolder.applicantNameTextView.setText(applicant.getApplicant_Name());
        dashboardViewHolder.applicantPhoneTextView.setText(applicant.getPhone_Number());
        dashboardViewHolder.applicantEmailTextView.setText(applicant.getEmail());
        dashboardViewHolder.applicant_avatar.setImageResource(R.drawable.account_ic);
        String time = DateTimeUtils.formatTimeAgo(applicant.getTime());
        dashboardViewHolder.applicantTimeTextView.setText(time);

        dashboardViewHolder.itemView.setOnClickListener(view -> {
            ApiApplicantService.ApiApplicantService.getApplicantById(applicant.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            applicants -> {
                                if (applicants != null) {
                                    applicantUserId = applicants.getiD_User();
                                    Intent intent = new Intent(holder.itemView.getContext(), ResumeActivity.class);
                                    intent.putExtra("id_Recruiter", recruiterId);
                                    intent.putExtra("applicant_id", applicant.getId());
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("applicantUserId",applicants.getiD_User());
                                    intent.putExtra("jobName",jobName);
                                    applicantDetailLauncher.launch(intent);
                                    Log.e("ApplicantDetailActivity","applicantUserId: " + applicantUserId);
                                } else {
                                    Log.e("ApplicantDetailActivity","Recruiter not found");
                                }
                            },
                            throwable -> {
                                Log.e("ApplicantDetailActivity", "Error fetching recruiter: " + throwable.getMessage());
                            }
                    );
            });
        }
    }

    @Override
    public int getItemCount() {
        if(listApplicant != null) return listApplicant.size();
        return 0;
    }

    public static class DashboardViewHolder extends RecyclerView.ViewHolder{
        private ImageView applicant_avatar;
        private final TextView applicantNameTextView;
        private final TextView applicantPhoneTextView;
        private final TextView applicantEmailTextView;
        private final TextView applicantTimeTextView;
        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantNameTextView = itemView.findViewById(R.id.ActionOfRecruiterTextView);
            applicantPhoneTextView = itemView.findViewById(R.id.ActionDetailsOfRecruiterTextView);
            applicantEmailTextView = itemView.findViewById(R.id.ActionDetailsOfEmailRecruiterTextView);
            applicantTimeTextView = itemView.findViewById(R.id.TimeOfNotification);
            applicant_avatar = itemView.findViewById(R.id.applicant_avatar);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder{
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            ProgressBar progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        listApplicant.add(new ApplicantJob(0, "", "", "", "", "", "", false, ""));
        notifyItemInserted(listApplicant.size() - 1);
    }

    public void removeFooterLoading() {
        if(isLoadingAdd)
            isLoadingAdd = false;

        int position = listApplicant.size() - 1;
        ApplicantJob applicantJob = listApplicant.get(position);
        if (applicantJob != null) {
            listApplicant.remove(position);
            notifyItemRemoved(position);
        }
    }

}
