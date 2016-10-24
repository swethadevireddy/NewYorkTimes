package com.codepath.newyorktimes.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.newyorktimes.activities.databinding.ActivityArticleBinding;
import com.codepath.newyorktimes.model.Article;

/**
 * Created by sdevired on 10/22/16.
 * Activity to sow the embedded webview
 */
public class ArticleActivity extends AppCompatActivity {

    ActivityArticleBinding binding;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        //to display home icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_article);
        Intent intent = getIntent();
        final Article article = intent.getParcelableExtra("article");
        ActionBar actionBar = getSupportActionBar();
        //set actionbar title
        if(actionBar != null) {
            actionBar.setTitle(article.getHeadline());
        }

        binding.wvArticle.setWebViewClient(new WebViewClient(){
            //override this method to embed webview in app.
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        binding.wvArticle.loadUrl(article.getWebUrl());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article, menu);
        MenuItem item = menu.findItem(R.id.menu_article_share);
        // Fetch reference to the share action provider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        // get reference to WebView
        shareIntent.putExtra(Intent.EXTRA_TEXT, binding.wvArticle.getUrl());
        mShareActionProvider.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
