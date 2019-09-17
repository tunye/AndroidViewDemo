package com.buaa.ct.imageselector;

import com.buaa.ct.imageselector.model.LocalMedia;

import java.util.List;

public class MediaListManager {
    private List<LocalMedia> mediaList;

    public static MediaListManager getInstance() {
        return InstanceHelper.instance;
    }

    public List<LocalMedia> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<LocalMedia> mediaList) {
        this.mediaList = mediaList;
    }

    private static class InstanceHelper {
        private static MediaListManager instance = new MediaListManager();
    }
}
