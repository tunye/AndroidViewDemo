package com.buaa.ct.pudding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author: zhaochunyu
 * @description: ${DESP}
 * @date: 2019-07-01
 */
public class PuddingTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pudding_test);
    }

    public void click1(View view) {
        Pudding pudding = PuddingManager.getInstance().create(this);
        pudding.getPuddingView().setTitle("This is a title");
        pudding.getPuddingView().setSubTitle("This is a content");
        pudding.show();
    }
}
