package com.moviemon.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RelatedImages{

    @SerializedName("Size")
   	private String size;

    @SerializedName("Url")
   	private String url;

 	public String getSize(){
		return this.size;
	}
	public void setSize(String size){
		this.size = size;
	}
 	public String getUrl(){
		return this.url;
	}
	public void setUrl(String url){
		this.url = url;
	}
}
