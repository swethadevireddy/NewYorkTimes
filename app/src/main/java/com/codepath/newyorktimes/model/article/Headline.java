package com.codepath.newyorktimes.model.article;

import com.google.gson.annotations.SerializedName;


public class Headline {
  @SerializedName("main")
  private String main;

  public String getMain() {
    return main;
  }

  public void setMain(String main) {
    this.main = main;
  }
}
