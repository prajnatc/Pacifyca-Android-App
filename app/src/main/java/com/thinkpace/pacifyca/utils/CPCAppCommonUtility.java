

package com.thinkpace.pacifyca.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.thinkpace.pacifyca.entity.CPCParentDetailsModel;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.leolin.shortcutbadger.ShortcutBadger;


public class CPCAppCommonUtility {


    private static boolean isDebug = true;

    public static String getRegistrationId(Context context) {
        try {
            SecureSharedPreferences prefs = new SecureSharedPreferences(context);
            String id = prefs.getString(CPCAppConstants.KEY_GCM_REGISTRATION_ID, "");
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getImageFromHTMLTag(String htmlContent) {
        org.jsoup.nodes.Document doc = Jsoup.parse(htmlContent);
        Elements pngs = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");

        String str = pngs.toString();
        String imgRegex = "<[iI][mM][gG][^>]+[sS][rR][cC]\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";

        Pattern p = Pattern.compile(imgRegex);
        Matcher m = p.matcher(str);

        if (m.find()) {
            String imgSrc = m.group(1);
            return imgSrc;
        }
        return "";
    }

    public static String getDeviceID(Context context) {
        return ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }


    public static String getUniqueDeviceId(Context incontext,
                                           TelephonyManager inTelephonyManger) {
        String uniqueDeviceId = null;
        try {
            String ImeiNumber = inTelephonyManger.getDeviceId();
            String AndroidIdetifier = Settings.Secure.getString(
                    incontext.getContentResolver(), Settings.Secure.ANDROID_ID);
            String SubscriberId = inTelephonyManger.getSubscriberId();

            if (ImeiNumber != null && ImeiNumber.length() > 0) {
                CPCAppCommonUtility.log("CSM", "IMEI number is : " + ImeiNumber);
                uniqueDeviceId = ImeiNumber;
            } else if (AndroidIdetifier != null
                    && AndroidIdetifier.length() == 16) {
                uniqueDeviceId = AndroidIdetifier;
                CPCAppCommonUtility.log("CSM", "Android Id is : " + AndroidIdetifier);
            } else if (SubscriberId != null && SubscriberId.length() > 0) {
                uniqueDeviceId = SubscriberId;
                CPCAppCommonUtility.log("CSM", "Subscriber Id is : " + SubscriberId);
            } else {
                String UniqueId = UUID.randomUUID().toString();
                CPCAppCommonUtility.log("CSM", "Create New uniqe id : " + UniqueId);
                uniqueDeviceId = UniqueId;
            }
        } catch (Exception e) {
            CPCAppCommonUtility.log("CSM", "Exception message : " + e.toString());
        }
        return uniqueDeviceId;
    }

    public static void log(String tag, String inMsg) {
        try {
            if (isDebug) {
                if (tag != null && inMsg != null) {
                    Log.d(tag, inMsg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width;
        if (activity != null && !activity.isFinishing()) {
            activity.getWindowManager().getDefaultDisplay()
                    .getMetrics(displayMetrics);
            width = displayMetrics.widthPixels;
        } else {
            width = 0;
        }
        return width;
    }


    public static int getOSVersion() {
        return Build.VERSION.SDK_INT;
    }


    public static String getDate(String publishedTime) {
        if (publishedTime != null) {
            try {
                Date date1 = null, date2 = null;
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = df.format(c.getTime());
                SimpleDateFormat simpleDateFormat =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date1 = simpleDateFormat.parse(currentTime);
                date2 = simpleDateFormat.parse(publishedTime);
                if (date1 != null && date2 != null) {
                    long different = date1.getTime() - date2.getTime();
                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;
                    long elapsedDays = different / daysInMilli;
                    different = different % daysInMilli;
                    long elapsedHours = different / hoursInMilli;
                    different = different % hoursInMilli;
                    long elapsedMinutes = different / minutesInMilli;
                    different = different % minutesInMilli;
                    long elapsedSeconds = different / secondsInMilli;
                    CPCAppCommonUtility.log("CBN", "D " + elapsedDays + " H " + elapsedHours + " M " + elapsedMinutes + " S " + elapsedSeconds);
                    if (elapsedDays == 0 && elapsedHours != 0) {
                        return elapsedHours + "h";
                    } else if (elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes != 0) {
                        return elapsedMinutes + "m";
                    } else if (elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes == 0 && elapsedSeconds != 0) {
                        return elapsedSeconds + "s";
                    } else {
                        return elapsedDays + "d";
                    }

                } else {
                    return null;
                }
            } catch (ParseException e) {
                return null;
            }

        }
        return null;
    }


    public static int getScreenGridUnitBy32(Context context) {
        return getScreenWidth((Activity) context) / 32;
    }

    public static int getScreenGridUnitBy16(Context context) {
        return getScreenWidth((Activity) context) / 16;
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager ConnectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (ConnectMgr == null)
            return false;
        NetworkInfo NetInfo = ConnectMgr.getActiveNetworkInfo();
        if (NetInfo == null)
            return false;

        return NetInfo.isConnected();
    }


    public static String stripHtml(String str) {
        try {
            if (str != null) {
                return Html.fromHtml(str).toString().replace((char) 65532, (char) 32).trim();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void writeObject(Context context, String key, Object object) {
        try {
            FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        } catch (IOException ex) {
            CPCAppCommonUtility.log(key, "could not write the object");
        }

    }

    public static String getAccessToken(Context context) {
        String accessToken = null;
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            accessToken = prefs.getString(CPCAppConstants.ACCESS_TOKEN, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public static String getUserName(Context context) {
        String accessToken = null;
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            accessToken = prefs.getString(CPCAppConstants.USER_NAME, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public static String getUserId(Context context) {
        String accessToken = null;
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            accessToken = prefs.getString(CPCAppConstants.KEY_USER_ID, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public static void deleteObject(Context context, String key) {
        boolean isDeleted = context.deleteFile(key);
        if(isDeleted)
            log("delete object:","object with key "+key+" deleted successfully");
        else
            log("delete object:","object with key "+key+" deletion failed");
    }

    public static Object readObject(Context context, String key) {

        try {
            FileInputStream fis = context.openFileInput(key);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            return object;
        } catch (IOException e) {
            CPCAppCommonUtility.log("IO Error", e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            CPCAppCommonUtility.log("ClassNotFoundException", e.getMessage());
            return null;
        } catch (Exception ex) {
            CPCAppCommonUtility.log("Exception", ex.getMessage());
            return null;
        }
    }

    public static void unbindDrawables(View paramView) {
        try {
            if (paramView.getBackground() != null) {
                paramView.getBackground().setCallback(null);
                paramView.setBackgroundDrawable(null);

            }
            paramView.setOnClickListener(null);
            paramView.removeCallbacks(null);
            if ((paramView instanceof ViewGroup)) {
                for (int i = 0; i < ((ViewGroup) paramView).getChildCount(); i++) {

                    unbindDrawables(((ViewGroup) paramView).getChildAt(i));
                }
                ((ViewGroup) paramView).removeAllViews();
            }
            return;
        } catch (Exception localException) {
        }
    }

    /**
     * Utility function to convert java Date to TimeZone format
     */
    public static String formatDateToString(String date11) {
        Date date = new Date();
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        try {
            date = simpleDateFormat.parse(date11);
            format.setTimeZone(simpleDateFormat.getTimeZone());
            Log.e("IST TIME", "" + format.format(date));
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void showAlert(Context context, String title, String message) {
        CustomDialog.showAlert(context, title, message);
    }

    public static boolean getBooleanFromSharedPreference(Context context, String key) {
        boolean value = false;
        SharedPreferences sharedpreferences = context.getSharedPreferences(
                "newskannada", Context.MODE_PRIVATE);
        if (sharedpreferences.contains(key)) {
            value = sharedpreferences.getBoolean(key, false);
        }
        return value;
    }

    public static void saveBooleanIntoSharedPreference(Context context,
                                                       String key, boolean value) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(
                "newskannadaS", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedpreferences.edit();
        prefEditor.putBoolean(key, value);
        prefEditor.commit();
    }

    public static void handleErrorResponse(Context context, String errorTitle, String errorMsg) {
        if (errorMsg != null && !errorMsg.isEmpty()) {
            if (errorTitle != null && errorTitle.isEmpty())
                CustomDialog.showAlert(context, errorTitle, errorMsg);
            else
                CustomDialog.showAlert(context, "Error", errorMsg);
        } else
            CustomDialog.showAlert(context, "Error", "Error , Something went wrong, please try again later.");
    }


    public static boolean isValidMobile(String number) {
        String mobileNumber = number;
        Pattern p = Pattern.compile("^[5789][0-9]{9}$");
        Matcher m = p.matcher(mobileNumber);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static boolean isValidEmail(String email) {
        String emailId = email;
        Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*(\\+[_A-Za-z0-9-]+){0,1}@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher m = p.matcher(emailId);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static String getOtpFromSms(String from, String smsBody) {
        String receivedOtp = null;
        try {
            if (from != null && smsBody != null) {
                Pattern p = Pattern.compile("(?<!\\d)\\d{6}(?!\\d)");
                Matcher m = p.matcher(smsBody);
                while (m.find()) {
                    receivedOtp = m.group();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receivedOtp;
    }

    public static boolean isUserSignedIn(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ssoToken = preferences.getString(CPCAppConstants.ACCESS_TOKEN, null);
        if (ssoToken != null && ssoToken.trim().length() > 0) {
            return true;
        }
        return false;
    }

    public static void storeStringValue(Context context, String key, String value) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.apply();
            CPCAppCommonUtility.log("KR", "Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getStringValue(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = preferences.getString(key, null);
        return value;
    }

    public static String getStudentId(Context context, String key) {
        return getStringValue(context, CPCAppConstants.KEY_STUDENT_ID);
    }

    public static String getClientId(Context context, String key) {
        return getStringValue(context, CPCAppConstants.KEY_CLIENT_ID);
    }
    public static String getStudentName(Context context, String key) {
        return getStringValue(context, CPCAppConstants.KEY_STUDENT_NAME);
    }
    public static String getStudentAdmissionNo(Context context, String key) {
        return getStringValue(context, CPCAppConstants.KEY_STUDENT_ADMISSION_NO);
    }
    public static String getStudentClass(Context context, String key) {
        return getStringValue(context, CPCAppConstants.KEY_STUDENT_CLASS);
    }

    public static void setClientId(Context context, String value) {
        storeStringValue(context, CPCAppConstants.KEY_CLIENT_ID, value);
    }

    public static void setStudentId(Context context, String value) {
        storeStringValue(context, CPCAppConstants.KEY_STUDENT_ID, value);
    }

    public static void setStudentName(Context context, String value) {
        storeStringValue(context, CPCAppConstants.KEY_STUDENT_NAME, value);
    }

    public static void setStudentAdmissionNo(Context context, String value) {
        storeStringValue(context, CPCAppConstants.KEY_STUDENT_ADMISSION_NO, value);
    }

    public static void setStudentCourse(Context context, String value) {
        storeStringValue(context, CPCAppConstants.KEY_STUDENT_CLASS, value);
    }

    public static String getStudentImage(Context context, String key) {
        return getStringValue(context, CPCAppConstants.KEY_STUDENT_IMAGE);
    }

    public static void setStudentImage(Context context, String value) {
        storeStringValue(context, CPCAppConstants.KEY_STUDENT_IMAGE, value);
    }

    public static String getElapsedTime(String publishedTime) {
        if (publishedTime != null) {
            try {
                Date date1 = null, date2 = null;
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = df.format(c.getTime());
                SimpleDateFormat simpleDateFormat =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date1 = simpleDateFormat.parse(currentTime);
                date2 = simpleDateFormat.parse(publishedTime);
                if (date1 != null && date2 != null) {
                    long different = date1.getTime() - date2.getTime();
                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;
                    long elapsedDays = different / daysInMilli;
                    different = different % daysInMilli;
                    long elapsedHours = different / hoursInMilli;
                    different = different % hoursInMilli;
                    long elapsedMinutes = different / minutesInMilli;
                    different = different % minutesInMilli;
                    long elapsedSeconds = different / secondsInMilli;
                    CPCAppCommonUtility.log("CBN", "D " + elapsedDays + " H " + elapsedHours + " M " + elapsedMinutes + " S " + elapsedSeconds);
                    if (elapsedDays == 0 && elapsedHours != 0) {
                        return elapsedHours + " hours ago";
                    } else if (elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes != 0) {
                        return elapsedMinutes + " min ago";
                    } else if (elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes == 0 && elapsedSeconds != 0) {
                        return elapsedSeconds + " sec ago";
                    } else {
                        return elapsedDays + " days ago";
                    }

                } else {
                    return null;
                }
            } catch (ParseException e) {
                return null;
            }

        }
        return null;
    }

    public static String formatDate(Activity mContext, String inputDate, String inputFormat, String outputFormat) {
        try {
            Locale appLocale = Locale.ENGLISH;
            DateFormat originalFormat = new SimpleDateFormat(inputFormat, appLocale);
            DateFormat targetFormat = new SimpleDateFormat(outputFormat, appLocale);
            Date dateObject = originalFormat.parse(inputDate);
            String formattedDate = targetFormat.format(dateObject);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static void setShortcutBadger(Context context){
        ShortcutBadger.applyCount(context, CPCAppConstants.KEY_UNREAD_NOTICES_COUNT + CPCAppConstants.KEY_UNREAD_NEWS_COUNT + CPCAppConstants.KEY_UNREAD_EXAM_COUNT + CPCAppConstants.KEY_UNREAD_ASSIGNMENT_COUNT + CPCAppConstants.KEY_UNREAD_MESSAGE_COUNT);
    }
}
