package com.example.topcvrecruiter.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.ApplicantDetailActivity;
import com.example.topcvrecruiter.Model.ApplicantJob;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.ResumeActivity;
import com.example.topcvrecruiter.Utils.DateTimeUtils;

import java.util.List;

public class DashboardRejectedApplicantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    private List<ApplicantJob> listApplicant;

    private final ActivityResultLauncher<Intent> applicantDetailLauncher;

    private boolean isLoadingAdd;

    private final int recruiterId;
    private final int userId;

    public DashboardRejectedApplicantAdapter(ActivityResultLauncher<Intent> applicantDetailLauncher, int recruiterId, int userId){
        this.applicantDetailLauncher = applicantDetailLauncher;
        this.recruiterId = recruiterId;
        this.userId = userId;
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM){

        ApplicantJob applicant = listApplicant.get(position);
        if (applicant == null) return;

        DashboardViewHolder dashboardViewHolder = (DashboardViewHolder) holder;
        dashboardViewHolder.applicantNameTextView.setText(applicant.getApplicant_Name());
        dashboardViewHolder.applicantPhoneTextView.setText(applicant.getPhone_Number());
        dashboardViewHolder.applicantEmailTextView.setText(applicant.getEmail());
        String time = DateTimeUtils.formatTimeAgo(applicant.getTime());
        dashboardViewHolder.applicantTimeTextView.setText(time);

        dashboardViewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), ApplicantDetailActivity.class);
            intent.putExtra("id_Recruiter", recruiterId);
            intent.putExtra("applicant_id", applicant.getId());
            intent.putExtra("userId", userId);
            intent.putExtra("check",true);
            applicantDetailLauncher.launch(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if(listApplicant != null) return listApplicant.size();
        return 0;
    }

    public static class DashboardViewHolder extends RecyclerView.ViewHolder{

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
