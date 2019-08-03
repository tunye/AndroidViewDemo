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

public class EnterAdapter extends RecyclerView.Adapter<EnterAdapter.ItemViewHolder> {
    private String[] names = {"Banner", "仿QQ小气泡", "回弹效果", "照片选择器", "皮肤效果", "视频缓存", "数字效果", "进度条", "动画效果", "画中画"};
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
                    case 1:
                        context.startActivity(new Intent(context, TestActivity2.class));
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
