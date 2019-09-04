package com.buaa.ct.myapplication.sample.appskin;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.buaa.ct.appskin.SkinManager;
import com.buaa.ct.appskin.sample.FlavorAdapter;
import com.buaa.ct.core.util.GetAppColor;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.receiver.ChangePropertyBroadcast;
import com.buaa.ct.myapplication.sample.base.BaseActivity;

import java.util.Arrays;


public class SkinActivity extends BaseActivity implements FlavorAdapter.OnItemClickListener {
    private String initSkin;
    private RecyclerView recyclerView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_skin;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        recyclerView = findViewById(R.id.recycler);
        FlavorAdapter mAdapter = new FlavorAdapter(this);
        mAdapter.setItemClickListener(this);
        mAdapter.addAll(Arrays.asList(context.getResources().getStringArray(R.array.flavors)), Arrays.asList(context.getResources().getStringArray(R.array.flavors_def)));
        mAdapter.setCurrentFlavor(GetAppColor.getInstance().getSkinFlg(SkinManager.getInstance().getCurrSkin()));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        setRecyclerViewProperty(recyclerView);
        title.setText(R.string.test_skin);
        toolbarOper.setText(R.string.test_skin_oper);
        initSkin = SkinManager.getInstance().getCurrSkin();
    }

    @Override
    public void setListener() {
        super.setListener();
        toolbarOper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!initSkin.equals(SkinManager.getInstance().getCurrSkin())) {
                    Intent intent = new Intent(ChangePropertyBroadcast.FLAG);
                    sendBroadcast(intent);
                } else {
                    CustomToast.getInstance().showToast(R.string.app_no_change);
                }
            }
        });
    }

    @Override
    public void onItemClicked(View view, String item, int position) {
        SkinManager.getInstance().changeSkin(item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (position == 0) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.skin_app_color));
            } else {
                getWindow().setStatusBarColor(getResources().getColor(getResource("skin_app_color_" + item)));
            }
        }
    }

    private int getResource(String colorName) {
        return getResources().getIdentifier(colorName, "color", getPackageName());
    }
}
