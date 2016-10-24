package com.codepath.newyorktimes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.newyorktimes.activities.R;

/**
 * Created by sdevired on 10/23/16.
 * ViewHolder for Headline with ThumbNail.
 */
public class ItemArticleImageViewHolder extends RecyclerView.ViewHolder {
    // for any view that will be set as you render a row
    public TextView tvHeadLine;
    public ImageView ivThumbNail;

    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    public ItemArticleImageViewHolder(View itemView) {
        // Stores the itemView in a public final member variable that can be used
        // to access the context from any ViewHolder instance.
        super(itemView);

        tvHeadLine = (TextView) itemView.findViewById(R.id.tvHeadline);
        ivThumbNail = (ImageView) itemView.findViewById(R.id.ivThumbNail);
    }
}
