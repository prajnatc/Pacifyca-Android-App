package com.thinkpace.pacifyca.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CustomDialog {
	private static boolean isDialogShowing;
	
	public static void showAlert(Context context, String title, String message){
		if(CustomDialog.isDialogShowing){
			return;
		}else {
			if (((Activity) context).isFinishing() == false) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(title).setMessage(message).setCancelable(false);
				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								CustomDialog.isDialogShowing = false;
							}
						});
				builder.show();
				CustomDialog.isDialogShowing = true;
			}
		}
	}
}
