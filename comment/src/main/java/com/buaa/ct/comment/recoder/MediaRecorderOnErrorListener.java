package com.buaa.ct.comment.recoder;


public interface MediaRecorderOnErrorListener extends android.media.MediaRecorder.OnErrorListener {

    public abstract void onError(int i, String s);
}
