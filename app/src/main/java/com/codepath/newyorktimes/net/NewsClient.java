package com.codepath.newyorktimes.net;

import com.codepath.newyorktimes.model.SearchFilter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsClient {
    private static final String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    private static final String API_KEY= "aa37bbc7b4624a5a8c9628018d034fb2";
    private AsyncHttpClient client;

    public NewsClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void searchArticles(final String query,int page, SearchFilter searchFilter, JsonHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("api-key",API_KEY );
        requestParams.put("q", query);
        requestParams.put("page", page);

        if(searchFilter.getBeginDate() != null && !searchFilter.getBeginDate().isEmpty()){
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(searchFilter.getBeginDate());
                requestParams.put("begin_date", new SimpleDateFormat("yyyyMMdd", Locale.US).format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //&fq=news_desk:("Sports" "Foreign")
        StringBuilder newsDesk = new StringBuilder();
        if(searchFilter.getArts()){
            newsDesk.append("\"Arts\" ");
        }
        if(searchFilter.getFashionStyle()){
            newsDesk.append("\"Fashion & Style\" ");
        }
        if(searchFilter.getSports()){
            newsDesk.append("\"Sports\"");
        }
        if(newsDesk.length() > 0){
            newsDesk.insert(0, "news_desk:(");
            newsDesk.append(")");
            requestParams.put("fq", newsDesk.toString());
        }

        if(searchFilter.getSortOrder() != null){
            requestParams.put("sort", searchFilter.getSortOrder().toLowerCase());
        }
            String url = getApiUrl( "articlesearch.json");
            client.get(url, requestParams, handler);

    }
}
