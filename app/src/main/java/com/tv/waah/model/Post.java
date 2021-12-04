package com.tv.waah.model;

public class Post {

    private final String id;
    private final String displayId;
    private final String categoryType;
    private final String displayName;
    private final String iconUrl;
    private final String lastUpdatedTime;
    private final String userEmail;

    public Post(String id, String displayid, String displayName, String categoryType, String iconUrl, String lastupdatetime, String useremail) {
        this.id = id;
        this.displayId = displayid;
        this.categoryType = categoryType;
        this.displayName = displayName;
        this.iconUrl = iconUrl;
        this.lastUpdatedTime = lastupdatetime;
        this.userEmail = useremail;
    }

    public String getId() {
        return id;
    }

    public String getDisplayid() {
        return displayId;
    }

    public String getIconUrl() { return iconUrl; }

    public String getlastUpdatetime() {
        return lastUpdatedTime;
    }

    public String getuserEmail() {
        return userEmail;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCategoryType() {
        return categoryType;
    }
}
