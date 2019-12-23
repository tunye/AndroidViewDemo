package com.buaa.ct.myapplication;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.okhttp.ErrorInfoWrapper;
import com.buaa.ct.core.okhttp.RequestClient;
import com.buaa.ct.core.okhttp.SimpleRequestCallBack;
import com.buaa.ct.myapplication.activity.SettingActivity;
import com.buaa.ct.myapplication.adapter.EnterAdapter;
import com.buaa.ct.myapplication.request.GetLrcRequest;
import com.buaa.ct.myapplication.sample.base.BaseActivity;

public class MainActivity extends BaseActivity {
    RecyclerView recyclerView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        recyclerView = findViewById(R.id.enter);
    }

    @Override
    public void setListener() {
        super.setListener();
        toolbarOper.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                startActivity(new Intent(context, SettingActivity.class));
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        recyclerView.setAdapter(new EnterAdapter(this));
        setRecyclerViewProperty(recyclerView);
        title.setText(R.string.app_name);
        back.setVisibility(View.INVISIBLE);
        enableToolbarOper(R.string.setting);
//        getLrcTest();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ((MyApplication) getApplication()).clearActivityList();
    }

    private void getLrcTest() {
        RequestClient.requestAsync(new GetLrcRequest(), new SimpleRequestCallBack<String[]>() {

                    @Override
                    public void onSuccess(String[] strings) {
                        for (String item : strings) {
                            Log.e("ccc", item);
                        }
                    }

                    @Override
                    public void onError(ErrorInfoWrapper errorInfo) {
                    }
                }
        );
    }
}
