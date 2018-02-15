/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thinkpace.pacifyca.net;

import android.net.Uri;

import com.android.volley.NetworkResponse;

/**
 * Exception style class encapsulating Volley errors
 */
@SuppressWarnings("serial")
public class VolleyError extends Exception {
    public final NetworkResponse networkResponse;
    
    private String mUrl;
    
    private String mAlertMessage;
    
    private String mAlertTitle;

    public String getAlertMessage() {
		return mAlertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.mAlertMessage = alertMessage;
	}

	public String getAlertTitle() {
		return mAlertTitle;
	}

	public void setmAlertTitle(String alertTitle) {
		this.mAlertTitle = alertTitle;
	}

	public String getUrl() {
    	try {
    		Uri uri = Uri.parse(mUrl.toString());
    		return uri.getHost()+uri.getPath();
		} catch (Exception e) {
			return mUrl;
		}
		
	}
	public String getFullUrl() {
		return mUrl;
	}
	public void setUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	public VolleyError() {
        networkResponse = null;
    }

    public VolleyError(NetworkResponse response) {
        networkResponse = response;
    }

    public VolleyError(String exceptionMessage) {
       super(exceptionMessage);
       networkResponse = null;
    }

    public VolleyError(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
        networkResponse = null;
    }

    public VolleyError(Throwable cause) {
        super(cause);
        networkResponse = null;  
    }
    
    public VolleyError(Throwable cause, String message) {
        super(cause);
        
        networkResponse = null;  
    }
    
    public VolleyError(NetworkResponse response, String exceptionMessage) {
    	super(exceptionMessage);
        networkResponse = response;
    }
}