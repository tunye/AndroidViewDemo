package com.buaa.ct.easyui.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.easyui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class SkidDetailActivity extends CoreBaseActivity {
    private ImageView mImgBg;
    private ImageView mImgGif;
    private TextView mTvTitle;
    private int mImgPath;

    @Override
    public int getLayoutId() {
        return R.layout.activity_skid_detail;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mImgBg = findViewById(R.id.img_bg);
        mTvTitle = findViewById(R.id.tv_title);
        mImgGif = findViewById(R.id.img_gif);
    }

    @Override
    public void setListener() {
        super.setListener();
        mImgGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        mImgPath = getIntent().getIntExtra("img", R.drawable.skid_right_3);
        String titleContent = getIntent().getStringExtra("title");
        mTvTitle.setText(titleContent);
        Glide.with(this).asBitmap().load(mImgPath).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(mImgBg);
        mTvTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                Glide.with(context).asGif().load(mImgPath).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(mImgGif);
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mImgGif.setVisibility(View.INVISIBLE);
    }
}
