package com.thinkpace.pacifyca.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.activity.APCAssignments;
import com.thinkpace.pacifyca.activity.APCAttendanceActivity;
import com.thinkpace.pacifyca.activity.APCContactUs;
import com.thinkpace.pacifyca.activity.APCDashboard;
import com.thinkpace.pacifyca.activity.APCHomeScreen;
import com.thinkpace.pacifyca.activity.APCNotice;
import com.thinkpace.pacifyca.activity.APCStudentInfoActivity;
import com.thinkpace.pacifyca.activity.APCTimeTable;
import com.thinkpace.pacifyca.activity.APCVideos;
import com.thinkpace.pacifyca.activity.APCWebViewActivity;
import com.thinkpace.pacifyca.calander.APCInstituteCalendarActivity;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;
import com.thinkpace.pacifyca.widget.RoboTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrawerFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.assignment_lyt)
    LinearLayout mAssignmentLyt;
    @BindView(R.id.message_lyt)
    LinearLayout mMessageLyt;
    @BindView(R.id.attendance_lyt)
    LinearLayout mAttendanceLyt;
    @BindView(R.id.time_table_lyt)
    LinearLayout mTimeTableLyt;
    @BindView(R.id.about_lyt)
    LinearLayout mAboutLyt;
    @BindView(R.id.logout_lyt)
    LinearLayout mLogOutLyt;

    @BindView(R.id.user_name)
    RoboTextView mUserName;
    @BindView(R.id.profile_img_drawer)
    ImageView mImageView;
    @BindView(R.id.student_admission_no)
    RoboTextView mStudentAdmissionNo;
    @BindView(R.id.student_class)
    RoboTextView mStudentClass;

    IJRDrawerListener ijrDrawerListener;
    @BindView(R.id.institute_lyt)
    LinearLayout mInstituteLyt;

    //Notice Board
    @BindView(R.id.notice_lyt)
    LinearLayout mNoticeLyt;

    //News and Events
    @BindView(R.id.news_lyt)
    LinearLayout mNewsLyt;

    //News and Events
    @BindView(R.id.video_lyt)
    LinearLayout mVideosLyt;

    //Student Info
    @BindView(R.id.student_lyt)
    LinearLayout mstudentInfoLyt;

    //Exam Schedule
    @BindView(R.id.exam_lyt)
    LinearLayout mExamLyt;

    //Exam Schedule
    @BindView(R.id.contact_lyt)
    LinearLayout mContactLyt;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @BindView(R.id.tv_about)
    TextView tvAbout;

    @BindView(R.id.tv_assignment)
    TextView tvAssignment;

    public interface IJRDrawerListener {
        public void onDrawerClose();
        public void onLogoutClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);
        ButterKnife.bind(this, view);
        initUi();
        return view;
    }

    private void initUi() {
        mInstituteLyt.setOnClickListener(this);
        mAssignmentLyt.setOnClickListener(this);
        mMessageLyt.setOnClickListener(this);
        mAttendanceLyt.setOnClickListener(this);
        mTimeTableLyt.setOnClickListener(this);
        mAboutLyt.setOnClickListener(this);
        mLogOutLyt.setOnClickListener(this);
        //Notice Board
        mNoticeLyt.setOnClickListener(this);
        //News
        mNewsLyt.setOnClickListener(this);
        mstudentInfoLyt.setOnClickListener(this);
        mVideosLyt.setOnClickListener(this);
        mMessageLyt.setOnClickListener(this);
        mExamLyt.setOnClickListener(this);
        mContactLyt.setOnClickListener(this);
        setChildName();
        toolbarTitle.setText("Menu");
    }

    public void setChildName() {
        String userName = CPCAppCommonUtility.getStudentName(getActivity(), CPCAppConstants.KEY_STUDENT_NAME);
        String profilePhoto = CPCAppCommonUtility.getStudentImage(getActivity(), CPCAppConstants.KEY_STUDENT_IMAGE);
        String admissionNo = CPCAppCommonUtility.getStudentAdmissionNo(getActivity(), CPCAppConstants.KEY_STUDENT_ADMISSION_NO);
        String course = CPCAppCommonUtility.getStudentClass(getActivity(), CPCAppConstants.KEY_STUDENT_CLASS);

        if (userName != null) {
            mUserName.setText(userName);
        } else {
            mUserName.setText("Pacifyca User");
        }

        if (profilePhoto != null) {
            mUserName.setText(userName);
        } else {
            mUserName.setText("Pacifyca User");
        }

        if (admissionNo != null) {
            mStudentAdmissionNo.setText("Admission No : " + admissionNo);
        } else {
            mStudentAdmissionNo.setText("");
        }

        if (course != null) {
            mStudentClass.setText("Class : " + course);
        } else {
            mStudentClass.setText("");
        }

        if (!TextUtils.isEmpty(profilePhoto)) {
            try {
                Picasso.with(getActivity())
                        .load(profilePhoto)
                        .placeholder(R.drawable.profile_icon)
                        .error(R.drawable.profile_icon)
                        .into(mImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ijrDrawerListener = (IJRDrawerListener) activity;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.assignment_lyt:
                closeDrawer();
                handleAssignmentClick();
                break;
            case R.id.institute_lyt:
                closeDrawer();
                handleInstituteCalendarClick();
                break;
            case R.id.message_lyt:
                closeDrawer();
                handleSMSClick();
                break;
            case R.id.attendance_lyt:
                closeDrawer();
                handleAttendanceClick();
                break;
            case R.id.time_table_lyt:
                closeDrawer();
                handleTimeTableClick();

                break;
            case R.id.about_lyt:
                closeDrawer();
                handleAboutClick();
                break;
            case R.id.logout_lyt:
                closeDrawer();
                onLogOutClicked();
                break;
            //Notice
            case R.id.notice_lyt:
                closeDrawer();
                handleNoticeClick();
                break;
            //News
            case R.id.news_lyt:
                closeDrawer();
                handleNewsClick();
                break;
            //Videos
            case R.id.video_lyt:
                closeDrawer();
                handleVideosClick();
                break;
            //Exam Schedule
            case R.id.exam_lyt:
                closeDrawer();
                handleExamClick();
                break;
            //Contact Us
            case R.id.contact_lyt:
                closeDrawer();
                handleContactClick();
                break;
            //Student
            case R.id.student_lyt:
                closeDrawer();
                handleStudentInfoClick();
        }
    }

    private void handleTimeTableClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null && getActivity().isFinishing()) {
                    return;
                }
                startActivity(new Intent(getActivity(), APCTimeTable.class));
            }
        }, 400);
    }

    private void handleAttendanceClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null && getActivity().isFinishing()) {
                    return;
                }
                startActivity(new Intent(getActivity(), APCAttendanceActivity.class));
            }
        }, 400);
    }

    private void handleAssignmentClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null && getActivity().isFinishing()) {
                    return;
                }
                startActivity(new Intent(getActivity(), APCAssignments.class));
            }
        }, 400);
    }
    private void handleInstituteCalendarClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null && getActivity().isFinishing()) {
                    return;
                }
                startActivity(new Intent(getActivity(), APCInstituteCalendarActivity.class));
            }
        }, 400);
    }

    private void onLogOutClicked() {
        if (ijrDrawerListener != null)
            ijrDrawerListener.onLogoutClick();
    }

    //Notice Board
    private void handleNoticeClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null && getActivity().isFinishing()) {
                    return;
                }
                Intent noticeIntent = new Intent(getActivity(), APCNotice.class);
                noticeIntent.putExtra(CPCAppConstants.KEY_NOTICE_TYPE, CPCAppConstants.KEY_CIRCULAR_TYPE); //Optional parameters
                DrawerFragment.this.startActivity(noticeIntent);
            }
        }, 400);
    }

    //News
    private void handleNewsClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null && getActivity().isFinishing()) {
                    return;
                }

                Intent noticeIntent = new Intent(getActivity(), APCNotice.class);
                noticeIntent.putExtra(CPCAppConstants.KEY_NOTICE_TYPE, CPCAppConstants.KEY_NEWS_TYPE); //Optional parameters
                startActivity(noticeIntent);
            }
        }, 400);
    }

    //Videos
    private void handleVideosClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null && getActivity().isFinishing()) {
                    return;
                }
                startActivity(new Intent(getActivity(), APCVideos.class));
            }
        }, 400);
    }

    private void closeDrawer() {
        if (ijrDrawerListener != null) {
            ijrDrawerListener.onDrawerClose();
        }
    }

    private void handleAboutClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null && getActivity().isFinishing()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), APCWebViewActivity.class);
                intent.putExtra("WEB_URL", getString(R.string.web_view_url));
                intent.putExtra("PAGE_TITLE", "About us");
                startActivity(intent);
            }
        }, 250);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (getActivity() == null && getActivity().isFinishing()) {
//                    return;
//                }
//                Intent intent = new Intent(getActivity(), APCWebViewActivity.class);
//                //Specify all the flavors here
//                intent.putExtra("WEB_URL", R.string.web_view_url);
////
////                if (BuildConfig.FLAVOR.equals("aac")){
////                    intent.putExtra("WEB_URL", "http://www.pacifyca.com/aboutus_aac.html");
////                } else if (BuildConfig.FLAVOR.equals("rotary")){
////                    intent.putExtra("WEB_URL", "http://www.pacifyca.com/aboutus_rotary.html");
////                } else if (BuildConfig.FLAVOR.equals("joyland")){
////                    intent.putExtra("WEB_URL", "http://www.pacifyca.com/aboutus_joyland.html");
////                }else {
////                    intent.putExtra("WEB_URL", "http://www.pacifyca.com/aboutus_lourdes.html");
////                }
//                intent.putExtra("PAGE_TITLE", "About us");
//                startActivity(intent);
//            }
//        }, 250);
    }

    private void handleStudentInfoClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null && getActivity().isFinishing()) {
                    return;
                }

                Intent intent = new Intent(getActivity(), APCStudentInfoActivity.class);
                intent.putExtra("student_id", CPCAppCommonUtility.getStudentId(getActivity(), CPCAppConstants.KEY_STUDENT_ID));
                intent.putExtra("client_id", CPCAppCommonUtility.getClientId(getActivity(), CPCAppConstants.KEY_CLIENT_ID));
                startActivity(intent);
            }
        }, 400);
    }

    private void handleSMSClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null) {
                    return;
                }
                startActivity(new Intent(getActivity(), APCHomeScreen.class));
            }
        }, 400);
    }

    private void handleExamClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null && getActivity().isFinishing()) {
                    return;
                }

                Intent noticeIntent = new Intent(getActivity(), APCNotice.class);
                noticeIntent.putExtra(CPCAppConstants.KEY_NOTICE_TYPE, CPCAppConstants.KEY_EXAM_TYPE); //Optional parameters
                startActivity(noticeIntent);
            }
        }, 400);
    }

    private void handleContactClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null && getActivity().isFinishing()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), APCContactUs.class);
                intent.putExtra("PAGE_TITLE", "Contact us");
                startActivity(intent);
            }
        }, 250);
    }
}
