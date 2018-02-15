package com.thinkpace.pacifyca.utils;

import java.lang.reflect.Array;

/**
 * Created by sapna on 26-03-2016.
 */
public class CPCAppConstants {
    public static final String ACCESS_TOKEN = "access_token";
    public static final String KEY_USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String KEY_HEADER_API_KEY = "api_key";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_AUTHORIZATION = "Authorization";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CONTENT_TYPE = "Content-Type";
    public static final String KEY_APPLICATION_JSON = "application/json";
    public static final String MIN_DATE = "min_date";
    public static final String MAX_DATE = "max_date";

    public static final String DATE_FORMAT = "date_format";
    public static final String KEY_DEFAULT_DOB = "default_dob";

    //Production Base URL
//    public static final String KEY_BASE_URL = "http://api.pacifyca.com/v1";

    //Staging Base URL
    public static final String KEY_BASE_URL = "http://devstageapi.pacifyca.com/v1";

    //Local Base URL
//    public static final String KEY_BASE_URL = "http://192.168.1.63:8080/v1";

    public static final String KEY_USER_TYPE_URL = KEY_BASE_URL + "/login-available-types";
    public static final String KEY_GENERATE_OTP_URL =  KEY_BASE_URL + "/auth/generate-otp";
    public static final String KEY_SIGN_IN_URL = KEY_BASE_URL + "/auth/signin";
    public static final String KEY_GET_USER_DETAILS_URL = KEY_BASE_URL + "/parent";
    public static final String KEY_GET_USER_MESSAGES_URL = "http://devstageapi.pacifyca.com/parent/{parentid}/messages";
    public static final String KEY_GET_ATTENDANCE_DETAILS_URL = KEY_BASE_URL + "/parent/";
    public static final String KEY_GET_STUDENT_INFO_URL = KEY_BASE_URL + "/parent/";

    public static final String KEY_SELECTED_STUDENT = "selected_student";
    public static final String KEY_STUDENT_ID = "student_id";
    public static final String KEY_STUDENT_NAME = "student_name";
    public static final String KEY_STUDENT_IMAGE = "student_image";
    public static final String KEY_STUDENT_ADMISSION_NO = "student_admission_no";
    public static final String KEY_STUDENT_CLASS = "student_class";
    public static final String KEY_CLIENT_ID = "client_id";
    public static final String KEY_FAVOURITE_MESSAGE_ID_LIST = "favourite_message_id";
    public static final String KEY_GROUP_DAILY_TWICE = "GROUP_DAILY_TWICE";
    public static final String KEY_GROUP_DAILY_ONCE = "GROUP_DAILY_ONCE";
    public static final String KEY_GROUP_SUBJECT = "GROUP_SUBJECT";
    public static final String KEY_GET_ASSIGNMENTS_URL = KEY_BASE_URL + "/parent/";

    public static final String SUBJECT_ID = "subject_id";
    public static final String MONTH_YEAR = "month_year";
    public static final String IS_DAILY_TWICE = "daily_twice";
    public static final String MESSAGE_DETAILS = "message_details";
    public static final String ASSIGNMENT_DETAILS = "assignment_details";
    public static final String NO_INTERNET_CONNECTION = "No internet connection, Please try again later." ;

    public static final String PROJECT_NUMBER = "905524398038";
    public static final String KEY_USER_TOKEN = "user_token";
    public static final String WEEK_NAME = "week_name";
    public static final String TIME_TABLE_LIST = "time_table_list";
    public static String KEY_GCM_REGISTRATION_ID = "key_registration_id";
    public static String NOTIFICATION_MSG_EXTRA = "notification_data_extra";

    //prajna
    public static final String KEY_READ_MESSAGE_ID_LIST = "read_message_id";
    public static final String KEY_READ_ASSIGNMENT_ID_LIST = "read_message_id";

    //Notice Board
    public static final String NOTICE_DETAILS = "notice_details";
    public static final String KEY_READ_NOTICE_ID_LIST = "read_message_id";

    //News
    public static final String KEY_NOTICE_TYPE="type";

    //Unread Message Count
    public static Integer KEY_UNREAD_NOTICES_COUNT = 0;
    public static Integer KEY_UNREAD_NEWS_COUNT = 0;
    public static Integer KEY_UNREAD_EXAM_COUNT = 0;

    public static Integer KEY_UNREAD_MESSAGE_COUNT = 0;
    public static Integer KEY_UNREAD_ASSIGNMENT_COUNT = 0;

    public static String[] KEY_PARENT_STUDENTS;

    public  static final String KEY_NEWS_TYPE = "NEWS_EVENTS";
    public  static final String KEY_CIRCULAR_TYPE = "NOTICE_BOARD";
    public  static final String KEY_EXAM_TYPE = "EXAM_SCHEDULE";

}