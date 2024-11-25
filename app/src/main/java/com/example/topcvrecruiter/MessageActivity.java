package com.example.topcvrecruiter;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.API.ApiRecruiterService;
import com.example.topcvrecruiter.Adapter.MessengerShowAdapter;
import com.example.topcvrecruiter.API.ApiMessageService;
import com.example.topcvrecruiter.Model.Message;
import com.example.topcvrecruiter.Utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MessageActivity extends AppCompatActivity {

    private ImageButton back_button;

    private RecyclerView MessageShowRecyclerView;

    private MessengerShowAdapter messengerShowAdapter;

    private List<Message> messageList;

    private Disposable disposable;

    private ImageButton messenger_send_button;

    private EditText input_message_edittext;

    private int userId;
    private int user_id_applicant;

    private TextView friend_name;

    private String applicantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setWidget();

        setClick();

    }

    private void setClick(){

        back_button.setOnClickListener(view -> finish());

        messenger_send_button.setOnClickListener(v -> sendMessage());
    }

    private void setWidget(){
        back_button = findViewById(R.id.back_button);
        MessageShowRecyclerView = findViewById(R.id.MessageShowRecyclerView);
        messenger_send_button = findViewById(R.id.messenger_send_button);
        input_message_edittext = findViewById(R.id.input_message_edittext);
        friend_name = findViewById(R.id.friend_name);

        userId = getIntent().getIntExtra("userId", -1);
        if (userId != -1) {
            getAPIData(userId);
        }
        applicantName = getIntent().getStringExtra("userId");
        getApplicantName(userId);
        user_id_applicant = getIntent().getIntExtra("user_id",0);
        messageList = new ArrayList<>();

        messengerShowAdapter = new MessengerShowAdapter(messageList, userId);

        // Gán LayoutManager và Adapter cho RecyclerView
        MessageShowRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MessageShowRecyclerView.setAdapter(messengerShowAdapter);

    }

    private void getApplicantName(int userId) {
        ApiRecruiterService.ApiRecruiterService.getRecruiterByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recruiter -> {
                            if (recruiter != null) {
                                friend_name.setText(applicantName);

                                Log.d("MessengerAdapter", "Fetched applicant name: " + applicantName);
                            } else {
                                friend_name.setText("Unknown User"); // Hoặc xử lý lỗi nếu không có dữ liệu
                            }
                        },
                        throwable -> {
                            Log.e("MessengerAdapter", "Error fetching applicant name: " + throwable.getMessage());
                            Toast.makeText(this, "Failed to load applicant name", Toast.LENGTH_SHORT).show();
                        }
                );

    }
    // Hàm gửi tin nhắn
    private void sendMessage() {
        String messageContent = input_message_edittext.getText().toString().trim();
        String currentTime = DateTimeUtils.getCurrentTime();

        if (!messageContent.isEmpty()) {
            // Tạo đối tượng Message mới
            Message newMessage = new Message(
                    0,  // ID tạm thời (server sẽ tự sinh)
                    userId,  // sender_ID là 9
                    user_id_applicant,  // receiver_ID
                    messageContent,
                    false,  // status giả định là "sent"
                    currentTime  // send_Time giả định, dùng thời gian hiện tại
            );

            // Gọi API để gửi tin nhắn
            ApiMessageService.apiMessageService.postMessage(newMessage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Message>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            // Quản lý Disposable nếu cần
                        }

                        @Override
                        public void onNext(@NonNull Message messageResponse) {
                            // Tin nhắn được gửi thành công, xóa nội dung trong input sau khi gửi
                            input_message_edittext.setText("");

                            // Sau khi gửi tin nhắn thành công, gọi lại API để cập nhật toàn bộ tin nhắn
                            getAPIData(userId);
                            Toast.makeText(MessageActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            // Xử lý lỗi khi gửi tin nhắn thất bại
                            e.printStackTrace();
                            Toast.makeText(MessageActivity.this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            // Xử lý sau khi quá trình gửi hoàn tất
                        }
                    });
        } else {
            Toast.makeText(MessageActivity.this, "Message content cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAPIData(int userId) {
        // Thay vì gọi getAllMessages, bạn gọi getAllMessageByTwoUserId với userId hiện tại và userId bạn nhận từ Intent
        ApiMessageService.apiMessageService.getAllMessageByTwoUserId(9, userId)  // 9 là ID của người dùng hiện tại
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Message>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull List<Message> messages) {
                        // Log để kiểm tra
                        Log.d("MessageActivity", "Received messages: " + messages.toString());

                        // Cập nhật danh sách và thông báo adapter rằng dữ liệu đã thay đổi
                        messageList.clear();  // Xóa dữ liệu cũ
                        messageList.addAll(messages);  // Thêm dữ liệu mới
                        messengerShowAdapter.notifyDataSetChanged();  // Thông báo dữ liệu thay đổi
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(MessageActivity.this, "Call API error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MessageActivity.this, "Call API successful", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
