package com.buaa.ct.pudding;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class PuddingManager {
    private Map<String, Pudding> puddingMap;

    private PuddingManager() {
        puddingMap = new HashMap<>();
    }

    private static class InstanceHelper {
        private static PuddingManager instance = new PuddingManager();
    }

    public static PuddingManager getInstance() {
        return InstanceHelper.instance;
    }

    public Pudding create(final AppCompatActivity appCompatActivity) {
        Pudding pudding = new Pudding();
        pudding.setActivity(appCompatActivity);
        String key = appCompatActivity.toString();
        if (puddingMap.containsKey(key)) {
            final Pudding lastPudding = puddingMap.get(key);
            if (lastPudding != null && lastPudding.getPuddingView().isAttachedToWindow()) {
                ViewCompat.animate(lastPudding.getPuddingView()).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        appCompatActivity.getWindowManager().removeViewImmediate(lastPudding.getPuddingView());
                    }
                });
            }
        }
        puddingMap.put(appCompatActivity.toString(), pudding);
        return pudding;
    }
}
