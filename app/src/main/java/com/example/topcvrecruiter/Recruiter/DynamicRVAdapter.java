package com.example.topcvrecruiter.Recruiter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Recruiter.DRVinterface.LoadMore;

import java.util.List;

class LoadingViewHolder extends RecyclerView.ViewHolder{

    public ProgressBar progressBar;
    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById((R.id.progress_bar));
    }
}
class  ItemViewHolder extends RecyclerView.ViewHolder{

    public TextView name;
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById((R.id.name));
    }
}

public class DynamicRVAdapter extends RecyclerView.Adapter{
    private final  int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    LoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<DynamicRvModel> items;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;

    public DynamicRVAdapter(RecyclerView recyclerView, Activity activity, List<DynamicRvModel> items) {

        this.activity = activity;
        this.items = items;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if(isLoading && totalItemCount <= (lastVisibleItem+visibleThreshold)){
                    if(loadMore!= null) loadMore.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    public  void steLoadMore(LoadMore loadMore){
        this.loadMore = loadMore;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == VIEW_TYPE_ITEM){

            View view = LayoutInflater.from(activity).inflate(R.layout.dynamic_rv_item_layout,parent,false);
            return new LoadingViewHolder(view);
        }
        else if (viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(activity).inflate(R.layout.dynamic_rv_progress_bar,parent,false);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof  ItemViewHolder){
            DynamicRvModel item = items.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.name.setText(items.get(position).getName());
        }
        else if(holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {

        return items.size();
    }
    public void setLoaded(){
        isLoading = false;
    }
}
