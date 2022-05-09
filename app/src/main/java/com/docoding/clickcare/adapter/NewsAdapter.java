package com.docoding.clickcare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.docoding.clickcare.R;
import com.docoding.clickcare.model.NewsModel;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsExploreViewHolder> {
    private ArrayList<NewsModel> listNews;

    public NewsAdapter(ArrayList<NewsModel> list) {
        this.listNews = list;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsExploreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsExploreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsExploreViewHolder holder, int position) {
        NewsModel news = listNews.get(position);
        Glide.with(holder.itemView.getContext())
                .load(news.getPhoto())
                .into(holder.newsPhoto);
        holder.newsTitle.setText(news.getTitle());
        holder.newsTime.setText(news.getTime());
        holder.newsDate.setText(news.getDate());

    }

    @Override
    public int getItemCount() {
        return listNews.size();
    }

    public class NewsExploreViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        TextView newsTime;
        TextView newsDate;
        ImageView newsPhoto;

        public NewsExploreViewHolder(@NonNull View itemView) {
            super(itemView);

            newsTitle = itemView.findViewById(R.id.news_title);
            newsTime = itemView.findViewById(R.id.news_time);
            newsDate = itemView.findViewById(R.id.news_date);
            newsPhoto = itemView.findViewById(R.id.img_item_photo);
        }
    }
}
