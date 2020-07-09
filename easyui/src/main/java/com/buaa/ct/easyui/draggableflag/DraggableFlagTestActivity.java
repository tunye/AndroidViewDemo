package com.buaa.ct.easyui.draggableflag;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.buaa.ct.core.listener.INoDoubleClick;
import com.buaa.ct.easyui.R;

public class DraggableFlagTestActivity extends AppCompatActivity implements OnDraggableFlagViewListener {
    DraggableFlagView draggableFlagView, buttonDraggableFlagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draggable_flag_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new INoDoubleClick() {
            @Override
            public void activeClick(View v) {
                Snackbar.make(v, "一键退朝已清除", Snackbar.LENGTH_SHORT).setAction("恢复一键退朝", new INoDoubleClick() {
                    @Override
                    public void activeClick(View v) {
                        buttonDraggableFlagView.reset();
                        draggableFlagView.reset();
                    }
                }).show();
                draggableFlagView.dismiss();
                buttonDraggableFlagView.dismiss();
            }
        });
        draggableFlagView = findViewById(R.id.main_dfv);
        buttonDraggableFlagView = findViewById(R.id.button_dfv);
//        draggableFlagView.setFixLocation(new int[]{16, 16});
        draggableFlagView.setPaintColor(0xff000000);
        draggableFlagView.setTextColor(Color.RED);
        draggableFlagView.setTextSize(14);
        draggableFlagView.setMaxMove(100);
        draggableFlagView.setText("28");
        draggableFlagView.setAnimInterval(500);
        draggableFlagView.setOnDraggableFlagViewListener(this);
    }

    @Override
    public void onFlagDismiss(DraggableFlagView view) {
        Toast.makeText(this, "onFlagDismiss", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFlagUnDismiss(DraggableFlagView view) {
        Toast.makeText(this, "more distance", Toast.LENGTH_SHORT).show();
    }
}
