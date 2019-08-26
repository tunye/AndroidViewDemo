/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.buaa.ct.qrcode.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.buaa.ct.qrcode.QRCode;
import com.buaa.ct.qrcode.R;
import com.buaa.ct.qrcode.util.QRCodeProduceUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <pre>
 *     desc   : 二维码生成界面
 *     author : xuexiang
 *     time   : 2018/5/5 下午11:06
 * </pre>
 */
public class QRCodeProduceActivity extends AppCompatActivity implements View.OnClickListener {
    private final int SELECT_FILE_REQUEST_CODE = 700;

    /**
     * 二维码背景图片
     */
    private Bitmap backgroundImage = null;

    EditText mEtInput;
    ImageView mIvQrcode;

    SwitchCompat mScChange;

    LinearLayout mLLNormalCreate;

    LinearLayout mLLComplexCreate;
    EditText mEtSize;
    EditText mEtMargin;
    EditText mEtDotScale;

    CheckBox mCbAutoColor;
    EditText mEtColorDark;
    EditText mEtColorLight;

    CheckBox mCbWhiteMargin;
    CheckBox mCbBinarize;
    EditText mEtBinarizeThreshold;


    private boolean isQRCodeCreated = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_produce);

        mEtInput = findViewById(R.id.et_input);
        mIvQrcode = findViewById(R.id.iv_qrcode);
        mScChange = findViewById(R.id.sc_change);
        mLLNormalCreate = findViewById(R.id.ll_normal_create);

        mLLComplexCreate = findViewById(R.id.ll_complex_create);
        mEtSize = findViewById(R.id.et_size);
        mEtMargin = findViewById(R.id.et_margin);
        mEtDotScale = findViewById(R.id.et_dotScale);

        mCbAutoColor = findViewById(R.id.cb_autoColor);
        mEtColorDark = findViewById(R.id.et_colorDark);
        mEtColorLight = findViewById(R.id.et_colorLight);

        mCbWhiteMargin = findViewById(R.id.cb_whiteMargin);
        mCbBinarize = findViewById(R.id.cb_binarize);
        mEtBinarizeThreshold = findViewById(R.id.et_binarizeThreshold);
        initListeners();
    }

    protected void initListeners() {
        mScChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mLLComplexCreate.setVisibility(View.VISIBLE);
                    mLLNormalCreate.setVisibility(View.GONE);
                } else {
                    mLLComplexCreate.setVisibility(View.GONE);
                    mLLNormalCreate.setVisibility(View.VISIBLE);
                }
            }
        });

        mCbAutoColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEtColorDark.setEnabled(!isChecked);
                mEtColorLight.setEnabled(!isChecked);
            }
        });

        mCbBinarize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEtBinarizeThreshold.setEnabled(isChecked);
            }
        });

        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_create_no_logo).setOnClickListener(this);
        findViewById(R.id.btn_create_with_logo).setOnClickListener(this);
        findViewById(R.id.btn_background_image).setOnClickListener(this);
        findViewById(R.id.btn_remove_background_image).setOnClickListener(this);
        findViewById(R.id.btn_create).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_save) {
            saveQRCode();
        } else if (id == R.id.btn_create_no_logo) {
            if (TextUtils.isEmpty(mEtInput.getEditableText().toString())) {
                Toast.makeText(this, "请输入二维码内容!", Toast.LENGTH_SHORT).show();
                return;
            }

            createQRCodeWithLogo(null);
        } else if (id == R.id.btn_create_with_logo) {
            if (TextUtils.isEmpty(mEtInput.getEditableText().toString())) {
                Toast.makeText(this, "请输入二维码内容!", Toast.LENGTH_SHORT).show();
                return;
            }
            createQRCodeWithLogo(((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap());
        } else if (id == R.id.btn_background_image) {
            startActivityForResult(ScanUtil.getDocumentPickerIntent(ScanUtil.IMAGE), SELECT_FILE_REQUEST_CODE);
        } else if (id == R.id.btn_remove_background_image) {
            backgroundImage = null;
            Toast.makeText(this, "背景图片已被去除!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_create) {
            if (TextUtils.isEmpty(mEtInput.getEditableText().toString())) {
                Toast.makeText(this, "请输入二维码内容!", Toast.LENGTH_SHORT).show();
                return;
            }
            createQRCodeWithBackgroundImage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE_REQUEST_CODE && resultCode == RESULT_OK && data.getData() != null) {
            try {
                Uri imageUri = data.getData();
                backgroundImage = BitmapFactory.decodeFile(PathUtils.getFilePathByUri(this, imageUri));
                Toast.makeText(this, "成功添加背景图片!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "添加背景图片失败!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveQRCode() {
        if (isQRCodeCreated) {
            boolean result = save(mIvQrcode.getDrawingCache(false), new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "QRCode_" + System.currentTimeMillis() + ".png"), Bitmap.CompressFormat.PNG, false);
            Toast.makeText(this, "二维码保存到相册" + (result ? "成功" : "失败"), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请先生成二维码!", Toast.LENGTH_SHORT).show();
        }
    }

    private void createQRCodeWithLogo(Bitmap logo) {
        showQRCode(QRCode.createQRCodeWithLogo(mEtInput.getText().toString(), 400, 400, logo));
        isQRCodeCreated = true;
    }

    private void showQRCode(Bitmap QRCode) {
        mIvQrcode.setImageBitmap(QRCode);
    }

    private void createQRCodeWithBackgroundImage() {
        QRCodeProduceUtils.Builder builder = QRCode.newQRCodeBuilder(mEtInput.getText().toString())
                .setAutoColor(mCbAutoColor.isChecked())
                .setWhiteMargin(mCbWhiteMargin.isChecked())
                .setBinarize(mCbBinarize.isChecked())
                .setBackgroundImage(backgroundImage);
        if (mEtSize.getText().length() != 0) {
            builder.setSize(Integer.parseInt(mEtSize.getText().toString(), 400));
        }
        if (mEtMargin.getText().length() != 0) {
            builder.setMargin(Integer.parseInt(mEtMargin.getText().toString(), 20));
        }
        if (mEtDotScale.getText().length() != 0) {
            builder.setDataDotScale(Float.parseFloat(mEtDotScale.getText().toString()));
        }
        if (mEtDotScale.getText().length() != 0) {
            builder.setDataDotScale(Float.parseFloat(mEtDotScale.getText().toString()));
        }
        if (!mCbAutoColor.isChecked()) {
            try {
                builder.setColorDark(Color.parseColor(mEtColorDark.getText().toString()));
                builder.setColorLight(Color.parseColor(mEtColorLight.getText().toString()));
            } catch (Exception e) {
                Toast.makeText(this, "色值填写出错!", Toast.LENGTH_SHORT).show();
            }
        }
        if (mEtBinarizeThreshold.getText().length() != 0) {
            builder.setBinarizeThreshold(Integer.parseInt(mEtBinarizeThreshold.getText().toString(), 128));
        }

        showQRCode(builder.build());
        isQRCodeCreated = true;
    }

    public static boolean save(final Bitmap src,
                               final File file,
                               final Bitmap.CompressFormat format,
                               final boolean recycle) {
        if (src == null || !createFileByDeleteOldFile(file)) return false;
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled()) src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    private static boolean createFileByDeleteOldFile(final File file) {
        if (file == null) return false;
        if (file.exists() && !file.delete()) return false;
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }
}
