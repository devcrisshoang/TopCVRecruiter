package com.example.topcvrecruiter.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.ApplicantDetailActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.model.ApplicantJob;

import java.util.List;

public class DashboardApplicantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    private List<ApplicantJob> listApplicant;
    private ActivityResultLauncher<Intent> applicantDetailLauncher;
    private boolean isLoadingAdd;

    public DashboardApplicantAdapter(ActivityResultLauncher<Intent> applicantDetailLauncher){
        this.applicantDetailLauncher = applicantDetailLauncher;
    }

    public void setListApplicant(List<ApplicantJob> listApplicant) {
        this.listApplicant = listApplicant;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (listApplicant != null && position == listApplicant.size()-1 && isLoadingAdd == true){
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant, parent, false);
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
        dashboardViewHolder.applicantTimeTextView.setText(applicant.getTime());

        dashboardViewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), ApplicantDetailActivity.class);
            intent.putExtra("applicant_id", applicant.getId());
            intent.putExtra("isAccepted", applicant.isAccepted());
            intent.putExtra("isRejected", applicant.isRejected());
            applicantDetailLauncher.launch(intent);
        });

    }
    }

    @Override
    public int getItemCount() {
        if(listApplicant != null) return listApplicant.size();
        return 0;
    }

    public class DashboardViewHolder extends RecyclerView.ViewHolder{

        private TextView applicantNameTextView;
        private TextView applicantPhoneTextView;
        private TextView applicantEmailTextView;
        private TextView applicantTimeTextView;
        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantNameTextView = itemView.findViewById(R.id.ActionOfRecruiterTextView);
            applicantPhoneTextView = itemView.findViewById(R.id.ActionDetailsOfRecruiterTextView);
            applicantEmailTextView = itemView.findViewById(R.id.ActionDetailsOfEmailRecruiterTextView);
            applicantTimeTextView = itemView.findViewById(R.id.TimeOfNotification);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        private ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        listApplicant.add(new ApplicantJob(0, "", "", "", "", "", "", false, "")); // Thêm một đối tượng ApplicantJob trống vào danh sách
        notifyItemInserted(listApplicant.size() - 1); // Thông báo rằng có một item mới được thêm
    }

    public void removeFooterLoading() {
        if(isLoadingAdd)
            isLoadingAdd = false;

        int position = listApplicant.size() - 1;
        ApplicantJob applicantJob = listApplicant.get(position);
        if (applicantJob != null) {
            listApplicant.remove(position);
            notifyItemRemoved(position); // Thông báo rằng item đã bị xóa
        }
    }

}
