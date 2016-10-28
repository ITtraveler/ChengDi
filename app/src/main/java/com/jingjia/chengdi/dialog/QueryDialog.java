package com.jingjia.chengdi.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.jingjia.chengdi.R;

/**
 * Created by Administrator on 2016/9/28.
 * 询问对话框，确认后  进行中
 */
public class QueryDialog {
    private CDDialog cdDialog;
    private ImageView imIcon;
    private Button bnConfirm, bnCancel;
    private Context context;

    public QueryDialog(Context context) {
        this.context = context;
        cdDialog = new CDDialog(context);
        imIcon = cdDialog.getImIcon();
        bnConfirm = cdDialog.getBnConfirm();
        bnCancel = cdDialog.getBnCancel();
        // cdDialog.setBnConfirmListener(this);
    }


//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.dialog_confirm)
//            confirm();
//    }

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
                bnConfirm.setClickable(false);

                cdDialog.setCanceledOnTouchOutside(false);//禁止边界取消
                cdDialog.hideButton();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imIcon.startAnimation(mAnimOut);
    }

    public void setBnConfirmListener(View.OnClickListener listener) {
        bnConfirm.setOnClickListener(listener);
    }

    public void setContent(String content) {
        cdDialog.setContent(content);
    }

    public void show() {
        cdDialog.show();
    }

    public void cancel() {
        cdDialog.cancel();
    }

    public void delayCancel(long delayTime) {
        handler.sendEmptyMessageDelayed(0x1, delayTime);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1) {
                cdDialog.cancel();
            }
        }
    };
}
