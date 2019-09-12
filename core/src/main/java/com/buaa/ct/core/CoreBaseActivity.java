package com.buaa.ct.core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.buaa.ct.appskin.BaseSkinActivity;
import com.buaa.ct.core.manager.ImmersiveManager;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.core.manager.ScreenShotManager;
import com.buaa.ct.core.util.PermissionPool;
import com.buaa.ct.core.view.MaterialRippleLayout;
import com.buaa.ct.core.view.image.DividerItemDecoration;
import com.buaa.ct.core.view.swiperefresh.MySwipeRefreshLayout;

public class CoreBaseActivity extends BaseSkinActivity {
    protected Context context;
    protected MaterialRippleLayout back;
    protected FrameLayout toolBarLayout;
    protected TextView title, toolbarOper;

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
    }

    public void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void requestMultiPermission(@PermissionPool.PermissionCode final int[] codes, @PermissionPool.PermissionName final String[] permissions) {
        requestMultiPermission(codes, permissions, RuntimeManager.getInstance().getString(R.string.storage_permission_content));
    }

    public void requestMultiPermission(@PermissionPool.PermissionCode final int[] codes, @PermissionPool.PermissionName final String[] permissions, String dialogContent) {
        boolean result = true;
        int i = 0;
        for (String permission : permissions) {
            result = result && (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED);
            if (result) {
                onAccreditSucceed(codes[i]);
            }
            i++;
        }
        if (this.isFinishing() || result) {
            return;
        }

        onRequestPermissionDenied(dialogContent, codes, permissions);
    }

    public void onRequestPermissionDenied(String dialogContent, final int[] codes, final String[] permissions) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.permission);
        builder.setMessage(dialogContent);
        builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                for (int i = 0; i < codes.length; i++) {
                    permissionDispose(codes[i], permissions[i]);
                }
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
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //授权成功
            onAccreditSucceed(requestCode);
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            //授权失败
            onAccreditFailure(requestCode);
        }
    }

    public void onAccreditSucceed(int requestCode) {
    }


    public void onAccreditFailure(int requestCode) {
    }
}
