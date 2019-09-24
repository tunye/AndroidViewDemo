package com.buaa.ct.myapplication.sample.swipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buaa.ct.core.adapter.CoreRecyclerViewAdapter;
import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.swipe.SmartSwipe;
import com.buaa.ct.swipe.SwipeConsumer;
import com.buaa.ct.swipe.consumer.SpaceConsumer;
import com.buaa.ct.swipe.consumer.StretchConsumer;

public class SwipeAdapter extends CoreRecyclerViewAdapter<String, SwipeAdapter.MyViewHolder> {
    private int mode;

    public SwipeAdapter(Context context) {
        this(context, 0);
    }

    public SwipeAdapter(Context context, int mode) {
        super(context);
        this.mode = mode;
    }

    public int getCurConsumer() {
        return mode;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(context).inflate(R.layout.swipe_test_item, viewGroup, false);
        if (mode == 0) {
            return new MyViewHolder(SmartSwipe.wrap(root).addConsumer(new StretchConsumer()).enableHorizontal().getWrapper());
        } else {
            return new MyViewHolder(SmartSwipe.wrap(root).addConsumer(new SpaceConsumer()).enableHorizontal().getWrapper());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.root.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                onRecycleViewItemClickListener.onItemClick(v, position);
            }
        });
        holder.title.setText("This is title " + position);
    }

    static class MyViewHolder extends CoreRecyclerViewAdapter.MyViewHolder {
        TextView title;
        View root;

        MyViewHolder(View view) {
            super(view);
            root = view.findViewById(R.id.swipte_test_item_root);
            title = view.findViewById(R.id.swipte_test_item_title);
        }
    }
}
