/*
 * Created by chentong1 on 2019.8.20
 */

package com.buaa.ct.myapplication.sample.pudding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Toast;

import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.core.util.GetAppColor;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.activity.TestActivity;
import com.buaa.ct.myapplication.sample.base.BaseActivity;
import com.buaa.ct.pudding.callback.PuddingCallBack;
import com.buaa.ct.pudding.util.PuddingBuilder;


public class PuddingTestActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.layout_pudding_test;
    }

    public void click1(View view) {
        new PuddingBuilder().setTitleText(RuntimeManager.getInstance().getString(R.string.pudding_title))
                .setSubTitleText(RuntimeManager.getInstance().getString(R.string.pudding_content))
                .setEnableIconAnimation().create(this).show();
    }

    public void click2(View view) {
        new PuddingBuilder().setTitleText(RuntimeManager.getInstance().getString(R.string.pudding_title))
                .setSubTitleText(RuntimeManager.getInstance().getString(R.string.pudding_content))
                .setBackgroundColor(GetAppColor.getInstance().getAppColorAccent())
                .setTitleTypeface(Typeface.DEFAULT_BOLD)
                .setSubTitleTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC))
                .setEnableIconAnimation().create(this).show();
    }

    public void click3(View view) {
        new PuddingBuilder().setTitleText(RuntimeManager.getInstance().getString(R.string.pudding_title))
                .setSubTitleText(RuntimeManager.getInstance().getString(R.string.pudding_content))
                .setBackgroundColor(GetAppColor.getInstance().getAppColorAccent())
                .setIconDrawableRes(R.drawable.ic_event_available_black_24dp)
                .setEnableIconAnimation().create(this).show();
    }

    public void click4(View view) {
        new PuddingBuilder().setTitleText(RuntimeManager.getInstance().getString(R.string.pudding_title))
                .setSubTitleText(getResources().getString(R.string.verbose_text_text))
                .setBackgroundColor(GetAppColor.getInstance().getAppColorAccent())
                .setIconDrawableRes(R.drawable.ic_event_available_black_24dp)
                .setEnableIconAnimation().create(this).show();
    }

    public void click5(View view) {
        new PuddingBuilder().setTitleText(RuntimeManager.getInstance().getString(R.string.pudding_title))
                .setBackgroundColor(GetAppColor.getInstance().getAppColorAccent())
                .setIconDrawableRes(R.drawable.ic_event_available_black_24dp)
                .setEnableInfiniteDuration()
                .setEnableIconAnimation().create(this).show();
    }

    public void click6(View view) {
        new PuddingBuilder().setTitleText(RuntimeManager.getInstance().getString(R.string.pudding_title))
                .setBackgroundColor(GetAppColor.getInstance().getAppColorAccent())
                .setIconDrawableRes(R.drawable.ic_event_available_black_24dp)
                .setProgressMode()
                .setEnableIconAnimation().create(this).show();
    }

    public void click7(View view) {
        new PuddingBuilder().setTitleText(RuntimeManager.getInstance().getString(R.string.pudding_title))
                .setSubTitleText(RuntimeManager.getInstance().getString(R.string.pudding_content))
                .setTitleColor(GetAppColor.getInstance().getAppColorAccent())
                .setSubTitleColor(GetAppColor.getInstance().getAppColorLight())
                .setIconDrawableRes(R.drawable.ic_event_available_black_24dp)
                .setEnableSwipeDismiss()
                .setEnableIconAnimation().create(this).show();
    }

    public void click8(View view) {
        new PuddingBuilder().setTitleText(RuntimeManager.getInstance().getString(R.string.pudding_title))
                .setSubTitleText(getResources().getString(R.string.verbose_text_text))
                .setBackgroundColor(GetAppColor.getInstance().getAppColorAccent())
                .setIconDrawableRes(R.drawable.ic_event_available_black_24dp)
                .setEnableSwipeDismiss()
                .setEnableIconAnimation()
                .setPuddingCallBack(new PuddingCallBack() {
                    @Override
                    public void show() {
                        CustomToast.getInstance().showToast("显示");
                    }

                    @Override
                    public void dismiss() {
                        CustomToast.getInstance().showToast("隐藏");
                    }

                    @Override
                    public void onSwing(boolean dismiss) {
                        CustomToast.getInstance().showToast("将要隐藏");
                    }
                }).create(this).show();
    }

    public void click9(View view) {
        new PuddingBuilder().setTitleText(RuntimeManager.getInstance().getString(R.string.pudding_title))
                .setSubTitleText(RuntimeManager.getInstance().getString(R.string.pudding_content))
                .setBackgroundColor(GetAppColor.getInstance().getAppColorAccent())
                .setEnableIconAnimation()
                .setEnableInfiniteDuration()
                .setPositive("确定", R.style.PuddingButton, new INoDoubleClick() {
                    @Override
                    public void activeClick(View v) {
                        CustomToast.getInstance().showToast("确定");
                    }
                })
                .setNegative("取消", R.style.PuddingButton, new INoDoubleClick() {
                    @Override
                    public void activeClick(View v) {
                        CustomToast.getInstance().showToast("取消");
                    }
                })
                .create(this).show();
    }

    public void showAlertDialog(View view) {
        new AlertDialog.Builder(this)
                .setTitle("AlertDialog")
                .setMessage("normal AlertDialog will cover Pudding")
                .create().show();
    }

    public void showFancyDialog(View view) {
        new AlertDialog.Builder(this)
                .setTitle("AlertDialog")
                .setMessage("normal AlertDialog will cover Pudding")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PuddingTestActivity.this, "确认", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PuddingTestActivity.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                })
                .create().show();
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.test_pudding);
    }

    public void startAnActivity(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }
}
