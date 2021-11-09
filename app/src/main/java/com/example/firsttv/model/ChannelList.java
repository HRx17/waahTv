package com.example.firsttv.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChannelList {

    @SerializedName("channelName")
    @Expose
    private String channelName;
    @SerializedName("channellogo")
    @Expose
    private String channellogo;
    @SerializedName("channelurl")
    @Expose
    private String channelurl;
    @SerializedName("seasons")
    @Expose
    private List<Object> seasons = null;

    public String getChannelName() {
        return channelName;
    }


    public String getChannellogo() {
        return channellogo;
    }


    public String getChannelurl() {
        return channelurl;
    }

    public List<Object> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Object> seasons) {
        this.seasons = seasons;
    }

}
