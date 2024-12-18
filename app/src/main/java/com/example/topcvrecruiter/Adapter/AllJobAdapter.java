package com.example.topcvrecruiter.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcvrecruiter.JobDetailActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Model.Job;
import com.example.topcvrecruiter.Utils.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AllJobAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private final Context context;

    private final List<Job> jobs;

    public AllJobAdapter(Context context,List<Job> jobs) {
        this.jobs = jobs;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return (jobs.get(position) == null) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
            return new JobViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof JobViewHolder) {
            Job job = jobs.get(position);
            JobViewHolder jobViewHolder = (JobViewHolder) holder;
            jobViewHolder.name.setText(job.getJob_Name());
            jobViewHolder.address.setText(job.getWorking_Address());
            String time = DateTimeUtils.formatTimeAgo(job.getApplication_Date()); // Error
            jobViewHolder.createTime.setText(time);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, JobDetailActivity.class);
                intent.putExtra("jobId", job.getId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return jobs != null ? jobs.size() : 0;
    }

    public void addFooterLoading() {
        if (!jobs.contains(null)) {
            jobs.add(null);
            notifyItemInserted(jobs.size() - 1);
        }
    }

    public void removeFooterLoading() {
        int position = jobs.indexOf(null);
        if (position != -1) {
            jobs.remove(position);
            notifyItemRemoved(position);
        }
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;
        TextView createTime;

        public JobViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.job_name);
            address = itemView.findViewById(R.id.job_address);
            createTime = itemView.findViewById(R.id.job_time);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
