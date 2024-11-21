package com.example.topcvrecruiter.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.API.ApiRecruiterService;
import com.example.topcvrecruiter.MessageActivity;
import com.example.topcvrecruiter.Model.Applicant;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.API.ApiMessageService;
import com.example.topcvrecruiter.Model.Message;
import com.example.topcvrecruiter.Model.User;
import com.example.topcvrecruiter.Utils.DateTimeUtils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MessengerAdapter extends RecyclerView.Adapter<MessengerAdapter.MessengerViewHolder> {

    private List<User> userList;
    private Context context;
    private int currentUserId = 9;  // Assuming 9 is the logged-in user's ID
    private Applicant applicant = new Applicant();

    public MessengerAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_messenger, parent, false);
        return new MessengerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessengerViewHolder holder, int position) {
        if (position >= userList.size()) {
            return;  // If position is invalid, skip
        }

        User user = userList.get(position);
        int userId = user.getId();

        // Fetch applicant name and latest message for this user
        getRecruiterName(userId, holder);  // Fetch and set the applicant name
        getLatestMessage(currentUserId, userId, holder);  // Fetch and set the latest message and timestamp

        // Handle item click to open the chat with the selected user
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessageActivity.class);
            intent.putExtra("userId",userId);
            context.startActivity(intent);
        });
    }

    // Method to get applicant name based on the user ID
    private void getRecruiterName(int userId, MessengerViewHolder holder) {
        ApiRecruiterService.ApiRecruiterService.getRecruiterByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recruiter -> {
                            if (recruiter != null) {
                                holder.sender_name.setText(recruiter.getRecruiterName());
                                Log.d("MessengerAdapter", "Fetched applicant name: " + recruiter.getRecruiterName());
                            } else {
                                holder.sender_name.setText("Unknown User"); // Hoặc xử lý lỗi nếu không có dữ liệu
                            }
                        },
                        throwable -> {
                            Log.e("MessengerAdapter", "Error fetching applicant name: " + throwable.getMessage());
                            Toast.makeText(context, "Failed to load applicant name", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    // Method to get the latest message between the current user (ID = 9) and another user
    private void getLatestMessage(int currentUserId, int otherUserId, MessengerViewHolder holder) {
        ApiMessageService.apiMessageService.getAllMessageByTwoUserId(currentUserId, otherUserId)
                .subscribeOn(Schedulers.io())  // Run in the background
                .observeOn(AndroidSchedulers.mainThread())  // Observe on the main thread
                .subscribe(
                        messages -> {
                            if (!messages.isEmpty()) {
                                // Get the latest message
                                Message latestMessage = messages.get(messages.size() - 1);
                                holder.message.setText(latestMessage.getContent());

                                // Format and set the timestamp
                                String time = latestMessage.getSend_Time();
                                if (time != null && !time.isEmpty()) {
                                    String formattedTime = DateTimeUtils.formatTimeAgo(time);
                                    holder.send_time.setText(formattedTime);
                                } else {
                                    holder.send_time.setText("No time available");
                                }
                            } else {
                                holder.message.setText("No messages");
                                holder.send_time.setText("No time available");
                            }
                        },
                        throwable -> {
                            Log.e("MessengerAdapter", "Error fetching messages: " + throwable.getMessage());
                            Toast.makeText(context, "Failed to load messages", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ViewHolder for managing each item in the RecyclerView
    public static class MessengerViewHolder extends RecyclerView.ViewHolder {
        public TextView sender_name;
        public TextView message;
        public TextView send_time;

        public MessengerViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            sender_name = itemView.findViewById(R.id.sender_name);
            send_time = itemView.findViewById(R.id.send_time);
        }
    }
}
