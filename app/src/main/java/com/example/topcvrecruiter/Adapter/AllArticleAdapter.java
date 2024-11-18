package com.example.topcvrecruiter.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.topcvrecruiter.ArticleDetailActivity;
import com.example.topcvrecruiter.R;
import com.example.topcvrecruiter.Model.Article;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AllArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private List<Article> articles;

    public AllArticleAdapter(Context context, List<Article> articles) {
        this.articles = articles;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        // Kiểm tra xem item có phải là "footer loading" không
        return (articles.get(position) == null) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
            return new ArticleViewHolder(view);
        } else {
            // "footer loading" item
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ArticleViewHolder) {
            Article article = articles.get(position);
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            articleViewHolder.title.setText(article.getArticle_Name());
            articleViewHolder.content.setText(article.getContent());

            // Thiết lập sự kiện nhấn vào mỗi item
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ArticleDetailActivity.class);
                intent.putExtra("article_id", article.getId()); // Chuyển article_id
                Log.d("ArticleAdapter", "Article ID: " + article.getId()); // Log article_id
                context.startActivity(intent);
            });

            // Định dạng thời gian
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

            try {
                Date createDate = inputFormat.parse(article.getCreate_Time());
                long createTimeInMillis = createDate.getTime();
                long currentTimeInMillis = System.currentTimeMillis();

                long timeDifference = currentTimeInMillis - createTimeInMillis;
                long minutesDifference = timeDifference / (60 * 1000);
                long hoursDifference = timeDifference / (60 * 60 * 1000);

                if (minutesDifference < 60) {
                    ((ArticleViewHolder) holder).createTime.setText(minutesDifference + " minutes ago");
                } else if (hoursDifference < 24) {
                    ((ArticleViewHolder) holder).createTime.setText(hoursDifference + " hours ago");
                } else if (hoursDifference < 24 * 2) {
                    ((ArticleViewHolder) holder).createTime.setText("Yesterday");
                } else if (hoursDifference < 24 * 7) {
                    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                    String dayOfWeek = dayFormat.format(createDate);
                    ((ArticleViewHolder) holder).createTime.setText(dayOfWeek);
                } else {
                    String formattedDate = outputFormat.format(createDate);
                    ((ArticleViewHolder) holder).createTime.setText(formattedDate);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((ArticleViewHolder) holder).createTime.setText("Create Time: " + article.getCreate_Time());
            }
        }

    }

    @Override
    public int getItemCount() {
        return articles != null ? articles.size() : 0;
    }

    // Thêm footer loading vào cuối RecyclerView
    public void addFooterLoading() {
        if (!articles.contains(null)) {
            articles.add(null);
            notifyItemInserted(articles.size() - 1);
        }
    }

    // Loại bỏ footer loading sau khi dữ liệu được tải
    public void removeFooterLoading() {
        int position = articles.indexOf(null);
        if (position != -1) {
            articles.remove(position);
            notifyItemRemoved(position);
        }
    }

    // ViewHolder cho item bài viết
    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        TextView createTime;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
            createTime = itemView.findViewById(R.id.time);
        }
    }

    // ViewHolder cho footer loading
    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
