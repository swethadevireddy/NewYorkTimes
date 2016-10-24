package com.codepath.newyorktimes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.newyorktimes.activities.R;
import com.codepath.newyorktimes.model.Article;

import java.util.List;

/**
 * Created by sdevired on 10/20/16.
 * RecyclerView Adapter to populate Article data
 */
public class ArticleAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Store a member variable for the contacts
    private List<Article> articles;

    // Pass in the contact array into the constructor
    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;

        // Inflate the custom layout based on the viewtype
        if(viewType == Article.Category.WITH_IMAGE.ordinal()){
            View articleView = inflater.inflate(R.layout.item_article_image, parent, false);
            viewHolder = new ItemArticleImageViewHolder(articleView);
        }else{
            View articleView = inflater.inflate(R.layout.item_article_title, parent, false);
            viewHolder = new ItemArticleTitleViewHolder(articleView);
        }

        return viewHolder;
    }

    /**
     * method to load the data into the view
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get the data model based on position
        Article article = articles.get(position);
        if(holder instanceof ItemArticleImageViewHolder){
            ItemArticleImageViewHolder viewHolder = (ItemArticleImageViewHolder) holder;
            // Set item views based on your views and data model
            viewHolder.tvHeadLine.setText(article.getHeadline());
            viewHolder.ivThumbNail.setImageResource(0);
            Glide.with(viewHolder.itemView.getContext())
                    .load(article.getThumbNail())
                    .into(viewHolder.ivThumbNail);
        }else{
            ItemArticleTitleViewHolder viewHolder = (ItemArticleTitleViewHolder) holder;
            viewHolder.tvHeadLine.setText(article.getHeadline());
        }
    }


    /**
     * returns article view for heterogeneous layout
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        Article article = articles.get(position);
        return article.getCategory().ordinal();
    }


    @Override
    public int getItemCount() {
        return articles.size();
    }


    /**
     * method to clear the adapter
     */
    public void clear(){
        final int size = getItemCount();
        articles.clear();
        notifyItemRangeRemoved(0, size);
    }

}
