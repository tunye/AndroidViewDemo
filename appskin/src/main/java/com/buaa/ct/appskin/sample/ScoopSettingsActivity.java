package com.buaa.ct.appskin.sample;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.buaa.ct.appskin.BaseSkinActivity;
import com.buaa.ct.appskin.R;
import com.buaa.ct.appskin.SkinManager;

import java.util.Arrays;


public class ScoopSettingsActivity extends BaseSkinActivity implements FlavorRecyclerAdapter.OnItemClickListener {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getSkinFlg() == 0) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.skin_app_color));
            } else {
                getWindow().setStatusBarColor(getResources().getColor(getResource("skin_app_color_" + Arrays.asList(getResources().getStringArray(R.array.flavors_def)).get(getSkinFlg()))));
            }
        }
        setContentView(R.layout.scoop_settings);
        context = this;
        setupActionBar();
        setupRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        Toolbar mAppBar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(mAppBar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(context.getResources().getColor(R.color.skin_background), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        mAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAppBar.setTitle(R.string.app_name);
        mAppBar.setTitleTextColor(context.getResources().getColor(R.color.skin_background));
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        FlavorRecyclerAdapter mAdapter = new FlavorRecyclerAdapter(this);
        mAdapter.setItemClickListener(this);
        mAdapter.addAll(Arrays.asList(context.getResources().getStringArray(R.array.flavors)), Arrays.asList(context.getResources().getStringArray(R.array.flavors_def)));
        mAdapter.setCurrentFlavor(getSkinFlg());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    private int getSkinFlg() {
        String skinFlg = SkinManager.getInstance().getCurrSkin();
        int position = 0;
        if (!TextUtils.isEmpty(skinFlg)) {
            for (String name : getResources().getStringArray(R.array.flavors_def)) {
                if (name.equals(skinFlg)) {
                    break;
                } else {
                    position++;
                }
            }
        }
        return position;
    }
}
