package com.buaa.ct.myapplication.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.buaa.ct.appskin.SkinManager;
import com.buaa.ct.core.util.GetAppColor;
import com.buaa.ct.core.util.SPUtils;
import com.buaa.ct.myapplication.ChangePropery;
import com.buaa.ct.myapplication.ConfigManager;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.receiver.ChangePropertyBroadcast;
import com.buaa.ct.myapplication.sample.appskin.SkinActivity;
import com.buaa.ct.myapplication.sample.base.BaseActivity;

public class SettingActivity extends BaseActivity {
    public static final String LANGUAGE = "language";
    public static final String NIGHT = "night";
    TextView appColorValue, appLanguageValue, nightValue;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        appColorValue = findViewById(R.id.setting_app_color_value);
        appLanguageValue = findViewById(R.id.setting_app_language_value);
        nightValue = findViewById(R.id.setting_app_night_value);
    }

    @Override
    public void setListener() {
        super.setListener();
        findViewById(R.id.setting_app_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SkinActivity.class));
            }
        });
        findViewById(R.id.setting_app_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur = getCurLanguage();
                if (cur == 0) {
                    setCurLanguage(1);
                } else {
                    setCurLanguage(0);
                }
                ChangePropery.updateLanguageMode(cur);
                Intent intent = new Intent(ChangePropertyBroadcast.FLAG);
                sendBroadcast(intent);
            }
        });
        findViewById(R.id.setting_app_night).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur = getCurNight();
                if (cur == 0) {
                    setCurNight(1);
                } else {
                    setCurNight(0);
                }
                ChangePropery.updateNightMode(cur == 1);
                Intent intent = new Intent(ChangePropertyBroadcast.FLAG);
                sendBroadcast(intent);
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        appColorValue.setText(getAppColorName());
        appLanguageValue.setText(getCurLanguage() == 0 ? R.string.setting_app_language_zh : R.string.setting_app_language_en);
        nightValue.setText(getCurNight() == 1 ? R.string.setting_app_night_on : R.string.setting_app_night_off);
        title.setText(R.string.setting);
    }

    public String getAppColorName() {
        return context.getResources().getStringArray(R.array.flavors)[GetAppColor.getInstance().getSkinFlg(SkinManager.getInstance().getCurrSkin())];
    }

    public int getCurLanguage() {
        return SPUtils.loadInt(ConfigManager.getInstance().getPreferences(), LANGUAGE, 0);
    }

    public void setCurLanguage(int language) {
        SPUtils.putInt(ConfigManager.getInstance().getPreferences(), LANGUAGE, language);
    }

    public int getCurNight() {
        return SPUtils.loadInt(ConfigManager.getInstance().getPreferences(), NIGHT, 0);
    }

    public void setCurNight(int night) {
        SPUtils.putInt(ConfigManager.getInstance().getPreferences(), NIGHT, night);
    }
}

