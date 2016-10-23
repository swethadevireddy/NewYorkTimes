package com.codepath.newyorktimes.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codepath.newyorktimes.adapter.ArticleAdapter;
import com.codepath.newyorktimes.fragments.SettingsDialogFragment;
import com.codepath.newyorktimes.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.newyorktimes.listeners.ItemClickSupport;
import com.codepath.newyorktimes.model.Article;
import com.codepath.newyorktimes.model.SearchFilter;
import com.codepath.newyorktimes.net.NewsClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements SettingsDialogFragment.SettingsDialogListener {

    // Store the binding
    //private ActivitySearchBinding binding;
    private NewsClient client;
    ArrayList<Article> articles;
    ArticleAdapter adapter;
    RecyclerView rvArticles;
    SearchFilter searchFilter;
    String searchQuery;
    Snackbar mSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Toolbar toolbar = binding.toolbarinclude.toolbar;
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar) ;

        setSupportActionBar(toolbar);
     */   //binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
        if(searchFilter == null){
           searchFilter = new SearchFilter();
        }

       //rvArticles = binding.rvArticles;

        articles = new ArrayList<>();
        adapter = new ArticleAdapter( articles);
        // Attach the adapter to the recyclerview to populate items
        rvArticles.setAdapter(adapter);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvArticles.setLayoutManager(layoutManager);

        prepareSnackBar();

        // Click Listener
        ItemClickSupport.addTo(rvArticles).setOnItemClickListener((recyclerView, position, v) -> {
            Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
            Article article = articles.get(position);
            i.putExtra("article", article);
            startActivity(i);
        });

        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if(!isNetworkAvailable()){
                    mSnackBar.show();
                }else {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to the bottom of the list
                    fetchNews(page);
                }
            }
        });



       }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
            public boolean onQueryTextSubmit(String query) {
               searchQuery = query;
               adapter.clear();
               if(!isNetworkAvailable()){
                   mSnackBar.show();
               }else {
                   // Triggered only when new data needs to be appended to the list
                   // Add whatever code is needed to append new items to the bottom of the list
                   fetchNews(0);
               }

               searchView.clearFocus();
               return true;

            }
            @Override
           public boolean onQueryTextChange(String newText) {
              return false;

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void fetchNews(int page) {
        client = new NewsClient();
        client.searchArticles(searchQuery, page, searchFilter,  new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray docs;
                    if(response != null) {
                        // Get the docs json array
                        docs = response.getJSONObject("response").getJSONArray("docs");
                        Log.d("DEBUG", docs.toString());
                        int curSize = adapter.getItemCount();
                        ArrayList<Article> newArticles = Article.fromJsonArray(docs);
                        articles.addAll(newArticles);
                        adapter.notifyItemRangeInserted(curSize, newArticles.size());
                       rvArticles.setHasFixedSize(true);
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }


        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                //showSettings();
                showSettingsDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showSettingsDialog(){
        FragmentManager fm = getSupportFragmentManager();
        SettingsDialogFragment settingsDialog = new SettingsDialogFragment();
        if(searchFilter == null){
           searchFilter = new SearchFilter();
        }
        settingsDialog.setSearchFilter(searchFilter);
        settingsDialog.show(fm, "settings");
    }


    @Override
    public void onFinishSettingFilters(SearchFilter searchFilter) {
        this.searchFilter = searchFilter;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void prepareSnackBar(){
        mSnackBar = Snackbar.make(findViewById(android.R.id.content), "No Internet connection available", Snackbar.LENGTH_INDEFINITE);
        TextView textView = (TextView) mSnackBar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        mSnackBar.setActionTextColor(Color.CYAN);
        mSnackBar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackBar.dismiss();
            }
        });
    }

}
