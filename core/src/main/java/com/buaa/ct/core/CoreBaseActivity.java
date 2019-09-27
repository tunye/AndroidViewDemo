package com.buaa.ct.core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.ct.appskin.BaseSkinActivity;
import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.manager.ImmersiveManager;
import com.buaa.ct.core.manager.ScreenShotManager;
import com.buaa.ct.core.util.PermissionPool;
import com.buaa.ct.core.view.MaterialRippleLayout;
import com.buaa.ct.core.view.image.DividerItemDecoration;
import com.buaa.ct.core.view.swiperefresh.MySwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class CoreBaseActivity extends BaseSkinActivity {
    protected Context context;
    protected MaterialRippleLayout back;
    protected ImageView backIcon;
    protected FrameLayout toolBarLayout;
    protected TextView title, toolbarOper, toolbarOperSub;

    protected ScreenShotManager screenShotManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        ImmersiveManager.getInstance().updateImmersiveStatus(this);
        beforeSetLayout(savedInstanceState);
        setContentView(getLayoutId());
        afterSetLayout();
        initWidget();
        setListener();
        onActivityCreated();
        screenShotManager = ScreenShotManager.newInstance(this);
        screenShotManager.setListener(new ScreenShotManager.OnScreenShotListener() {
            @Override
            public void onShot(String imagePath) {
                CoreBaseActivity.this.onShot(imagePath);
            }
        });
        screenShotManager.startListen();
    }

    public void beforeSetLayout(Bundle savedInstanceState) {

    }

    public @LayoutRes
    int getLayoutId() {
        return -1;
    }

    public void afterSetLayout() {

    }

    public void initWidget() {
        back = findViewById(R.id.back);
        toolBarLayout = findViewById(R.id.toolbar_title_layout);
        title = findViewById(R.id.toolbar_title);
        toolbarOper = findViewById(R.id.toolbar_oper);
        toolbarOperSub = findViewById(R.id.toolbar_oper_sub);
        backIcon = findViewById(R.id.back_img);
        if (findViewById(R.id.toolbar_oper_ripple) != null) {
            findViewById(R.id.toolbar_oper_ripple).setVisibility(View.GONE);
        }
        if (findViewById(R.id.toolbar_oper_sub_ripple) != null) {
            findViewById(R.id.toolbar_oper_sub_ripple).setVisibility(View.GONE);
        }
    }

    public void setListener() {
        back.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onActivityCreated() {

    }

    public void onActivityResumed() {

    }

    public void setRecyclerViewProperty(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration());
    }

    public MySwipeRefreshLayout findSwipeRefresh() {
        MySwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_widget);
        swipeRefreshLayout.setColorSchemeColors(0xff259CF7, 0xff2ABB51, 0xffE10000, 0xfffaaa3c);
        swipeRefreshLayout.setFirstIndex(0);
        swipeRefreshLayout.setRefreshing(true);
        return swipeRefreshLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        onActivityResumed();
    }

    public void onShot(String imgPath) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        screenShotManager.stopListen();
        screenShotManager = null;
    }

    public void enableToolbarOper(@StringRes int text) {
        toolbarOper.setText(text);
        findViewById(R.id.toolbar_oper_ripple).setVisibility(View.VISIBLE);
    }

    public void enableToolbarOper(String text) {
        toolbarOper.setText(text);
        findViewById(R.id.toolbar_oper_ripple).setVisibility(View.VISIBLE);
    }

    public void enableToolbarOperSub(@StringRes int text) {
        if (TextUtils.isEmpty(toolbarOper.getText())) {
            throw new IllegalStateException("you must set toolbar oper first!");
        }
        toolbarOperSub.setText(text);
        findViewById(R.id.toolbar_oper_sub_ripple).setVisibility(View.VISIBLE);
    }

    public boolean hasPermission(String permissionName) {
        return ContextCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestMultiPermission(@PermissionPool.PermissionCode final int[] codes, @PermissionPool.PermissionName final String[] permissions) {
        boolean result = true;
        int i = 0;
        List<String> denyPermissions = new ArrayList<>();
        for (String permission : permissions) {
            result = result && (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED);
            if (!result) {
                denyPermissions.add(permission);
            }
            i++;
        }
        if (result) {
            onAccreditSucceed(codes[i - 1]);
        } else {
            ActivityCompat.requestPermissions(this, denyPermissions.toArray(new String[]{}), codes[i - 1]);
        }
    }

    public void onRequestPermissionDenied(String dialogContent, final @PermissionPool.PermissionCode int[] codes, final @PermissionPool.PermissionName String[] permissions) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.permission);
        builder.setMessage(dialogContent);
        builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestMultiPermission(codes, permissions);
            }
        });
        builder.show();
    }

    public void permissionDispose(@PermissionPool.PermissionCode int code, @PermissionPool.PermissionName String permissionName) {
        if (ContextCompat.checkSelfPermission(this, permissionName) != PackageManager.PERMISSION_GRANTED) {
            //没有权限,开始申请
            ActivityCompat.requestPermissions(this, new String[]{permissionName}, code);
        } else {
            //有权限
            onAccreditSucceed(code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 0) {
            return;
        }
        boolean hasFailed = false;
        for (int item : grantResults) {
            if (item == PackageManager.PERMISSION_DENIED) {
                hasFailed = true;
            }
        }

        if (!hasFailed) {
            //授权成功
            onAccreditSucceed(requestCode);
        } else {
            onAccreditFailure(requestCode);
        }
    }

    public void onAccreditSucceed(int requestCode) {
    }


    public void onAccreditFailure(int requestCode) {
    }
}
