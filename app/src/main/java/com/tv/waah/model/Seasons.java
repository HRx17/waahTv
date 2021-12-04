package com.tv.waah.model;

import java.util.List;

public class Seasons {
    String season;
    List<ChannelList> channelUrl;

    public Seasons(String season, List<ChannelList> channelListList) {
        this.season = season;
        this.channelUrl = channelListList;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public List<ChannelList> getChannelListList() {
        return channelUrl;
    }

    public void setChannelListList(List<ChannelList> channelListList) {
        this.channelUrl = channelListList;
    }
}
