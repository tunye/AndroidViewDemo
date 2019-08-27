package com.buaa.ct.myapplication.adapter;

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
import com.buaa.ct.comment.sample.CommentTestActivity;
import com.buaa.ct.copyboard.CopyboardTestActivity;
import com.buaa.ct.easyui.listview.ParallaxScollListViewTrestActivity;
import com.buaa.ct.easyui.pulldoor.PullDoorTestActivity;
import com.buaa.ct.easyui.pulldown.FlexibleTestActivity;
import com.buaa.ct.imageselector.sample.ImageSelectorTestActivity;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.TestActivity;
import com.buaa.ct.myapplication.UIActivity;
import com.buaa.ct.pudding.PuddingTestActivity;
import com.buaa.ct.qrcode.sample.QRCodeTestActivity;
import com.buaa.ct.videocache.sample.VideoCacheTestActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnterAdapter extends RecyclerView.Adapter<EnterAdapter.ItemViewHolder> {
    private static final int count = 11;
    private List<String> names = new ArrayList<>();

    private Context context;

    public EnterAdapter(Context context) {
        this.context = context;
        String[] nameList = {"网页回弹效果", "照片选择器", "皮肤效果", "视频缓存", "仿雅虎digest阅读效果", "微信评论框", "应用内toast通知", "UC剪贴板", "录屏", "二维码"};
        names.addAll(Arrays.asList(nameList));
        names.add(context.getResources().getString(R.string.ui));
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.enter_item_view, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int i) {
        viewHolder.name.setText(names.get(i));
        viewHolder.icon.setBackground(context.getResources().getDrawable(context.getResources().getIdentifier("test_icon_" + (i % 4 + 1), "drawable", context.getPackageName())));
        viewHolder.itemView.setTag(i);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((int) v.getTag()) {
                    case 0:
                        context.startActivity(new Intent(context, FlexibleTestActivity.class));
                        break;
                    case 1:
                        context.startActivity(new Intent(context, ImageSelectorTestActivity.class));
                        break;
                    case 2:
                        context.startActivity(new Intent(context, ScoopSettingsActivity.class));
                        break;
                    case 3:
                        context.startActivity(new Intent(context, VideoCacheTestActivity.class));
                        break;
                    case 4:
                        context.startActivity(new Intent(context, ParallaxScollListViewTrestActivity.class));
                        break;
                    case 5:
                        context.startActivity(new Intent(context, CommentTestActivity.class));
                        break;
                    case 6:
                        context.startActivity(new Intent(context, PuddingTestActivity.class));
                        break;
                    case 7:
                        context.startActivity(new Intent(context, CopyboardTestActivity.class));
                        break;
                    case 8:
                        context.startActivity(new Intent(context, PullDoorTestActivity.class));
                        break;
                    case 9:
                        context.startActivity(new Intent(context, QRCodeTestActivity.class));
                        break;
                    case count - 1:
                        context.startActivity(new Intent(context, UIActivity.class));
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
        return count;
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
