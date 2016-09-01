package com.up.testjavasdkdemo.ssltest;

import com.up.testjavasdkdemo.util.LogCat;

import android.content.Context;

/**
 * HTTP接口
 * 
 */
public class HttpManager {
	
	private Http http;
	private static Context mContext;
	
	private static class HttpManagerHolder {
		public static HttpManager instance = new HttpManager();
	}

	public static HttpManager getInstance(Context context) {
		mContext = context;
		return HttpManagerHolder.instance;
	}

	private HttpManager() {
		super();
	}
	
	/**
	 * 请求后台接口
	 * @param params
	 * @param urlPath
	 * @param callBack
	 */
	public void doPost(AjaxParams params, String urlPath, AjaxCallBack<String> callBack) {
		http = new Http(mContext);
		LogCat.v("params:" + params);
		LogCat.v("urlPath:" + urlPath);
		http.post(urlPath, params, callBack);
	}
	/**
	 * 请求后台接口
	 * @param params
	 * @param urlPath
	 * @param callBack
	 */
	public void doGet(AjaxParams params, String urlPath, AjaxCallBack<String> callBack) {
		http = new Http(mContext);
		LogCat.v("params:" + params);
		LogCat.v("urlPath:" + urlPath);
		http.get(urlPath, params, callBack);
	}
	
	/**
	 * 请求后台接口
	 * @param urlPath
	 * @param callBack
	 */
	public void doPost(String urlPath, AjaxCallBack<String> callBack) {
		http = new Http(mContext);
		LogCat.v("urlPath:" + urlPath);
		http.post(urlPath, callBack);
	}
	
}
