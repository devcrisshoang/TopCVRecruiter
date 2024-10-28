package com.example.topcvrecruiter.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.model.CV;

import java.util.List;

public class DashboardResumeAdapter extends RecyclerView.Adapter<DashboardResumeAdapter.DashboardViewHolder>{
    private List<CV> listCV;
    public DashboardResumeAdapter(List<CV> listCV){
        this.listCV = listCV;
    }
    public void setListCV(List<CV> listCV) {
        this.listCV = listCV;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DashboardResumeAdapter.DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resume, parent, false);
        return new DashboardResumeAdapter.DashboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardResumeAdapter.DashboardViewHolder holder, int position) {
        CV cv = listCV.get(position);
        if(cv == null) return;
        holder.applicantNameTextView.setText(cv.getName());
        holder.phoneNumberTextView.setText(cv.getPhone_Number());
        holder.emailTextView.setText(cv.getEmail());
        holder.educationTextView.setText(cv.getEducation());
        holder.skillsTextView.setText(cv.getSkills());
        holder.certificateTextView.setText(cv.getCertifications());
        holder.jobApplyingTextView.setText(cv.getPosition());

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class DashboardViewHolder extends RecyclerView.ViewHolder{

        private TextView applicantNameTextView;
        private TextView phoneNumberTextView;
        private TextView emailTextView;
        private TextView educationTextView;
        private TextView skillsTextView;
        private TextView certificateTextView;
        private TextView jobApplyingTextView;
        private TextView introductionTextView;
        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantNameTextView = itemView.findViewById(R.id.resume_applicantNameTextView);
            phoneNumberTextView = itemView.findViewById(R.id.resume_phoneNumberTextView);
            emailTextView = itemView.findViewById(R.id.resume_emailTextView);
            educationTextView = itemView.findViewById(R.id.resume_educationTextView);
            skillsTextView = itemView.findViewById(R.id.resume_skillsTextView);
            certificateTextView = itemView.findViewById(R.id.resume_certificateTextView);
            jobApplyingTextView = itemView.findViewById(R.id.resume_jobApplyingTextView);

        }
    }
}
