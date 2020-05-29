package com.buaa.ct.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buaa.ct.core.adapter.CoreRecyclerViewAdapter;
import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.entity.ScrollPosItem;

import java.util.Collections;

public class ScrollPosAdapter extends CoreRecyclerViewAdapter<ScrollPosItem, ScrollPosAdapter.ItemViewHolder> {
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

    class ItemViewHolder extends CoreRecyclerViewAdapter.MyViewHolder {
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
