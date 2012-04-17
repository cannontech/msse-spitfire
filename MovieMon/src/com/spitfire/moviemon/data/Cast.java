package com.spitfire.moviemon.data;

import com.google.gson.annotations.SerializedName;

public class Cast {

    @SerializedName("Name")
    private String name;

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}
