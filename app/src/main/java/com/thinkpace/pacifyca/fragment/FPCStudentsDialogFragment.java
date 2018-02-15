package com.thinkpace.pacifyca.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.thinkpace.pacifyca.Adapter.CPCStudentsAdapter;
import com.thinkpace.pacifyca.BuildConfig;
import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.activity.APCStudentInfoActivity;
import com.thinkpace.pacifyca.app.CPCVolley;
import com.thinkpace.pacifyca.entity.CPCStudentDetails;
import com.thinkpace.pacifyca.entity.CPCStudents;
import com.thinkpace.pacifyca.entity.IPCDataModel;
import com.thinkpace.pacifyca.net.CPCGsonGetRequest;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;
import com.thinkpace.pacifyca.utils.CustomDialog;
import com.thinkpace.pacifyca.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FPCStudentsDialogFragment extends DialogFragment implements CPCStudentsAdapter.OnStudentsClickListener, Response.ErrorListener, Response.Listener<IPCDataModel> {

    ArrayList<CPCStudentDetails> mStudentsList = new ArrayList<>();
    private RecyclerView mStudentsRecyclerView;
    private CPCStudentsAdapter mStudentsAdapter;
    CPCStudentDetails mSelectedStudentItem;
    private int mGridUnit;
    @BindView(R.id.load_indicator)
    ProgressBar progressBar;
    private Unbinder unbinder;
    @BindView(R.id.selected_student_name)
    TextView mSelectedStudent;
    @BindView(R.id.change_student_btn)
    Button mChangeStudentBtn;
    @BindView(R.id.no_students_lyt)
    LinearLayout mNoStudentsLyt;
    @BindView(R.id.item_success_lyt)
    RelativeLayout studentsItemLyt;
    @BindView(R.id.student_refresh)
    ImageView mStudentRefereshImage;
    // Define listener member variable
    private OnStudentsDialogClickListener onStudentsDialogClickListener;

    @Override
    public void onStudentsItemClick(CPCStudentDetails item, int position) {
        if (item != null && getActivity() != null && !isDetached()) {
            String studentId = CPCAppCommonUtility.getStudentId(getActivity(), CPCAppConstants.KEY_STUDENT_ID);
            if (!TextUtils.isEmpty(studentId) && studentId.equals(item.getStudent_id()))
                return;
            mSelectedStudent.setText(item.getFullName());
            mSelectedStudent.setTextColor(getActivity().getResources().getColor(R.color.black));
            setStudentData(item);
            mStudentsAdapter.updateStudentList(mStudentsList);
        }
    }

    @Override
    public void onInfoClick(CPCStudentDetails item, int position) {
        Intent intent = new Intent(getActivity(), APCStudentInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("student_info", item);
        intent.putExtra("info", bundle);
        startActivity(intent);

    }

    private void setStudentData(CPCStudentDetails item) {
        CPCAppCommonUtility.writeObject(getActivity(), CPCAppConstants.KEY_SELECTED_STUDENT, item);
        CPCAppCommonUtility.setClientId(getActivity(), item.getClient_id());
        CPCAppCommonUtility.setStudentId(getActivity(), item.getStudent_id());
        CPCAppCommonUtility.setStudentName(getActivity(), item.getFullName());
        CPCAppCommonUtility.setStudentImage(getActivity(), item.getProfilePhoto());
        CPCAppCommonUtility.setStudentAdmissionNo(getActivity(), item.getStudentAdmissionNo());
        CPCAppCommonUtility.setStudentCourse(getActivity(), item.getCourseName());
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        showProgressbar(false);
    }


    @Override
    public void onResponse(IPCDataModel response) {
        showProgressbar(false);
        if (response instanceof CPCStudents) {
            CPCStudents students = (CPCStudents) response;
            setStudentsAdapter(students);
        }
    }

    private void showProgressbar(boolean visibility) {
        if (getActivity() == null || isDetached()) return;
        if (progressBar != null) {
            if (visibility)
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);
        }
    }

    // Define the listener interface
    public interface OnStudentsDialogClickListener {
        void onStudentSelected(CPCStudentDetails mSelectedStudentItem);

    }

    public FPCStudentsDialogFragment() {

    }

    public void setDialogListener(OnStudentsDialogClickListener dialogClickListener) {
        onStudentsDialogClickListener = dialogClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_students_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        mChangeStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() == null && isDetached()) return;
                CPCStudentDetails student = (CPCStudentDetails) CPCAppCommonUtility.readObject(getActivity(), CPCAppConstants.KEY_SELECTED_STUDENT);
                if (student == null) {
                    mSelectedStudent.setText(R.string.select_student);
                    mSelectedStudent.setTextColor(getActivity().getResources().getColor(R.color.red));
//                    Toast.makeText(getActivity(),"Select student!!",Toast.LENGTH_SHORT).show();
                } else {
                    dismiss();
                    onStudentsDialogClickListener.onStudentSelected(mSelectedStudentItem);
                }
//                restartApplication();
            }
        });
        mStudentsRecyclerView = (RecyclerView) view.findViewById(R.id.students_recycler_view);
        mStudentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mStudentsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mStudentRefereshImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStudentsList();
            }
        });
        int height = mGridUnit * 14;
        getDialog().getWindow().setLayout(GridLayout.LayoutParams.MATCH_PARENT, height);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getStudentsList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getStudentsList() {
        if (getActivity() != null && !isDetached()) {
            CPCStudentDetails student = (CPCStudentDetails) CPCAppCommonUtility.readObject(getActivity(), CPCAppConstants.KEY_SELECTED_STUDENT);
            if (student != null)
                mSelectedStudent.setText(student.getFullName());

            if (CPCAppCommonUtility.isNetworkAvailable(getActivity().getApplicationContext())) {
                showProgressbar(true);
                String parentId = CPCAppCommonUtility.getUserId(getActivity());
                Map<String, String> headers = new HashMap<>();
                headers.put(CPCAppConstants.KEY_HEADER_API_KEY, BuildConfig.API_KEY);
                headers.put(CPCAppConstants.KEY_AUTHORIZATION, CPCAppCommonUtility.getAccessToken(getActivity()));
                headers.put("client_unique_key", "");
                //String getStudentsAPI;// = "https://dl.dropboxusercontent.com/s/cwtqdsveazyarbw/students.json?dl=0";
                String getStudentsAPI = CPCAppConstants.KEY_BASE_URL + "/parent/" + parentId + "/students";
                CPCVolley.getRequestQueue(getActivity().getApplicationContext()).add(
                        new CPCGsonGetRequest(getStudentsAPI, this, this,
                                new CPCStudents(), headers));

            } else {
                if (getActivity() == null || isDetached()) return;
                CustomDialog.showAlert(getActivity(), getString(R.string.no_internet_connection), getString(R.string.no_internet_connection));
            }
        }
    }

    private void setStudentsAdapter(CPCStudents data) {
        mStudentsList.clear();
        if (getActivity() == null || isDetached()) return;
        if (data != null && data.getData() != null && data.getData().size() > 0) {
            for (int index = 0; index < data.getData().size(); index++) {
                mStudentsList.add(data.getData().get(index));

            }
            mNoStudentsLyt.setVisibility(View.GONE);
            mStudentsRecyclerView.setVisibility(View.VISIBLE);
            mStudentsAdapter = new CPCStudentsAdapter(getActivity(), mStudentsList, this);
            mStudentsRecyclerView.setAdapter(mStudentsAdapter);
        } else {
            mNoStudentsLyt.setVisibility(View.VISIBLE);
            mStudentsRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mGridUnit = CPCAppCommonUtility.getScreenGridUnitBy16(getActivity());
        getDialog().getWindow().setLayout(GridLayout.LayoutParams.MATCH_PARENT, mGridUnit * 14);
    }
}

