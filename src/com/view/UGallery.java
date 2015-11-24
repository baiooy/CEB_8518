package com.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;
import android.widget.Toast;

public class UGallery extends Gallery {  
	Context mContext;
    @SuppressWarnings("deprecation")
	public UGallery(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        mContext = context;
    }  
  
    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {  
        return e2.getX() > e1.getX();  
    }  
  
    @SuppressWarnings("deprecation")
	@Override  
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
            float velocityY) {  
        int keyCode;  
        if (isScrollingLeft(e1, e2)) {        
            keyCode = KeyEvent.KEYCODE_DPAD_LEFT;  
        } else {  
            keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;  
        }  
        onKeyDown(keyCode, null);  
        return true;  
    }  
}  
