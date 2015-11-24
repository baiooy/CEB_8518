package com.ceb.widge;

import com.caifuline.R;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class MyRadioButton extends RadioButton {
	public MyRadioButton(Context context) {
		super(context);
	}

	public MyRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean isChecked() {
		return super.isChecked();
	}

	@Override
	public void setButtonDrawable(int resid) {
		super.setButtonDrawable(resid);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isChecked()) {
			super.setButtonDrawable(R.drawable.circle_green); // 为选中RadioButton的图片
		} else {
			super.setButtonDrawable(R.drawable.circle_grey); // 未选中RadioButton的图片
		}
		super.onDraw(canvas);
	}
}
