package com.example.topcvrecruiter.Adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.ArticleDetailActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Model.Article;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Article> articles;
    private Context context;

    public ArticleAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }


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

        // Thiết lập sự kiện nhấn vào mỗi item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArticleDetailActivity.class);
            intent.putExtra("article_id", article.getId()); // Chuyển article_id
            Log.d("ArticleAdapter", "Article ID: " + article.getId()); // Log article_id
            context.startActivity(intent);
        });

        holder.createTime.setText("Create Time: " + article.getCreate_Time());
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
            articleName = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
            createTime = itemView.findViewById(R.id.time);
        }
    }

    public void updateData(List<Article> newArticles) {
        articles.clear();
        articles.addAll(newArticles);
        notifyDataSetChanged();
    }
}
