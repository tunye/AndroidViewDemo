package com.buaa.ct.myapplication.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.adapter.ScrollPosAdapter;
import com.buaa.ct.myapplication.entity.ScrollPosItem;
import com.buaa.ct.myapplication.sample.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class TouchScrollRecyclerActivity extends BaseActivity {
    RecyclerView recyclerView;
    ScrollPosAdapter scrollPosAdapter;
    ItemTouchHelper helper;
    List<ScrollPosItem> datas = new ArrayList<>(20);

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
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.test_touch_scroll);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        scrollPosAdapter = new ScrollPosAdapter(this, helper);
        recyclerView.setAdapter(scrollPosAdapter);
        setRecyclerViewProperty(recyclerView);
        makeFakeData();
    }

    private void makeFakeData() {
        for (int i = 0; i < 20; i++) {
            datas.add(new ScrollPosItem(i));
        }
        scrollPosAdapter.addDatas(datas);
    }

    ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
//            int swipeFlag = ItemTouchHelper.START | ItemTouchHelper.END;//侧滑删除
            return makeMovementFlags(dragFlag, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            if (viewHolder.getItemViewType() != target.getItemViewType()) {
                return false;
            }
            scrollPosAdapter.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setAlpha(0.5f);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public boolean isLongPressDragEnabled() {
            // 不需要长按拖拽功能  手动调用startDrag
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            // 不需要滑动功能
            return false;
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(1.0f);
            scrollPosAdapter.notifyDataSetChanged();
            Log.e("ccc", getIndexString());
            CustomToast.getInstance().showToast("调整完毕");
        }
    };

    private String getIndexString() {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        for (ScrollPosItem item : scrollPosAdapter.getDatas()) {
            stringBuilder.append(item.index).append(',');
            stringBuilder2.append(item.initPos).append(',');
        }
        return stringBuilder2.toString() + "\\n" + stringBuilder.toString();
    }
}
