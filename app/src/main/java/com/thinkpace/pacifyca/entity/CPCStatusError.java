package com.thinkpace.pacifyca.entity;


import com.google.gson.annotations.SerializedName;

public class CPCStatusError implements IPCDataModel {

	private static final long serialVersionUID = 1L;
	
	@SerializedName("result")
	private String mResult;
	
	@SerializedName("message")
	private CPCError mMessage;
	
	@SerializedName("code")
	private int mCode;
	
	public String getmResult() {
		return mResult;
	}
	public CPCError getmMessage() {
		return mMessage;
	}
	public int getmCode() {
		return mCode;
	}
	
	

}
