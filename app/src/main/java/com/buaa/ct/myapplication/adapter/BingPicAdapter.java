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
import com.buaa.ct.core.util.ImageUtil;
import com.buaa.ct.imageselector.view.OnlyPreviewActivity;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.entity.BingPic;

import java.util.ArrayList;
import java.util.List;

public class BingPicAdapter extends CoreRecyclerViewAdapter<BingPic, BingPicAdapter.MyViewHolder> {
    public BingPicAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bing_pic, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final BingPic item = getDatas().get(position);
        holder.time.setText(item.date);
        holder.copyright.setText(item.copyRight);
        holder.copyrightDetail.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                Uri uri = Uri.parse(item.copyRightUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
        ImageUtil.loadImage("http://s.cn.bing.net" + item.urlBase + "_640x360.jpg&rf=LaDigue_640x360.jpg", holder.pic);
        holder.pic.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                OnlyPreviewActivity.startPreview(context, getPicUrls(), holder.getAdapterPosition(), true);
            }
        });
    }

    private List<String> getPicUrls() {
        List<String> urls = new ArrayList<>();
        for (BingPic pic : getDatas()) {
            urls.add("http://s.cn.bing.net" + pic.url);
        }
        return urls;
    }

    static class MyViewHolder extends CoreRecyclerViewAdapter.MyViewHolder {
        public TextView time, copyright, copyrightDetail;
        public ImageView pic;

        public MyViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.bing_pic_time);
            copyright = view.findViewById(R.id.bing_pic_copyright);
            copyrightDetail = view.findViewById(R.id.bing_pic_copyright_url);
            pic = view.findViewById(R.id.bing_pic_pic);
        }

    }
}
