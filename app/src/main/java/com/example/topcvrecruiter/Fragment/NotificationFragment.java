package com.example.topcvrecruiter.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.topcvrecruiter.Utils.PaginationScrollListener;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.Stack;

public class NotificationFragment extends Fragment {

    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private Stack<Notification> notificationStack;
    private Stack<Notification> notificationStackDisplay;
    private int id_User;
    private int currentPage = 0;
    private final int pageSize = 10;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // Khởi tạo RecyclerView và LayoutManager
        setWidget(view);

        // Khởi tạo danh sách và Adapter
        notificationStack = new Stack<>();
        notificationStackDisplay = new Stack<>();
        notificationAdapter = new NotificationAdapter(notificationStackDisplay, getContext());
        notificationRecyclerView.setAdapter(notificationAdapter);

        // Tải dữ liệu thông báo
        loadNotifications(id_User);

        // Gắn ScrollListener cho phân trang
        notificationRecyclerView.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) notificationRecyclerView.getLayoutManager()) {
            @Override
            public void loadMoreItem() {
                if (!isLoading && !isLastPage) {
                    isLoading = true;
                    notificationAdapter.addFooterLoading();
                    loadNextPage();
                }
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });

        return view;
    }

    private void setWidget(View view) {
        // Khởi tạo RecyclerView và LayoutManager
        notificationRecyclerView = view.findViewById(R.id.NotificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        assert getArguments() != null;
        id_User = getArguments().getInt("user_id", 0);
    }

    @SuppressLint({"CheckResult", "NotifyDataSetChanged"})
    private void loadNotifications(int userId) {
        ApiNotificationService.ApiNotificationService.getNotificationByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notifications -> {
                    notificationStack.clear();
                    // Đảo ngược thứ tự các thông báo, để phần tử mới nhất nằm ở đầu
                    for (int i = notifications.size() - 1; i >= 0; i--) {
                        notificationStack.push(notifications.get(i));
                    }
                    loadPage(currentPage);

                    if (notificationStack.size() <= pageSize) {
                        isLastPage = true;
                    }
                }, throwable -> Log.e("NotificationFragment", "Error loading notifications"));
    }

    private void loadNextPage() {
        // Giả lập việc tải dữ liệu từ API với một delay
        new Handler().postDelayed(() -> {
            // Tăng trang sau khi tải
            currentPage++;
            loadPage(currentPage);

            // Đánh dấu không còn loading nữa
            isLoading = false;

            // Loại bỏ footer loading sau khi tải xong
            notificationAdapter.removeFooterLoading();

            // Kiểm tra nếu đã tới trang cuối cùng
            if (currentPage * pageSize >= notificationStack.size()) {
                isLastPage = true;
            }
        }, 2000);  // Thời gian giả lập việc tải thêm (2 giây)
    }

    private void loadPage(int page) {
        int start = page * pageSize;  // Tính vị trí bắt đầu của trang hiện tại
        int end = Math.min(start + pageSize, notificationStack.size());  // Tính vị trí kết thúc (không vượt quá kích thước danh sách)

        if (start < notificationStack.size()) {
            notificationStackDisplay.addAll(notificationStack.subList(start, end));  // Thêm dữ liệu vào danh sách hiển thị
            notificationAdapter.notifyItemRangeInserted(start, end - start);  // Cập nhật phần tử mới thêm
        }

        // Kiểm tra nếu đã tải hết dữ liệu
        if (end >= notificationStack.size()) {
            isLastPage = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
