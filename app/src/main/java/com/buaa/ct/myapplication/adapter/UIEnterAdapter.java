package com.buaa.ct.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.ct.core.adapter.CoreRecyclerViewAdapter;
import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.easyui.Banner.BannerTestActivity;
import com.buaa.ct.easyui.DraggableFlag.DraggableFlagTestActivity;
import com.buaa.ct.easyui.FallStar.FallSnowTestActivity;
import com.buaa.ct.easyui.boundnumber.BoundNumTestActivity;
import com.buaa.ct.easyui.progressimage.ProgressImageTestActivity;
import com.buaa.ct.easyui.progresssbar.NumberProgressbarTestActivity;
import com.buaa.ct.easyui.pulldoor.PullDoorTestActivity;
import com.buaa.ct.easyui.share.ShareDialogTestActivity;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.activity.TestActivity;
import com.buaa.ct.stickydot.StickyDotTestActivity;

import java.util.Arrays;

public class UIEnterAdapter extends CoreRecyclerViewAdapter<String, UIEnterAdapter.ItemViewHolder> {
    private String[] names = {RuntimeManager.getInstance().getString(R.string.banner_test),
            "一键退朝",
            RuntimeManager.getInstance().getString(R.string.bound_num_test),
            RuntimeManager.getInstance().getString(R.string.progress_test),
            RuntimeManager.getInstance().getString(R.string.share_test),
            "另一种一键退朝",
            RuntimeManager.getInstance().getString(R.string.test_indicator),
            RuntimeManager.getInstance().getString(R.string.test_animation),
            RuntimeManager.getInstance().getString(R.string.test_material_edittext),
            RuntimeManager.getInstance().getString(R.string.pull_door_test),
            RuntimeManager.getInstance().getString(R.string.fall_test),
            RuntimeManager.getInstance().getString(R.string.qq_send_pic_test)};

    public UIEnterAdapter(Context context) {
        super(context);
        addDatas(Arrays.asList(names));
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.enter_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int i) {
        viewHolder.name.setText(getDatas().get(i));
        viewHolder.icon.setBackground(context.getResources().getDrawable(context.getResources().getIdentifier("test_icon_" + (i % 4 + 1), "drawable", context.getPackageName())));
        viewHolder.root.setTag(i);
        viewHolder.root.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                switch ((int) v.getTag()) {
                    case 0:
                        context.startActivity(new Intent(context, BannerTestActivity.class));
                        break;
                    case 1:
                        context.startActivity(new Intent(context, StickyDotTestActivity.class));
                        break;
                    case 2:
                        context.startActivity(new Intent(context, BoundNumTestActivity.class));
                        break;
                    case 3:
                        context.startActivity(new Intent(context, NumberProgressbarTestActivity.class));
                        break;
                    case 4:
                        context.startActivity(new Intent(context, ShareDialogTestActivity.class));
                        break;
                    case 5:
                        context.startActivity(new Intent(context, DraggableFlagTestActivity.class));
                        break;
                    case 6:
                        MusicTools.goMusicTools(context,"iyumusic://eggshell/indicator");
                        break;
                    case 7:
                        MusicTools.goMusicTools(context,"iyumusic://eggshell/animation");
                        break;
                    case 8:
                        MusicTools.goMusicTools(context,"iyumusic://eggshell/material_edittext");
                        break;
                    case 9:
                        context.startActivity(new Intent(context, PullDoorTestActivity.class));
                        break;
                    case 10:
                        context.startActivity(new Intent(context, FallSnowTestActivity.class));
                        break;
                    case 11:
                        context.startActivity(new Intent(context, ProgressImageTestActivity.class));
                        break;
                    default:
                        context.startActivity(new Intent(context, TestActivity.class));
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    class ItemViewHolder extends CoreRecyclerViewAdapter.MyViewHolder {
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
