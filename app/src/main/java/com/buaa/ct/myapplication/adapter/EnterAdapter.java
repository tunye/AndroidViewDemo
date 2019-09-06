package com.buaa.ct.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.ct.myapplication.sample.copyboard.CopyboardTestActivity;
import com.buaa.ct.core.adapter.CoreRecyclerViewAdapter;
import com.buaa.ct.core.manager.RuntimeManager;
import com.buaa.ct.easyui.listview.ParallaxScollListViewTrestActivity;
import com.buaa.ct.easyui.pulldoor.PullDoorTestActivity;
import com.buaa.ct.easyui.pulldown.FlexibleTestActivity;
import com.buaa.ct.imageselector.sample.ImageSelectorTestActivity;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.activity.TestActivity;
import com.buaa.ct.myapplication.activity.UIActivity;
import com.buaa.ct.myapplication.sample.appskin.SkinActivity;
import com.buaa.ct.myapplication.sample.comment.CommentTestActivity;
import com.buaa.ct.pudding.PuddingTestActivity;
import com.buaa.ct.qrcode.sample.QRCodeTestActivity;
import com.buaa.ct.videocache.sample.VideoCacheTestActivity;

import java.util.Arrays;

public class EnterAdapter extends CoreRecyclerViewAdapter<String, EnterAdapter.ItemViewHolder> {
    private static final int count = 11;

    public EnterAdapter(Context context) {
        super(context);
        String[] nameList = {"网页回弹效果", "照片选择器",
                RuntimeManager.getInstance().getString(R.string.test_skin),
                "视频缓存", "仿雅虎digest阅读效果",
                RuntimeManager.getInstance().getString(R.string.test_comment),
                "应用内toast通知",
                RuntimeManager.getInstance().getString(R.string.test_copyboard), "录屏", "二维码"};
        addDatas(Arrays.asList(nameList));
        addData(context.getResources().getString(R.string.ui));
    }

    @NonNull
    @Override
    public EnterAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new EnterAdapter.ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.enter_item_view, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int i) {
        viewHolder.name.setText(getDatas().get(i));
        viewHolder.icon.setBackground(context.getResources().getDrawable(context.getResources().getIdentifier("test_icon_" + (i % 4 + 1), "drawable", context.getPackageName())));
        viewHolder.root.setTag(i);
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
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
                        context.startActivity(new Intent(context, SkinActivity.class));
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

    public static class ItemViewHolder extends CoreRecyclerViewAdapter.MyViewHolder {
        ImageView icon;
        TextView name;
        View root;

        public ItemViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            icon = itemView.findViewById(R.id.enter_item_icon);
            name = itemView.findViewById(R.id.enter_item_name);
        }
    }
}
