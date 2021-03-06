package com.buaa.ct.core.util;

import android.text.TextUtils;

import com.buaa.ct.appskin.SkinManager;
import com.buaa.ct.core.R;
import com.buaa.ct.core.manager.RuntimeManager;

import java.util.Arrays;
import java.util.List;


/**
 * Created by 10202 on 2016/7/19.
 */
public class GetAppColor {
    private List<String> flavorsDef;

    private GetAppColor() {
        flavorsDef = Arrays.asList(RuntimeManager.getInstance().getContext().getResources().getStringArray(R.array.flavors_def));
    }

    public static GetAppColor getInstance() {
        return SingleInstanceHelper.instance;
    }

    public static int getResource(String colorName) {
        return RuntimeManager.getInstance().getContext().getResources().getIdentifier(colorName, "color", RuntimeManager.getInstance().getApplication().getPackageName());
    }

    public int getAppTheme() {
        int skin = getSkinFlg(SkinManager.getInstance().getCurrSkin());
        if (skin == 0) {
            return R.style.MyAppTheme;
        } else {
            return RuntimeManager.getInstance().getContext().getResources().getIdentifier("MyAppTheme_" + SkinManager.getInstance().getCurrSkin(), "style", RuntimeManager.getInstance().getApplication().getPackageName());
        }
    }

    public int getAppColor() {
        int skin = getSkinFlg(SkinManager.getInstance().getCurrSkin());
        if (skin == 0) {
            return RuntimeManager.getInstance().getContext().getResources().getColor(R.color.skin_app_color);
        } else {
            return RuntimeManager.getInstance().getContext().getResources().getColor(GetAppColor.getResource("skin_app_color_" + flavorsDef.get(skin)));
        }
    }

    public int getAppColorRes() {
        int skin = getSkinFlg(SkinManager.getInstance().getCurrSkin());
        if (skin == 0) {
            return R.color.skin_app_color;
        } else {
            return GetAppColor.getResource("skin_app_color_" + flavorsDef.get(skin));
        }
    }

    public int getAppColorLight() {
        int skin = getSkinFlg(SkinManager.getInstance().getCurrSkin());
        if (skin == 0) {
            return RuntimeManager.getInstance().getContext().getResources().getColor(R.color.skin_app_color_light);
        } else {
            return RuntimeManager.getInstance().getContext().getResources().getColor(GetAppColor.getResource("skin_app_color_light_" + flavorsDef.get(skin)));
        }
    }

    public int getAppColorLightRes() {
        int skin = getSkinFlg(SkinManager.getInstance().getCurrSkin());
        if (skin == 0) {
            return R.color.skin_app_color_light;
        } else {
            return GetAppColor.getResource("skin_app_color_light_" + flavorsDef.get(skin));
        }
    }

    public int getAppColorAccent() {
        int skin = getSkinFlg(SkinManager.getInstance().getCurrSkin());
        if (skin == 0) {
            return RuntimeManager.getInstance().getContext().getResources().getColor(R.color.skin_color_accent);
        } else {
            return RuntimeManager.getInstance().getContext().getResources().getColor(GetAppColor.getResource("skin_color_accent_" + flavorsDef.get(skin)));
        }
    }

    public int getAppColorAccentRes() {
        int skin = getSkinFlg(SkinManager.getInstance().getCurrSkin());
        if (skin == 0) {
            return R.color.skin_color_accent;
        } else {
            return GetAppColor.getResource("skin_color_accent_" + flavorsDef.get(skin));
        }
    }

    public int getSkinFlg(String curSkin) {
        int position = 0;
        if (!TextUtils.isEmpty(curSkin)) {
            for (String name : flavorsDef) {
                if (name.equals(curSkin)) {
                    break;
                } else {
                    position++;
                }
            }
        }
        return position;
    }

    private static class SingleInstanceHelper {
        private static GetAppColor instance = new GetAppColor();
    }
}
