package com.example.topcvrecruiter.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Model.Message;

import java.util.List;

public class MessengerShowAdapter extends RecyclerView.Adapter<MessengerShowAdapter.MessengerViewHolder> {

    private List<Message> messageList;
    private int ID;

    public MessengerShowAdapter(List<Message> messageList, int ID) {
        this.messageList = messageList;
        this.ID = ID;
    }

    @NonNull
    @Override
    public MessengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_show_message, parent, false);
        return new MessengerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessengerViewHolder holder, int position) {
        Message message = messageList.get(position);
        Log.d("MessengerAdapter", "Message content: " + message.getContent() + ", Sender ID: " + message.getSender_ID());

        // Reset visibility trước khi áp dụng logic mới
        holder.me.setVisibility(View.VISIBLE);
        holder.other_people.setVisibility(View.VISIBLE);

        // Xử lý hiển thị dựa trên Sender_ID
        if (message.getSender_ID() == 9) {
            // Nếu Sender_ID = 9, hiển thị tin nhắn của chính người dùng
            holder.me.setText(message.getContent());
            holder.other_people.setVisibility(View.GONE);  // Ẩn tin nhắn của người khác
        } else if (message.getSender_ID() == ID) {
            // Nếu Sender_ID là người khác, hiển thị tin nhắn của họ
            holder.other_people.setText(message.getContent());
            holder.me.setVisibility(View.GONE);  // Ẩn tin nhắn của chính mình
        }
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // ViewHolder để quản lý từng phần tử trong RecyclerView
    public static class MessengerViewHolder extends RecyclerView.ViewHolder {
        public TextView other_people;
        public TextView me;

        public MessengerViewHolder(View itemView) {
            super(itemView);
            other_people = itemView.findViewById(R.id.other_people);
            me = itemView.findViewById(R.id.me);
        }
    }
}
