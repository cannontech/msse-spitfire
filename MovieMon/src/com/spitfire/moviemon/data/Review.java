package com.spitfire.moviemon.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by IntelliJ IDEA.
 * User: Todd
 * Date: 4/16/12
 * Time: 7:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Review {

    @SerializedName("Comment")
    private String comment;

    @SerializedName("Critic")
    private String critic;

    @SerializedName("Rating")
    private String rating;

    @SerializedName("ReviewProviderName")
    private String reviewProviderName;


    public String getReviewProviderName() {
        return reviewProviderName;
    }
    public void setReviewProviderName(String reviewProviderName) {
        this.reviewProviderName = reviewProviderName;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getCritic() {
        return critic;
    }
    public void setCritic(String critic) {
        this.critic = critic;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
}
