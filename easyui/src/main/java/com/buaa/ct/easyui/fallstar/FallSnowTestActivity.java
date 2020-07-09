package com.buaa.ct.easyui.fallstar;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.easyui.R;

public class FallSnowTestActivity extends CoreBaseActivity {
    FallingView fallingView;

    @Override
    public int getLayoutId() {
        return R.layout.fall_snow_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        fallingView = findViewById(R.id.fall_view);
        fallingView.addFallObject(100, new FallObject.Builder(getResources().getDrawable(R.drawable.fall_snow))
                .setSpeed(100, true)
                .setSize(50, 50, true)
                .setWind(5, true, true)
        );
    }

    @Override
    public void onActivityResumed() {
        super.onActivityResumed();
        title.setText(R.string.fall_test);
        fallingView.startFalling();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fallingView.stopFalling();
    }
}
