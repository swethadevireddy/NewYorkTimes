package com.codepath.newyorktimes.model.article;

import com.google.gson.annotations.SerializedName;


public class ApiResponse {
    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @SerializedName("response")
  private Response response;


}
