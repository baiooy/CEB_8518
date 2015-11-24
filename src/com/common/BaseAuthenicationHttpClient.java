package com.common;

import java.util.Map;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.spp.Global;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;



public class BaseAuthenicationHttpClient {
    public static String sessionId = "";
    private static int timeoutConnection = 20000;   
    private BaseAuthenicationHttpClient(){
    }
   
    public static String doRequest(Context context,String urlString,String name,String password, Map<String,String>params) throws IOException{
         URL url = null;
         HttpURLConnection uc = null; 
        try{ 
// 			看是否可以wap上网
//        	{
//               url = new URL(urlString);
//                Proxy proxy=new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("10.0.0.172",80));
//                uc =(HttpURLConnection)url.openConnection(proxy); 
//        	}  
        	NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();  
        	if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
        		String host = android.net.Proxy.getDefaultHost();  
        	       // 获取端口  
        	       int port = android.net.Proxy.getDefaultPort();  
        	       if (host != null && port != -1) {  
		        	String realLink = urlString.substring(7);
		            String realHost = realLink.substring(0, realLink.indexOf("/"));
		            String newUrl = "http://"+host+ realLink.substring(realLink.indexOf("/"));
		            url = new URL(newUrl);
		            uc = (HttpURLConnection) url.openConnection();
		            uc.setRequestProperty("X-Online-Host", realHost);
        	       }else{
        	    	   url = new URL(urlString);  
        	    	   uc = (HttpURLConnection) url.openConnection();
        	       }
        	}
        	else{
        		 url = new URL(urlString);  
        		 uc = (HttpURLConnection) url.openConnection();
        	}
        	
            url = new URL(urlString);  
            uc = (HttpURLConnection) url.openConnection();
            if(params!=null && !params.isEmpty()){
                uc.setRequestMethod("POST");
                uc.setDoOutput(true);
            }else{
            	uc.setRequestMethod("GET");
            }
            uc.setConnectTimeout(timeoutConnection);
            uc.setReadTimeout(timeoutConnection);
            uc.setDoInput(true);
            
            if(Global.cookies != null && Global.cookies.length() > 0) {
            	uc.addRequestProperty("Cookie", Global.cookies);
            }

//          
            uc.setDefaultUseCaches(false);
 //          uc.setRequestProperty("Content-Type",   "text/html");
 //           uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
            uc.setRequestProperty("Content-Type","application/json");
            uc.setRequestProperty("accept", "*/*");
            uc.setRequestProperty("connection", "Keep-Alive");
            if(name !=null && !"".equals(name) && password != null && !"".equals(password)){
               uc.setRequestProperty("Authorization",name+password); 
            }
            if(params!=null && !params.isEmpty()){
                StringBuffer sb = new StringBuffer();
                for(Map.Entry entry : params.entrySet()){
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
                sb.deleteCharAt(0);

                byte bytes[] = sb.toString().getBytes("UTF-8");
                uc.setRequestProperty("Content-Length",   String.valueOf(bytes.length));
                uc.getOutputStream().write(bytes);
                
            }
            
            String cookies = uc.getHeaderField("Set-Cookie");
            if(cookies != null && cookies.length() > 0) {
            	Global.cookies = cookies;
            }
            
            InputStream in = uc.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            String line = reader.readLine();
            
           
            
            
            StringBuffer buffer = new StringBuffer(); 
            while((line = reader.readLine()) != null){
            	Log.i("line", line);
            	buffer.append(line);
            }
            reader.close();
            return buffer.toString().trim(); 
        } catch (RuntimeException e) {
      
        }catch(SocketTimeoutException e){
        	
    	}finally {
           if(uc !=null){
        	   uc.disconnect();
           }
        }
    	return "";
    }
    
    
    public static String _doRequest(Context context,String urlString,String name,String password, Map<String,String>params) throws IOException{
        URL url = null;
        HttpURLConnection uc = null; 
       try{ 
//			看是否可以wap上网
//       	{
//              url = new URL(urlString);
//               Proxy proxy=new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("10.0.0.172",80));
//               uc =(HttpURLConnection)url.openConnection(proxy); 
//       	}  
       	NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();  
       	if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
       		String host = android.net.Proxy.getDefaultHost();  
       	       // 获取端口  
       	       int port = android.net.Proxy.getDefaultPort();  
       	       if (host != null && port != -1) {  
		        	String realLink = urlString.substring(7);
		            String realHost = realLink.substring(0, realLink.indexOf("/"));
		            String newUrl = "http://"+host+ realLink.substring(realLink.indexOf("/"));
		            url = new URL(newUrl);
		            uc = (HttpURLConnection) url.openConnection();
		            uc.setRequestProperty("X-Online-Host", realHost);
//		            uc.setRequestProperty("accept", "*/*");
//		            uc.setRequestProperty("connection", "Keep-Alive");
       	       }else{
       	    	   url = new URL(urlString);  
       	    	   uc = (HttpURLConnection) url.openConnection();
       	    	
       	       }
       	}
       	else{
       		 url = new URL(urlString);  
       		 uc = (HttpURLConnection) url.openConnection();
       	}
       	
           url = new URL(urlString);  
           uc = (HttpURLConnection) url.openConnection();
           if(params!=null && !params.isEmpty()){
               uc.setRequestMethod("POST");
               uc.setDoOutput(true);
           }else{
           	uc.setRequestMethod("GET");
           }
           uc.setConnectTimeout(timeoutConnection);
           uc.setReadTimeout(timeoutConnection);
           uc.setDoInput(true);
           
           if(Global.cookies != null && Global.cookies.length() > 0) {
           	uc.addRequestProperty("Cookie", Global.cookies);
           }

//         
           uc.setDefaultUseCaches(false);
//         uc.setRequestProperty("Content-Type",   "text/html");
         //设置post请求必要的请求头
           uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); // 请求头, 必须设置
//           uc.setRequestProperty("Content-Type","application/json");
           uc.setRequestProperty("Connection",   "Keep-Alive");
           if(name !=null && !"".equals(name) && password != null && !"".equals(password)){
              uc.setRequestProperty("Authorization",name+password); 
           }
           if(params!=null && !params.isEmpty()){
               StringBuffer sb = new StringBuffer();
               for(Map.Entry entry : params.entrySet()){
                   sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
               }
               sb.deleteCharAt(0);
               Log.i("login sb", sb.toString());
               byte bytes[] = sb.toString().getBytes("UTF-8");
               uc.setRequestProperty("Content-Length",   String.valueOf(bytes.length));
               uc.getOutputStream().write(bytes);
               
           }
           
