package com.example.topcvrecruiter.Recruiter;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Recruiter.DRVinterface.LoadMore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecruiterDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;


    List<DynamicRvModel> items = new ArrayList();
    DynamicRVAdapter dynamicRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recruiter_dashboard);

        ArrayList<StaticRvModel> item = new ArrayList<>();
        item.add(new StaticRvModel(R.drawable.dashboard_ic,"dashboard1"));
        item.add(new StaticRvModel(R.drawable.dashboard_ic,"dashboard2"));
        item.add(new StaticRvModel(R.drawable.dashboard_ic,"dashboard3"));
        item.add(new StaticRvModel(R.drawable.dashboard_ic,"dashboard4"));
        item.add(new StaticRvModel(R.drawable.dashboard_ic,"dashboard5"));

        recyclerView = findViewById(R.id.rv_1);
        staticRvAdapter = new StaticRvAdapter(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(staticRvAdapter);

        items.add(new DynamicRvModel("pizza1"));
        items.add(new DynamicRvModel("pizza2"));
        items.add(new DynamicRvModel("pizza3"));
        items.add(new DynamicRvModel("pizza4"));
        items.add(new DynamicRvModel("pizza5"));
        items.add(new DynamicRvModel("pizza6"));
        items.add(new DynamicRvModel("pizza7"));
        items.add(new DynamicRvModel("pizza8"));
        items.add(new DynamicRvModel("pizza9"));
        items.add(new DynamicRvModel("pizza10"));
        items.add(new DynamicRvModel("pizza11"));
        items.add(new DynamicRvModel("pizza12"));
        items.add(new DynamicRvModel("pizza13"));
        items.add(new DynamicRvModel("pizza14"));
        items.add(new DynamicRvModel("pizza15"));


        RecyclerView drv = findViewById(R.id.rv_2);
        drv.setLayoutManager(new LinearLayoutManager(this));
        dynamicRVAdapter = new DynamicRVAdapter(drv,this,items);
        drv.setAdapter(dynamicRVAdapter);

        dynamicRVAdapter.steLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {
                if(item.size() <= 10){
                    item.add(null);
                    dynamicRVAdapter.notifyItemInserted(item.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            items.remove((item.size()-1));
                            dynamicRVAdapter.notifyItemRemoved(item.size());

                            int index = items.size();
                            int end = index+10;
                            for(int i = index; i<end;i++){
                                String name = UUID.randomUUID().toString();
                                DynamicRvModel item = new DynamicRvModel(name);
                                items.add(item);
                            }
                            dynamicRVAdapter.notifyDataSetChanged();
                            dynamicRVAdapter.setLoaded();
                        }
                    },4000);

                }
                else
                    Toast.makeText(RecruiterDashboardActivity.this, "Data completed", Toast.LENGTH_SHORT).show();
            }
        });

/*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ConstraintRecruiter), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
*/
    }
}