package com.jingjia.chengdi.dialog;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.jingjia.chengdi.R;

/**
 * Created by Administrator on 2016/10/2.
 */
public class WaitingDialog {
    private final ImageView imIcon;
    private Context context;
    private CDDialog cdDialog;

    public WaitingDialog(Context context) {
        this.context = context;
        cdDialog = new CDDialog(context);
        cdDialog.hideButton();
        cdDialog.setContent("完成");
        imIcon = cdDialog.getImIcon();
        confirm();
    }


    public void confirm() {
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
                cdDialog.setContent("请稍后...");
                cdDialog.setCanceledOnTouchOutside(false);//禁止边界取消

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imIcon.startAnimation(mAnimOut);
    }
    public void setContent(String content){
        cdDialog.setContent(content);
    }

    public void cancel() {
        cdDialog.cancel();
    }

    public void show() {
        cdDialog.show();
    }
}
