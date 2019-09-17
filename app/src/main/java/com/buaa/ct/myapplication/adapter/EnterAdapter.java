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
import com.buaa.ct.easyui.pulldown.FlexibleTestActivity;
import com.buaa.ct.imageselector.sample.ImageSelectorTestActivity;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.activity.TestActivity;
import com.buaa.ct.myapplication.activity.UIActivity;
import com.buaa.ct.myapplication.sample.appskin.SkinActivity;
import com.buaa.ct.myapplication.sample.comment.CommentTestActivity;
import com.buaa.ct.myapplication.sample.copyboard.CopyboardTestActivity;
import com.buaa.ct.myapplication.sample.pudding.PuddingTestActivity;
import com.buaa.ct.myapplication.sample.videocache.VideoCacheTestActivity;
import com.buaa.ct.qrcode.sample.QRCodeTestActivity;

import java.util.Arrays;

public class EnterAdapter extends CoreRecyclerViewAdapter<String, EnterAdapter.ItemViewHolder> {
    private static final int count = 9;

    public EnterAdapter(Context context) {
        super(context);
        String[] nameList = {
                RuntimeManager.getInstance().getString(R.string.web_flexible_test),
                RuntimeManager.getInstance().getString(R.string.test_select_image),
                RuntimeManager.getInstance().getString(R.string.test_skin),
                RuntimeManager.getInstance().getString(R.string.test_video_cache),
                RuntimeManager.getInstance().getString(R.string.qr_code_test),
                RuntimeManager.getInstance().getString(R.string.test_comment),
                RuntimeManager.getInstance().getString(R.string.test_pudding),
                RuntimeManager.getInstance().getString(R.string.test_copyboard)};
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
        viewHolder.root.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
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
                        context.startActivity(new Intent(context, QRCodeTestActivity.class));
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
