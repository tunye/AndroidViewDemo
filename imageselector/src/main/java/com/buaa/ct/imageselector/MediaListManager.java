package com.buaa.ct.imageselector;

import com.buaa.ct.imageselector.model.LocalMedia;

import java.util.List;

public class MediaListManager {
    List<LocalMedia> mediaList;
    private static class InstanceHelper{
        private static MediaListManager instance=new MediaListManager();
    }

    public static MediaListManager getInstance(){
        return InstanceHelper.instance;
    }

    public List<LocalMedia> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<LocalMedia> mediaList) {
        this.mediaList = mediaList;
    }
}