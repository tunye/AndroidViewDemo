package com.buaa.ct.myapplication.sample.swipe;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.listener.OnRecycleViewItemClickListener;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.sample.base.BaseActivity;
import com.buaa.ct.swipe.SmartSwipe;
import com.buaa.ct.swipe.consumer.SpaceConsumer;
import com.buaa.ct.swipe.consumer.StretchConsumer;

import java.util.ArrayList;
import java.util.List;

public class SwipeTestListActivity extends BaseActivity {
    RecyclerView recyclerView;
    SwipeAdapter swipeAdapter;
    List<String> datas = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.swipe_test_list_activity;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        datas.add("1");
        datas.add("2");
        datas.add("3");
        datas.add("4");
        datas.add("5");
        datas.add("6");
        datas.add("7");
        datas.add("8");
        datas.add("9");
        datas.add("10");
        datas.add("11");
        datas.add("12");
        datas.add("13");
        datas.add("14");
        datas.add("15");
        datas.add("16");
        datas.add("17");
        datas.add("18");
        datas.add("19");

        recyclerView = findViewById(R.id.recyclerview_widget);
        swipeAdapter = new SwipeAdapter(context);
        swipeAdapter.setDataSet(datas);
        recyclerView.setAdapter(swipeAdapter);
        setRecyclerViewProperty(recyclerView);
    }

    @Override
    public void setListener() {
        super.setListener();
        swipeAdapter.setOnItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CustomToast.getInstance().showToast("点击了" + position + "位置");
            }
        });
        toolbarOper.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                if (swipeAdapter.getCurConsumer() == 1) {
                    swipeAdapter = new SwipeAdapter(context);
                    swipeAdapter.setDataSet(datas);
                    recyclerView.setAdapter(swipeAdapter);
                    recyclerView.scrollToPosition(0);
                    SmartSwipe.wrap(recyclerView).removeAllConsumers().addConsumer(new StretchConsumer()).enableVertical();
                    CustomToast.getInstance().showToast("拉伸效果");
                } else {
                    swipeAdapter = new SwipeAdapter(context, 1);
                    swipeAdapter.setDataSet(datas);
                    recyclerView.setAdapter(swipeAdapter);
                    recyclerView.scrollToPosition(0);
                    SmartSwipe.wrap(recyclerView).removeAllConsumers().addConsumer(new SpaceConsumer()).enableVertical();
                    CustomToast.getInstance().showToast("加空白效果");
                }
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.test_swipe_list);
        enableToolbarOper("变更效果");
        SmartSwipe.wrap(recyclerView).addConsumer(new StretchConsumer()).enableVertical();
    }
}
