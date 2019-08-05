package com.buaa.ct.easyui.DraggableFlag;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.buaa.ct.easyui.R;

public class DraggableFlagTestActivity extends AppCompatActivity implements DraggableFlagView.OnDraggableFlagViewListener {
    DraggableFlagView draggableFlagView, buttonDraggableFlagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draggable_flag_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //buttonDraggableFlagView.reset();

                draggableFlagView.dismiss();
//                new UndoBarController.UndoBar(MainActivity.this).message("恢复一键退朝").style(UndoBarController.RETRYSTYLE).duration(3000L).listener(MainActivity.this).show();
                //draggableFlagView.reset();
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
        buttonDraggableFlagView.setOnDraggableFlagViewListener(this);
    }

    @Override
    public void onFlagDismiss(DraggableFlagView view) {
        Toast.makeText(this, "onFlagDismiss", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFlagUnDismiss(DraggableFlagView view) {
        Toast.makeText(this, "more distance", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onUndo(Parcelable var) {
//        draggableFlagView.reset();
//    }

}
