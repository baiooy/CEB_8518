package com.ceb.activity;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("SetJavaScriptEnabled")
public class ActBannerDetail extends BaseActivity {
	private WebView wv_aboutus;
	private String requestUrl = null;
	private SharedPreferences sp;
	private String userID,title;
	UserInfoService _userInfoService;
	private Handler _handler;
	private TextView tv_title;
	private LoadingDialog dialog;
	@Override
	public void setView() {
		setContentView(R.layout.act_bannerdetail);
		
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
//		_userInfoService = new UserInfoService(this);
		
		
		MobclickAgent.setDebugMode(true);
		
		
		
		Intent in = getIntent();
		requestUrl = in.getStringExtra("url");
		title = in.getStringExtra("title");
		
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActBannerDetail.this,
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
				//	wv_aboutus.loadUrl(requestUrl);
					
					break;
				case 3:
					break;
				case 2:
					break;
				default:
					break;
				}
			};
		};
		
	
		
	}

	@Override
	protected void onResume() {
		/**
		 * arg0  -- Context 
		 * arg1  -- id 为事件ID 
		 * arg2  -- map 为当前事件的属性和取值 
		 * arg3  -- du 为当前事件的数值，取值范围是-2,147,483,648 到 +2,147,483,647 之间的有符号整数，
		 * 			即int 32整型，如果du数据值超过该范围，会造成数据丢包，影响数据统计的准确性。 
		 */
		   HashMap<String,String> map = new HashMap<String,String>();
		   map.put("userID",userID);
		   MobclickAgent.onEvent(this, "Banner", map);  
		super.onResume();
	}
	
	@Override
	public void initView() {
		wv_aboutus = (WebView) findViewById(R.id.wv_aboutus);
		tv_title = (TextView) findViewById(R.id.tv_title);
		
		tv_title.setText(title);
		wv_aboutus.setOnTouchListener(new View.OnTouchListener() {  
		    @Override  
		    public boolean onTouch(View v, MotionEvent event) {  
		    	wv_aboutus.requestFocus();  
		         return false;  
		    }
 
		});  
		
		
		wv_aboutus.setWebViewClient(new WebViewClient()
	    {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url)
	      {

	        view.loadUrl(url); 

	        return true;
	      }
	    });
		
		dialog = new LoadingDialog(ActBannerDetail.this);
		dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
		dialog.show();
		
		WebSettings webSettings = wv_aboutus.getSettings();
		webSettings.setBuiltInZoomControls(true);//����
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		wv_aboutus.setWebChromeClient(new WebChromeClient());
		wv_aboutus.loadUrl(requestUrl);
		
		wv_aboutus.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) 
			{
				if(newProgress > 70){
					if(dialog != null & dialog.isShowing())
						dialog.dismiss();
				}
				super.onProgressChanged(view, newProgress);
			}
			
			@Override
			public void onReceivedTitle(WebView view, String title) 
			{
				super.onReceivedTitle(view, title);
				tv_title.setText(title);
			}
		});
		
	}
	


	@Override
	public boolean onKeyDown(int keyCoder,KeyEvent event){
        if(wv_aboutus.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
        	wv_aboutus.goBack();   //goBack()��ʾ����webView����һҳ��

                return true;
        }else if(keyCoder == KeyEvent.KEYCODE_BACK){
        	ActBannerDetail.this.finish();//按了返回键，但已经不能返回(mWebView.canGoBack()=false)，则执行退出确认
        	return true; 
        }  
        return false;
}
	
	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	public void onback(View v){
		finish();
	}
		

}
