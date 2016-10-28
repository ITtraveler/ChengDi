package com.jingjia.chengdi.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jingjia.chengdi.R;

/**
 * Created by Administrator on 2016/9/27.
 */
public class CDDialog extends AlertDialog implements View.OnClickListener {

    private ImageView imIcon;
    private TextView tvContent;
    private Context context;
    private Button bnCancel, bnConfirm;
    private ProgressBar progressBar;
    private View buttons;

    public CDDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CDDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        View v = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        buttons = v.findViewById(R.id.dialog_bn);
        bnCancel = (Button) v.findViewById(R.id.dialog_cancel);
        bnConfirm = (Button) v.findViewById(R.id.dialog_confirm);
        imIcon = (ImageView) v.findViewById(R.id.dialog_icon);
        tvContent = (TextView) v.findViewById(R.id.dialog_content);
        progressBar = (ProgressBar) v.findViewById(R.id.dialog_progress);
        bnCancel.setOnClickListener(this);
        bnConfirm.setOnClickListener(this);
        setView(v);
    }

    /**
     * 设置内容
     *
     * @param content
     */
    public void setContent(String content) {
        tvContent.setText(content);
    }

    /**
     * 隐藏按钮
     */
    public void setGoneButton() {
        bnCancel.setVisibility(View.GONE);
        bnConfirm.setVisibility(View.GONE);
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

    //设置确认按钮的监听器
    public void setBnConfirmListener(View.OnClickListener confirmListener) {
        bnConfirm.setOnClickListener(confirmListener);
    }

    public void setBnConfirmText(String s) {
        bnConfirm.setText(s);
    }

    public ImageView getImIcon() {
        return imIcon;
    }

    public void setImIcon(int resource) {
        Drawable drawable = context.getResources().getDrawable(resource);
        imIcon.setImageDrawable(drawable);
    }

    public Button getBnCancel() {
        return bnCancel;
    }

    public Button getBnConfirm() {
        return bnConfirm;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void hideButton() {
        buttons.setVisibility(View.GONE);
    }
}
