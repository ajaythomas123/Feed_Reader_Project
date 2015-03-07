package com.tiramisu.feedreadermk4;

import android.graphics.Bitmap;

/**
 * Created by ASUS on 17-01-2015.
 */
public class StreamResult {
    String title;
    String content;
    String feedId;
    String enContent;
    String itemUrl;
    Bitmap thumbnail;

    public StreamResult(String title, String content, String feedId, String enContent, String itemUrl, Bitmap thumbnail) {
        this.title = title;
        this.content = content;
        this.feedId = feedId;
        this.enContent = enContent;
        this.itemUrl = itemUrl;
        this.thumbnail = thumbnail;
    }
    public String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }
    public String getFeedId(){
        return feedId;
    }
    public String getEnContent(){
        return enContent;
    }
    public String getItemUrl(){
        return itemUrl;
    }
    public Bitmap getThumbnail(){
        return thumbnail;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setFeedId(String feedId){
        this.feedId = feedId;
    }
    public void setEnContent(String enContent){
        this.enContent = enContent;
    }
    public void setItemUrl(String itemUrl){
        this.itemUrl = itemUrl;
    }
    public void setThumbnail(Bitmap thumbnail){
        this.thumbnail = thumbnail;
    }
}
