package com.todocode.recipesdemo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.todocode.recipesdemo.Model.Article;
import com.todocode.recipesdemo.Model.Cuisine;
import com.todocode.recipesdemo.R;

import java.util.List;

public class ArticlesAdapter extends  RecyclerView.Adapter<ArticlesAdapter.ArticleHolder> {

    private Context context;
    private List<Article> articles;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public class ArticleHolder extends RecyclerView.ViewHolder {
        private ImageView articleImage;
        private TextView articleTitle, articleText;

        public ArticleHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            articleImage = (ImageView) itemView.findViewById(R.id.articleImage);
            articleText = (TextView) itemView.findViewById(R.id.articleText);
            articleTitle = (TextView) itemView.findViewById(R.id.articleTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
        public void setDetails(Article article) {
            articleText.setText(Html.fromHtml(article.getTextSmall()));
            articleTitle.setText(article.getTitle());
            String imageUrl = context.getResources().getString(R.string.domain_name)+"/uploads/articles/"+article.getImage();
            Picasso.get().load(imageUrl).fit().centerInside().into(articleImage);
        }
    }

    public ArticlesAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_article_layout, parent, false);
        return new ArticleHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        Article article = articles.get(position);
        holder.setDetails(article);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

}




