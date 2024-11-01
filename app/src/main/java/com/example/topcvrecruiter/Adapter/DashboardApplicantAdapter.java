package com.example.topcvrecruiter.Adapter;

import static com.github.dhaval2404.imagepicker.ImagePicker.REQUEST_CODE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.ApplicantDetailActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.model.Applicant;
import com.example.topcvrecruiter.model.CV;

import java.util.ArrayList;
import java.util.List;

public class DashboardApplicantAdapter extends RecyclerView.Adapter<DashboardApplicantAdapter.DashboardViewHolder>{
    private List<Applicant> listApplicant;
    private  List<CV> listCV;
    public DashboardApplicantAdapter(List<Applicant> listApplicant){
        this.listApplicant = listApplicant;
    }
    public void setListApplicant(List<Applicant> listApplicant) {
        this.listApplicant = listApplicant;
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
        Applicant applicant = listApplicant.get(position);
        if(applicant == null) return;

        holder.applicantNameTextView.setText(applicant.getApplicant_Name());
        holder.applicantPhoneTextView.setText(applicant.getPhone_number());
        holder.applicantEmailTextView.setText(applicant.getEmail());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), ApplicantDetailActivity.class);
            intent.putExtra("applicant_id", applicant.getId());
            ((Activity) holder.itemView.getContext()).startActivityForResult(intent, REQUEST_CODE);
        });

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
        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantNameTextView = itemView.findViewById(R.id.ActionOfRecruiterTextView);
            applicantPhoneTextView = itemView.findViewById(R.id.ActionDetailsOfRecruiterTextView);
            applicantEmailTextView = itemView.findViewById(R.id.ActionDetailsOfEmailRecruiterTextView);
        }
    }

}
