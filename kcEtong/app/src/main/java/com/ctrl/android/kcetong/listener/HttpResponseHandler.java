/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package com.ctrl.android.kcetong.listener;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class HttpResponseHandler extends TextHttpResponseHandler {
	private static final String LOG_TAG = "HttpResponseHandler";
	private static final String TAG = "HttpResponseHandler";
	private String action = null;
	public HttpResponse response = null;
	private Context context = null;

	public HttpResponseHandler(String action, HttpResponse response,
							   Activity context) {
		super(DEFAULT_CHARSET);
		this.response = response;
		this.action = action;
		this.context = context;
	}

	/**
	 * Returns when request succeeds
	 *
	 * @param statusCode
	 *            http response status line
	 * @param headers
	 *            response headers if any
	 * @param result
	 *            返回result值
	 */
	public void onSuccess(int statusCode, Header[] headers,
			JSONObject jresponse, int result, String action) {
		response.onSuccess(statusCode, headers, (JSONObject) jresponse, result,
				action);
	}

	/**
	 * Returns when request succeeds
	 *
	 * @param statusCode
	 *            http response status line
	 * @param headers
	 *            response headers if any
	 * @param response
	 *            parsed response if any
	 */
	public void onSuccess(int statusCode, Header[] headers, JSONArray response,
			String action) {

	}

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
			Throwable throwable, JSONObject errorResponse, String action) {
		Log.i(TAG, "onFailure==="+statusCode);
		response.onFailure(statusCode, headers, throwable, action);

	}

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
			Throwable throwable, JSONArray errorResponse, String action) {

	}

	@Override
	public final void onSuccess(final int statusCode, final Header[] headers,
			final byte[] responseBytes) {
		if (statusCode != HttpStatus.SC_NO_CONTENT) {
			Runnable parser = new Runnable() {
				@Override
				public void run() {
					try {
						final Object jsonResponse = parseResponse(responseBytes);

						postRunnable(new Runnable() {
							@Override
							public void run() {
								if (jsonResponse instanceof JSONObject) {
									Log.d("http---",
											((JSONObject) jsonResponse)
													.toString());
									int result = ((JSONObject) jsonResponse)
											.optInt("result");
									boolean isReturn = false;
									try {
										isReturn = onFileBreak(result,
												(JSONObject) jsonResponse);
									} catch (Exception e) {
										e.printStackTrace();
									}
									if (!isReturn) {
										onSuccess(statusCode, headers,
												(JSONObject) jsonResponse,
												result, action);
									}

								} else if (jsonResponse instanceof JSONArray) {
									onSuccess(statusCode, headers,
											(JSONArray) jsonResponse, action);
								} else if (jsonResponse instanceof String) {
									onFailure(
											statusCode,
											headers,
											(String) jsonResponse,
											new JSONException(
													"Response cannot be parsed as JSON data"));
								} else {
									onFailure(
											statusCode,
											headers,
											new JSONException(
													"Unexpected response type "
															+ jsonResponse
															.getClass()
															.getName()),
											(JSONObject) null, action);
								}

							}
						});
					} catch (final JSONException ex) {
						postRunnable(new Runnable() {
							@Override
							public void run() {
								onFailure(statusCode, headers, ex,
										(JSONObject) null, action);
							}
						});
					}
				}
			};
			if (!getUseSynchronousMode())
				// new Thread().start();
				ExecutorsManager.getInsence().execute(parser);
			else
				// In synchronous mode everything should be run on one thread
				parser.run();
		} else {
			onSuccess(statusCode, headers, new JSONObject(), 1, action);
		}
	}

	@Override
	public final void onFailure(final int statusCode, final Header[] headers,
			final byte[] responseBytes, final Throwable throwable) {
		// String srt2;
		// try {
		// srt2 = new String(responseBytes, "UTF-8");
		// Log.d("test1", "onFailure" + srt2);
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		if (responseBytes != null) {
			Runnable parser = new Runnable() {
				@Override
				public void run() {
					try {
						final Object jsonResponse = parseResponse(responseBytes);
						postRunnable(new Runnable() {
							@Override
							public void run() {
								if (jsonResponse instanceof JSONObject) {
									onFailure(statusCode, headers, throwable,
											(JSONObject) jsonResponse, action);
								} else if (jsonResponse instanceof JSONArray) {
									onFailure(statusCode, headers, throwable,
											(JSONArray) jsonResponse, action);
								} else if (jsonResponse instanceof String) {
									onFailure(statusCode, headers,
											(String) jsonResponse, throwable);
								} else {
									onFailure(
											statusCode,
											headers,
											new JSONException(
													"Unexpected response type "
															+ jsonResponse
																	.getClass()
																	.getName()),
											(JSONObject) null, action);
								}
							}
						});

					} catch (final JSONException ex) {
						postRunnable(new Runnable() {
							@Override
							public void run() {
								onFailure(statusCode, headers, ex,
										(JSONObject) null, action);
							}
						});

					}
				}
			};
			if (!getUseSynchronousMode())
				// new Thread(parser).start();
				ExecutorsManager.getInsence().execute(parser);
			else
				// In synchronous mode everything should be run on one thread
				parser.run();
		} else {
			Log.v(LOG_TAG,
					"response body is null, calling onFailure(Throwable, JSONObject)");
			onFailure(statusCode, headers, throwable, (JSONObject) null, action);
		}
	}

	/**
	 * Returns Object of type {@link JSONObject}, {@link JSONArray}, String,
	 * Boolean, Integer, Long, Double or {@link JSONObject#NULL}, see
	 * {@link JSONTokener#nextValue()}
	 *
	 * @param responseBody
	 *            response bytes to be assembled in String and parsed as JSON
	 * @return Object parsedResponse
	 * @throws JSONException
	 *             exception if thrown while parsing JSON
	 */
	protected Object parseResponse(byte[] responseBody) throws JSONException {
		if (null == responseBody)
			return null;
		Object result = null;
		// trim the string to prevent start with blank, and test if the string
		// is valid JSON, because the parser don't do this :(. If JSON is not
		// valid this will return null
		String jsonString = getResponseString(responseBody, getCharset());
		if (jsonString != null) {
			jsonString = jsonString.trim();
			if (jsonString.startsWith("{") || jsonString.startsWith("[")) {
				result = new JSONTokener(jsonString).nextValue();
			}
		}
		if (result == null) {
			result = jsonString;
		}
		return result;
	}

	public static enum HTTP_TYPE {
		RESGISTER
	}

	@Override
	public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, String arg2) {

	}

	/**
	 * 弹出错误停止运行
	 */
	private boolean onFileBreak(int code, JSONObject jsonResponse) throws JSONException {
	
		// if (code.equals("501")) {
		// // response.onSessionFailure();
		// // Intent intent = new Intent(mContext, GroupActivity.class);
		// // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// // intent.putExtra("conflict", true);
		// // mContext.startActivity(intent);
		// Utils.toastError(mContext, "登录超时,请重新登录");
		// ((LTApplication)mContext.getApplication()).logout(null);
		// mContext.finish();
		// mContext.startActivity(new Intent(mContext,
		// LoginActivity.class));
		// return true;
		// } else if (code.equals("701")) {
		// if (action.equals(ActionUrl.DETAIL_GOODS)) {
		// Utils.toastError(mContext, "宝贝已下架或删除");
		// return true;
		// }
		// Utils.toastError(mContext, JsonPraise.opt701(result));
		// response.dataError(code, jsonResponse);
		// return true;
		// } else if (code.equals("500")) {
		// Utils.toastError(mContext,
		// mContext.getResources().getString(R.string.server_error));
		// response.dataError(code, jsonResponse);
		// return true;
		// } else if (code.equals("503")) {
		// Utils.toastError(mContext,
		// mContext.getResources().getString(R.string.work_error));
		// response.dataError(code, jsonResponse);
		// return true;
		// } else if (code.equals("600")) {
		// response.moreLogin();
		// return true;
		// } else if (code.equals("404")) {
		// Utils.toastError(mContext,
		// mContext.getResources().getString(R.string.work_error));
		// response.dataError(code, jsonResponse);
		// return true;
		// }
		response.dataError(code, jsonResponse);
//		switch (code) {
//		case 1600:
//		
//			return true ;
//		case -1:
//			Utils.toastError(context, R.string.resonse_code01);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 2:
//			Utils.toastError(context, R.string.resonse_code2);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 1001:
//			Utils.toastError(context, R.string.resonse_code1001);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 1002:
//			Utils.toastError(context, R.string.resonse_code1002);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 1003:
//			Utils.toastError(context, R.string.resonse_code1003);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 1004:
//			Utils.toastError(context, R.string.resonse_code1004);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 1005:
//			Utils.toastError(context, R.string.resonse_code1005);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 1006:
//			Utils.toastError(context, R.string.resonse_code1006);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 1007:
//			Utils.toastError(context, R.string.resonse_code1007);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 1008:
//			Utils.toastError(context, R.string.resonse_code1008);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 1009:
//			Utils.toastError(context, R.string.resonse_code1009);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 1010:
//			Utils.toastError(context, R.string.resonse_code1010);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 1011:
//			response.dataError(code, jsonResponse);
//			Utils.toastError(context, R.string.resonse_code1011);//此处应跳转至补充用户资料界面
//			Intent intent = new Intent (context, SetDetailUserActivity.class);
//			context.startActivity(intent);
//			return true;
//		case 1012:
//			Utils.toastError(context, R.string.resonse_code1012);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 1013:
//			Utils.toastError(context, R.string.resonse_code1013);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 1014:
//			Utils.toastError(context, R.string.resonse_code1014);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1001:
//			Utils.toastError(context, R.string.resonse_code01001);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1002:
//			Utils.toastError(context, R.string.resonse_code01002);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1003:
//			Utils.toastError(context, R.string.resonse_code01003);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1004:
//			Utils.toastError(context, R.string.resonse_code01004);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1005:
//			Utils.toastError(context, R.string.resonse_code01005);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1006:
//			Utils.toastError(context, R.string.resonse_code01006);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1007:
//			Utils.toastError(context, R.string.resonse_code01007);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1008:
//			Utils.toastError(context, R.string.resonse_code01008);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1009:
//			Utils.toastError(context, R.string.resonse_code01009);
//			return true;
//		case -1010:
//			Utils.toastError(context, R.string.resonse_code01010);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1011:
//			Utils.toastError(context, R.string.resonse_code01011);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1012:
//			Utils.toastError(context, R.string.resonse_code01012);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1013:
//			Utils.toastError(context, R.string.resonse_code01013);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1014:
//			Utils.toastError(context, R.string.resonse_code01014);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1015:
//			Utils.toastError(context, R.string.resonse_code01015);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1016:
//			Utils.toastError(context, R.string.resonse_code01016);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1017:
//			Utils.toastError(context, R.string.resonse_code01017);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1018:
//			Utils.toastError(context, R.string.resonse_code01018);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1019:
//			Utils.toastError(context, R.string.resonse_code01019);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1020:
//			Utils.toastError(context, R.string.resonse_code01020);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -1021:
//			response.dataError(code, jsonResponse);
//			return true;
//		case 2001:
//			Utils.toastError(context, R.string.resonse_code2001);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 2002:
//			Utils.toastError(context, R.string.resonse_code2002);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 2003:
//			Utils.toastError(context, R.string.resonse_code2003);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 2004:
//			Utils.toastError(context, R.string.resonse_code2004);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 2005:
//			Utils.toastError(context, R.string.resonse_code2005);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 2006:
//			Utils.toastError(context, R.string.resonse_code2006);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 2007:
//			Utils.toastError(context, R.string.resonse_code2007);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -2001:
//			Utils.toastError(context, R.string.resonse_code02001);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -2002:
//			Utils.toastError(context, R.string.resonse_code02002);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -2003:
//			Utils.toastError(context, R.string.resonse_code02003);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -2004:
//			Utils.toastError(context, R.string.resonse_code02004);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -3001:
//			Utils.toastError(context, R.string.resonse_code03001);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -3002:
//			Utils.toastError(context, R.string.resonse_code03002);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -3003:
//			Utils.toastError(context, R.string.resonse_code03003);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -3004:
//			Utils.toastError(context, R.string.resonse_code03004);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -3005:
//			Utils.toastError(context, R.string.resonse_code03005);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -3006:
//			Utils.toastError(context, R.string.resonse_code03006);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -3007:
//			Utils.toastError(context, R.string.resonse_code03007);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -3008:
//			Utils.toastError(context, R.string.resonse_code03008);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -4001:
//			Utils.toastError(context, R.string.resonse_code04001);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -4002:
//			Utils.toastError(context, R.string.resonse_code04002);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -4003:
//			Utils.toastError(context, R.string.resonse_code04003);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -4004:
//			Utils.toastError(context, R.string.resonse_code04004);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -4005:
//			Utils.toastError(context, R.string.resonse_code04005);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -4006:
//			Utils.toastError(context, R.string.resonse_code04006);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -4007:
//			Utils.toastError(context, R.string.resonse_code04007);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -4008:
//			Utils.toastError(context, R.string.resonse_code04008);
//			response.dataError(code, jsonResponse);
//			return true;
//		case 5001:
//			Utils.toastError(context, R.string.resonse_code5001);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -5001:
//			Utils.toastError(context, R.string.resonse_code05001);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -5002:
//			Utils.toastError(context, R.string.resonse_code05002);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -6001:
//			Utils.toastError(context, R.string.resonse_code06001);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -6002:
//			Utils.toastError(context, R.string.resonse_code06002);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -6003:
//			Utils.toastError(context, R.string.resonse_code06003);
//			response.dataError(code, jsonResponse);
//			return true;
//		case -8888:
//			Utils.toastError(context, R.string.resonse_code08888);
//			response.dataError(code, jsonResponse);
//			return true;
//		}
		return false;
	}
}
