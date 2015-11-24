package com.ceb.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
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

import com.caifuline.R;
import com.ceb.activity.ActTixianHuifu.WebTOANDROIDInterface;
import com.ceb.base.BaseActivity;
import com.ceb.fragment.FragmentHot;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.json.JSONException;
import com.json.JSONObject;
import com.loadingdialog.LoadingDialog;
import com.spp.SppaConstant;

public class ActTouziHuifu extends BaseActivity {
	private WebView wv_touzi;
	private String money,productID,type,title;
	private SharedPreferences sp;
	private String loginname,userID;
	private String requestUrl;
	LoadingDialog dialog;
	private TextView tv_title;
	@Override
	public void setView() {
		setContentView(R.layout.act_touzihuifu);
		
		Intent in = getIntent();
		money = in.getStringExtra("money");
		productID = in.getStringExtra("productID");
		type = in.getStringExtra("type");
		title = in.getStringExtra("title");
		
		sp = getSharedPreferences("USERID", 0);
		loginname = sp.getString("loginname", "null");
		userID = sp.getString("userID", "0");
		tv_title = (TextView) findViewById(R.id.tv_title);
		String  signature = "apply.php:"+System.currentTimeMillis()/1000+":hxpay";
		try {
			requestUrl = SppaConstant.getIPmobilev11()+"apply.php?"
			+"signature="+RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes())+
									"&platformID="+SppaConstant.ANDROID+"&userID="+userID+
									"&loginname="+loginname+"&money="+money+"&borrowid="+productID;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i("requestUrl", requestUrl);
	}

	@Override
	public void initView() {
		wv_touzi = (WebView) findViewById(R.id.wv_touzi);
		
		wv_touzi.setOnTouchListener(new View.OnTouchListener() {  
		    @Override  
		    public boolean onTouch(View v, MotionEvent event) {  
		    	wv_touzi.requestFocus();  
		         return false;  
		    }
 
		});  
		
		
		wv_touzi.setWebViewClient(new WebViewClient()
	    {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url)
	      {

	        view.loadUrl(url); // 

	        return true;
	      }
	      
	     
	      
	      @Override
	    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
	    	  if (dialog == null) {
	    		  dialog=new LoadingDialog(ActTouziHuifu.this);
	    		  dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
	//    		  dialog.show();
	 //   		  wv_touzi.setEnabled(false);// 当加载网页的时候将网页进行隐藏
	    		  }
	    		super.onPageStarted(view, url, favicon);
	    	}
	      
	      @Override
	    	public void onPageFinished(WebView view, String url) {
	    	  if (dialog != null && dialog.isShowing()) {
	    		  dialog.dismiss();
	    		  dialog = null;
	    		  wv_touzi.setEnabled(true);
	    		  }
	    //		super.onPageFinished(view, url);
	    	}
	      
	    });
		
		
		
		WebSettings webSettings = wv_touzi.getSettings();
		webSettings.setBuiltInZoomControls(true);//����
		webSettings.setJavaScriptEnabled(true);
		
		wv_touzi.addJavascriptInterface(new WebTOANDROIDInterface(), "android");
//		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//		wv_touzi.clearCache(true);  
		wv_touzi.loadUrl(requestUrl);
		
		 wv_touzi.setWebChromeClient(new WebChromeClient(){
				@Override
				public void onProgressChanged(WebView view, int newProgress) 
				{
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
		public void share(String data) {  
//			ActTouziHuifu.this.startActivity(new Intent(ActTouziHuifu.this,ActInvestHistory.class));
//			//投资完成后将入口的两层activity关闭，防止在这里状态不能刷新造成误投
//			ActCebDetail.instance.finish();
//			ActTouzi.instance.finish();
//			ActTouziHuifu.this.finish();
//			overridePendingTransition(android.R.anim.fade_in,
//					android.R.anim.fade_out);
	//		Toast.makeText(ActTouziHuifu.this, "投资-"+data, 1000).show();
	//		Log.i("data", data.substring(13));
			
			try {
				String jsonString = data.substring(13);
				JSONObject json = new JSONObject(jsonString);
				
				String RespCode = json.getString("RespCode");
				Intent ii = new Intent(ActTouziHuifu.this,ActTouBiaoResult.class);
				ii.putExtra("RespCode", RespCode);
				ii.putExtra("type", type);
				ii.putExtra("title", title);
				ActTouziHuifu.this.startActivity(ii);
				ActTouziHuifu.this.finish();
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
				//投资完成后将入口的两层activity关闭，防止在这里状态不能刷新造成误投
				ActCebDetail.instance.finish();
				ActTouzi.instance.finish();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
	}

	@Override
	public boolean onKeyDown(int keyCoder,KeyEvent event){
        if(wv_touzi.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
        	wv_touzi.goBack();   //goBack()��ʾ����webView����һҳ��

                return true;
        }else if(keyCoder == KeyEvent.KEYCODE_BACK){
        	
        	ActTouziHuifu.this.finish();//按了返回键，但已经不能返回(mWebView.canGoBack()=false)，则执行退出确认
        	return true; 
        }  
        return false;
}
	
	@Override
	public void setListener() {
	}
	
	@Override
	public void onClick(View v) {
	}

	public void onback(View v){
		finish();
	}

}
