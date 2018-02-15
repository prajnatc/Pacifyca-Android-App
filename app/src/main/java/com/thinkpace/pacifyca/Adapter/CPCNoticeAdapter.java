package com.thinkpace.pacifyca.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.entity.CPCNoticeList;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.utils.CPCAppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by prajna on 25-10-2017.
 */

public class CPCNoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<CPCNoticeList> mListItems;
    int mGridUnit;

    public CPCNoticeAdapter(Context context, ArrayList<CPCNoticeList> mArrayList) {
        this.mContext = context;
        this.mListItems = mArrayList;
        mGridUnit = CPCAppCommonUtility.getScreenGridUnitBy16(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_content_layout, parent, false);
        return new CPCNoticeAdapter.NoticeItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CPCNoticeAdapter.NoticeItemViewHolder mItemViewHolder = (CPCNoticeAdapter.NoticeItemViewHolder) holder;
        final CPCNoticeList noticeDetail = mListItems.get(position);
        if (noticeDetail != null) {



            mItemViewHolder.mNoticeDate.setText(noticeDetail.geNoticeddate());
            mItemViewHolder.mNoticeTitle.setText(noticeDetail.getTitle());
//            mItemViewHolder.mnoticeDesc.setText(noticeDetail.getAssigneeName());
            mItemViewHolder.mItemsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticeClickListener.onNoticeItemClick(position, noticeDetail);
                }
            });

            if (noticeDetail.isRead()) {
                mItemViewHolder.mNoticeTitle.setTypeface(null, Typeface.NORMAL);
                mItemViewHolder.mnoticeDesc.setTypeface(null, Typeface.NORMAL);
            } else {
                mItemViewHolder.mNoticeTitle.setTypeface(null, Typeface.BOLD);
                mItemViewHolder.mnoticeDesc.setTypeface(null, Typeface.BOLD);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (mListItems != null)
            return mListItems.size();
        else
            return 0;
    }


    // Define listener member variable
    private OnNoticeClickListener noticeClickListener;


    // Define the listener interface
    public interface OnNoticeClickListener {
        void onNoticeItemClick(int position, CPCNoticeList noticeDetail);

    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnNoticeClickListener(OnNoticeClickListener listener) {
        this.noticeClickListener = listener;
    }

    public void updateList(ArrayList<CPCNoticeList> noticeList) {
        this.mListItems = noticeList;
        notifyDataSetChanged();
    }

    class NoticeItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notice_name)
        TextView mNoticeTitle;

        @BindView(R.id.notice_description)
        TextView mnoticeDesc;

        @BindView(R.id.notice_date)
        TextView mNoticeDate;

       /* @BindView(R.id.message_time)
        TextView mMessageTime;*/

        @BindView(R.id.notice_item_card)
        CardView mNoticeCard;

        @BindView(R.id.notice_list_item_lyt)
        RelativeLayout mItemsLayout;


        public NoticeItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mNoticeCard.getLayoutParams().height = mGridUnit * 4;
            mItemsLayout.getLayoutParams().height = mGridUnit * 4;
        }
    }
}
