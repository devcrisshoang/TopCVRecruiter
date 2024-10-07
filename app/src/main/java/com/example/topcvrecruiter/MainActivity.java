package com.example.topcvrecruiter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.topcvrecruiter.Fragment.AccountFragment;
import com.example.topcvrecruiter.Fragment.DashboardFragment;
import com.example.topcvrecruiter.Fragment.MessengerFragment;
import com.example.topcvrecruiter.Fragment.NotificationFragment;
import com.example.topcvrecruiter.Fragment.PostingFragment;

public class MainActivity extends AppCompatActivity {
    private LinearLayout layout_header;
    private EditText search_edit_text;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_PROFILE = 1;
    private static final int FRAGMENT_NOTIFICATION = 2;
    private static final int FRAGMENT_ACCOUNT = 3;
    private static final int FRAGMENT_MESSENGER = 4;
    private int currentFragment = FRAGMENT_HOME;
    private ImageButton Dashboard, Posting, messengerButton, notificationButton, accountButton;
    private TextView Dashboard_textview,Posting_Textview,Messenger_Textview,Notification_Textview,Account_Textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        layout_header = findViewById(R.id.layout_header);
        search_edit_text = findViewById(R.id.search_edit_text);
        Dashboard = findViewById(R.id.Dashboard);
        Posting = findViewById(R.id.Posting);
        messengerButton = findViewById(R.id.Messenger);
        notificationButton = findViewById(R.id.Notification);
        accountButton = findViewById(R.id.Account);
        Dashboard_textview = findViewById(R.id.Dashboard_textview);
        Posting_Textview = findViewById(R.id.Posting_Textview);
        Messenger_Textview = findViewById(R.id.Messenger_Textview);
        Notification_Textview = findViewById(R.id.Notification_Textview);
        Account_Textview = findViewById(R.id.Account_Textview);
        replaceFragment(new DashboardFragment());
        setImageButtonColor(this,Dashboard, R.color.green_color);
        setImageButtonColor(this,Posting, R.color.black);
        setImageButtonColor(this,messengerButton, R.color.black);
        setImageButtonColor(this,notificationButton, R.color.black);
        setImageButtonColor(this,accountButton, R.color.black);

        Dashboard.setOnClickListener(view -> {
            setImageButtonColor(this,Dashboard, R.color.green_color);
            setImageButtonColor(this,Posting, R.color.black);
            setImageButtonColor(this,messengerButton, R.color.black);
            setImageButtonColor(this,notificationButton, R.color.black);
            setImageButtonColor(this,accountButton, R.color.black);
            Dashboard_textview.setTextColor(getResources().getColor(R.color.green_color));
            Posting_Textview.setTextColor(getResources().getColor(R.color.black));
            Messenger_Textview.setTextColor(getResources().getColor(R.color.black));
            Notification_Textview.setTextColor(getResources().getColor(R.color.black));
            Account_Textview.setTextColor(getResources().getColor(R.color.black));

            if(currentFragment != FRAGMENT_HOME){
                replaceFragment(new DashboardFragment());
                currentFragment = FRAGMENT_HOME;
                layout_header.setVisibility(View.VISIBLE);
            }
        });
        Posting.setOnClickListener(view -> {
            setImageButtonColor(this,Posting, R.color.green_color);
            setImageButtonColor(this,Dashboard, R.color.black);
            setImageButtonColor(this,messengerButton, R.color.black);
            setImageButtonColor(this,notificationButton, R.color.black);
            setImageButtonColor(this,accountButton, R.color.black);
            Dashboard_textview.setTextColor(getResources().getColor(R.color.black));
            Posting_Textview.setTextColor(getResources().getColor(R.color.green_color));
            Messenger_Textview.setTextColor(getResources().getColor(R.color.black));
            Notification_Textview.setTextColor(getResources().getColor(R.color.black));
            Account_Textview.setTextColor(getResources().getColor(R.color.black));
            if(currentFragment != FRAGMENT_PROFILE){
                replaceFragment(new PostingFragment());
                currentFragment = FRAGMENT_PROFILE;
                layout_header.setVisibility(View.GONE);

            }
        });
        messengerButton.setOnClickListener(v -> {
            setImageButtonColor(this,messengerButton, R.color.green_color);
            setImageButtonColor(this,Dashboard, R.color.black);
            setImageButtonColor(this,Posting, R.color.black);
            setImageButtonColor(this,notificationButton, R.color.black);
            setImageButtonColor(this,accountButton, R.color.black);
            Dashboard_textview.setTextColor(getResources().getColor(R.color.black));
            Posting_Textview.setTextColor(getResources().getColor(R.color.black));
            Messenger_Textview.setTextColor(getResources().getColor(R.color.green_color));
            Notification_Textview.setTextColor(getResources().getColor(R.color.black));
            Account_Textview.setTextColor(getResources().getColor(R.color.black));
            if(currentFragment != FRAGMENT_MESSENGER){
                replaceFragment(new MessengerFragment());
                currentFragment = FRAGMENT_MESSENGER;
                layout_header.setVisibility(View.GONE);

            }
        });
        notificationButton.setOnClickListener(v -> {
            setImageButtonColor(this,notificationButton, R.color.green_color);
            setImageButtonColor(this,Dashboard, R.color.black);
            setImageButtonColor(this,Posting, R.color.black);
            setImageButtonColor(this,messengerButton, R.color.black);
            setImageButtonColor(this,accountButton, R.color.black);
            Dashboard_textview.setTextColor(getResources().getColor(R.color.black));
            Posting_Textview.setTextColor(getResources().getColor(R.color.black));
            Messenger_Textview.setTextColor(getResources().getColor(R.color.black));
            Notification_Textview.setTextColor(getResources().getColor(R.color.green_color));
            Account_Textview.setTextColor(getResources().getColor(R.color.black));
            if(currentFragment != FRAGMENT_NOTIFICATION){
                replaceFragment(new NotificationFragment());
                currentFragment = FRAGMENT_NOTIFICATION;
                layout_header.setVisibility(View.GONE);
            }
        });
        accountButton.setOnClickListener(v -> {
            setImageButtonColor(this,accountButton, R.color.green_color);
            setImageButtonColor(this,Dashboard, R.color.black);
            setImageButtonColor(this,Posting, R.color.black);
            setImageButtonColor(this,messengerButton, R.color.black);
            setImageButtonColor(this,notificationButton, R.color.black);
            Dashboard_textview.setTextColor(getResources().getColor(R.color.black));
            Posting_Textview.setTextColor(getResources().getColor(R.color.black));
            Messenger_Textview.setTextColor(getResources().getColor(R.color.black));
            Notification_Textview.setTextColor(getResources().getColor(R.color.black));
            Account_Textview.setTextColor(getResources().getColor(R.color.green_color));
            if(currentFragment != FRAGMENT_ACCOUNT){
                replaceFragment(new AccountFragment());
                currentFragment = FRAGMENT_ACCOUNT;
                layout_header.setVisibility(View.GONE);
            }
        });
        search_edit_text.setOnClickListener(view -> {
//            Intent intent = new Intent(this, SearchActivity.class);
//            startActivity(intent);
        });
    }
    public static void setImageButtonColor(Context context, ImageButton button, int colorResId) {
        int color = ContextCompat.getColor(context, colorResId); // Lấy màu từ resources
        button.setColorFilter(color, PorterDuff.Mode.SRC_IN);

    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.news,fragment);
        transaction.commit();
    }
}