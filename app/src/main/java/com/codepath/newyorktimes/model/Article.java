package com.codepath.newyorktimes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.codepath.newyorktimes.model.article.Doc;
import com.codepath.newyorktimes.model.article.Multimedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by sdevired on 10/20/16.
 */
public class Article implements Parcelable {
    String webUrl;
    String headline;
    String thumbNail;

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

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() > 0) {
                int imgIndex = new Random().nextInt(multimedia.length());
                JSONObject multimediaJson = multimedia.getJSONObject(imgIndex);
                this.thumbNail = "https://www.nytimes.com/" + multimediaJson.getString("url");
            } else {
                this.thumbNail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * used if we get JSONObject when AyscHttpClient is used to retrieve data.
     * @param jsonArray
     * @return
     */

    public static ArrayList<Article> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Article> articles = new ArrayList<Article>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Article a = new Article(jsonArray.getJSONObject(i));
                articles.add(a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return articles;
    }


    /**
     * retrieve articles from Doc.
     * @param docs
     * @return
     */
    public static ArrayList<Article> fromDocs(List<Doc> docs) {
        ArrayList<Article> articles = new ArrayList<Article>();
        for (int i = 0; i < docs.size(); i++) {
           Article a = new Article(docs.get(i));
           articles.add(a);

        }
        return articles;
    }

    /**
     * Article from Doc
     * @param doc
     */
    public Article(Doc doc) {
        this.webUrl = doc.getWebUrl();
        this.headline = doc.getHeadline().getMain();
        List<Multimedia> multimedia = doc.getMultimedia();
        if (multimedia != null && multimedia.size() > 0) {
            int imgIndex = new Random().nextInt(multimedia.size());
            this.thumbNail = "https://www.nytimes.com/" + multimedia.get(imgIndex).getUrl();
        } else {
            this.thumbNail = "";
        }

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


    public Category getCategory() {
        if (thumbNail != null && !thumbNail.isEmpty()) {
            return Category.WITH_IMAGE;
        }
        return Category.WITH_OUT_IMAGE;
    }
    //for heterogeneous view
    public enum Category {
        WITH_IMAGE, WITH_OUT_IMAGE
    }
}
