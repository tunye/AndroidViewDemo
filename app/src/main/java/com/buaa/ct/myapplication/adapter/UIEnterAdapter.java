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

import com.buaa.ct.easyui.Banner.BannerTestActivity;
import com.buaa.ct.easyui.DraggableFlag.DraggableFlagTestActivity;
import com.buaa.ct.easyui.boundnumber.BoundNumTestActivity;
import com.buaa.ct.easyui.progressimage.ProgressImageTestActivity;
import com.buaa.ct.easyui.progresssbar.NumberProgressbarTestActivity;
import com.buaa.ct.easyui.pulldoor.PullDoorTestActivity;
import com.buaa.ct.easyui.share.ShareDialogTestActivity;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.TestActivity;
import com.buaa.ct.stickydot.StickyDotTestActivity;

public class UIEnterAdapter extends RecyclerView.Adapter<UIEnterAdapter.ItemViewHolder> {
    private String[] names = {"Banner", "一键退朝", "数字效果", "另类进度条", "分享气泡", "另一种一键退朝","推门效果","QQ发图效果"};
    private Context context;

    public UIEnterAdapter(Context context) {
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
        viewHolder.root.setTag(i);
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
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
                        context.startActivity(new Intent(context, PullDoorTestActivity.class));
                        break;
                    case 7:
                        context.startActivity(new Intent(context, ProgressImageTestActivity.class));
                        break;
                    default:
                        context.startActivity(new Intent(context, TestActivity.class));
                        break;
                }
            }
        });
//        AddRippleEffect.addRippleEffect(viewHolder.itemView, 300);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
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
