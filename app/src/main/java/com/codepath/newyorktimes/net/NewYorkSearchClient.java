package com.codepath.newyorktimes.net;

import com.codepath.newyorktimes.model.SearchFilter;
import com.codepath.newyorktimes.model.article.ApiResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sdevired on 10/23/16.
 * Client for article search using Retrofit
 */
public class NewYorkSearchClient {

    private static final String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    private static final String API_KEY= "aa37bbc7b4624a5a8c9628018d034fb2";

    private Retrofit retrofit;

    private NewYorkSearchApi newsService;

    public NewYorkSearchClient() {
        this.retrofit =new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newsService =  retrofit.create(NewYorkSearchApi.class);

    }

   public void getArticles(final String query, int page, SearchFilter searchFilter, Callback<ApiResponse> callback) {
       Map<String, String> requestParams = new HashMap<>();
       requestParams.put("api-key",API_KEY );
       requestParams.put("q", query);
       requestParams.put("page", page+"");

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

       // simplified call to request the news with already initialized service
       Call<ApiResponse> call = newsService.getArticles(requestParams);
       call.enqueue(callback);
   }


}
