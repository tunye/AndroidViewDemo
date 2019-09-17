package com.buaa.ct.imageselector.sample;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.imageselector.MediaListManager;
import com.buaa.ct.imageselector.R;
import com.buaa.ct.imageselector.view.ImageSelectorActivity;
import com.buaa.ct.imageselector.view.OnlyPreviewActivity;

import java.util.ArrayList;
import java.util.List;

public class ImageSelectorTestActivity extends CoreBaseActivity {
    private ImageButton minus;
    private ImageButton plus;
    private EditText selectNumText;

    private RadioGroup selectMode;
    private RadioGroup showCamera;
    private RadioGroup enablePreview;
    private RadioGroup enableCrop;

    private Button selectPicture;

    private int maxSelectNum = 9;
    private String fileUrl;

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_selector_test_enter;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
        selectNumText = findViewById(R.id.select_num);

        selectMode = findViewById(R.id.select_mode);
        showCamera = findViewById(R.id.show_camera);
        enablePreview = findViewById(R.id.enable_preview);
        enableCrop = findViewById(R.id.enable_crop);

        selectPicture = findViewById(R.id.select_picture);
    }

    @Override
    public void setListener() {
        super.setListener();
        selectMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.mode_single) {
                    enableCrop.check(R.id.crop_enable);
                    findViewById(R.id.crop_enable).setEnabled(true);

                    enablePreview.check(R.id.preview_disable);
                    findViewById(R.id.preview_enable).setEnabled(false);
                } else {
                    enableCrop.check(R.id.crop_disable);
                    findViewById(R.id.crop_enable).setEnabled(false);

                    enablePreview.check(R.id.preview_enable);
                    findViewById(R.id.preview_enable).setEnabled(true);
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxSelectNum--;
                selectNumText.setText(maxSelectNum + "");
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxSelectNum++;
                selectNumText.setText(maxSelectNum + "");
            }
        });
        selectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mode = selectMode.getCheckedRadioButtonId() == R.id.mode_multiple ? ImageSelectorActivity.MODE_MULTIPLE : ImageSelectorActivity.MODE_SINGLE;
                boolean isShow = showCamera.getCheckedRadioButtonId() == R.id.camera_yes ? true : false;
                boolean isPreview = enablePreview.getCheckedRadioButtonId() == R.id.preview_enable ? true : false;
                boolean isCrop = enableCrop.getCheckedRadioButtonId() == R.id.crop_enable ? true : false;
                ImageSelectorActivity.start(ImageSelectorTestActivity.this, maxSelectNum, mode, isShow, isPreview, isCrop);
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.test_select_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            startActivity(new Intent(this, SelectResultActivity.class).putExtra(SelectResultActivity.EXTRA_IMAGES, images));
        } else if (resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_CAMERA) {
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            startActivity(new Intent(this, SelectResultActivity.class).putExtra(SelectResultActivity.EXTRA_IMAGES, images));
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "取消选择照片", Toast.LENGTH_SHORT).show();
        } else if (resultCode == -2) {
            Toast.makeText(this, "无权限", Toast.LENGTH_SHORT).show();
        }
    }
}
