package com.example.topcvrecruiter.Adapter;

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
import com.example.topcvrecruiter.model.ApplicantJob;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DasboardJobAdapter extends RecyclerView.Adapter<DasboardJobAdapter.DashboardViewHolder>{
    private List<Job> listJob;
    private ActivityResultLauncher<Intent> applicantDetailLauncher;
    private int applicantOfJob;
    public DasboardJobAdapter(ActivityResultLauncher<Intent> applicantDetailLauncher, List<Job> listJob) {
        this.applicantDetailLauncher = applicantDetailLauncher;
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
        if (job == null) return;

        holder.jobNameTextView.setText(job.getJob_Name());
        holder.createTimeTextView.setText(job.getCreate_Time());
        holder.numberJobTextView.setText(String.valueOf(applicantOfJob));

        // Xử lý sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            int jobId = job.getId(); // Lấy ID của công việc hiện tại
            int recruiterId = job.getiD_Recruiter();

            // Gọi API để lấy danh sách ứng viên cho công việc này
            ApiDashboardService.apiDashboardService.getListApplicantsForJob(recruiterId, jobId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<ApplicantJob>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            // Thêm xử lý khi bắt đầu gọi API, như hiện loading nếu cần
                        }

                        @Override
                        public void onNext(List<ApplicantJob> applicantList) {
                            // Tạo intent để chuyển sang NumberApplicantActivity
                            applicantOfJob = applicantList.size();
                            Intent intent = new Intent(holder.itemView.getContext(), NumberApplicantActivity.class);
                            intent.putExtra("applicantList", new ArrayList<>(applicantList));
                            applicantDetailLauncher.launch(intent);
                        }

                        @Override
                        public void onError(Throwable e) {
                            // Xử lý lỗi khi gọi API, như hiện thông báo cho người dùng
                            Log.e("API Error", "Error fetching applicants: " + e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            // Hoàn tất quá trình gọi API
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return listJob.size();
    }

    public class DashboardViewHolder extends RecyclerView.ViewHolder{

        private TextView jobNameTextView;
        private TextView createTimeTextView;
        private TextView numberJobTextView;
        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            jobNameTextView = itemView.findViewById(R.id.ActionOfRecruiterTextView);
            createTimeTextView = itemView.findViewById(R.id.ActionDetailsOfRecruiterTextView);
            numberJobTextView = itemView.findViewById(R.id.TimeOfNotification);
        }
    }

}
