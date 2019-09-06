package com.buaa.ct.imageselector.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.listener.OnRecycleViewItemClickListener;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.core.util.PermissionPool;
import com.buaa.ct.imageselector.MediaListManager;
import com.buaa.ct.imageselector.R;
import com.buaa.ct.imageselector.adapter.ImageListAdapter;
import com.buaa.ct.imageselector.model.LocalMedia;
import com.buaa.ct.imageselector.model.LocalMediaFolder;
import com.buaa.ct.imageselector.provider.LocalMediaLoader;
import com.buaa.ct.imageselector.utils.FileUtils;
import com.buaa.ct.imageselector.utils.GridSpacingItemDecoration;
import com.buaa.ct.imageselector.widget.FolderWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dee on 15/11/19.
 */
public class ImageSelectorActivity extends CoreBaseActivity {
    public final static int REQUEST_IMAGE = 66;
    public final static int REQUEST_CAMERA = 67;

    public final static int SPAN_COUNT = 4;
    public final static String BUNDLE_CAMERA_PATH = "CameraPath";

    public final static String REQUEST_OUTPUT = "outputList";

    public final static String EXTRA_SELECT_MODE = "SelectMode";
    public final static String EXTRA_SHOW_CAMERA = "ShowCamera";
    public final static String EXTRA_ENABLE_PREVIEW = "EnablePreview";
    public final static String EXTRA_ENABLE_CROP = "EnableCrop";
    public final static String EXTRA_MAX_SELECT_NUM = "MaxSelectNum";

    public final static int MODE_MULTIPLE = 1;
    public final static int MODE_SINGLE = 2;

    private int maxSelectNum = 9;
    private int selectMode = MODE_MULTIPLE;
    private boolean showCamera = true;
    private boolean enablePreview = true;
    private boolean enableCrop = false;
    private String cameraPath;

    private TextView previewText;
    private ImageListAdapter imageAdapter;
    private LinearLayout folderLayout;
    private TextView folderName;
    private FolderWindow folderWindow;

    public static void start(Activity activity, int maxSelectNum, int mode, boolean isShow, boolean enablePreview, boolean enableCrop) {
        Intent intent = new Intent(activity, ImageSelectorActivity.class);
        intent.putExtra(EXTRA_MAX_SELECT_NUM, maxSelectNum);
        intent.putExtra(EXTRA_SELECT_MODE, mode);
        intent.putExtra(EXTRA_SHOW_CAMERA, isShow);
        intent.putExtra(EXTRA_ENABLE_PREVIEW, enablePreview);
        intent.putExtra(EXTRA_ENABLE_CROP, enableCrop);
        activity.startActivityForResult(intent, REQUEST_IMAGE);
    }

