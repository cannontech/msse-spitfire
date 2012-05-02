package com.spitfire.moviemon.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by IntelliJ IDEA.
 * User: Todd
 * Date: 5/1/12
 * Time: 9:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class MovieReservation {

    @SerializedName("ProviderName")
    private String providerName;

    @SerializedName("Title")
    private String title;

    @SerializedName("Format")
    private String format;

    @SerializedName("Longitude")
    private String longitude;

    @SerializedName("Latitude")
    private String latitude;

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
