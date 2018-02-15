package com.thinkpace.pacifyca.entity;


import com.google.gson.annotations.SerializedName;

public class CPCIllegalCodeError implements IPCDataModel {


	private static final long serialVersionUID = 1L;
	
	@SerializedName("error")
	private String mError;
	
	@SerializedName("status")
	private CPCStatusError mStatusError;

	public CPCStatusError getStatusError() {
		return mStatusError;
	}

	public String getError() {
		return mError;
	}
	
	public void setStatusError(CPCStatusError statusError) {
		this.mStatusError = statusError;
	}
	
}
