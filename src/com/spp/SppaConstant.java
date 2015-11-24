package com.spp;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;






public class SppaConstant {
	static int width,height;
	public static String getIPmobile() {
				return "http://interface.8518.com/app/cebv11/";//测试服务器
				//				return "http://interface.8518.com/app/ceb/";		//正式
	}
	public static String getIPmobilev11() {
		return "http://interface.8518.com/app/cebv11/";//测试服务器
		//				return "http://interface.8518.com";		//正式
}

    public final static String WCC_KEYWORD = "PRODUCT_URL";
    public final static String APP_VERSION = "1";		 // Official
    public final static String ANDROID = "2";
    public final static String IOS = "1";
    
    /**
     * 获取设备的分辨率-宽度
     * @param ac
     * @return
     */
    public static int getWidth(Activity ac){
    	DisplayMetrics dm = new DisplayMetrics();
    	ac.getWindowManager().getDefaultDisplay().getMetrics(dm);
    	width = dm.widthPixels;
 //   	height = dm.heightPixels;
    	Log.i("系统信息", "该设备的分辨是：" + width + "*" + height);//480*854
    	return width;
    	
    }
    
    /**
     * 获取设备的分辨率-高度
     * @param ac
     * @return
     */
    public static int getHeight(Activity ac){
    	DisplayMetrics dm = new DisplayMetrics();
    	ac.getWindowManager().getDefaultDisplay().getMetrics(dm);
//    	width = dm.widthPixels;
    	height = dm.heightPixels;
    	Log.i("系统信息", "该设备的分辨是：" + width + "*" + height);//480*854
    	return height;
    	
    }
    
    /**
     * 判断设备里是否安装某个程序包
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible( Context context, String packageName )
    {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for ( int i = 0; i < pinfo.size(); i++)
        {
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
	 * 获取设备唯一标识符
	 * @param context
	 * @return device_id ，mac
	 */
	public static String getDeviceInfo(Context context) {
		try {
			Log.i("SppaConstant_getDeviceInfo", "getDeviceInfo");
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			return device_id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
}
	
	
	public final static boolean isScreenLocked(Context c) {
//        android.app.KeyguardManager mKeyguardManager = (KeyguardManager) c.getSystemService(c.KEYGUARD_SERVICE);
//        return !mKeyguardManager.inKeyguardRestrictedInputMode();
		
		PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
		boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
		
		return isScreenOn;
    }
	
	
	
	/**
	 * 将时间戳转换成年月日格式的字符串
	 * @param time   时间戳字符串
	 * @return       年月日格式的字符串
	 */
	public static String getStrTime(String time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = null;
        if (time.equals("")) {
                return "";
        }
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long loc_time = Long.valueOf(time);
        re_StrTime = sdf.format(new Date(loc_time * 1000L));
        return re_StrTime;
}

	public static boolean isNumeric(String str){    
		  
	       Pattern pattern = Pattern.compile("[.0-9]*");    
	  
	        Matcher isNum = pattern.matcher(str);   
	 
	        if( !isNum.matches() ){   
	 
	           return false;    
	 
	       }    
	
	       return true;    
	
	     }  
	
	
	/**
	 * 判断一串字符串中是否包含中文字符
	 * @param str
	 * @return
	 */
	 public static boolean isCN(String str){
	        try {
	            byte [] bytes = str.getBytes("UTF-8");
	            if(bytes.length == str.length()){
	                return false;
	            }else{
	                return true;
	            }
	        } catch (UnsupportedEncodingException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return false;
	    }
	 
	 /**
	  * 判断网络是否连接
	  * @param c
	  * @return
	  */
	 public static boolean isNetworkAvailable(Context c) {
			try {
				ConnectivityManager connectivity = (ConnectivityManager) 
						c.getSystemService(Context.CONNECTIVITY_SERVICE);
				if (connectivity == null) {
					return false;
				} else {
					NetworkInfo info = connectivity.getActiveNetworkInfo();
					if (info.isAvailable()) {
						return true;
					}
				}
			} catch (Exception e) {
			}
			return false;
		}
	 
	 
	 
	 
	 
	 
	 
	 
	 
    
}
