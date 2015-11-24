package com.ceb.activity;


import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;


@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak" })
public class ActAboutUs extends BaseActivity {
	private WebView wv_aboutus;
	private String requestUrl = null;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	private Handler _handler;
	private ProgressDialog progressBar;   
	LoadingDialog dialog;
	private RelativeLayout rl_contactus,rl_weixin,rl_mail,rl_guanwang,rl_shangwu;
	@Override
	public void setView() {
		setContentView(R.layout.act_aboutus);
		
		sp = getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		_userInfoService = new UserInfoService(this);
		
		_handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case -1:
					Toast.makeText(ActAboutUs.this,
							"网络不给力，麻烦重试~o(>_<)o",
							Toast.LENGTH_SHORT).show();
					break;
				case 0:
					wv_aboutus.loadUrl(requestUrl);
					
					if(dialog != null & dialog.isShowing())
						dialog.dismiss();
					wv_aboutus.setWebChromeClient(new WebChromeClient() {  
					        public void onProgressChanged(WebView view, int progress)     
					        {  
					         //Make the bar disappear after URL is loaded, and changes string to Loading...  
					        	ActAboutUs.this.setTitle("Loading...");  
					        	ActAboutUs.this.setProgress(progress * 100); //Make the bar disappear after URL is loaded  
					   
					         // Return the app name after finish loading  
					            if(progress == 100)  
					            	ActAboutUs.this.setTitle(R.string.app_name);  
					          }  
					        });  
					
					break;
				default:
					break;
				}
			};
		};
		
//		try {
//			about();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}

	@Override
	public void initView() {
		rl_weixin = (RelativeLayout) findViewById(R.id.rl_weixin);
		rl_contactus = (RelativeLayout) findViewById(R.id.rl_contactus);
		rl_mail = (RelativeLayout) findViewById(R.id.rl_mail);
		rl_guanwang = (RelativeLayout) findViewById(R.id.rl_guanwang);
		rl_shangwu = (RelativeLayout) findViewById(R.id.rl_shangwu);
//		wv_aboutus = (WebView) findViewById(R.id.wv_aboutus);
//
//		WebSettings webSettings = wv_aboutus.getSettings();
////		webSettings.setBuiltInZoomControls(true);//����
//		webSettings.setJavaScriptEnabled(true);
//		wv_aboutus.getSettings().setJavaScriptEnabled(true);
////		wv_aboutus.getSettings().setSupportZoom(true);
//		wv_aboutus.getSettings().setDomStorageEnabled(true);
//		wv_aboutus.getSettings().setAllowFileAccess(true);
//		wv_aboutus.getSettings().setUseWideViewPort(true);
//		wv_aboutus.requestFocus();
//		wv_aboutus.getSettings().setLoadWithOverviewMode(true);
//		
//		wv_aboutus.setOnTouchListener(new View.OnTouchListener() {  
//		    @Override  
//		    public boolean onTouch(View v, MotionEvent event) {  
//		    	wv_aboutus.requestFocus();  
//		         return false;  
//		    }
// 
//		});  
//		
//		
//		wv_aboutus.setWebViewClient(new WebViewClient()
//	    {
//	      @Override
//	      public boolean shouldOverrideUrlLoading(WebView view, String url)
//	      {
//
//	    	//调用拨号程序  
//	            if (url.startsWith("mailto:") || url.startsWith("geo:") ||url.startsWith("tel:")) {  
////	              Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));  
////	               startActivity(intent);  
//	            	 new SweetAlertDialog(ActAboutUs.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
//	    			 .setTitleText("400-860-8518")
//	                 .setContentText("服务时间：工作日9:00-21:00")
//	                 .setCustomImage(R.drawable.ceb_icon)
//	                 .setCancelText("取消")
//	                 .setConfirmText("呼叫")
//	                 .showCancelButton(true)
//	                 .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//	                     @Override
//	                     public void onClick(SweetAlertDialog sDialog) {
//	                        sDialog.dismiss();
//	                     }
//	                 })
//	                 .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//	                     @Override
//	                     public void onClick(SweetAlertDialog sDialog) {
//	                    	 String url = "tel:" + "4008608518";
//	    						Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(url));
//	    						startActivity(in);
//	    						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//	                     	sDialog.dismiss();
//	                     }
//	                 })
//	                 .show();
//	              } else{
//	            	  view.loadUrl(url); 
//	              }
//		        return true;
//	      }
//	    });
		
		
	}

