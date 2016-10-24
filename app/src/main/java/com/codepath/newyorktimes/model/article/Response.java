package com.codepath.newyorktimes.model.article;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Response {

  @SerializedName("docs")
  private List<Doc> docs;

  public List<Doc> getDocs() {
    return docs;
  }

  public void setDocs(List<Doc> docs) {
    this.docs = docs;
  }

  public Response(){
    docs = new ArrayList<Doc>();
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  private String status;

  private String copyright;


}