           String cookies = uc.getHeaderField("Set-Cookie");
           if(cookies != null && cookies.length() > 0) {
           	Global.cookies = cookies;
           }
           
           InputStream in = uc.getInputStream();
           BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
  //         String line;
           String line = reader.readLine();
           
           byte bytes1[] = line.toString().getBytes("UTF-8");
           reader.close();
           return line.trim(); 
           
           
//           StringBuffer buffer = new StringBuffer(); 
//           while((line = reader.readLine()) != null){
//           	Log.i("line", line);
//           	buffer.append(line);
//           }
//           reader.close();
//           return buffer.toString().trim(); 
       } catch (RuntimeException e) {
     
       }catch(SocketTimeoutException e){
       	
   	}finally {
          if(uc !=null){
       	   uc.disconnect();
          }
       }
       
   	return "";
   }
    
    
    
    public static String __doRequest(Context context,String urlString,String name,String password, Map<String,String>params) throws IOException{
        URL url = null;
        HttpURLConnection uc = null; 
       try{ 
//			看是否可以wap上网
//       	{
//              url = new URL(urlString);
//               Proxy proxy=new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("10.0.0.172",80));
//               uc =(HttpURLConnection)url.openConnection(proxy); 
//       	}  
       	NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();  
       	if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
       		String host = android.net.Proxy.getDefaultHost();  
       	       // 获取端口  
       	       int port = android.net.Proxy.getDefaultPort();  
       	       if (host != null && port != -1) {  
		        	String realLink = urlString.substring(7);
		            String realHost = realLink.substring(0, realLink.indexOf("/"));
		            String newUrl = "http://"+host+ realLink.substring(realLink.indexOf("/"));
		            url = new URL(newUrl);
		            uc = (HttpURLConnection) url.openConnection();
		            uc.setRequestProperty("X-Online-Host", realHost);
       	       }else{
       	    	   url = new URL(urlString);  
       	    	   uc = (HttpURLConnection) url.openConnection();
       	       }
       	}
       	else{
       		 url = new URL(urlString);  
       		 uc = (HttpURLConnection) url.openConnection();
       	}
       	
           url = new URL(urlString);  
           uc = (HttpURLConnection) url.openConnection();
           if(params!=null && !params.isEmpty()){
               uc.setRequestMethod("POST");
               uc.setDoOutput(true);
           }else{
           	uc.setRequestMethod("GET");
           }
           uc.setConnectTimeout(timeoutConnection);
           uc.setReadTimeout(timeoutConnection);
           uc.setDoInput(true);
           
           if(Global.cookies != null && Global.cookies.length() > 0) {
           	uc.addRequestProperty("Cookie", Global.cookies);
           }

//         
           uc.setDefaultUseCaches(false);
//           uc.setRequestProperty("Accept",   "text/html");
           uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
//           uc.setRequestProperty("Content-Type","application/json");
           uc.setRequestProperty("Connection",   "close");
           if(name !=null && !"".equals(name) && password != null && !"".equals(password)){
              uc.setRequestProperty("Authorization",name+password); 
           }
           if(params!=null && !params.isEmpty()){
               StringBuffer sb = new StringBuffer();
               for(Map.Entry entry : params.entrySet()){
                   sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
               }
               sb.deleteCharAt(0);

               byte bytes[] = sb.toString().getBytes("UTF-8");
               uc.setRequestProperty("Content-Length",   String.valueOf(bytes.length));
               uc.getOutputStream().write(bytes);
               
           }
           
//           String cookies = uc.getHeaderField("Set-Cookie");
//           if(cookies != null && cookies.length() > 0) {
//           	Global.cookies = cookies;
//           }
           
           InputStream in = uc.getInputStream();
           BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
           String line = reader.readLine();
           byte bytes1[] = line.toString().getBytes("UTF-8");
           reader.close();
           return line.trim(); 
       } catch (RuntimeException e) {
     
       }catch(SocketTimeoutException e){
       	
   	}finally {
          if(uc !=null){
       	   uc.disconnect();
          }
       }
   	return "";
   }
    
    
    
    
    
    

    public static String doRequest(Context context,String urlString,String name, String password) throws IOException{
        return doRequest(context,urlString,name,password,null);
    }

    public static String doRequest(Context context,String urlString)throws IOException{
        return doRequest(context,urlString,null,null,null);
    }

    public static String doRequest(Context context,String urlString,Map<String,String>params)throws IOException{
        return doRequest(context,urlString,null,null,params);
    }
    
    public static String _doRequest(Context context,String urlString,Map<String,String>params)throws IOException{
        return _doRequest(context,urlString,null,null,params);
    }
    
    
//    public static boolean isNetworkAvailable(Context context) {   
//        ConnectivityManager cm = (ConnectivityManager) context   
//                .getSystemService(Context.CONNECTIVITY_SERVICE);   
//        if (cm == null)  
//        	return false;
//      
//        	
//        return cm.getActiveNetworkInfo().isAvailable();  
//        }    	
        
      
     
    
    
    
    
    
    
    
    
    
}
