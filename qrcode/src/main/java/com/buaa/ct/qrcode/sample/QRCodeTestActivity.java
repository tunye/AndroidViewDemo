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
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.buaa.ct.qrcode.QRCode;
import com.buaa.ct.qrcode.R;
import com.buaa.ct.qrcode.ui.CaptureFragment;
import com.buaa.ct.qrcode.util.QRCodeAnalyzeUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class QRCodeTestActivity extends AppCompatActivity {
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;

    public Button enter1, enter2, enter3, enter4, enter5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_test);
        CaptureFragment.onCreate(this);
        enter1 = findViewById(R.id.test_enter_1);
        enter2 = findViewById(R.id.test_enter_2);
        enter3 = findViewById(R.id.test_enter_3);
        enter4 = findViewById(R.id.test_enter_4);
        enter5 = findViewById(R.id.test_enter_5);
        setOnclickListener();
    }

    public void setOnclickListener() {
        enter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan(DEFAULT);
            }
        });
        enter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan(DEFAULT_CUSTOM);
            }
        });
        enter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QRCodeTestActivity.this, QRCodeProduceActivity.class));
            }
        });
        enter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan(REMOTE);
            }
        });
        enter5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(ScanUtil.getDocumentPickerIntent(ScanUtil.IMAGE), REQUEST_IMAGE);
            }
        });
    }

    private void startScan(@ScanType int type) {
        switch (type) {
            case DEFAULT:
                QRCode.startScan(this, REQUEST_CODE);
                break;
            case DEFAULT_CUSTOM:
                CustomQRCodeTestActivity.start(this, REQUEST_CODE, R.style.QRCodeTheme_Custom);
                break;
            case REMOTE:
                Intent intent = new Intent(QRCode.ACTION_DEFAULT_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理二维码扫描结果
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            //处理扫描结果（在界面上显示）
            handleScanResult(data);
        }

        //选择系统图片并解析
        else if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                getAnalyzeQRCodeResult(uri);
            }
        }
    }

    private void handleScanResult(Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (bundle.getInt(QRCode.RESULT_TYPE) == QRCode.RESULT_SUCCESS) {
                    String result = bundle.getString(QRCode.RESULT_DATA);
                    Toast.makeText(QRCodeTestActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(QRCode.RESULT_TYPE) == QRCode.RESULT_FAILED) {
                    Toast.makeText(QRCodeTestActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void getAnalyzeQRCodeResult(Uri uri) {
        QRCode.analyzeQRCode(PathUtils.getFilePathByUri(this, uri), new QRCodeAnalyzeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                Toast.makeText(QRCodeTestActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAnalyzeFailed() {
                Toast.makeText(QRCodeTestActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static final int DEFAULT = 0;
    public static final int DEFAULT_CUSTOM = 1;
    public static final int REMOTE = 2;
    public static final int CUSTOM_SINGLE = 3;
    public static final int CUSTOM_MULTIPLE = 4;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DEFAULT, DEFAULT_CUSTOM, REMOTE, CUSTOM_SINGLE, CUSTOM_MULTIPLE})
    public @interface ScanType {
    }
}
