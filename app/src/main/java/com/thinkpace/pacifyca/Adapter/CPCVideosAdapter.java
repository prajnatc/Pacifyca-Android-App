package com.thinkpace.pacifyca.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.entity.CPCAssignmentList;
import com.thinkpace.pacifyca.entity.CPCVideoList;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sapna on 15-03-2017.
 */

public class CPCVideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<CPCVideoList> mListItems;
    int mGridUnit;

    public CPCVideosAdapter(Context context, ArrayList<CPCVideoList> mArrayList) {
        this.mContext = context;
        this.mListItems = mArrayList;
        mGridUnit = CPCAppCommonUtility.getScreenGridUnitBy16(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_content_layout, parent, false);
        return new CPCVideosAdapter.VideosItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CPCVideosAdapter.VideosItemViewHolder mItemViewHolder = (CPCVideosAdapter.VideosItemViewHolder) holder;
        final CPCVideoList videoDetail = mListItems.get(position);
        if (videoDetail != null) {
            mItemViewHolder.mWebViewVideo.loadData(videoDetail.getVideoLink(), "text/html; video/mpeg", "UTF-8");
            mItemViewHolder.mVideoTitle.setText(videoDetail.getTitle().concat(" - ").concat(videoDetail.getSentDate()));
//            mItemViewHolder.mAssignmentTitle.setText(assignmentDetail.getTitle());
//            mItemViewHolder.mAssignmentDesc.setText(assignmentDetail.getAssigneeName());
//            mItemViewHolder.mItemsLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    assignmentClickListener.onAssignmentItemClick(position, assignmentDetail);
//                }
//            });
//
//            if (assignmentDetail.isRead()) {
//                mItemViewHolder.mAssignmentTitle.setTypeface(null, Typeface.NORMAL);
//                mItemViewHolder.mAssignmentDesc.setTypeface(null, Typeface.NORMAL);
//            } else {
//                mItemViewHolder.mAssignmentTitle.setTypeface(null, Typeface.BOLD);
//                mItemViewHolder.mAssignmentDesc.setTypeface(null, Typeface.BOLD);
//            }
        }

    }

    @Override
    public int getItemCount() {
        if (mListItems != null)
            return mListItems.size();
        else
            return 0;
    }

//
//    // Define listener member variable
//    private OnAssignmentClickListener assignmentClickListener;
//
//
//    // Define the listener interface
//    public interface OnAssignmentClickListener {
//        void onAssignmentItemClick(int position, CPCAssignmentList assignmentDetail);
//
//    }
//
//    // Define the method that allows the parent activity or fragment to define the listener
//    public void setOnAssignmentClickListener(OnAssignmentClickListener listener) {
//        this.assignmentClickListener = listener;
//    }
//
//    public void updateList(ArrayList<CPCAssignmentList> assignmentList) {
//        this.mListItems = assignmentList;
//        notifyDataSetChanged();
//    }

    class VideosItemViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.assignment_name)
//        TextView mAssignmentTitle;
//
//        @BindView(R.id.assignment_description)
//        TextView mAssignmentDesc;

        @BindView(R.id.wv_video)
        WebView mWebViewVideo;

        @BindView(R.id.tv_video_title)
        TextView mVideoTitle;

       /* @BindView(R.id.message_time)
        TextView mMessageTime;*/

        @BindView(R.id.video_item_card)
        CardView mVideoCard;

        @BindView(R.id.video_list_item_lyt)
        RelativeLayout mItemsLayout;


        public VideosItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mVideoCard.getLayoutParams().height = mGridUnit * 14;
            mItemsLayout.getLayoutParams().height = mGridUnit * 14;
        }
    }
}
