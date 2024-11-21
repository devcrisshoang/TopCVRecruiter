package com.example.topcvrecruiter.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.topcvrecruiter.API.ApiNotificationService;
import com.example.topcvrecruiter.Adapter.NotificationAdapter;
import com.example.topcvrecruiter.Model.Notification;
import com.example.topcvrecruiter.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NotificationFragment extends Fragment {
    private RecyclerView NotificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int id_User;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        NotificationRecyclerView = view.findViewById(R.id.NotificationRecyclerView);

        id_User = getArguments().getInt("user_id", -1);

        // Khởi tạo danh sách thông báo
        notificationList = new ArrayList<>();

        // Thiết lập RecyclerView
        NotificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationAdapter = new NotificationAdapter(notificationList, getContext());
        NotificationRecyclerView.setAdapter(notificationAdapter);

        // Gọi API để lấy thông báo của userId = 9
        loadNotifications(id_User);

        return view;
    }

    private void loadNotifications(int userId) {
        ApiNotificationService.ApiNotificationService.getNotificationByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notifications -> {
                    // Cập nhật danh sách thông báo và làm mới RecyclerView
                    notificationList.clear();
                    notificationList.addAll(notifications);
                    notificationAdapter.notifyDataSetChanged();
                }, throwable -> {
                    // Xử lý lỗi khi gọi API
                    Toast.makeText(getContext(), "Failed to load notifications: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear(); // Xóa các request để tránh rò rỉ bộ nhớ
    }
}
