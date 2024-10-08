package com.example.topcvrecruiter.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.topcvrecruiter.ArticleActivity;
import com.example.topcvrecruiter.JobActivity;
import com.example.topcvrecruiter.R;

import java.util.ArrayList;
import java.util.Calendar;

public class PostingFragment extends Fragment {
    private Button post_button;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting, container, false);
        post_button = view.findViewById(R.id.post_button);
        post_button.setOnClickListener(view1 -> {
            showPostTypeDialog();
        });
        return view;
    }
    private void showPostTypeDialog() {
        // Create AlertDialog to ask user if they want to post Article or Job
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Post Type");

        // Define options for the dialog (Article and Job)
        String[] options = {"Article", "Job"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // If user chooses Article, navigate to ArticleActivity
                    Intent intent = new Intent(getActivity(), ArticleActivity.class);
                    startActivity(intent);
                } else if (which == 1) {
                    // If user chooses Job, navigate to JobActivity
                    Intent intent = new Intent(getActivity(), JobActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Add "Cancel" button if the user doesn't want to choose
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();  // Close the dialog if the user cancels
            }
        });

        // Show the dialog
        builder.create().show();
    }
}