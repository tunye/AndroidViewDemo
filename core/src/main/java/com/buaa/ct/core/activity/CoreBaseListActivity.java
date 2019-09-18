package com.buaa.ct.core.activity;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.R;
import com.buaa.ct.core.adapter.CoreRecyclerViewAdapter;
import com.buaa.ct.core.listener.IOnClickListener;
import com.buaa.ct.core.listener.IOnDoubleClick;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.core.view.swiperefresh.MySwipeRefreshLayout;

import java.util.List;


/**
 * Created by 10202 on 2015/10/23.
 */
public abstract class CoreBaseListActivity<T> extends CoreBaseActivity implements MySwipeRefreshLayout.OnRefreshListener, IOnClickListener {
    protected MySwipeRefreshLayout swipeRefreshLayout;
    protected int curPage;
    protected boolean isLastPage = false;

    protected RecyclerView owner;
    protected CoreRecyclerViewAdapter<T, ?> ownerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.list;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        swipeRefreshLayout = findSwipeRefresh();
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void setListener() {
        super.setListener();
        toolBarLayout.setOnTouchListener(new IOnDoubleClick(this, context.getString(R.string.list_double)));
    }

    public void assembleRecyclerView() {
        setRecyclerViewProperty(owner);
        owner.setAdapter(ownerAdapter);
    }


    @Override
    public void onClick(View view, Object message) {
        owner.scrollToPosition(0);
    }

    /**
     * 下拉刷新
     *
     * @param index 当前分页索引
     */
    @Override
    public void onRefresh(int index) {
        curPage = 1;
        isLastPage = false;
        getNetData();
    }

    /**
     * 加载更多
     *
     * @param index 当前分页索引
     */
    @Override
    public void onLoad(int index) {
        if (!isLastPage) {
            curPage++;
            getNetData();
        } else {
            swipeRefreshLayout.setRefreshing(false);
            if (getToastResource() != -1) {
                CustomToast.getInstance().showToast(getToastResource());
            }
        }
    }

    public @StringRes
    int getToastResource() {
        return -1;
    }

    public void getNetData() {

    }

    public void onNetDataReturnSuccess(List<T> netData) {
        swipeRefreshLayout.setRefreshing(false);
        if (isLastPage) {
            if (getToastResource() != -1) {
                CustomToast.getInstance().showToast(getToastResource());
            }
        } else {
            handleBeforeAddAdapter(netData);
            ownerAdapter.addDatas(netData);
            handleAfterAddAdapter(netData);
        }
    }

    public void handleBeforeAddAdapter(List<T> netData) {

    }

    public void handleAfterAddAdapter(List<T> netData) {
        if (curPage != 1) {
            owner.scrollToPosition(ownerAdapter.getDatas().size() - netData.size());
        }
    }
}
