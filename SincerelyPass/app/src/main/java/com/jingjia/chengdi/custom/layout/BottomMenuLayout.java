package com.jingjia.chengdi.custom.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class BottomMenuLayout extends ViewGroup{
	Context context;
	final int MAIN_CIRCLE_R = 40;
	final int CHILE_CIRCLE_R = 35;
	final int margin = 15;
	private int w;
	private int height;


	public BottomMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

	}

//	private void initPaint() {
//		paint = new Paint();
//		paint.setAntiAlias(true);
//		paint.setColor(Color.GRAY);
//		paint.setAlpha(150);
//
//	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		w = getWidth();
		View bg_view = getChildAt(0);//
		bg_view.layout(0, 0, getWidth(), getHeight());
		View mainView = getChildAt(1);
		mainView.layout(w / 2 - MAIN_CIRCLE_R, margin, w / 2 + MAIN_CIRCLE_R, 2
				* MAIN_CIRCLE_R + margin);
		View childView2 = getChildAt(2);
		childView2.layout(w / 4 - CHILE_CIRCLE_R, MAIN_CIRCLE_R / 2 + margin, w
				/ 4 + CHILE_CIRCLE_R, MAIN_CIRCLE_R / 2 + 2 * CHILE_CIRCLE_R
				+ margin);
		View childView3 = getChildAt(3);
		childView3.layout(w / 4 - CHILE_CIRCLE_R + w / 2, MAIN_CIRCLE_R / 2
				+ margin, w / 4 + CHILE_CIRCLE_R + w / 2, MAIN_CIRCLE_R / 2 + 2
				* CHILE_CIRCLE_R + margin);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		height = (MAIN_CIRCLE_R + CHILE_CIRCLE_R) * 2 - 2*margin;
		setMeasuredDimension(widthMeasureSpec, height);
	}

}
