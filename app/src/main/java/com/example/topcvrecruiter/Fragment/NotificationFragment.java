package com.example.topcvrecruiter.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Adapter.NotificationAdapter;
import com.example.topcvrecruiter.API.ApiNotificationService;
import com.example.topcvrecruiter.Model.Notification;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NotificationFragment extends Fragment {

    private NotificationAdapter notificationAdapter;

    private List<Notification> notificationList;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int id_User;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        setWidget(view);
        loadNotifications(id_User);
        return view;
    }

    private void setWidget(View view){
        RecyclerView notificationRecyclerView = view.findViewById(R.id.NotificationRecyclerView);
        assert getArguments() != null;
        id_User = getArguments().getInt("user_id", -1);
        notificationList = new ArrayList<>();
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationAdapter = new NotificationAdapter(notificationList, getContext());
        notificationRecyclerView.setAdapter(notificationAdapter);
    }

    @SuppressLint({"CheckResult", "NotifyDataSetChanged"})
    private void loadNotifications(int userId) {
        ApiNotificationService.ApiNotificationService.getNotificationByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notifications -> {
                    notificationList.clear();
                    notificationList.addAll(notifications);
                    notificationAdapter.notifyDataSetChanged();
                }, throwable -> Log.e("NotificationFragment","Null"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
