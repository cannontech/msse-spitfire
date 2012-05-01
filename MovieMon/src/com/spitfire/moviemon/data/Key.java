package com.spitfire.moviemon.data;

import android.text.method.DateTimeKeyListener;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Todd
 * Date: 4/25/12
 * Time: 7:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Key {

    @SerializedName("Comment")
    private String comment;

    @SerializedName("IsInQueue")
    private Boolean isInQueue;

    @SerializedName("NetflixId")
    private String netflixId;

    @SerializedName("Rating")
    private Integer rating;
    
    @SerializedName("RottenTomatoesId")
    private String rottenTomatoesId;

    @SerializedName("wasWatched")
    private Boolean wasWatched;

    @SerializedName("WatchedDateTime")
    private Date watchedDateTime;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getInQueue() {
        return isInQueue;
    }

    public void setInQueue(Boolean inQueue) {
        isInQueue = inQueue;
    }

    public String getNetflixId() {
        return netflixId;
    }

    public void setNetflixId(String netflixId) {
        this.netflixId = netflixId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getRottenTomatoesId() {
        return rottenTomatoesId;
    }

    public void setRottenTomatoesId(String rottenTomatoesId) {
        this.rottenTomatoesId = rottenTomatoesId;
    }

    public Boolean getWasWatched() {
        return wasWatched;
    }

    public void setWasWatched(Boolean wasWatched) {
        this.wasWatched = wasWatched;
    }

    public Date getWatchedDateTime() {
        return watchedDateTime;
    }

    public void setWatchedDateTime(Date watchedDateTime) {
        this.watchedDateTime = watchedDateTime;
    }
}

