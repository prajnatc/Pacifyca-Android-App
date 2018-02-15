
package com.thinkpace.pacifyca.entity;


import com.google.gson.annotations.SerializedName;

public class CPCError implements IPCDataModel {
	
	private static final long serialVersionUID = 1L;

	@SerializedName("title")
	private String mTitle;
	
	@SerializedName("message")
	private String mMessage;

	@SerializedName("okbutton")
	private String mOkButtonText;
	
	@SerializedName("cancelButton")
	private String mCancelButtonText;

	public String getTitle() {
		return mTitle;
	}

	public String getMessage() {
		return mMessage;
	}

	public String getOkButtonText() {
		return mOkButtonText;
	}

	public String getCancelButtonText() {
		return mCancelButtonText;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}

	public void setMessage(String message) {
		mMessage = message;
	}
	public void setOkButton(String button) {
		mOkButtonText = button;
	}

}