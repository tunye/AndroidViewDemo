package com.buaa.ct.easyui.share.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;


import com.buaa.ct.easyui.R;
import com.buaa.ct.easyui.share.listener.IOperationResultInt;

/**
 * Created by 10202 on 2015/10/28.
 */
public class ContextMenu {
    private View root;
    private Context context;
    private IOperationResultInt operationResultInt;
    private ShareDialog dialog;
    private boolean shown;

    public ContextMenu(Context context) {
        this.context = context;
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = vi.inflate(R.layout.share_menu, null);
        init();
    }

    private void init() {
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog = new ShareDialog(context, root, true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                shown = false;
            }
        });
    }

    public void show() {
        dialog.show();
        //YoYo.with(Techniques.BounceIn).duration(1000).playOn(dialog);
        shown = true;
    }

    public void dismiss() {
        dialog.dismiss();
        shown = false;
    }

    public boolean isShown() {
        return shown;
    }
}