//	@Override
//	public boolean onKeyDown(int keyCoder,KeyEvent event){
//        if(wv_aboutus.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK){
//        	wv_aboutus.goBack();   
//
//                return true;
//        }else if(keyCoder == KeyEvent.KEYCODE_BACK){
//        	ActAboutUs.this.finish();//按了返回键，但已经不能返回(mWebView.canGoBack()=false)，则执行退出确认
//        	return true; 
//        }  
//        return false;
//}
	
	@Override
	public void setListener() {
		rl_weixin.setOnClickListener(this);
		rl_contactus.setOnClickListener(this);
		rl_mail.setOnClickListener(this);
		rl_guanwang.setOnClickListener(this);
		rl_shangwu.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_weixin:
			 new SweetAlertDialog(ActAboutUs.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
             .setContentText("公众号已复制到剪切板，您可以在微信-通讯录搜索框粘贴'welove8518'公众号,点击关注即可。")
             .setCancelText("取消")
             .setConfirmText("去关注")
             .showCancelButton(true)
             .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                 @Override
                 public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismiss();
                 }
             })
             .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                 @Override
                 public void onClick(SweetAlertDialog sDialog) {
                	 Log.d("weiixn", "weixin");
                	 /**
                	  * 复制内容到剪切板
                	  */
                	 ClipboardManager clipboard =  
                		      (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);  
                		 
                	 clipboard.setText("welove8518"); 


         			Intent intent = new Intent();
         			intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
         			startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                 	sDialog.dismiss();
                 }
             })
             .show();
			
			
			
			
			
			break;
		case R.id.rl_contactus:
			 new SweetAlertDialog(ActAboutUs.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
			 .setTitleText("400-860-8518")
             .setContentText("服务时间：工作日9:00-21:00")
             .setCustomImage(R.drawable.ceb_icon)
             .setCancelText("取消")
             .setConfirmText("呼叫")
             .showCancelButton(true)
             .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                 @Override
                 public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismiss();
                 }
             })
             .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                 @Override
                 public void onClick(SweetAlertDialog sDialog) {
                	 String url = "tel:" + "4008608518";
						Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(url));
						startActivity(in);
						ActAboutUs.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                 	sDialog.dismiss();
                 }
             })
             .show();
			break;
		case R.id.rl_mail://rl_guanwang,rl_shangwu
			 /**
       	  * 复制内容到剪切板
       	  */
       	 ClipboardManager clipboard1 =  
       		      (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);  
       		 
       	 clipboard1.setText("bao@8518.com"); 
       	 Toast.makeText(ActAboutUs.this, "已复制'bao@8518.com'到剪切板！", 1000).show();
			break;
		case R.id.rl_guanwang://rl_guanwang,rl_shangwu
			 /**
       	  * 复制内容到剪切板
       	  */
       	 ClipboardManager clipboard2 =  
       		      (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);  
       		 
       	 clipboard2.setText("bao.8518.com"); 
       	Toast.makeText(ActAboutUs.this, "已复制'bao.8518.com'到剪切板！", 1000).show();
			break;
		case R.id.rl_shangwu://rl_guanwang,rl_shangwu
			 /**
       	  * 复制内容到剪切板
       	  */
       	 ClipboardManager clipboard3 =  
       		      (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);  
       		 
       	 clipboard3.setText("BD@8518.com"); 
       	Toast.makeText(ActAboutUs.this, "已复制'BD@8518.com'到剪切板！", 1000).show();
			break;
		default:
			break;
		}

	}
	
	public void onback(View v){
		finish();
	}
	
	void about(){
		String  signature = "settingour.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(this);
		
		Log.i("about_signature", userInfo.signature);
		new AboutTask().execute(userInfo);
	}
	
	class AboutTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		@Override
		protected void onPreExecute() {
			dialog = new LoadingDialog(ActAboutUs.this);
			dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
			dialog.show();
		}
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.about(params[0]);
			
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null){
				_handler.sendEmptyMessage(-1);
			}else if(result.ret.equals("0")){
				
				requestUrl = result.url;
				
				_handler.sendEmptyMessage(0);
			}else{
				Toast.makeText(ActAboutUs.this,result.msg , Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	

}
