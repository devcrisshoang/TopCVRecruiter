package com.example.topcvrecruiter.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Model.Job;

import java.util.List;

public class DasboardJobAdapter extends RecyclerView.Adapter<DasboardJobAdapter.DashboardViewHolder>{
    private List<Job> listJob;
    public DasboardJobAdapter(List<Job> listJob) {
        this.listJob = listJob;
    }
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
        if(job == null) return;
        holder.jobNameTextView.setText(job.getJob_Name());
        holder.createTimeTextView.setText(job.getCreate_Time());
    }

    @Override
    public int getItemCount() {
        return listJob.size();
    }

    public class DashboardViewHolder extends RecyclerView.ViewHolder{

        private TextView jobNameTextView;
        private TextView createTimeTextView;
        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            jobNameTextView = itemView.findViewById(R.id.ActionOfRecruiterTextView);
            createTimeTextView = itemView.findViewById(R.id.ActionDetailsOfRecruiterTextView);

        }
    }
}
