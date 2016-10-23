package com.codepath.newyorktimes.model.article;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;


public class ApiResponse {
  @SerializedName("response")
  private Response response;

  public static ApiResponse fromJson(String responseString){
    Gson gson = new GsonBuilder().create();
    ApiResponse response = gson.fromJson(responseString, ApiResponse.class);
    return response;
  }
}
