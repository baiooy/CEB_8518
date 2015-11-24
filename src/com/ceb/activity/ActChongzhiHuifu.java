package com.ceb.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.loadingdialog.LoadingDialog;
import com.spp.SppaConstant;
import android.graphics.Bitmap; 

@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
public class ActChongzhiHuifu extends BaseActivity {
	private WebView wv_chongzhi;
	private String money;
	private SharedPreferences sp;
	private String loginname,userID;
	private String requestUrl;
	public int count;
	LoadingDialog dialog;
	private TextView tv_title;
	@Override
	public void setView() {
		setContentView(R.layout.act_chongzhihuifu);

		Intent in = getIntent();
		money = in.getStringExtra("money");
		
		sp = getSharedPreferences("USERID", 0);
		loginname = sp.getString("loginname", "null");
		userID = sp.getString("userID", "0");
		tv_title = (TextView) findViewById(R.id.tv_title);
		String  signature = "recharge.php:"+System.currentTimeMillis()/1000+":hxpay";
		try {
			requestUrl = SppaConstant.getIPmobilev11()+"recharge.php?" 
					+"signature="+RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes())
									+"&platformID="+SppaConstant.ANDROID+"&userID="+userID+
									"&loginname="+loginname+"&money="+money;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("requestUrl", requestUrl);
	}

	@Override
	public void initView() {
		wv_chongzhi = (WebView) findViewById(R.id.wv_chongzhi);
		
		wv_chongzhi.setOnTouchListener(new View.OnTouchListener() {  
		    @Override  
		    public boolean onTouch(View v, MotionEvent event) {  
		    	wv_chongzhi.requestFocus();  
		         return false;  
		    }
 
		});  
		
		
		wv_chongzhi.setWebViewClient(new WebViewClient()
	    {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url)
	      {

	    	view.loadUrl(url); 
	        return true;
	      }
	      
	      @Override
	    	public void onPageFinished(WebView view, String url) {
	    	  Log.i("onPageFinished%%%%", ""+ wv_chongzhi.getProgress());
	    	  
	    	  count++;
	    	  
	    	}
	    });
		
		
		
		WebSettings webSettings = wv_chongzhi.getSettings();
		webSettings.setBuiltInZoomControls(true);//支持缩放
		webSettings.setJavaScriptEnabled(true);
		wv_chongzhi.addJavascriptInterface(new WebTOANDROIDInterface(), "android");
//		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//		wv_chongzhi.clearCache(true);  
		wv_chongzhi.loadUrl(requestUrl);
		if (dialog == null) {
			  dialog=new LoadingDialog(ActChongzhiHuifu.this);
			  dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
		//	  dialog.show();
		//	  wv_chongzhi.setEnabled(false);// 当加载网页的时候将网页进行隐藏
		}
		wv_chongzhi.setWebChromeClient(new WebChromeClient(){
				@Override
				public void onProgressChanged(WebView view, int newProgress) 
				{
					setTitle("页面加载中，请稍候..." + newProgress + "%");
					 setProgress(newProgress * 100); 
					Log.i("onProgressChanged%%%%", ""+ wv_chongzhi.getProgress() +"////"+newProgress);
					if (newProgress>=90) {
						if (count>0) {
							 try {
									if (dialog != null && dialog.isShowing()) {
										  dialog.dismiss();
										  dialog = null;
										  wv_chongzhi.setEnabled(true);
										  count =0;
										  }
								} catch (Exception e) {
									e.printStackTrace();
								}
						}
						
						 
					}
					
					ActChongzhiHuifu.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
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
	public class WebTOANDROIDInterface{
		@JavascriptInterface
		public void finish() { 
			Intent ii = new Intent(ActChongzhiHuifu.this,MainActivity.class);
			MainActivity.instance.finish();
			ii.putExtra("hot", "hot");
			startActivity(ii);
			ActChongzhiHuifu.this.finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		    count =0;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCoder,KeyEvent event){
        if(wv_chongzhi.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
        	wv_chongzhi.goBack();   //goBack()

                return true;
        }else if(keyCoder == KeyEvent.KEYCODE_BACK){
        	ActChongzhiHuifu.this.finish();//按了返回键，但已经不能返回(mWebView.canGoBack()=false)，则执行退出确认
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

	}
	
	public void onback(View v){
		finish();
	}
	
//	  final class InJavaScriptLocalObj { 
//	        public void showSource(String html) { 
//	            System.out.println("====>html="+html); 
//	        } 
//	    } 
	
}
