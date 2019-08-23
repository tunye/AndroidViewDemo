/*
 * Created by chentong1 on 2019.8.20
 */

package com.buaa.ct.pudding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.buaa.ct.pudding.callback.PuddingCallBack;
import com.buaa.ct.pudding.util.PuddingBuilder;


public class PuddingTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pudding_test);
    }

    public void click1(View view) {
        new PuddingBuilder().setTitleText("This is a title")
                .setSubTitleText("This is a content")
                .setEnableIconAnimation().create(this).show();
    }

    public void click2(View view) {
        new PuddingBuilder().setTitleText("This is a title")
                .setSubTitleText("This is a content")
                .setBackgroundColor(getResources().getColor(R.color.colorAccent))
                .setTitleTypeface(Typeface.DEFAULT_BOLD)
                .setSubTitleTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC))
                .setEnableIconAnimation().create(this).show();
    }

    public void click3(View view) {
        new PuddingBuilder().setTitleText("This is a title")
                .setSubTitleText("This is a content")
                .setBackgroundColor(getResources().getColor(R.color.colorAccent))
                .setIconDrawableRes(R.drawable.ic_event_available_black_24dp)
                .setEnableIconAnimation().create(this).show();
    }

    public void click4(View view) {
        new PuddingBuilder().setTitleText("This is a title")
                .setSubTitleText(getResources().getString(R.string.verbose_text_text))
                .setBackgroundColor(getResources().getColor(R.color.colorAccent))
                .setIconDrawableRes(R.drawable.ic_event_available_black_24dp)
                .setEnableIconAnimation().create(this).show();
    }

    public void click5(View view) {
        new PuddingBuilder().setTitleText("This is a title")
                .setBackgroundColor(getResources().getColor(R.color.colorAccent))
                .setIconDrawableRes(R.drawable.ic_event_available_black_24dp)
                .setEnableInfiniteDuration()
                .setEnableIconAnimation().create(this).show();
    }

    public void click6(View view) {
        new PuddingBuilder().setTitleText("This is a title")
                .setBackgroundColor(getResources().getColor(R.color.colorAccent))
                .setIconDrawableRes(R.drawable.ic_event_available_black_24dp)
                .setProgressMode()
                .setEnableIconAnimation().create(this).show();
    }

    public void click7(View view) {
        new PuddingBuilder().setTitleText("This is a title")
                .setSubTitleText("This is a content")
                .setTitleColor(Color.BLUE)
                .setSubTitleColor(Color.parseColor("#cdcdcd"))
                .setIconDrawableRes(R.drawable.ic_event_available_black_24dp)
                .setEnableSwipeDismiss()
                .setEnableIconAnimation().create(this).show();
    }

    public void click8(View view) {
        new PuddingBuilder().setTitleText("This is a title")
                .setSubTitleText(getResources().getString(R.string.verbose_text_text))
                .setBackgroundColor(Color.parseColor("#FFCC00"))
                .setIconDrawableRes(R.drawable.ic_event_available_black_24dp)
                .setEnableSwipeDismiss()
                .setEnableIconAnimation()
                .setPuddingCallBack(new PuddingCallBack() {
                    @Override
                    public void show() {
                        Toast.makeText(PuddingTestActivity.this, "显示", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void dismiss() {
                        Toast.makeText(PuddingTestActivity.this, "隐藏", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSwing(boolean dismiss) {
                        Toast.makeText(PuddingTestActivity.this, "将要隐藏", Toast.LENGTH_SHORT).show();
                    }
                }).create(this).show();
    }

    public void click9(View view) {
        new PuddingBuilder().setTitleText("This is a title")
                .setSubTitleText("This is a content")
                .setBackgroundColor(Color.parseColor("#FFCC00"))
                .setEnableIconAnimation()
                .setEnableInfiniteDuration()
                .setPositive("确定", R.style.PuddingButton, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(PuddingTestActivity.this, "确定", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegative("取消", R.style.PuddingButton, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(PuddingTestActivity.this, "取消", Toast.LENGTH_SHORT).show();
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

    public void startAnActivity(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }
}
