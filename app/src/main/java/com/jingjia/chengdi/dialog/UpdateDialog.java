package com.jingjia.chengdi.dialog;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jingjia.chengdi.R;

/**
 * Created by Administrator on 2016/9/28.
 */
public class UpdateDialog implements View.OnClickListener {
    private CDDialog cdDialog;
    private ImageView imIcon;
    private Button bnConfirm, bnCancel;
    private Context context;
    private ProgressBar progress;

    public UpdateDialog(Context context) {
        this.context = context;
        cdDialog = new CDDialog(context);
        imIcon = cdDialog.getImIcon();
        bnConfirm = cdDialog.getBnConfirm();
        bnCancel = cdDialog.getBnCancel();
        progress = cdDialog.getProgressBar();
        cdDialog.setBnConfirmListener(this);
        cdDialog.setImIcon(R.drawable.ic_dialog_alert);
        cdDialog.setContent("发现新版本，有更多惊喜噢！");
        bnConfirm.setText("升级");
        bnCancel.setText("忽略");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_cancel:
                break;
            case R.id.dialog_confirm:
                confirm();
                break;
        }
    }

    private void confirm() {
//动画
        final AnimationSet mAnimWait = (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.ic_waiting_out);
        AnimationSet mAnimOut = (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.ic_out);
        mAnimOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cdDialog.setImIcon(R.drawable.ic_dialog_waiting);
                imIcon.startAnimation(mAnimWait);
                bnConfirm.setClickable(false);
                cdDialog.setContent("更新中...");
                cdDialog.setCanceledOnTouchOutside(false);//禁止边界取消
                progress.setVisibility(View.VISIBLE);
                bnCancel.setText("取消");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imIcon.startAnimation(mAnimOut);
    }

    public void show() {
        cdDialog.show();
    }
}
