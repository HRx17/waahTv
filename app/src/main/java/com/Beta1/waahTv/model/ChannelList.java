package com.Beta1.waahTv.model;

import com.Beta1.waahTv.R;
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

    public ChannelList(String channelName, String channellogo, String channelurl) {
        this.channelName = channelName;
        this.channellogo = String.valueOf(R.drawable.movie);
        this.channelurl = channelurl;
    }

    @SerializedName("channelurl")
    @Expose
    private String channelurl;
    @SerializedName("seasons")
    @Expose
    private List<Seasons> seasons = null;

    public String getChannelName() {
        return channelName;
    }


    public String getChannellogo() {
        return channellogo;
    }


    public String getChannelurl() {
        return channelurl;
    }

    public List<Seasons> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Seasons> seasons) {
        this.seasons = seasons;
    }

}
