package com.codepath.newyorktimes.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sdevired on 10/20/16.
 */
public class Article implements Parcelable{

    String webUrl;
    String headline;

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    String thumbNail;

    public Article(JSONObject jsonObject){
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if(multimedia.length() > 0){
                int imgIndex = new Random().nextInt(multimedia.length());
                JSONObject multimediaJson = multimedia.getJSONObject(imgIndex);
                this.thumbNail =  "https://www.nytimes.com/"+ multimediaJson.getString("url");
            }else{
                this.thumbNail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Article> fromJsonArray(JSONArray jsonArray){
        ArrayList<Article> articles = new ArrayList<Article>();
        for(int i = 0 ; i < jsonArray.length() ; i ++) {
            try {
                Article a = new Article(jsonArray.getJSONObject(i));
                articles.add(a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return articles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webUrl);
        dest.writeString(this.headline);
        dest.writeString(this.thumbNail);
    }

    protected Article(Parcel in) {
        this.webUrl = in.readString();
        this.headline = in.readString();
        this.thumbNail = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public Category getCategory(){
        if(thumbNail != null && !thumbNail.isEmpty()){
            return Category.WITH_IMAGE;
        }
        return Category.WITH_OUT_IMAGE;
    }

    public enum Category {
        WITH_IMAGE, WITH_OUT_IMAGE
    }
}
