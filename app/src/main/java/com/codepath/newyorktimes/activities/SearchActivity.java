package com.codepath.newyorktimes.activities;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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
import com.codepath.newyorktimes.model.article.ApiResponse;
import com.codepath.newyorktimes.net.NewYorkSearchClient;
import com.codepath.newyorktimes.net.NewsClient;
import com.codepath.newyorktimes.util.ChromeShareProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        ChromeShareProvider chromeShareProvider = new ChromeShareProvider(this);
        // Click Listener
        ItemClickSupport.addTo(rvArticles).setOnItemClickListener((recyclerView, position, v) -> {
            Article article = articles.get(position);
           /*  Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
            Article article = articles.get(position);
            i.putExtra("article", article);
            startActivity(i);
           */
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(this,R.color.colorPrimary));
            builder.setActionButton(chromeShareProvider.getBitmap(), "Share Link", chromeShareProvider.getPendingIntent(), true);
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(article.getWebUrl()));
        });

        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if(!isNetworkAvailable()){
                    mSnackBar.show();
                }else {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to the bottom of the list
                    fetchNewsWithRetrofit(page);
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
                 //  fetchNews(0);
                   fetchNewsWithRetrofit(0);
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
        client.searchArticles(searchQuery, page, searchFilter,  new TextHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                Gson gson = new GsonBuilder().create();
                ApiResponse response = gson.fromJson(res, ApiResponse.class);
                Log.d("DEBUG", "Response: " + response.toString());
                int curSize = adapter.getItemCount();
                ArrayList<Article> newArticles = Article.fromDocs(response.getResponse().getDocs());
                articles.addAll(newArticles);
                adapter.notifyItemRangeInserted(curSize, newArticles.size());
               /* try {
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
                }*/
                // called when response HTTP status is "200 OK"
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }


        });
    }

    private void fetchNewsWithRetrofit(int page) {
        NewYorkSearchClient client = new NewYorkSearchClient();
        client.getArticles(searchQuery, page, searchFilter,  new Callback<ApiResponse>(){

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("DEBUG", "Response: " + response.toString());
                ApiResponse  apiResponse = response.body();
                if(apiResponse != null && apiResponse.getResponse() != null && apiResponse.getResponse().getDocs() != null) {
                    ArrayList<Article> newArticles = Article.fromDocs(response.body().getResponse().getDocs());
                    int curSize = adapter.getItemCount();
                    articles.addAll(newArticles);
                    adapter.notifyItemRangeInserted(curSize, newArticles.size());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
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
