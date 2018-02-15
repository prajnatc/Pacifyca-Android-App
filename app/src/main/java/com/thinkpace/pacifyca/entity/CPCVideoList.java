package com.thinkpace.pacifyca.entity;

/**
 * Created by sapna on 15-03-2017.
 */

public class CPCVideoList implements IPCDataModel {

    private String videoTitle;

    private String videoLink;

    private String sentDate;

    public String getTitle() {
        return videoTitle;
    }

    public void setTitle(String title) {
        this.videoTitle = title;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate){
        this.sentDate = sentDate;
    }

}
