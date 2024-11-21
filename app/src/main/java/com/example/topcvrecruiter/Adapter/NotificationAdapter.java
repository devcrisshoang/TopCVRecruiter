package com.example.topcvrecruiter.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Model.Notification;
import com.example.topcvrecruiter.Utils.DateTimeUtils;

import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList;
    private Context context;

    public NotificationAdapter(List<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        // Lấy chuỗi thời gian từ notification
        String timeString = notification.getTime();

        // Kiểm tra null trước khi định dạng lại thời gian
        String formattedTime = (timeString != null) ? DateTimeUtils.formatTimeAgo(timeString) : "No time available";

        // Đặt thời gian và nội dung cho TextView
        holder.textViewTime.setText(formattedTime);
        holder.textViewContent.setText(notification.getContent());
    }




    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    // ViewHolder để quản lý từng phần tử trong RecyclerView
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewContent;
        public TextView textViewTime;
        public ImageView company_logo;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.ActionDetailsOfRecruiterTextView);
            textViewTime = itemView.findViewById(R.id.TimeOfNotification);
            company_logo = itemView.findViewById(R.id.company_logo);
        }
    }
}
