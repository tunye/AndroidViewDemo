package com.buaa.ct.myapplication.activity;

import com.buaa.ct.core.activity.CoreBaseListActivity;
import com.buaa.ct.core.okhttp.ErrorInfoWrapper;
import com.buaa.ct.core.okhttp.RequestClient;
import com.buaa.ct.core.okhttp.SimpleRequestCallBack;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.adapter.BingPicAdapter;
import com.buaa.ct.myapplication.entity.BingPic;
import com.buaa.ct.myapplication.request.BingPicRequest;

import java.util.List;

public class BingPicActivity extends CoreBaseListActivity<BingPic> {
    @Override
    public void initWidget() {
        super.initWidget();
        owner = findViewById(R.id.recyclerview_widget);
        ownerAdapter = new BingPicAdapter(context);
        assembleRecyclerView();
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.test_bing);
    }

    @Override
    public int getToastResource() {
        return R.string.test_bing_load_all;
    }

    @Override
    public void getNetData() {
        super.getNetData();
        RequestClient.requestAsync(new BingPicRequest((curPage - 1) * 7, 7), new SimpleRequestCallBack<List<BingPic>>() {
            @Override
            public void onSuccess(List<BingPic> bingPics) {
                onNetDataReturnSuccess(bingPics);
                if (curPage == 2) {
                    isLastPage = true;
                }
            }

            @Override
            public void onError(ErrorInfoWrapper errorInfo) {
                CustomToast.getInstance().showToast("加载失败");
            }
        });
    }
}
