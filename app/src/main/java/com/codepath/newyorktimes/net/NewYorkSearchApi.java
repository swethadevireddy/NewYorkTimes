package com.codepath.newyorktimes.net;

import com.codepath.newyorktimes.model.article.ApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by sdevired on 10/23/16.
 */
public interface NewYorkSearchApi {

    @GET("articlesearch.json")
    Call<ApiResponse> getArticles( @QueryMap Map<String, String> searchOptions);

}
