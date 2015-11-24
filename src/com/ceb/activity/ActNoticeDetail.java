package com.ceb.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.caifuline.R;
import com.ceb.base.BaseActivity;
import com.loadingdialog.LoadingDialog;

public class ActNoticeDetail extends BaseActivity {
	WebView wv_notice;
	String htmlurl;
	LoadingDialog dialog;
	@Override
	public void setView() {
		setContentView(R.layout.act_noticedetail);

		htmlurl = getIntent().getStringExtra("htmlurl");
		Log.d("htmlurl", htmlurl);
	}

	@Override
	public void initView() {
		wv_notice = (WebView) findViewById(R.id.wv_notice);

		wv_notice.setOnTouchListener(new View.OnTouchListener() {  
		    @Override  
		    public boolean onTouch(View v, MotionEvent event) {  
		    	wv_notice.requestFocus();  
		         return false;  
		    }
 
		});  
		
		
		wv_notice.setWebViewClient(new WebViewClient()
	    {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, final String url)
	      {

	    //	调用拨号程序  
	            if (url.startsWith("mailto:") || url.startsWith("geo:") ||url.startsWith("tel:")) {  
//	              Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));  
//	               startActivity(intent);  
	            	 new SweetAlertDialog(ActNoticeDetail.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
	    			 .setTitleText(url.split("\\:")[1])//("400-860-8518")
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
	                    	 String urlto = url;//"4008608518";
	    						Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(urlto));
	    						startActivity(in);
	    						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	                     	sDialog.dismiss();
	                     }
	                 })
	                 .show();
	              } else{
	            	  view.loadUrl(url); 
	              }
		        return true;
	      }
	      
	      @Override
	    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
	    	  if (dialog == null) {
	    		  dialog=new LoadingDialog(ActNoticeDetail.this);
	    		  dialog.getWindow().setWindowAnimations(R.style.dialog_anim);
	    		  dialog.show();
	    		  wv_notice.setEnabled(false);// 当加载网页的时候将网页进行隐藏
	    		  }
	    		super.onPageStarted(view, url, favicon);
	    	}
	      
	      @Override
	    	public void onPageFinished(WebView view, String url) {
	    	  if (dialog != null && dialog.isShowing()) {
	    		  dialog.dismiss();
	    		  dialog = null;
	    		  wv_notice.setEnabled(true);
	    		  }
	    //		super.onPageFinished(view, url);
	    	}
	    });
		
		WebSettings webSettings = wv_notice.getSettings();
		webSettings.setBuiltInZoomControls(true);//����
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		wv_notice.clearCache(true);  
		
		wv_notice.loadUrl(htmlurl);
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
