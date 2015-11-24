package com.ceb.widge;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.push.example.MyPushMessageReceiver;
import com.caifuline.R;

public class SelectPicPopupWindow extends PopupWindow {


	private View mMenuView;
	LinearLayout linear_tongzhishezhi,linear_yijianfankui,linear_jianchagengxin,linear_dafen,
										linear_mianzeshengming;
	TextView tv_showmsg ;
	Button btn_cancel ;
	
	public SelectPicPopupWindow(final Activity context,String str) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.push_msg, null);
		tv_showmsg = (TextView) mMenuView.findViewById(R.id.tv_showmsg);
		tv_showmsg.setText(str);
		
		
		
		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		//个人资料
		linear_tongzhishezhi = (LinearLayout) mMenuView.findViewById(R.id.linear_tongzhishezhi);
		linear_tongzhishezhi.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(context,ActNotificationSetting.class);
//				context.startActivity(intent);
				dismiss();
//				Toast.makeText(context, "通知设置", Toast.LENGTH_LONG).show();
//				dismiss();
			}
		});
		
//		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
//		
//		//意见反馈
//		linear_yijianfankui = (LinearLayout) mMenuView.findViewById(R.id.linear_yijianfankui);
//		linear_yijianfankui.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context,ActFeedBack.class);
//				context.startActivity(intent);
//				dismiss();
////				Toast.makeText(context, "意见反馈", Toast.LENGTH_LONG).show();
////				dismiss();
//			}
//		});
//		
//		
//		//设置按钮
//		linear_jianchagengxin = (LinearLayout) mMenuView.findViewById(R.id.linear_jianchagengxin);
//		linear_jianchagengxin.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
////				System.out.println("-----------------"+MessagesActivity.userNameStr);
////				Intent intent = new Intent(context,SettingActivity.class);
////				context.startActivity(intent);
////				dismiss();
//				Toast.makeText(context, "检查更新", Toast.LENGTH_LONG).show();
//				dismiss();
//			}
//		});
//		
//		//意见反馈
//		linear_dafen = (LinearLayout) mMenuView.findViewById(R.id.linear_dafen);
//		linear_dafen.setOnClickListener(new View.OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
////						Intent intent = new Intent(context,FeekBackActivity.class);
////						context.startActivity(intent);
////						dismiss();
//						Toast.makeText(context, "APP打分", Toast.LENGTH_LONG).show();
//						dismiss();
//					}
//				});
//				//意见反馈
//		linear_mianzeshengming = (LinearLayout) mMenuView.findViewById(R.id.linear_mianzeshengming);
//		linear_mianzeshengming.setOnClickListener(new View.OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						Intent intent = new Intent(context,ActMianZeTiaoKuan.class);
//						context.startActivity(intent);
//						dismiss();
////						Toast.makeText(context, "免责声明", Toast.LENGTH_LONG).show();
////						dismiss();
//					}
//				});
//				
				//意见反馈
		/*linear_tongzhishezhi = (LinearLayout) mMenuView.findViewById(R.id.linear_tongzhishezhi);
		linear_tongzhishezhi.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
//						Intent intent = new Intent(context,FeekBackActivity.class);
//						context.startActivity(intent);
//						dismiss();
						Toast.makeText(context, "通知设置", Toast.LENGTH_LONG).show();
						dismiss();
					}
				});
		//取消按钮
		linear_tuichu = (LinearLayout) mMenuView.findViewById(R.id.linear_tuichu);
		linear_tuichu.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//销毁弹出框
//				SaveDate.saveDate(context, new OAuthV2());
//				new AlertDialog.Builder(context)
//				.setMessage("确认退出登录?")
//				.setPositiveButton("确定",
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog,int which) 
//							{			
//								//退出登录
//								KFIMInterfaces.Logout(context);
//								Intent intent = new Intent(context,SignInActivity.class);
//								context.startActivity(intent);
//								context.finish();
//								dismiss();
//								
//							}
//						}).setNegativeButton("取消", null).create()
//				.show();
//				
				Toast.makeText(context, "退出", Toast.LENGTH_LONG).show();
				dismiss();
			}
		});*/
		//设置按钮监听
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.mystyle);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				Log.i("y", y+"");
				Log.i("height", height+"");
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true; 
			}
		});

	}


}
