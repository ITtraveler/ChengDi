package com.jingjia.chengdi.custom.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.jingjia.chengdi.R;

/**
 * Created by Administrator on 2016/9/11.
 */
public class SwitchView extends View implements View.OnClickListener, View.OnTouchListener {
    private final String TAG = "Switch_View";
    private Bitmap switch_bg, switch_handle;
    private int view_width, view_height;
    private int space;
    private Paint paint;
    private boolean switchStatus = false;//初始状态为关
    private int translateX = 0;
    private boolean haveSave;
    private int oldX;
    private int dis = 0;
    private boolean intercept;//touch中点击事件是否拦截

    public SwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SwitchView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);
       // switch_bg = BitmapFactory.decodeResource(getResources(), R.drawable.switch_bg);
        switch_handle = BitmapFactory.decodeResource(getResources(), R.drawable.switch_handle);
        view_height = switch_handle.getHeight();
        view_width = 350;//switch_bg.getWidth();
        space = view_width - switch_handle.getWidth();//显示的蓝色区域宽
        setOnClickListener(this);
        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(view_width, view_height);//设置下view的宽高
        Log.i(TAG, "Measure w and h:" + widthMeasureSpec + "    " + heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF reacF = new RectF(0f, 0f, 350f, 100f);
        paint.setColor(getResources().getColor(R.color.colorMain));
        canvas.drawRoundRect(reacF, 15f, 15f, paint);
//        canvas.drawBitmap(switch_bg, 0, 0, paint);
        canvas.drawBitmap(switch_handle, translateX, 0, paint);
    }

    @Override
    public void onClick(View v) {
        if (switchStatus) {
            translateX = 0;
            switchStatus = false;
        } else {
            translateX = space;
            switchStatus = true;
        }
        invalidate();
        //switchStatus = switchStatus ==true ? false:true;
    }

    /**
     * 得到开关当前状态
     *
     * @return
     */
    public boolean getSwitchStatus() {
        return switchStatus;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                if (!haveSave) {
                    oldX = (int) event.getX();
                    haveSave = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int curX = (int) event.getX();
                actionMove(curX);
                intercept = true;
                break;
            case MotionEvent.ACTION_UP:
                dealStatus();
                break;
            case MotionEvent.ACTION_CANCEL:
                dealStatus();
                break;
        }
        return intercept;
    }

    private void dealStatus() {
        haveSave = false;
        if (translateX < space / 2) {//只滑到了不到一半，弹回原来位置
            translateX = 0;
            switchStatus = false;
        } else {
            translateX = space;
            switchStatus = true;
        }
        invalidate();
    }

    private void actionMove(int curX) {
        if (!switchStatus) {//关状态
            if (curX > oldX && translateX <= space) {
                translateX += 8;
                invalidate();
            }
        } else {
            if (curX < oldX && translateX > 0) {
                translateX -= 8;
                invalidate();
            }
        }
        oldX = curX;
    }
}
