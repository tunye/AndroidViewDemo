package com.buaa.ct.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.buaa.ct.core.view.CustomToast;

public class MusicTools {

    public static void goMusicTools(Context context, String scheme) {
        try {
            Uri data = Uri.parse(scheme);
            Intent intent = new Intent(Intent.ACTION_VIEW, data);
            intent.setPackage("com.iyuba.music");
            context.startActivity(intent);
        } catch (Exception e) {
            CustomToast.getInstance().showToast("你没有安装测试版听歌");
        }
    }
}
