package com.jingjia.chengdi.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jingjia.chengdi.R;

/**
 * Created by Administrator on 2016/9/28.
 */
public class EditContentDialog extends AlertDialog implements View.OnClickListener {
    private EditText etContent;
    private Button bnCancel, bnConfirm;

    public EditContentDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        View v = getLayoutInflater().inflate(R.layout.dialog_content, null);
        etContent = (EditText) v.findViewById(R.id.dialog_edit_content);
        etContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                System.out.println(v.getText().toString());
                return false;
            }
        });
        bnCancel = (Button) v.findViewById(R.id.dialog_cancel);
        bnConfirm = (Button) v.findViewById(R.id.dialog_confirm);
        bnCancel.setOnClickListener(this);
        setView(v);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_cancel:
                cancel();
                break;
            case R.id.dialog_confirm:
                break;
        }
    }

    public void setBnConfirmListener(View.OnClickListener listener) {
        bnConfirm.setOnClickListener(listener);
    }

    //得到Edit填写的内容
    public String getEditContent() {
        return etContent.getText().toString().trim();
    }
}
