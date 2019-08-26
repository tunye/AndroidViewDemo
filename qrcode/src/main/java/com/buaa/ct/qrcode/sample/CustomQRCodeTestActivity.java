package com.buaa.ct.qrcode.sample;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Toast;

import com.buaa.ct.qrcode.QRCode;
import com.buaa.ct.qrcode.R;
import com.buaa.ct.qrcode.ui.CaptureActivity;


/**
 * 自定义二维码扫描界面
 *
 * @author xuexiang
 * @since 2019/5/30 10:43
 */
public class CustomQRCodeTestActivity extends CaptureActivity implements View.OnClickListener {

    private AppCompatImageView mIvFlashLight;
    private AppCompatImageView mIvFlashLight1;

    private boolean mIsOpen;

    /**
     * 开始二维码扫描
     *
     * @param fragment
     * @param requestCode 请求码
     * @param theme       主题
     */
    public static void start(Fragment fragment, int requestCode, int theme) {
        Intent intent = new Intent(fragment.getContext(), CustomQRCodeTestActivity.class);
        intent.putExtra(KEY_CAPTURE_THEME, theme);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 开始二维码扫描
     *
     * @param activity
     * @param requestCode 请求码
     * @param theme       主题
     */
    public static void start(Activity activity, int requestCode, int theme) {
        Intent intent = new Intent(activity, CustomQRCodeTestActivity.class);
        intent.putExtra(KEY_CAPTURE_THEME, theme);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getCaptureLayoutId() {
        return R.layout.qrcode_custom_test;
    }

    @Override
    protected void beforeCapture() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        mIvFlashLight = findViewById(R.id.iv_flash_light);
        mIvFlashLight1 = findViewById(R.id.iv_flash_light1);
    }

    @Override
    protected void onCameraInitSuccess() {
        mIvFlashLight.setVisibility(View.VISIBLE);
        mIvFlashLight1.setVisibility(View.VISIBLE);

        mIsOpen = QRCode.isFlashLightOpen();
        refreshFlashIcon();
        mIvFlashLight.setOnClickListener(this);
        mIvFlashLight1.setOnClickListener(this);
    }

    @Override
    protected void onCameraInitFailed() {
        mIvFlashLight.setVisibility(View.GONE);
        mIvFlashLight1.setVisibility(View.GONE);
    }

    private void refreshFlashIcon() {
        if (mIsOpen) {
            mIvFlashLight.setImageResource(R.drawable.ic_flash_light_on);
            mIvFlashLight1.setImageResource(R.drawable.ic_flash_light_open);
        } else {
            mIvFlashLight.setImageResource(R.drawable.ic_flash_light_off);
            mIvFlashLight1.setImageResource(R.drawable.ic_flash_light_close);
        }
    }

    private void switchFlashLight() {
        mIsOpen = !mIsOpen;
        try {
            QRCode.switchFlashLight(mIsOpen);
            refreshFlashIcon();
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(this,"设备不支持闪光灯", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.iv_flash_light || id == R.id.iv_flash_light1) {
            switchFlashLight();
        }
    }
}
