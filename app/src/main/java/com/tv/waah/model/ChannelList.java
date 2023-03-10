package com.tv.waah.model;

import com.tv.waah.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChannelList {

    @SerializedName("channelName")
    @Expose
    private final String channelName;
    @SerializedName("channellogo")
    @Expose
    private final String channellogo;

    public ChannelList(String channelName, String channellogo, String channelurl) {
        this.channelName = channelName;
        this.channellogo = String.valueOf(R.drawable.movie);
        this.channelurl = channelurl;
    }

    @SerializedName("channelurl")
    @Expose
    private final String channelurl;
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
