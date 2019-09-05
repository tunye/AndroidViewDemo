package com.buaa.ct.myapplication.sample.base;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.myapplication.ChangePropery;
import com.buaa.ct.myapplication.MyApplication;

public class BaseActivity extends CoreBaseActivity {

    @Override
    public void beforeSetLayout() {
        ((MyApplication) RuntimeManager.getInstance().getApplication()).pushActivity(this);
        ChangePropery.setAppConfig(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MyApplication) RuntimeManager.getInstance().getApplication()).popActivity(this);
    }
}
