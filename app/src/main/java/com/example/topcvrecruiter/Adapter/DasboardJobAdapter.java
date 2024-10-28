package com.example.topcvrecruiter.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.model.Applicant;
import com.example.topcvrecruiter.model.Job;
import com.example.topcvrecruiter.model.Jobs;

import java.util.List;

public class DasboardJobAdapter extends RecyclerView.Adapter<DasboardJobAdapter.DashboardViewHolder>{
    private List<Job> listJob;
    public DasboardJobAdapter(List<Job> listJob){
        this.listJob = listJob;
    }
    public void setListJob(List<Job> listJob) {
        this.listJob = listJob;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new DasboardJobAdapter.DashboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        Job job = listJob.get(position);
        if(job == null) return;
        holder.jobNameTextView.setText(job.getJob_Name());
        holder.experienceRequirementTextView.setText(job.getWorking_Experience_Require());
        holder.workingAddressTextView.setText(job.getWorking_Address());
        holder.createTimeTextView.setText(job.getCreate_Time().toString());
        holder.applicationDateTextView.setText(job.getApplication_Date().toString());
    }

    @Override
    public int getItemCount() {
        return listJob.size();
    }

    public class DashboardViewHolder extends RecyclerView.ViewHolder{

        private TextView jobNameTextView;
        private TextView experienceRequirementTextView;
        private TextView workingAddressTextView;
        private TextView createTimeTextView;
        private TextView applicationDateTextView;
        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            jobNameTextView = itemView.findViewById(R.id.job_jobNameTextView);
            experienceRequirementTextView = itemView.findViewById(R.id.job_experienceRequirementTextView);
            workingAddressTextView = itemView.findViewById(R.id.job_workingAddressTextView);
            createTimeTextView = itemView.findViewById(R.id.job_createTimeTextView);
            applicationDateTextView = itemView.findViewById(R.id.job_applicationDateTextView);

        }
    }
}
