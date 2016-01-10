package com.wuxianxi.wxxarcmenuview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.wuxianxi.wxxarcmenuview.R;

public class ArcMenu extends ViewGroup implements OnClickListener {

	private static final String TAG = "wxx";

	private static final int POS_LEFT_TOP = 0;
	private static final int POS_LEFT_BOTTOM = 1;
	private static final int POS_RIGHT_TOP = 2;
	private static final int POS_RIGHT_BOTTOM = 3;

	private Position mPosition = Position.RIGHT_BOTTOM;
	private int mRadius;
	private Status mStatus = Status.CLOSE;
	private View mCButton;
	private onMenuItemClickListener mOnMenuItemClickListener;

	public enum Position {
		LEFT_TOP, LEFT_BOTTOM, RIGHTF_TOP, RIGHT_BOTTOM
	}

	public enum Status {
		OPEN, CLOSE
	}

	public interface onMenuItemClickListener {
		void onClick(View view, int pos);
	}

	public void setOnMenuItemClickListener(
			onMenuItemClickListener onMenuItemClickListener) {
		this.mOnMenuItemClickListener = onMenuItemClickListener;
	}

	public ArcMenu(Context context) {
		this(context, null);
	}

	public ArcMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ArcMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				100, getResources().getDisplayMetrics());

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ArcMenu, defStyle, 0);
		int pos = a.getInt(R.styleable.ArcMenu_position, POS_RIGHT_BOTTOM);
		switch (pos) {
		case POS_LEFT_TOP:
			mPosition = Position.LEFT_TOP;
			break;
		case POS_LEFT_BOTTOM:
			mPosition = Position.LEFT_BOTTOM;
			break;
		case POS_RIGHT_TOP:
			mPosition = Position.RIGHTF_TOP;
			break;
		case POS_RIGHT_BOTTOM:
			mPosition = Position.RIGHT_BOTTOM;
			break;

		default:
			break;
		}

		mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius, TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
						getResources().getDisplayMetrics()));
		Log.d(TAG, "position = " + mPosition + ", radius = " + mRadius);
		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			layoutCButton();
			layoutItemButton();
		}

	}

	private void layoutCButton() {
		mCButton = getChildAt(0);
		mCButton.setOnClickListener(this);

		int l = 0;
		int t = 0;

		int width = mCButton.getMeasuredWidth();
		int height = mCButton.getMeasuredHeight();

		switch (mPosition) {
		case LEFT_TOP:
			l = 0;
			t = 0;
			break;
		case LEFT_BOTTOM:
			l = 0;
			t = getMeasuredHeight() - height;
			break;
		case RIGHTF_TOP:
			l = getMeasuredWidth() - width;
			t = 0;
			break;
		case RIGHT_BOTTOM:
			l = getMeasuredWidth() - width;
			t = getMeasuredHeight() - height;
			break;
		}

		mCButton.layout(l, t, l + width, t + height);

	}

	private void layoutItemButton() {
		int count = getChildCount();
		for (int i = 0; i < count - 1; i++) {
			View child = getChildAt(i + 1);

			child.setVisibility(View.GONE);

			int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
			int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));

			int cWidth = child.getMeasuredWidth();
			int cHeight = child.getMeasuredHeight();

			if (mPosition == Position.LEFT_BOTTOM
					|| mPosition == Position.RIGHT_BOTTOM) {
				ct = getMeasuredHeight() - cHeight - ct;
			}

			if (mPosition == Position.RIGHTF_TOP
					|| mPosition == Position.RIGHT_BOTTOM) {
				cl = getMeasuredWidth() - cWidth - cl;
			}

			child.layout(cl, ct, cl + cWidth, ct + cHeight);

		}
	}

	@Override
	public void onClick(View v) {
		// mCButton = findViewById(R.id.id_button);
		// if (mCButton == null) {
		// mCButton = getChildAt(0);
		// }

		rotateCButton(v, 0f, 360f, 300);
		toggleMenu(300);

	}

	public void toggleMenu(int duration) {

		int count = getChildCount();

		for (int i = 0; i < count - 1; i++) {
			final View child = getChildAt(i + 1);

			child.setVisibility(View.VISIBLE);

			int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
			int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));

			int xflag = 1;
			int yflag = 1;

			if (mPosition == Position.LEFT_TOP
					|| mPosition == Position.LEFT_BOTTOM) {
				xflag = -1;
			}

			if (mPosition == Position.LEFT_TOP
					|| mPosition == Position.RIGHTF_TOP) {
				yflag = -1;
			}

			AnimationSet animSet = new AnimationSet(true);
			Animation tranAnim = null;

			if (mStatus == Status.CLOSE) {
				tranAnim = new TranslateAnimation(xflag * cl, 0, yflag * ct, 0);
				child.setClickable(true);
				child.setFocusable(true);
			} else {
				tranAnim = new TranslateAnimation(0, xflag * cl, 0, yflag * ct);
				child.setClickable(false);
				child.setFocusable(false);
			}
			tranAnim.setFillAfter(true);
			tranAnim.setDuration(duration);
			tranAnim.setStartOffset(i * 100 / count);

			tranAnim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation arg0) {
					if (mStatus == Status.CLOSE) {
						child.setVisibility(View.GONE);
					}

				}
			});

			RotateAnimation rotateAnim = new RotateAnimation(0, 720,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnim.setDuration(duration);
			rotateAnim.setFillAfter(true);

			animSet.addAnimation(rotateAnim);
			animSet.addAnimation(tranAnim);
			child.startAnimation(animSet);

			final int pos = i + 1;
			child.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d(TAG, "Item onclick!");

					if (mOnMenuItemClickListener != null) {
						mOnMenuItemClickListener.onClick(child, pos);
					}

					clickItemAnim(pos - 1);
					changeStatus();

				}

			});

		}

		changeStatus();

	}

	private void clickItemAnim(int pos) {
		Log.d(TAG, "clickItemAnim");

		for (int i = 0; i < getChildCount() - 1; i++) {
			View child = getChildAt(i + 1);
			if (i == pos) {
				child.startAnimation(scaleBigAnim(300));
			} else {
				child.startAnimation(scaleSmallAnim(300));
			}

			child.setClickable(false);
			child.setFocusable(false);
		}

	}

	private Animation scaleSmallAnim(int duration) {
		AnimationSet animSet = new AnimationSet(true);

		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.0f);

		animSet.addAnimation(scaleAnim);
		animSet.addAnimation(alphaAnim);
		animSet.setDuration(duration);
		animSet.setFillAfter(true);
		return animSet;
	}

	private Animation scaleBigAnim(int duration) {
		AnimationSet animSet = new AnimationSet(true);

		ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.0f);

		animSet.addAnimation(scaleAnim);
		animSet.addAnimation(alphaAnim);
		animSet.setDuration(duration);
		animSet.setFillAfter(true);
		return animSet;
	}

	private void changeStatus() {
		mStatus = (mStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE);
	}

	private void rotateCButton(View v, float start, float end, int duration) {
		RotateAnimation anim = new RotateAnimation(start, end,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anim.setDuration(duration);
		anim.setFillAfter(true);
		v.startAnimation(anim);
	}
	
	public boolean isOpen() {
		return mStatus == Status.OPEN;
	}

}
