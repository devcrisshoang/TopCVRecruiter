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

    private int userIdRecruiter;
    private int mainUserId;
    private int applicantUserId;
    private int userIdContact;

    private TextView friend_name;

    private String applicantName;
    private String applicantNameContact;

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
        mainUserId = getIntent().getIntExtra("mainUserId", 0);
        applicantUserId = getIntent().getIntExtra("applicantUserId",0);
        applicantName = getIntent().getStringExtra("applicantName");

        applicantNameContact = getIntent().getStringExtra("applicantNameContact");
        userIdContact = getIntent().getIntExtra("userIdContact",0);
        userIdRecruiter = getIntent().getIntExtra("userIdRecruiter",0);

        if(getIntent().getIntExtra("mainUserId", 0) == 0){
            friend_name.setText(applicantNameContact);
            messageList = new ArrayList<>();
            messengerShowAdapter = new MessengerShowAdapter(messageList, userIdRecruiter, userIdContact);
        } else {
            friend_name.setText(applicantName);
            messageList = new ArrayList<>();
            messengerShowAdapter = new MessengerShowAdapter(messageList, mainUserId,applicantUserId);
        }

        setUserId();

        MessageShowRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MessageShowRecyclerView.setAdapter(messengerShowAdapter);

    }

    private void setUserId(){
        if (mainUserId == 0) {
            getAPIData(userIdContact,userIdRecruiter);
        }else {
            getAPIData(applicantUserId,mainUserId);
        }
    }

    private void sendMessage() {
        String messageContent = input_message_edittext.getText().toString().trim();
        String currentTime = DateTimeUtils.getCurrentTime();

        if (!messageContent.isEmpty()) {
            Message newMessage = new Message();
            if(userIdRecruiter != 0 && userIdContact != 0){
                newMessage.setSender_ID(userIdRecruiter);
                newMessage.setReceiver_ID(userIdContact);
                newMessage.setContent(messageContent);
                newMessage.setStatus(false);
                newMessage.setSend_Time(currentTime);
            }
            else {
                newMessage.setSender_ID(mainUserId);
                newMessage.setReceiver_ID(applicantUserId);
                newMessage.setContent(messageContent);
                newMessage.setStatus(false);
                newMessage.setSend_Time(currentTime);
            }
            ApiMessageService.apiMessageService.postMessage(newMessage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Message>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull Message messageResponse) {
                            input_message_edittext.setText("");
                            setUserId();
                            Toast.makeText(MessageActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            e.printStackTrace();
                            Toast.makeText(MessageActivity.this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Toast.makeText(MessageActivity.this, "Message content cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAPIData(int MainID, int SubID) {
        ApiMessageService.apiMessageService.getAllMessageByTwoUserId(MainID, SubID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Message>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull List<Message> messages) {
                        messageList.clear();
                        messageList.addAll(messages);
                        messengerShowAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        Log.e("MessageActivity","Call API error");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("MessageActivity","Call API successful");
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
