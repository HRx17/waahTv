package com.tv.waah.model;

import java.util.List;

public class Live {
    public String title;
    public String id;
    public String category;
    public String liveImageUrl;
    public List<Seasons> seasons;

    public Live(List<Seasons> seasons) {
        this.seasons = seasons;
    }

    public List<Seasons> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Seasons> seasons) {
        this.seasons = seasons;
    }

    public String getLiveImageUrl() {
        return liveImageUrl;
    }

    public void setLiveImageUrl(String liveImageUrl) {
        this.liveImageUrl = liveImageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void String(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Live() {
    }

    public Live(String title, String liveImageUrl, String category) {
        this.title = title;
        this.category = category;
        this.liveImageUrl = liveImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackgroundImageUrl() { return null;
    }
}
