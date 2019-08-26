package com.buaa.ct.qrcode.sample;

import android.content.Intent;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ScanUtil {
    public static final String IMAGE = "image/*";
    public static final String AUDIO = "audio/*";
    public static final String VIDEO = "video/*";
    public static final String ANY = "*/*";
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({IMAGE, AUDIO, VIDEO, ANY})
    public @interface DocumentType {
    }

    public static Intent getDocumentPickerIntent(@DocumentType String documentType) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (!TextUtils.isEmpty(documentType)) {
            return intent.setType(documentType);
        } else {
            return intent.setType(ANY);
        }
    }
}
