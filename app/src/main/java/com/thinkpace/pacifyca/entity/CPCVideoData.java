package com.thinkpace.pacifyca.entity;

import java.util.ArrayList;

/**
 * Created by sapna on 15-03-2017.
 */

public class CPCVideoData implements IPCDataModel {
    public ArrayList<CPCVideoList> getVideoLists() {
        if (videoList == null)
            videoList = new ArrayList<>();
        return videoList;
    }

    public void setVideoList(ArrayList<CPCVideoList> videoList) {
        this.videoList = videoList;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    private ArrayList<CPCVideoList> videoList;

    private String subname;


}
