package com.tiramisu.feedreadermk4;

import android.graphics.Bitmap;

/**
 * Created by ASUS on 16-01-2015.
 */
public class SearchResult {
    String feedId;
    String title;
    String description;
    String contentType;
    String subscribers;
    Bitmap sIcon;
    Bitmap lIcon;

    public SearchResult(String feedId, String title, String description, String contentType, String subscribers, Bitmap sIcon, Bitmap lIcon) {
        this.feedId = feedId;
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.subscribers = subscribers;
        this.sIcon = sIcon;
        this.lIcon = lIcon;
    }

    public String getFeedId() {
        return feedId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContentType() {
        return contentType;
    }

    public String getSubscribers() {
        return subscribers;
    }

    public Bitmap getsIcon() {
        return sIcon;
    }

    public Bitmap getlIcon() {
        return lIcon;
    }


    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setSubscribers(String subscribers) {
        this.subscribers = subscribers;
    }

    public void setsIcon(Bitmap sIcon) {
        this.sIcon = sIcon;
    }

    public void setlIcon(Bitmap lIcon) {
        this.lIcon = lIcon;
    }
}