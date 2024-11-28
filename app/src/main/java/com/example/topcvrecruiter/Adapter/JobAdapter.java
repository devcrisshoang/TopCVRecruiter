package com.example.topcvrecruiter.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Model.Job;
import com.example.topcvrecruiter.JobDetailActivity;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private List<Job> jobList;

    private final Context context;

    public JobAdapter(Context context, List<Job> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.jobName.setText(job.getJob_Name());
        holder.jobAddress.setText(job.getWorking_Address());
        holder.jobTime.setText(job.getCreate_Time());

        holder.jobTime.setText("");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, JobDetailActivity.class);
            intent.putExtra("jobId", job.getId());
            intent.putExtra("id_Recruiter", job.getiD_Recruiter());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Job> jobs) {
        this.jobList = jobs;
        notifyDataSetChanged();
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobName, jobAddress, jobTime;

        JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.job_name);
            jobAddress = itemView.findViewById(R.id.job_address);
            jobTime = itemView.findViewById(R.id.job_time);
        }
    }
}
