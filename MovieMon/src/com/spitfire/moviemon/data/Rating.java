package com.spitfire.moviemon.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by IntelliJ IDEA.
 * User: Todd
 * Date: 4/16/12
 * Time: 7:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class Rating {

    @SerializedName("CriticsScore")
    private String criticsScore;

    @SerializedName("AudienceScore")
    private String audienceScore;

    public String getCriticsScore(){
        return this.criticsScore;
    }
    public void setCriticsScore(String criticsScore){
        this.criticsScore = criticsScore;
    }
    public String getAudienceScore(){
        return this.audienceScore;
    }
    public void setAudienceScore(String audienceScore){
        this.audienceScore = audienceScore;
    }
           
}
