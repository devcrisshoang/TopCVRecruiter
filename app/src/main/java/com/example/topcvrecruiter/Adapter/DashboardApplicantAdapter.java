package com.example.topcvrecruiter.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.ApplicantDetailActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.model.Applicant;

import java.util.List;

public class DashboardApplicantAdapter extends RecyclerView.Adapter<DashboardApplicantAdapter.DashboardViewHolder>{
    private static final int REQUEST_CODE = 1;

    private List<Applicant> listApplicant;
    private Context context;

    public DashboardApplicantAdapter(Context context, List<Applicant> listApplicant){
        this.context = context;
        this.listApplicant = listApplicant;
    }

    public void setListApplicant(List<Applicant> listApplicant) {
        this.listApplicant = listApplicant;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant, parent, false);
        return new DashboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DashboardViewHolder holder, int position) {
        Applicant applicant = listApplicant.get(position);
        if(applicant == null) return;

        holder.applicantNameTextView.setText(applicant.getApplicant_Name());
        holder.applicantPhoneTextView.setText(applicant.getPhone_number());
        holder.applicantEmailTextView.setText(applicant.getEmail());

        holder.itemView.setOnClickListener(view -> {

            Intent intent = new Intent(context, ApplicantDetailActivity.class);
            intent.putExtra("applicant_id", applicant.getId());
            ((Activity) context).startActivityForResult(intent, REQUEST_CODE);

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
        public DashboardViewHolder(View itemView) {
            super(itemView);
            applicantNameTextView = itemView.findViewById(R.id.ActionOfRecruiterTextView);
            applicantPhoneTextView = itemView.findViewById(R.id.ActionDetailsOfRecruiterTextView);
            applicantEmailTextView = itemView.findViewById(R.id.ActionDetailsOfEmailRecruiterTextView);

        }
    }

}
