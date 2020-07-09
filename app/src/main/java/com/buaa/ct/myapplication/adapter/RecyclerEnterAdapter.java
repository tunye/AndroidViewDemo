package com.buaa.ct.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.ct.core.adapter.CoreRecyclerViewAdapter;
import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.easyui.recycler.BannerTestActivity;
import com.buaa.ct.easyui.recycler.EchelonTestActivity;
import com.buaa.ct.easyui.recycler.MultiUserTestActivity;
import com.buaa.ct.easyui.recycler.PickerTestActivity;
import com.buaa.ct.easyui.recycler.SkidTestActivity;
import com.buaa.ct.easyui.recycler.SlideTestActivity;
import com.buaa.ct.easyui.recycler.TouchScrollActivity;
import com.buaa.ct.easyui.recycler.ViewPagerTestActivity;
import com.buaa.ct.myapplication.R;

import java.util.Arrays;


public class RecyclerEnterAdapter extends CoreRecyclerViewAdapter<String, EnterAdapter.ItemViewHolder> {
    private String[] names = {RuntimeManager.getInstance().getString(R.string.recycler_echelon),
            RuntimeManager.getInstance().getString(R.string.recycler_slide),
            RuntimeManager.getInstance().getString(R.string.recycler_banner),
            RuntimeManager.getInstance().getString(R.string.recycler_skid),
            RuntimeManager.getInstance().getString(R.string.recycler_picker),
            RuntimeManager.getInstance().getString(R.string.recycler_view_pager),
            RuntimeManager.getInstance().getString(R.string.recycler_touch_scroll),
            RuntimeManager.getInstance().getString(R.string.recycler_multi_user),
    };

    public RecyclerEnterAdapter(Context context) {
        super(context);
        addDatas(Arrays.asList(names));
    }

    @NonNull
    @Override
    public EnterAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new EnterAdapter.ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.enter_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EnterAdapter.ItemViewHolder viewHolder, int i) {
        viewHolder.name.setText(getDatas().get(i));
        viewHolder.icon.setBackground(context.getResources().getDrawable(context.getResources().getIdentifier("test_icon_" + (i % 4 + 1), "drawable", context.getPackageName())));
        viewHolder.root.setTag(i);
        viewHolder.root.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                switch ((int) v.getTag()) {
                    case 0:
                        context.startActivity(new Intent(context, EchelonTestActivity.class));
                        break;
                    case 1:
                        context.startActivity(new Intent(context, SlideTestActivity.class));
                        break;
                    case 2:
                        context.startActivity(new Intent(context, BannerTestActivity.class));
                        break;
                    case 3:
                        context.startActivity(new Intent(context, SkidTestActivity.class));
                        break;
                    case 4:
                        context.startActivity(new Intent(context, PickerTestActivity.class));
                        break;
                    case 5:
                        context.startActivity(new Intent(context, ViewPagerTestActivity.class));
                        break;
                    case 6:
                        context.startActivity(new Intent(context, TouchScrollActivity.class));
                        break;
                    case 7:
                        context.startActivity(new Intent(context, MultiUserTestActivity.class));
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    static class ItemViewHolder extends MyViewHolder {
        ImageView icon;
        TextView name;
        View root;

        ItemViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            icon = itemView.findViewById(R.id.enter_item_icon);
            name = itemView.findViewById(R.id.enter_item_name);
        }
    }
}
