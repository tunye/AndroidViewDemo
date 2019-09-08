package com.buaa.ct.easyui.boundnumber;

import android.view.View;
import android.widget.Button;

import com.buaa.ct.core.CoreBaseActivity;
import com.buaa.ct.core.view.CustomToast;
import com.buaa.ct.easyui.R;


public class BoundNumTestActivity extends CoreBaseActivity {

    private RiseNumberTextView number1, number2, number3, number4, number5;
    private Button start;
    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == start) {
                initData();
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.bound_num_test;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
        number5 = findViewById(R.id.number5);
        start = findViewById(R.id.start);
    }

    @Override
    public void setListener() {
        super.setListener();
        start.setOnClickListener(listener);
    }

    @Override
    public void onActivityCreated() {
        super.onActivityCreated();
        title.setText(R.string.bound_num_test);
    }

    private void initData() {
        number1.withNumber(1112.3f).start();
        number2.withNumber(546316.3f, false).start();
        number3.withNumber(10.3f).start();
        number4.withNumber(18).start();
        number5.withNumber(1111231213).setDuration(500).start();
        number5.setOnEnd(new RiseNumberTextView.EndListener() {
            @Override
            public void onEndFinish() {
                CustomToast.getInstance().showToast(number5.getText() + " is faster!");
            }
        });
    }
}
