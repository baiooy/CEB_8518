/**
 * 
 */
package com.wujay.fund.common;

import java.util.List;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;

public class AppUtil {
    
	/**
     * 获取屏幕分辨率
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = 3*windowManager.getDefaultDisplay().getWidth()/4;// 手机屏幕的宽度
		int height = 3*windowManager.getDefaultDisplay().getHeight()/4;// 手机屏幕的高度
		int result[] = { width, height };
		return result;
	}
    
}
