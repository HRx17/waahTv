package com.example.firsttv.model;

import androidx.leanback.widget.HeaderItem;

public class HeaderItemModel extends HeaderItem {

    private static final String TAG =
            HeaderItemModel.class.getSimpleName();
    public static final int ICON_NONE = -1;
    private int mIconResId = ICON_NONE;

    // Title + Icon
    public HeaderItemModel(long id, String name, int iconResId) {
        super(id, name);
        mIconResId = iconResId;
    }

    // No icon, label only
    // Title label
    public HeaderItemModel(int id, String name, String liveImageUrl) {
        super(name);
    }

    public int getIconResId() {
        return mIconResId;
    }

    public void setIconResId(int iconResId) {
        this.mIconResId = iconResId;
    }
}

