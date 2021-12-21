package com.tv.waah.data;

import java.util.List;

import android.net.Uri;

public interface OnAppUpdateListener {
    public void appUpdateStatus(boolean isLatestVersion, String latestVersionName, List<String> changelog, Uri downloadUrl);
}