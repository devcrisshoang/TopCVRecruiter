package com.example.topcvrecruiter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.model.Job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private List<Job> jobList;

    public JobAdapter(List<Job> jobList) {
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

        // Chuyển đổi chuỗi thời gian sang định dạng ngày tháng năm
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            Date createDate = inputFormat.parse(job.getCreate_Time());
            long createTimeInMillis = createDate.getTime();
            long currentTimeInMillis = System.currentTimeMillis();

            long timeDifference = currentTimeInMillis - createTimeInMillis;
            long minutesDifference = timeDifference / (60 * 1000); // Chuyển đổi sang phút
            long hoursDifference = timeDifference / (60 * 60 * 1000); // Chuyển đổi sang giờ

            if (minutesDifference < 60) {
                holder.jobTime.setText(minutesDifference + " minutes ago");
            } else if (hoursDifference < 24) {
                holder.jobTime.setText(hoursDifference + " hours ago");
            } else if (hoursDifference < 24 * 2) {
                holder.jobTime.setText("Yesterday");
            } else if (hoursDifference < 24 * 7) {
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                String dayOfWeek = dayFormat.format(createDate);
                holder.jobTime.setText(dayOfWeek);
            } else {
                String formattedDate = outputFormat.format(createDate);
                holder.jobTime.setText(formattedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder.jobTime.setText("Create Time: " + job.getCreate_Time());
        }
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

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
