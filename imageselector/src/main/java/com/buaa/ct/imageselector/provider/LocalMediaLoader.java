package com.buaa.ct.imageselector.provider;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.widget.Toast;

import com.buaa.ct.imageselector.R;
import com.buaa.ct.imageselector.model.LocalMedia;
import com.buaa.ct.imageselector.model.LocalMediaFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by dee on 15/11/19.
 */
public class LocalMediaLoader {
    // load type
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    private final static String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID};

    private final static String[] VIDEO_PROJECTION = {
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DURATION};

    private int type = TYPE_IMAGE;
    private FragmentActivity activity;

    public LocalMediaLoader(FragmentActivity activity, int type) {
        this.activity = activity;
        this.type = type;
    }

    public void loadAllImage(final LocalMediaLoadListener imageLoadListener) {
        LoaderManager.getInstance(activity).initLoader(type, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader cursorLoader = null;
                if (id == TYPE_IMAGE) {
                    cursorLoader = new CursorLoader(
                            activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_PROJECTION, MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                            new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
                } else if (id == TYPE_VIDEO) {
                    cursorLoader = new CursorLoader(
                            activity, MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            VIDEO_PROJECTION, null, null, VIDEO_PROJECTION[2] + " DESC");
                }
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                ArrayList<LocalMediaFolder> imageFolders = new ArrayList<>();
                LocalMediaFolder allImageFolder = new LocalMediaFolder();
                List<LocalMedia> allImages = new ArrayList<>();

                if (data != null && data.getCount() > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        // 如原图路径不存在或者路径存在但文件不存在,就结束当前循环
                        if (TextUtils.isEmpty(path)) {
                            continue;
                        } else if (!new File(path).exists() || new File(path).length() < 30720) {
                            continue;
                        } else if (path.contains("/.") || path.contains("/Android/data/")) {
                            continue;
                        }
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int duration = (type == TYPE_VIDEO ? data.getInt(data.getColumnIndexOrThrow(VIDEO_PROJECTION[4])) : 0);

                        LocalMedia image = new LocalMedia(path, dateTime, duration);
                        LocalMediaFolder folder = getImageFolder(path, imageFolders);

                        folder.getImages().add(image);
                        folder.setImageNum(folder.getImageNum() + 1);

                        allImages.add(image);
                        allImageFolder.setImageNum(allImageFolder.getImageNum() + 1);
                    } while (data.moveToNext());
                    allImageFolder.setFirstImagePath(allImages.get(0).getPath());
                    allImageFolder.setName(activity.getString(R.string.all_image));
                    allImageFolder.setImages(allImages);
                    sortFolder(imageFolders);
                    imageFolders.add(0, allImageFolder);
                    imageLoadListener.loadComplete(imageFolders);
                } else {
                    Toast.makeText(activity, R.string.no_pic, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            }
        });
    }

    private void sortFolder(List<LocalMediaFolder> imageFolders) {
        // 文件夹按图片数量排序
        Collections.sort(imageFolders, new Comparator<LocalMediaFolder>() {
            @Override
            public int compare(LocalMediaFolder lhs, LocalMediaFolder rhs) {
                if (lhs.getImages() == null || rhs.getImages() == null) {
                    return 0;
                }
                int lsize = lhs.getImageNum();
                int rsize = rhs.getImageNum();
                return Integer.compare(rsize, lsize);
            }
        });
    }

    private LocalMediaFolder getImageFolder(String path, List<LocalMediaFolder> imageFolders) {
        File imageFile = new File(path);
        File folderFile = imageFile.getParentFile();

        for (LocalMediaFolder folder : imageFolders) {
            if (folder.getName().equals(folderFile.getName())) {
                return folder;
            }
        }
        LocalMediaFolder newFolder = new LocalMediaFolder();
        newFolder.setName(folderFile.getName());
        newFolder.setPath(folderFile.getAbsolutePath());
        newFolder.setFirstImagePath(path);
        imageFolders.add(newFolder);
        return newFolder;
    }

    public interface LocalMediaLoadListener {
        void loadComplete(List<LocalMediaFolder> folders);
    }
}