    public static String startCameraDirect(Context context) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(context.getPackageManager()) != null) {
            File cameraFile = FileUtils.createCameraFile(context);
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // android N获取uri的新方式
                uri = FileProvider.getUriForFile(context, context.getPackageName(), cameraFile);
            } else {
                uri = Uri.fromFile(cameraFile);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            ((Activity) context).startActivityForResult(cameraIntent, REQUEST_CAMERA);
            return cameraFile.getAbsolutePath();
        } else {
            return null;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_selector;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        folderWindow = new FolderWindow(this);
        previewText = findViewById(R.id.preview_text);
        folderLayout = findViewById(R.id.folder_layout);
        folderName = findViewById(R.id.folder_name);

        RecyclerView recyclerView = findViewById(R.id.folder_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(SPAN_COUNT, RuntimeManager.getInstance().dip2px(2), false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        imageAdapter = new ImageListAdapter(this, maxSelectNum, selectMode, showCamera, enablePreview);
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void setListener() {
        super.setListener();
        folderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (folderWindow.isShowing()) {
                    folderWindow.dismiss();
                } else {
                    folderWindow.showAsDropDown(toolBarLayout);
                }
            }
        });
        toolbarOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectDone(imageAdapter.getSelectedImages());
            }
        });
        previewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPreview(0);
            }
        });
        imageAdapter.setOnImageSelectChangedListener(new ImageListAdapter.OnImageSelectChangedListener() {
            @Override
            public void onChange(List<LocalMedia> selectImages) {
                boolean enable = selectImages.size() != 0;
                toolbarOper.setEnabled(enable);
                previewText.setEnabled(enable);
                if (enable) {
                    toolbarOper.setText(getString(R.string.done_num, selectImages.size(), maxSelectNum));
                    previewText.setText(getString(R.string.preview_num, selectImages.size()));
                } else {
                    toolbarOper.setText(R.string.done);
                    previewText.setText(R.string.preview);
                }
            }

            @Override
            public void onTakePhoto() {
                requestMultiPermission(new int[]{PermissionPool.CAMERA}, new String[]{Manifest.permission.CAMERA});
            }

            @Override
            public void onPictureClick(LocalMedia media, int position) {
                if (enablePreview) {
                    startPreview(position);
                } else if (enableCrop) {
                    startCrop(media.getPath());
                } else {
                    onSelectDone(media.getPath());
                }
            }
        });
        folderWindow.setOnItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                folderWindow.dismiss();
                LocalMediaFolder curFolder = folderWindow.getCurFolderInfo(position);
                imageAdapter.setSource(curFolder.getImages());
                folderName.setText(curFolder.getName());
            }
        });
    }

    @Override
    public void beforeSetLayout(Bundle savedInstanceState) {
        super.beforeSetLayout(savedInstanceState);
        maxSelectNum = getIntent().getIntExtra(EXTRA_MAX_SELECT_NUM, 9);
        selectMode = getIntent().getIntExtra(EXTRA_SELECT_MODE, MODE_MULTIPLE);
        showCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        enablePreview = getIntent().getBooleanExtra(EXTRA_ENABLE_PREVIEW, true);
        enableCrop = getIntent().getBooleanExtra(EXTRA_ENABLE_CROP, false);

        if (selectMode == MODE_MULTIPLE) {
            enableCrop = false;
        } else {
            enablePreview = false;
        }

        if (savedInstanceState != null) {
            cameraPath = savedInstanceState.getString(BUNDLE_CAMERA_PATH);
        }

        requestMultiPermission(new int[]{PermissionPool.WRITE_EXTERNAL_STORAGE}, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        if (selectMode == MODE_MULTIPLE) {
            toolbarOper.setText(R.string.done);
        } else {
            toolbarOper.setText("");
        }
        previewText.setVisibility(enablePreview ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onAccreditSucceed(int requestCode) {
        super.onAccreditSucceed(requestCode);
        if (requestCode == PermissionPool.WRITE_EXTERNAL_STORAGE) {
            new LocalMediaLoader(this, LocalMediaLoader.TYPE_IMAGE).loadAllImage(new LocalMediaLoader.LocalMediaLoadListener() {

                @Override
                public void loadComplete(List<LocalMediaFolder> folders) {
                    folderWindow.bindFolder(folders);
                    imageAdapter.setSource(folders.get(0).getImages());
                    MediaListManager.getInstance().setMediaList(folders.get(0).getImages());
                }
            });
        } else if (requestCode == PermissionPool.CAMERA) {
            startCamera();
        }
    }

    @Override
    public void onAccreditFailure(int requestCode) {
        super.onAccreditFailure(requestCode);
        setResult(-2);
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // on take photo success
            if (requestCode == REQUEST_CAMERA) {
                Uri uri;
                File target = new File(cameraPath);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // android N获取uri的新方式
                    uri = FileProvider.getUriForFile(ImageSelectorActivity.this, getApplication().getPackageName(), target);
                } else {
                    uri = Uri.fromFile(target);
                }
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                if (enableCrop) {
                    startCrop(cameraPath);
                } else {
                    onSelectDone(cameraPath);
                }
            }
            //on preview select change
            else if (requestCode == ImagePreviewActivity.REQUEST_PREVIEW) {
                boolean isDone = data.getBooleanExtra(ImagePreviewActivity.OUTPUT_ISDONE, false);
                List<LocalMedia> images = (List<LocalMedia>) data.getSerializableExtra(ImagePreviewActivity.OUTPUT_LIST);
                if (isDone) {
                    onSelectDone(images);
                } else {
                    imageAdapter.bindSelectImages(images);
                }
            }
            // on crop success
            else if (requestCode == ImageCropActivity.REQUEST_CROP) {
                String path = data.getStringExtra(ImageCropActivity.OUTPUT_PATH);
                onSelectDone(path);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_CAMERA_PATH, cameraPath);
        super.onSaveInstanceState(outState);
    }

    /**
     * start to camera、preview、crop
     */
    private void startCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File cameraFile = FileUtils.createCameraFile(this);
            cameraPath = cameraFile.getAbsolutePath();
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // android N获取uri的新方式
                uri = FileProvider.getUriForFile(ImageSelectorActivity.this, getApplication().getPackageName(), cameraFile);
            } else {
                uri = Uri.fromFile(cameraFile);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    public void startPreview(int position) {
        ImagePreviewActivity.startPreview(this, imageAdapter.getSelectedImages(), maxSelectNum, position);
    }

    public void startCrop(String path) {
        ImageCropActivity.startCrop(this, path);
    }

    /**
     * on select done
     *
     * @param medias
     */
    public void onSelectDone(List<LocalMedia> medias) {
        ArrayList<String> images = new ArrayList<>();
        for (LocalMedia media : medias) {
            images.add(media.getPath());
        }
        onResult(images);
    }

    public void onSelectDone(String path) {
        ArrayList<String> images = new ArrayList<>();
        images.add(path);
        onResult(images);
    }

    public void onResult(ArrayList<String> images) {
        setResult(RESULT_OK, new Intent().putStringArrayListExtra(REQUEST_OUTPUT, images));
        finish();
    }
}
