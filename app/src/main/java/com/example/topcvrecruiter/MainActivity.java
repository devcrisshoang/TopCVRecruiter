package com.example.topcvrecruiter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
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
    private String recruiterName;
    private int id_User;      // Lưu trữ ID ứng viên
    private int id_Recruiter;
    private String phoneNumber;
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
        setWidget();
        openDashboardFragment(id_User);
        setDefaultColorButton();
        setClick();
    }

    private void setDefaultColorButton(){
        // Thiết lập fragment mặc định và màu sắc ban đầu cho nút
        setImageButtonColor(this, Dashboard, R.color.green_color);
        setImageButtonColor(this, Posting, R.color.black);
        setImageButtonColor(this, messengerButton, R.color.black);
        setImageButtonColor(this, notificationButton, R.color.black);
        setImageButtonColor(this, accountButton, R.color.black);
    }

    private void openDashboardFragment(int userId) {
        DashboardFragment newsFeedFragment = new DashboardFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", userId);
        bundle.putInt("id_Recruiter",id_Recruiter);
        newsFeedFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.news, newsFeedFragment)
                .commit();
    }

    private void setClick(){
        Dashboard.setOnClickListener(view -> selectFragment(FRAGMENT_HOME, new DashboardFragment(), layout_header, View.VISIBLE));
        Posting.setOnClickListener(view -> selectFragment(FRAGMENT_PROFILE, new PostingFragment(), layout_header, View.GONE));
        messengerButton.setOnClickListener(view -> selectFragment(FRAGMENT_MESSENGER, new MessengerFragment(), layout_header, View.GONE));
        notificationButton.setOnClickListener(view -> selectFragment(FRAGMENT_NOTIFICATION, new NotificationFragment(), layout_header, View.GONE));
        accountButton.setOnClickListener(view -> selectFragment(FRAGMENT_ACCOUNT, new AccountFragment(), layout_header, View.GONE));
        search_edit_text.setOnClickListener(view -> {
//            Intent searchIntent = new Intent(this, SearchActivity.class);
//            startActivity(searchIntent);
        });
    }

    private void selectFragment(int fragmentCode, Fragment fragment, LinearLayout layoutHeader, int headerVisibility) {
        if (currentFragment != fragmentCode) {
            // Kiểm tra fragment và truyền id_User vào bundle khi cần
            if (fragment instanceof AccountFragment || fragment instanceof PostingFragment || fragment instanceof MessengerFragment || fragment instanceof NotificationFragment || fragment instanceof DashboardFragment) {
                Bundle bundle = new Bundle();
                bundle.putString("applicantName", recruiterName); // chỉ cần thiết cho AccountFragment
                bundle.putInt("user_id", id_User); // truyền id_User cho tất cả các fragment
                bundle.putString("phoneNumber", phoneNumber); // chỉ cần thiết cho AccountFragment
                bundle.putInt("id_Recruiter",id_Recruiter);
                fragment.setArguments(bundle); // Đặt Bundle vào Fragment
            }
            replaceFragment(fragment); // Thay thế fragment hiện tại bằng fragment đã chọn
            currentFragment = fragmentCode;
            layoutHeader.setVisibility(headerVisibility);
            resetButtonColors();
            setImageButtonColor(this, getButtonForFragment(fragmentCode), R.color.green_color);
            getTextViewForFragment(fragmentCode).setTextColor(getResources().getColor(R.color.green_color));
        }
    }

    private ImageButton getButtonForFragment(int fragmentCode) {
        switch (fragmentCode) {
            case FRAGMENT_PROFILE:
                return Posting;
            case FRAGMENT_MESSENGER:
                return messengerButton;
            case FRAGMENT_NOTIFICATION:
                return notificationButton;
            case FRAGMENT_ACCOUNT:
                return accountButton;
            default:
                return Dashboard;
        }
    }

    private TextView getTextViewForFragment(int fragmentCode) {
        switch (fragmentCode) {
            case FRAGMENT_PROFILE:
                return Posting_Textview;
            case FRAGMENT_MESSENGER:
                return Messenger_Textview;
            case FRAGMENT_NOTIFICATION:
                return Notification_Textview;
            case FRAGMENT_ACCOUNT:
                return Account_Textview;
            default:
                return Dashboard_textview;
        }
    }

    private void resetButtonColors() {
        setImageButtonColor(this, Dashboard, R.color.black);
        setImageButtonColor(this, Posting, R.color.black);
        setImageButtonColor(this, messengerButton, R.color.black);
        setImageButtonColor(this, notificationButton, R.color.black);
        setImageButtonColor(this, accountButton, R.color.black);

        Dashboard_textview.setTextColor(getResources().getColor(R.color.black));
        Posting_Textview.setTextColor(getResources().getColor(R.color.black));
        Messenger_Textview.setTextColor(getResources().getColor(R.color.black));
        Notification_Textview.setTextColor(getResources().getColor(R.color.black));
        Account_Textview.setTextColor(getResources().getColor(R.color.black));
    }

    private void setWidget(){
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
        Intent intent = getIntent();
        recruiterName = intent.getStringExtra("recruiterName");
        id_User = intent.getIntExtra("user_id", -1);  // Lấy userId từ Intent
        if (id_User == -1) {
            Log.e("Error", "userId không hợp lệ");
        }
        phoneNumber = intent.getStringExtra("phoneNumber");
        id_Recruiter = intent.getIntExtra("id_Recruiter",0);
        //Log.e("MainActivity","ID: " + id_Recruiter);
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