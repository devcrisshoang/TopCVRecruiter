package com.example.topcvrecruiter.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.Model.Notification;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Utils.DateTimeUtils;

import java.util.List;
import java.util.Stack;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private final List<Notification> notificationStack;
    private final Context context;

    public NotificationAdapter(Stack<Notification> notificationStack, Context context) {
        this.notificationStack = notificationStack;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return (notificationStack.get(position) == null) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
            return new NotificationViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NotificationViewHolder) {
            Notification notification = notificationStack.get(position);
            NotificationViewHolder notificationViewHolder = (NotificationViewHolder) holder;
            String timeString = notification.getTime();
            String formattedTime = (timeString != null) ? DateTimeUtils.formatTimeAgo(timeString) : "No time available";
            notificationViewHolder.textViewTime.setText(formattedTime);
            notificationViewHolder.textViewContent.setText(notification.getContent());
        }
    }

    public void addFooterLoading() {
        if (!notificationStack.contains(null)) {
            notificationStack.add(null);
            notifyItemInserted(notificationStack.size() - 1);
        }
    }

    public void removeFooterLoading() {
        int position = notificationStack.indexOf(null);
        if (position != -1) {
            notificationStack.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return notificationStack.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewContent;
        private final TextView textViewTime;
        private final ImageView company_logo;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.ActionDetailsOfRecruiterTextView);
            textViewTime = itemView.findViewById(R.id.TimeOfNotification);
            company_logo = itemView.findViewById(R.id.company_logo);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
