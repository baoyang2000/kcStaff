package com.ctrl.android.kcetong.listener;

import org.apache.http.Header;
import org.json.JSONObject;

public interface HttpResponse {
	/**
	 * Returns when request failed
	 * 
	 * @param statusCode
	 *            http response status line
	 * @param headers
	 *            response headers if any
	 * @param throwable
	 *            throwable describing the way request failed
	 * @param errorResponse
	 *            parsed response if any
	 */
	public void onFailure(int statusCode, Header[] headers,
						  Throwable throwable, String action);

	/**
	 * Returns when request succeeds
	 * 
	 * @param statusCode
	 *            http response status line
	 * @param headers
	 *            response headers if any
	 * @param response
	 *            parsed response if any
	 * @param result
	 *            返回result值
	 */
	public void onSuccess(int statusCode, Header[] headers,
						  JSONObject response, int result, String action);

	/**
	 * 数据错误,非致命错误
	 * @param response 
	 */
	public void dataError(int result, JSONObject response);
}
