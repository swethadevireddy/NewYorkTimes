package com.codepath.newyorktimes.model;

/**
 * Created by sdevired on 10/22/16.
 */
public class SearchFilter {
    public String getBeginDate() {

        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }


    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getArts() {
        return arts;
    }

    public void setArts(Boolean arts) {
        this.arts = arts;
    }

    public Boolean getFashionStyle() {
        return fashionStyle;
    }

    public void setFashionStyle(Boolean fashionStyle) {
        this.fashionStyle = fashionStyle;
    }

    public Boolean getSports() {
        return sports;
    }

    public void setSports(Boolean sports) {
        this.sports = sports;
    }


    private String beginDate;
    private String sortOrder;
    private Boolean arts = false;
    private Boolean fashionStyle = false;
    private Boolean sports = false;
}
