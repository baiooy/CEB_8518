package com.ceb.activity;

import android.annotation.SuppressLint;
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

import com.caifuline.R;
import com.ceb.activity.ActChongzhiHuifu.WebTOANDROIDInterface;
import com.ceb.base.BaseActivity;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.loadingdialog.LoadingDialog;
import com.spp.SppaConstant;

@SuppressLint("SetJavaScriptEnabled")
public class ActHuifu extends BaseActivity {
	private WebView mWebView;
	private String requestUrl;
	private SharedPreferences sp;
	private String loginname,userID;
	LoadingDialog dialog;
	private TextView tv_title;
	@Override
	public void setView() {
		setContentView(R.layout.act_huifu);

		sp = getSharedPreferences("USERID", 0);
		loginname = sp.getString("loginname", "null");
		userID = sp.getString("userID", "0");
		tv_title = (TextView) findViewById(R.id.tv_title);
//		Intent in = getIntent();
//		requestUrl = in.getStringExtra("url");
//		Log.i("requestUrl", "requestUrl="+requestUrl);
//		if (requestUrl.equals("null")) {
		String  signature = "payaccounts.php:"+System.currentTimeMillis()/1000+":hxpay";
			try {
				requestUrl = SppaConstant.getIPmobilev11()+"payaccounts.php?"
						+ "userID="
						+ userID
						+"&signature="
						+RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes())
						+ "&platformID="
						+ SppaConstant.ANDROID
						+ "&version="
						+ SppaConstant.APP_VERSION
						+ "&loginname="
						+ loginname
						+ "&udid=" + SppaConstant.getDeviceInfo(ActHuifu.this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("requestUrl", "requestUrl="+requestUrl);
	//	}
	}

	@Override
	public void initView() {
		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.setOnTouchListener(new View.OnTouchListener() {  
		    @Override  
		    public boolean onTouch(View v, MotionEvent event) {  
		    	mWebView.requestFocus();  
		         return false;  
		    }
 
		});  
		
		
		mWebView.setWebViewClient(new WebViewClient()
	    {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url)
	      {

	        view.loadUrl(url); 

	        return true;
	      }
	      @Override
	    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
	    	  if (dialog == null) {
	    		  dialog=new LoadingDialog(ActHuifu.this);
	    		  dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
	    		  dialog.show();
	    //		  mWebView.setEnabled(false);// 当加载网页的时候将网页进行隐藏
	    		  }
	    		super.onPageStarted(view, url, favicon);
	    	}
	      
	      @Override
	    	public void onPageFinished(WebView view, String url) {
	    	  if (dialog != null && dialog.isShowing()) {
	    		  dialog.dismiss();
	    		  dialog = null;
	    		  mWebView.setEnabled(true);
	    		  }
	    //		super.onPageFinished(view, url);
	    	}
	      
	    });
		
		
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setBuiltInZoomControls(true);
		//允许执行javascript语句
		webSettings.setJavaScriptEnabled(true);
		
		mWebView.addJavascriptInterface(new WebTOANDROIDInterface(), "android");
//		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//		mWebView.clearCache(true);  
		mWebView.loadUrl(requestUrl);
		
		mWebView.setWebChromeClient(new WebChromeClient(){
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
	
	private class WebTOANDROIDInterface{
		@JavascriptInterface
		public void finish() {  
//			Intent in = new Intent(ActHuifu.this,GestureEditActivity.class);
//			in.putExtra("flag", "open");
//			startActivity(in);
	//		finish();
	//		overridePendingTransition (android.R.anim.fade_in, android.R.anim.fade_out);
			
			Intent ii = new Intent(ActHuifu.this,MainActivity.class);
			MainActivity.instance.finish();
			ii.putExtra("hot", "hot");
			startActivity(ii);
			ActHuifu.this.finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			
	//		startActivity(new Intent(ActHuifu.this,ActHuiFuSucceed.class));
			SharedPreferences sp = getSharedPreferences("huifukaihu", 0);
			SharedPreferences.Editor editor = sp.edit();
			editor.clear();
			editor.commit();
			editor.putBoolean("kaihu", true);
			editor.commit();
			
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCoder,KeyEvent event){
        if(mWebView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
        	mWebView.goBack();  

                return true;
        }else if(keyCoder == KeyEvent.KEYCODE_BACK){
        	ActHuifu.this.finish();//按了返回键，但已经不能返回(mWebView.canGoBack()=false)，则执行退出确认
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
	
//	@Override
//    public boolean shouldOverrideUrlLoading(WebView view, String url) { 
// 	    if(url!=null){
// 	   if(url.contains("geke")){
//             String newUrl = url.replace("geke","http");
//             if(MainActivity.autoScan){
//             	Intent intent =  new Intent(con, BarcodeScanActivity.class);
//             	intent.putExtra("urlstr",newUrl);
//             	con.startActivity(intent);
//             }else{
//             	Intent intent =  new Intent(con, BarcodeInputActivity.class);
//             	intent.putExtra("urlstr",newUrl);
//             	con.startActivity(intent);
//             }
//             return true;
//         }else if(url.contains("tel")){
//             Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
//             con.startActivity(i);
//             return true;
//         
//         }else if(url.contains("mailto")){
//            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
//            con.startActivity(intent);
//            return true;
//         }else if(url.contains("sms:")){
//         	strSMS = url.split("=");
//         	Intent intent;
//             if(url.contains("body"))//添加好友sms
//             {
//             	/*Log.d("Rock", RegExpValidator.getLocalIpAddress());
//             	try{
//                 	Message message = Message.obtain(MainActivity.getGPSProgressHandler(), 8);//wcc
//                     message.sendToTarget();
//                	}catch (Exception e){
//                	 Log.d("Rock","thread error");
//                    }*/
//             	//朋友推荐点击 
//             	 String urlcons = WccConstant.WCC_RGF+"?act=click&mod=recommend&udid="+urlEncode(wccactivity.uuid);
//                 	 Log.d("Rock", urlcons);
//                 	 mservice.setRetrieveUrl(urlcons);
//                 	 new Thread(LoginThread).start();//发送手机参数给服务器
//                	intent = new Intent(con, MeunDialogActivity.class);
//               //---end
//             }
//             else   
//             {
//             	intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
//             	intent.putExtra("sms_body","");
//             }
//             con.startActivity(intent);
//             return true;
//         }else if(url.contains("wccb")){
//         	progressDialog.setMessage("加载中，请稍等");
//      	    progressDialog.setCancelable(true);
//             progressDialog.show();
//         	 
//             String strUrl = url.replace("wccb","http");
//   //          strSID = strUrl.split("=",3);
//             strUrl =strUrl+"&udid="+mainactivity.urlEncode(mainactivity.uuid);
//             mservice.setRetrieveUrl(strUrl);//Send message to server
//             new Thread(mthread).start();
//         	
//             return true;
//             
//         }else{
//         	
//         	return super.shouldOverrideUrlLoading(view, url);
//         }
// 	   } 
// 	   return super.shouldOverrideUrlLoading(view, url);
//    }

}
