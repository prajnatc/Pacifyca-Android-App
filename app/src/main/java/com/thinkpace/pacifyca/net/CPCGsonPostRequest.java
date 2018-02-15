
package com.thinkpace.pacifyca.net;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.thinkpace.pacifyca.entity.CPCIllegalCodeError;
import com.thinkpace.pacifyca.entity.IPCDataModel;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class CPCGsonPostRequest extends Request<IPCDataModel> {

    public static final int ERROR_CODE_499 = 499;
    public static final int ERROR_CODE_449 = 449;
    public static final int ERROR_CODE_502 = 502;
    public static final int ERROR_CODE_503 = 503;
    public static final int ERROR_CODE_504 = 504;
    public static final int ERROR_CODE_401 = 401;
    public static final int ERROR_CODE_410 = 410;
    public static final int ERROR_CODE_400 = 400;
    private final Listener<IPCDataModel> mListener;
    private IPCDataModel mDataModel;
    private final Gson mGson;
    private Map<String, String> mHeaders;
    private Map<String, String> mParams;
    private final String mRequestBody;
    private String TAG = CPCGsonPostRequest.class.getName();
    private String mUrl;

    /**
     * Charset for request.
     */
    private static final String PROTOCOL_CHARSET = "utf-8";

    public static final int MY_SOCKET_TIMEOUT_MS = 30000;


    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    public CPCGsonPostRequest(String url, Listener<IPCDataModel> listener, ErrorListener errorListener, IPCDataModel model, Map<String, String> params,
                              Map<String, String> headers, String requestBody, int httpRequestMethod) {
        super(httpRequestMethod, url, errorListener);
        mListener = listener;
        mDataModel = model;
        mGson = new Gson();
        mParams = params;
        mHeaders = headers;
        mRequestBody = requestBody;
        mUrl = url;
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        retryPolicy = new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        return super.setRetryPolicy(retryPolicy);
    }

    @Override
    protected void deliverResponse(IPCDataModel model) {
        mListener.onResponse(model);
    }


    @Override
    protected Response<IPCDataModel> parseNetworkResponse(NetworkResponse response) {
        String jsonString = new String(response.data);
        CPCAppCommonUtility.log(TAG, "json Response String: " + jsonString + " statusCode:" + response.statusCode);
        int statusCode = response.statusCode;
        try {
            if (statusCode == ERROR_CODE_401 || statusCode == ERROR_CODE_410) {
                throw new IllegalErrorCode(jsonString);
            }
            if (statusCode == ERROR_CODE_449 || statusCode == ERROR_CODE_499 || statusCode == ERROR_CODE_502
                    || statusCode == ERROR_CODE_503 || statusCode == ERROR_CODE_504) {
                throw new IllegalErrorCode(jsonString);
            }
            if (statusCode == ERROR_CODE_400) {
                throw new IllegalErrorCode(jsonString);
            }
            if (jsonString == null || jsonString.trim().length() == 0) {
                throw new IllegalStateException();
            }
            try {

                CPCIllegalCodeError errorStatus = new CPCIllegalCodeError();
                errorStatus = mGson.fromJson(jsonString, errorStatus.getClass());
                if (errorStatus != null && errorStatus.getStatusError() != null && errorStatus.getStatusError().getmResult() != null
                        && errorStatus.getStatusError().getmResult().equalsIgnoreCase("failure_error")) {
                    VolleyError volleyError = new VolleyError("failure_error");
                    // volleyError.setUrl(mUrl);
                    if (errorStatus != null && errorStatus.getStatusError() != null && errorStatus.getStatusError().getmMessage() != null) {
                        //     volleyError.setAlertMessage(errorStatus.getStatusError().getmMessage().getMessage());
                        //    volleyError.setmAlertTitle(errorStatus.getStatusError().getmMessage().getTitle());
                    }
                    return Response.error(volleyError);
                }

            } catch (Exception ex) {

            }
            if ((statusCode == 404 || statusCode == 503)) {
                throw new IllegalErrorCode(jsonString);
                /*statusCode = 503;
                throw new IllegalErrorCode(jsonString);*/
            } else
                return Response.success((IPCDataModel) mGson.fromJson(jsonString, mDataModel.getClass()), getCacheEntry());
        } catch (IllegalErrorCode e) {
            e.printStackTrace();
            VolleyError volleyError = new VolleyError(String.valueOf(statusCode));
            volleyError.setUrl(mUrl);
            CPCIllegalCodeError errorStatus = new CPCIllegalCodeError();
            try {
                errorStatus = mGson.fromJson(jsonString, errorStatus.getClass());
                if (errorStatus != null && errorStatus.getStatusError() != null && errorStatus.getStatusError().getmMessage() != null) {
                    volleyError.setAlertMessage(errorStatus.getStatusError().getmMessage().getMessage());
                    volleyError.setmAlertTitle(errorStatus.getStatusError().getmMessage().getTitle());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return Response.error(volleyError);
        } catch (Exception exception) {
            exception.printStackTrace();
            VolleyError volleyError = new VolleyError("parsing_error");
            volleyError.setUrl(mUrl);
            return Response.error(volleyError);
        }


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams != null ? mParams : super.getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {

            return null;
        }
    }


    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    /**
     * @deprecated Use {@link #getBody()}.
     */
    @Override
    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    /**
     * @throws AuthFailureError
     * @deprecated Use {@link #getBody()}.
     */
    @Override
    public byte[] getPostBody() throws AuthFailureError {
        return getBody();
    }


}

