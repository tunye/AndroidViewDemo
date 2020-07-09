package com.buaa.ct.easyui.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.adapter.CoreRecyclerViewAdapter;
import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.easyui.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TouchScrollActivity extends CoreBaseActivity {
    RecyclerView recyclerView;
    ScrollPosAdapter scrollPosAdapter;
    ItemTouchHelper helper;
    List<ScrollPosItem> datas = new ArrayList<>(20);

    @Override
    public int getLayoutId() {
        return R.layout.recycler_scroll_pos;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        recyclerView = findViewById(R.id.enter);
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.recycler_touch_scroll);
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

    public static class ScrollPosAdapter extends CoreRecyclerViewAdapter<ScrollPosItem, ScrollPosAdapter.ItemViewHolder> {
        private ItemTouchHelper touchHelper;
        private long startTime;
        private final static long SPACE_TIME = 100;

        public ScrollPosAdapter(Context context, ItemTouchHelper touchHelper) {
            super(context);
            this.touchHelper = touchHelper;
        }

        @NonNull
        @Override
        public ScrollPosAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            return new ScrollPosAdapter.ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_scroll_pos, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ScrollPosAdapter.ItemViewHolder viewHolder, int i) {
            viewHolder.name.setText(getDatas().get(i).showText);
            viewHolder.root.setOnClickListener(new INoDoubleClick() {
                @Override
                public void activeClick(View v) {
                    CustomToast.getInstance().showToast("点击了 " + getDatas().get(viewHolder.getAdapterPosition()).index);
                }
            });
            viewHolder.icon.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (MotionEventCompat.getActionMasked(event)) {
                        case MotionEvent.ACTION_DOWN:
                            startTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (System.currentTimeMillis() - startTime > SPACE_TIME) {
                                touchHelper.startDrag(viewHolder);
                                return true;
                            } else {
                                return false;
                            }
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            startTime = 0;
                            break;
                    }
                    return true;
                }
            });
        }

        public void onMove(int fromPosition, int toPosition) {
            getDatas().get(fromPosition).index = toPosition;
            getDatas().get(toPosition).index = fromPosition;
            //对原数据进行移动
            Collections.swap(datas, fromPosition, toPosition);
            //通知数据移动
            notifyItemMoved(fromPosition, toPosition);
        }

        public static class ItemViewHolder extends CoreRecyclerViewAdapter.MyViewHolder {
            View icon;
            TextView name;
            View root;

            ItemViewHolder(View itemView) {
                super(itemView);
                root = itemView.findViewById(R.id.root);
                icon = itemView.findViewById(R.id.item_scroll_pos_icon);
                name = itemView.findViewById(R.id.item_scroll_pos_text);
            }
        }
    }

    public static class ScrollPosItem {
        public String showText;
        public int index;
        public int initPos;

        public ScrollPosItem(int i) {
            showText = "Pos " + i;
            index = i;
            initPos = i;
        }
    }
}
