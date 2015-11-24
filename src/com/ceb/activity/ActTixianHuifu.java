package com.ceb.activity;

import java.net.URLEncoder;
import java.util.TreeMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
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

import com.adapter.AdapterBank;
import com.caifuline.R;
import com.ceb.activity.ActChongzhiHuifu.WebTOANDROIDInterface;
import com.ceb.activity.ActTixian.BankCardTask;
import com.ceb.base.BaseActivity;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.BankCardList;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

public class ActTixianHuifu extends BaseActivity {
	private WebView wv_tixianhuifu;
	private String money,bankID,mentionType,withdrawbank;
	private SharedPreferences sp;
	private String loginname,userID;
	private String requestUrl;
	private LoadingDialog dialog;
	UserInfoService _userInfoService;
	private Handler mHandler;
	private TextView tv_title;
	@Override
	public void setView() {
		setContentView(R.layout.act_tixianhuifu);

		_userInfoService = new UserInfoService(ActTixianHuifu.this);
		Intent in = getIntent();
		money = in.getStringExtra("money");
		bankID = in.getStringExtra("bankID");
		mentionType = in.getStringExtra("mentionType");
		withdrawbank = in.getStringExtra("withdrawbank"); 
		tv_title = (TextView) findViewById(R.id.tv_title);
		sp = getSharedPreferences("USERID", 0);
		loginname = sp.getString("loginname", "null");
		userID = sp.getString("userID", "0");
		String  signature = "withdraw.php:"+System.currentTimeMillis()/1000+":hxpay";
		try {
			requestUrl = SppaConstant.getIPmobilev11()+"withdraw.php?" +
									"platformID="+SppaConstant.ANDROID
									+"&signature="+RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes())
									+"&userID="+userID+
									"&loginname="+loginname+"&money="+money
									+"&mentionType="+mentionType+"&withdrawbank="+withdrawbank;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("requestUrl", requestUrl);
		
//		try {
//			gettixian();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void initView() {
		wv_tixianhuifu = (WebView) findViewById(R.id.wv_tixianhuifu);
		
		wv_tixianhuifu.setOnTouchListener(new View.OnTouchListener() {  
		    @Override  
		    public boolean onTouch(View v, MotionEvent event) {  
		    	wv_tixianhuifu.requestFocus();  
		         return false;  
		    }
 
		});  
		
		
		wv_tixianhuifu.setWebViewClient(new WebViewClient()
	    {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url)
	      {

	        view.loadUrl(url); // 

	        return true;
	      }
	      
	      @Override
	    	public void onPageFinished(WebView view, String url) {
	    	  dialog.dismiss();  
	    	}
	    });
		
//		wv_tixianhuifu.loadUrl(requestUrl);
		
		WebSettings webSettings = wv_tixianhuifu.getSettings();
		webSettings.setBuiltInZoomControls(true);//����
		webSettings.setJavaScriptEnabled(true);
//		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//		wv_tixianhuifu.clearCache(true);  
		
		wv_tixianhuifu.addJavascriptInterface(new WebTOANDROIDInterface(), "android");
		
		loadUrl(requestUrl);
		wv_tixianhuifu.setWebChromeClient(new WebChromeClient(){
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
		
//		mHandler = new Handler(){
//			@SuppressWarnings("unchecked")
//			@Override
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case 0:
//					loadUrl(requestUrl);
//					break;
//				case -2:
//					
//					break;
//				default:
//					break;
//				}
//			}
//		};
		
	}
	
	 public void loadUrl(String url)  
	    {  
	        if(wv_tixianhuifu != null)  
	        {  
	        	wv_tixianhuifu.loadUrl(url);  
	        	dialog = new LoadingDialog(ActTixianHuifu.this);
	        	dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
	 //           dialog.show();  
	            wv_tixianhuifu.reload();  
	        }  
	    }  
	
	public class WebTOANDROIDInterface{
		@JavascriptInterface
		public void finish() { 
			Intent ii = new Intent(ActTixianHuifu.this,MainActivity.class);
			ii.putExtra("fragid", "hot");
			ActTixianHuifu.this.startActivity(ii);
			ActTixianHuifu.this.finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		}
	}

	@Override
	public boolean onKeyDown(int keyCoder,KeyEvent event){
        if(wv_tixianhuifu.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
        	wv_tixianhuifu.goBack();   //goBack()��ʾ����webView����һҳ��

                return true;
        }else if(keyCoder == KeyEvent.KEYCODE_BACK){
        	ActTixianHuifu.this.finish();//按了返回键，但已经不能返回(mWebView.canGoBack()=false)，则执行退出确认
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

	
	void gettixian(){

		new BankCardTask().execute();
	}
    
	class BankCardTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{
		@Override
		protected void onPreExecute() {
		}
		
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.gettixianhuifu(requestUrl);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			Log.i("result.ret", result.ret+"");
			if(!result.ret.equals("")){
				Toast.makeText(ActTixianHuifu.this,result.msg , Toast.LENGTH_SHORT).show();
			}else{
		//		Toast.makeText(ActTixianHuifu.this,result.msg , Toast.LENGTH_SHORT).show();
				mHandler.sendEmptyMessage(0);
			}
		}
		
	}
	
	
	
	
}
