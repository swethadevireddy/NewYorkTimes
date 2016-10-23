package com.codepath.newyorktimes.model.article;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Doc {
  @SerializedName("web_url")
  private String webUrl;

  @SerializedName("multimedia")
  private List<Multimedia> multimedia = new ArrayList<Multimedia>();

  @SerializedName("headline")
  private Headline headline;

  public String getWebUrl() {
    return webUrl;
  }

  public void setWebUrl(String webUrl) {
    this.webUrl = webUrl;
  }

  public List<Multimedia> getMultimedia() {
    return multimedia;
  }

  public void setMultimedia(List<Multimedia> multimedia) {
    this.multimedia = multimedia;
  }

  public Headline getHeadline() {
    return headline;
  }

  public void setHeadline(Headline headline) {
    this.headline = headline;
  }
}
