package com.example.topcvrecruiter.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.topcvrecruiter.API.ApiMessageService;
import com.example.topcvrecruiter.Adapter.MessengerAdapter;
import com.example.topcvrecruiter.Model.User;
import com.example.topcvrecruiter.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MessengerFragment extends Fragment {

    private RecyclerView messageRecyclerView;

    private MessengerAdapter messageAdapter;

    private List<User> userList = new ArrayList<>();

    private int id_User;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messenger, container, false);
        messageRecyclerView = view.findViewById(R.id.MessageRecyclerView);

        setWidget();

        return view;
    }

    private void setWidget() {
        id_User = getArguments().getInt("user_id", -1);
        Log.e("MessageFragment","User ID: " + id_User);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messageAdapter = new MessengerAdapter(userList, getContext(),id_User);
        messageRecyclerView.setAdapter(messageAdapter);
        assert getArguments() != null;
        getChatPartners(id_User);
    }

    @SuppressLint("CheckResult")
    private void getChatPartners(int userId) {
        ApiMessageService.apiMessageService.getAllChatPartnersByUserId(userId)
                .subscribeOn(Schedulers.io()) // Thực hiện trên thread background
                .observeOn(AndroidSchedulers.mainThread()) // Quan sát trên thread UI
                .subscribe(
                        users -> {
                            userList.clear();
                            userList.addAll(users);

                            if (messageAdapter != null) {
                                messageAdapter.notifyDataSetChanged();
                            }
                        },
                        throwable -> {
                            Log.e("MessengerFragment", "Error fetching chat partners: " + throwable.getMessage());
                        }
                );
    }
}
