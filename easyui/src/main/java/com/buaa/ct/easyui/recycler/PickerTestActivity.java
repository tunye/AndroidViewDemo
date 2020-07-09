package com.buaa.ct.easyui.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.easyui.R;
import com.buaa.ct.easyui.recycler.layoutmanager.PickerLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class PickerTestActivity extends CoreBaseActivity {
    private TextView mTvHour;
    private TextView mTvMinute;
    private PickerLayoutManager hourManager;
    private PickerLayoutManager minuteManager;

    private final static List<String> mHours = new ArrayList<>(24);
    private final static List<String> mMinutes = new ArrayList<>(60);

    static {
        for (int i = 0; i < 24; i++) {
            if (i <= 9) {
                mHours.add("0" + i);
            } else {
                mHours.add(i + "");
            }
        }
        for (int i = 0; i < 60; i++) {
            if (i <= 9) {
                mMinutes.add("0" + i);
            } else {
                mMinutes.add(i + "");
            }
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.recycler_picker;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        RecyclerView hour = findViewById(R.id.picker_hour);
        RecyclerView minute = findViewById(R.id.picker_minute);
        mTvHour = findViewById(R.id.tv_hour);
        mTvMinute = findViewById(R.id.tv_minute);

        hourManager = new PickerLayoutManager(context, hour, PickerLayoutManager.VERTICAL, false, 3, 0.4f, true);
        hour.setLayoutManager(hourManager);
        hour.setAdapter(new MyAdapter(mHours));

        minuteManager = new PickerLayoutManager(context, minute, PickerLayoutManager.VERTICAL, false, 3, 0.4f, true);
        minute.setLayoutManager(minuteManager);
        minute.setAdapter(new MyAdapter(mMinutes));

        hour.scrollToPosition(0);
        minute.scrollToPosition(0);
        mTvHour.setText("00");
        mTvMinute.setText("00");
    }

    @Override
    public void setListener() {
        super.setListener();
        hourManager.setOnSelectedViewListener(new PickerLayoutManager.OnSelectedViewListener() {
            @Override
            public void onSelectedView(View view, int position) {
                TextView textView = (TextView) view;
                if (textView != null) mTvHour.setText(textView.getText());
            }
        });

        minuteManager.setOnSelectedViewListener(new PickerLayoutManager.OnSelectedViewListener() {
            @Override
            public void onSelectedView(View view, int position) {
                TextView textView = (TextView) view;
                if (textView != null) mTvMinute.setText(textView.getText());
            }
        });
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.recycler_picker);
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<String> mList;

        public MyAdapter(List<String> list) {
            this.mList = list;
        }

        @Override
        public @NonNull
        ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picker, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tvText.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvText;

            public ViewHolder(View itemView) {
                super(itemView);
                tvText = itemView.findViewById(R.id.tv_text);
            }
        }
    }
}
