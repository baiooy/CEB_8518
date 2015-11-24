package com.ceb.widge;


import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class Tools  
{
	/**
	 * @author lianglin
	 * @param act The Activity which invoke this function
	 * @param viewId The view of the activity
	 * @param imageId The guide image
	 * @param preferenceName The sharePreference which save the flag whether first login in
	 * */
	public static void setGuidImage(Activity act,int viewId, int imageId, String preferenceName,String flag)
	{
		@SuppressWarnings("static-access")
		SharedPreferences preferences = act.getSharedPreferences(preferenceName,act.MODE_PRIVATE);
		final SharedPreferences.Editor editor = preferences.edit();
		final String key = preferenceName+"_firstLogin";//act.getClass().getName()+"_firstLogin";
//		if(!preferences.contains(key))
//		{
//			editor.putBoolean(key, true);
//			editor.commit();
//		}
//		
//		if(!preferences.getBoolean(key, true))
//			return;
		
		View view = act.getWindow().getDecorView().findViewById(viewId);
		ViewParent viewParent = (ViewParent) view;//.getParent();
		if(viewParent instanceof FrameLayout)
		{
            final FrameLayout frameLayout = (FrameLayout)viewParent;
            final ImageView guideImage = new ImageView(act.getApplication());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            guideImage.setLayoutParams(params);
            guideImage.setScaleType(ScaleType.FIT_XY);
                guideImage.setImageResource(imageId);
                frameLayout.removeView(guideImage);
            if(flag.equals("close")&frameLayout != null){
            	  frameLayout.removeView(guideImage);
            	  frameLayout.removeView(guideImage);
                  editor.putBoolean(key, false);
            	  Log.e("close", "close");
                 editor.commit();
            }else{
           
            	Log.e("open", "open");
            	  
              
                guideImage.setOnClickListener(new View.OnClickListener() 
                {
                    @Override
                    public void onClick(View v)
                    {
                    	Log.e("onClick", "onClick");
                        frameLayout.removeView(guideImage);
                        editor.putBoolean(key, false);
                        editor.commit();
                     //   MainActivity._handler.sendEmptyMessage(112);
                    }
                });
                frameLayout.removeView(guideImage);
                
                frameLayout.addView(guideImage);
                
              }
               
        }
	}
}
