package com.jingjia.chengdi.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import com.jingjia.chengdi.R;
import com.jingjia.chengdi.custom.view.OnWheelViewListener;
import com.jingjia.chengdi.custom.view.WheelView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */
public class SelectItemDialog extends AlertDialog {

    private WheelView mWheelView;

    public SelectItemDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        View v = getLayoutInflater().inflate(R.layout.dialog_list_select, null);
        mWheelView = (WheelView) v.findViewById(R.id.dialog_wheelView);
        setView(v);
    }

    public void setItem(List<String> items) {
        mWheelView.setItems(items);
    }

    public void setWheelViewListener(OnWheelViewListener listener) {
        mWheelView.setOnWheelViewListener(listener);
    }

}
