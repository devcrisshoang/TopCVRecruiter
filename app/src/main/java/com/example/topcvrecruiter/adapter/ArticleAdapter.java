package com.example.topcvrecruiter.adapter;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.model.Article;
import com.example.topcvrecruiter.model.Job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Article> articles;
    private List<Job> jobs;

    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

//



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.articleName.setText(article.getArticle_Name());
        holder.content.setText(article.getContent());

        // Chuyển đổi chuỗi thời gian sang định dạng ngày tháng năm
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            Date createDate = inputFormat.parse(article.getCreate_Time());
            long createTimeInMillis = createDate.getTime();
            long currentTimeInMillis = System.currentTimeMillis();

            long timeDifference = currentTimeInMillis - createTimeInMillis;
            long minutesDifference = timeDifference / (60 * 1000); // Chuyển đổi sang phút
            long hoursDifference = timeDifference / (60 * 60 * 1000); // Chuyển đổi sang giờ

            if (minutesDifference < 60) {
                holder.createTime.setText(minutesDifference + " minutes ago");
            } else if (hoursDifference < 24) {
                holder.createTime.setText(hoursDifference + " hours ago");
            } else if (hoursDifference < 24 * 2) {
                holder.createTime.setText("Yesterday");
            } else if (hoursDifference < 24 * 7) {
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                String dayOfWeek = dayFormat.format(createDate);
                holder.createTime.setText(dayOfWeek);
            } else {
                String formattedDate = outputFormat.format(createDate);
                holder.createTime.setText(formattedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            holder.createTime.setText("Create Time: " + article.getCreate_Time());
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView articleName;
        TextView content;
        TextView createTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            articleName = itemView.findViewById(R.id.tvArticleName);
            content = itemView.findViewById(R.id.tvContent);
            createTime = itemView.findViewById(R.id.tvCreateTime);

        }
    }
    public void updateData(List<Article> newArticles) {
        articles.clear();
        articles.addAll(newArticles);
        notifyDataSetChanged();
    }
}