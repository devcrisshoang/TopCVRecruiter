package com.example.topcvrecruiter.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.JobDetailActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.model.Article;
import com.example.topcvrecruiter.model.Job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AllJobAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private List<Job> jobs;

    public AllJobAdapter(Context context,List<Job> jobs) {
        this.jobs = jobs;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        // Kiểm tra xem item có phải là "footer loading" không
        return (jobs.get(position) == null) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job, parent, false);
            return new JobViewHolder(view);
        } else {
            // "footer loading" item
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof JobViewHolder) {
            Job job = jobs.get(position);
            JobViewHolder jobViewHolder = (JobViewHolder) holder;
            jobViewHolder.name.setText(job.getJob_Name());
            jobViewHolder.address.setText(job.getWorking_Address());

            // Thiết lập sự kiện khi nhấn vào item
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, JobDetailActivity.class);
                intent.putExtra("jobId", job.getId()); // Truyền jobId
                context.startActivity(intent);
            });

            // Định dạng thời gian
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

            try {
                Date createDate = inputFormat.parse(job.getCreate_Time());
                long createTimeInMillis = createDate.getTime();
                long currentTimeInMillis = System.currentTimeMillis();

                long timeDifference = currentTimeInMillis - createTimeInMillis;
                long minutesDifference = timeDifference / (60 * 1000);
                long hoursDifference = timeDifference / (60 * 60 * 1000);

                if (minutesDifference < 60) {
                    ((JobViewHolder) holder).createTime.setText(minutesDifference + " minutes ago");
                } else if (hoursDifference < 24) {
                    ((JobViewHolder) holder).createTime.setText(hoursDifference + " hours ago");
                } else if (hoursDifference < 24 * 2) {
                    ((JobViewHolder) holder).createTime.setText("Yesterday");
                } else if (hoursDifference < 24 * 7) {
                    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                    String dayOfWeek = dayFormat.format(createDate);
                    ((JobViewHolder) holder).createTime.setText(dayOfWeek);
                } else {
                    String formattedDate = outputFormat.format(createDate);
                    ((JobViewHolder) holder).createTime.setText(formattedDate);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((JobViewHolder) holder).createTime.setText("Create Time: " + job.getCreate_Time());
            }
        }

    }

    @Override
    public int getItemCount() {
        return jobs != null ? jobs.size() : 0;
    }

    // Thêm footer loading vào cuối RecyclerView
    public void addFooterLoading() {
        if (!jobs.contains(null)) {
            jobs.add(null);
            notifyItemInserted(jobs.size() - 1);
        }
    }

    // Loại bỏ footer loading sau khi dữ liệu được tải
    public void removeFooterLoading() {
        int position = jobs.indexOf(null);
        if (position != -1) {
            jobs.remove(position);
            notifyItemRemoved(position);
        }
    }

    // ViewHolder cho item bài viết
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

    // ViewHolder cho footer loading
    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
