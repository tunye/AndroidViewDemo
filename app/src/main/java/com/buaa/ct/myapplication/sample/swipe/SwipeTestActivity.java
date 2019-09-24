package com.buaa.ct.myapplication.sample.swipe;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.myapplication.R;
import com.buaa.ct.myapplication.sample.base.BaseActivity;
import com.buaa.ct.swipe.SmartSwipeWrapper;
import com.buaa.ct.swipe.consumer.SlidingConsumer;

public class SwipeTestActivity extends BaseActivity {
    Button recycler, drawer, bezier, stay, slide, door, shutters;

    @Override
    public int getLayoutId() {
        return R.layout.swipe_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        recycler = findViewById(R.id.go_list);
        drawer = findViewById(R.id.go_drawer);
        bezier = findViewById(R.id.go_bezier);
        stay = findViewById(R.id.go_stay);
        slide = findViewById(R.id.go_slide);
        door = findViewById(R.id.go_door);
        shutters = findViewById(R.id.go_shutters);
    }

    @Override
    public void setListener() {
        super.setListener();
        recycler.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                startActivity(new Intent(context, SwipeTestListActivity.class));
            }
        });
        drawer.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                startActivity(new Intent(context, SwipeTestForDrawer.class));
            }
        });
        bezier.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                startActivity(new Intent(context, SwipeTestForBezier.class));
            }
        });
        stay.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                startActivity(new Intent(context, SwipeTestForStay.class));
            }
        });
        slide.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                startActivity(new Intent(context, SwipeTestForSlide.class));
            }
        });
        door.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                startActivity(new Intent(context, SwipeTestForDoor.class));
            }
        });
        shutters.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                startActivity(new Intent(context, SwipeTestForShutters.class));
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.test_swipe);

        SmartSwipeWrapper wrapper = findViewById(R.id.swipe_example_view);
        wrapper.addConsumer(new SlidingConsumer()).setRelativeMoveFactor(SlidingConsumer.FACTOR_FOLLOW);
    }
}
