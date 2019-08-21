package com.buaa.ct.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.ct.appskin.sample.ScoopSettingsActivity;
import com.buaa.ct.easyui.Banner.BannerTestActivity;
import com.buaa.ct.easyui.DraggableFlag.DraggableFlagTestActivity;
import com.buaa.ct.easyui.boundnumber.BoundNumTestActivity;
import com.buaa.ct.easyui.listview.ParallaxScollListViewTrestActivity;
import com.buaa.ct.easyui.pulldoor.PullDoorTestActivity;
import com.buaa.ct.easyui.pulldown.FlexibleTestActivity;
import com.buaa.ct.easyui.share.ShareDialogTestActivity;
import com.buaa.ct.imageselector.sample.ImageSelectorTestActivity;
import com.buaa.ct.pictureinpicture.PinpTestActivity;
import com.buaa.ct.stickydot.StickyDotTestActivity;
import com.buaa.ct.videocache.sample.VideoCacheTestActivity;

public class EnterAdapter extends RecyclerView.Adapter<EnterAdapter.ItemViewHolder> {
    private String[] names = {"Banner", "一键退朝", "回弹效果", "照片选择器", "皮肤效果", "视频缓存", "数字效果", "分享气泡", "动画效果", "画中画", "仿雅虎digest阅读效果", "推门效果", "另一种一键退朝"};
    private Context context;

    public EnterAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.enter_item_view, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int i) {
        viewHolder.name.setText(names[i]);
        viewHolder.icon.setBackground(context.getResources().getDrawable(context.getResources().getIdentifier("test_icon_" + (i % 4 + 1), "drawable", context.getPackageName())));
        viewHolder.itemView.setTag(i);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((int) v.getTag()) {
                    case 0:
                        context.startActivity(new Intent(context, BannerTestActivity.class));
                        break;
                    case 1:
                        context.startActivity(new Intent(context, StickyDotTestActivity.class));
                        break;
                    case 2:
                        context.startActivity(new Intent(context, FlexibleTestActivity.class));
                        break;
                    case 3:
                        context.startActivity(new Intent(context, ImageSelectorTestActivity.class));
                        break;
                    case 4:
                        context.startActivity(new Intent(context, ScoopSettingsActivity.class));
                        break;
                    case 5:
                        context.startActivity(new Intent(context, VideoCacheTestActivity.class));
                        break;
                    case 6:
                        context.startActivity(new Intent(context, BoundNumTestActivity.class));
                        break;
                    case 7:
                        context.startActivity(new Intent(context, ShareDialogTestActivity.class));
                        break;
                    case 9:
                        context.startActivity(new Intent(context, PinpTestActivity.class));
                        break;
                    case 10:
                        context.startActivity(new Intent(context, ParallaxScollListViewTrestActivity.class));
                        break;
                    case 11:
                        context.startActivity(new Intent(context, PullDoorTestActivity.class));
                        break;
                    case 12:
                        context.startActivity(new Intent(context, DraggableFlagTestActivity.class));
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

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        ItemViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.enter_item_icon);
            name = itemView.findViewById(R.id.enter_item_name);
        }
    }
}
