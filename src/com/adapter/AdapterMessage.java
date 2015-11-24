package com.adapter;



import java.net.URLEncoder;
import java.util.List;
import java.util.TreeMap;

import com.caifuline.R;
import com.ceb.activity.ActMyMessage;
import com.ceb.widge.ParamSign;
import com.ceb.widge.RegExpValidator;
import com.ceb.widge.UtiEncrypt;
import com.common.Constants;
import com.loadingdialog.LoadingDialog;
import com.model.ActionResultInfo;
import com.model.MessageArray;
import com.model.Message_item;
import com.model.ProductList_item;
import com.model.UserInfo;
import com.service.UserInfoService;
import com.spp.SppaConstant;
import com.view.EmptyViewHelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterMessage extends BaseAdapter {
	private Context mContext;
	List<Message_item> mData;
	private SharedPreferences sp;
	private String userID;
	UserInfoService _userInfoService;
	public AdapterMessage(Context c,List<Message_item> data){
		mContext = c;
		mData = data;
		sp = mContext.getSharedPreferences("USERID", 0);
		userID = sp.getString("userID", "0");
		_userInfoService = new UserInfoService(mContext);
	}
	@Override
	public int getCount() {
		if (mData != null) {
			return mData.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final String title = mData.get(position).title;
		final String date = mData.get(position).date;
		final String type = mData.get(position).type;
		final String content = mData.get(position).content;
		final String msgID = mData.get(position).msgID;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext.getApplicationContext())
					.inflate(R.layout.message_item, null);
		}
		
		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
		TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
		TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
		final TextView tv_new = (TextView) convertView.findViewById(R.id.tv_new);
//		
		if(type.equals("0")){
			tv_new.setVisibility(View.VISIBLE);
		}else{
			tv_new.setVisibility(View.INVISIBLE);
		}
		
		tv_title.setText(title);
		tv_date.setText(SppaConstant.getStrTime(date));
		tv_content.setText(content);
		
		LinearLayout ll_msg = (LinearLayout) convertView.findViewById(R.id.ll_msg);
		ll_msg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv_new.setVisibility(View.INVISIBLE);
				getMessage(msgID);
			}
		});
		
		
		return convertView;
	}
	void getMessage(String msgId){
		String  signature = "messageupdate.php:"+System.currentTimeMillis()/1000+":hxpay";
		UserInfo userInfo = new UserInfo();
		try {
			userInfo.signature = RegExpValidator.encodeBase64(UtiEncrypt.encryptAES(RegExpValidator.encodeBase64(signature.getBytes())).getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfo.version = SppaConstant.APP_VERSION;
		userInfo.userID = userID;
		userInfo.msgID  = msgId ;
		userInfo.readStatus  = "1" ;
		userInfo.platformID = SppaConstant.ANDROID;
		userInfo.udid = SppaConstant.getDeviceInfo(mContext);
		
		
		new MessageTask().execute(userInfo);
	}
	
	class MessageTask extends AsyncTask<UserInfo, Void, ActionResultInfo>{

		
		@Override
		protected ActionResultInfo doInBackground(UserInfo... params) {
			ActionResultInfo info = null;
			
			info = _userInfoService.getUpdate(params[0]);
			
			Log.i("info", "info =="+info);
			return info;
		}
		
		@Override
		protected void onPostExecute(ActionResultInfo result) {
			if(result == null || result.toString().equals("null")){
			}else if(result.ret.equals("0")){
			
			}
		}
		
	}
}
