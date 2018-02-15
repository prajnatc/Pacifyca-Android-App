package com.thinkpace.pacifyca.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkpace.pacifyca.R;
import com.thinkpace.pacifyca.entity.CPCMessageDetails;
import com.thinkpace.pacifyca.utils.CPCAppCommonUtility;
import com.thinkpace.pacifyca.widget.RoboTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

import static com.thinkpace.pacifyca.utils.CPCAppCommonUtility.getElapsedTime;

/**
 * Created by sapna on 30-11-2016.
 */

public class CPCUserMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<CPCMessageDetails> mListItems;
    int mGridUnit;

    public CPCUserMessagesAdapter(Context context, ArrayList<CPCMessageDetails> mArrayList) {
        this.mContext = context;
        this.mListItems = mArrayList;
        mGridUnit = CPCAppCommonUtility.getScreenGridUnitBy16(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_content_layout, parent, false);
        return new CPCUserMessagesAdapter.MessagesItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CPCUserMessagesAdapter.MessagesItemViewHolder mItemViewHolder = (CPCUserMessagesAdapter.MessagesItemViewHolder) holder;
        final CPCMessageDetails messageDetail = mListItems.get(position);
        if (messageDetail != null) {
            mItemViewHolder.mNameTextView.setText(messageDetail.getInstitutionName());
            mItemViewHolder.mMessageTextView.setText(messageDetail.getMessage_content() + " - " + messageDetail.getId());
        }

        if (messageDetail.isFavourite()) {
            mItemViewHolder.mFavouriteIcon.setImageResource(R.drawable.start_selected);
        } else {
            mItemViewHolder.mFavouriteIcon.setImageResource(R.drawable.star_gray);
        }

        if (messageDetail.isRead()) {
            mItemViewHolder.mNameTextView.setTypeface(null, Typeface.NORMAL);
            mItemViewHolder.mMessageTextView.setTypeface(null, Typeface.NORMAL);
        } else {
            mItemViewHolder.mNameTextView.setTypeface(null, Typeface.BOLD);
            mItemViewHolder.mMessageTextView.setTypeface(null, Typeface.BOLD);
        }


        mItemViewHolder.mFavouriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesClickListener.onFavouritesClick(position, messageDetail);
            }
        });

        mItemViewHolder.mDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesClickListener.onDeleteMessageClick(position, messageDetail);
            }
        });

        mItemViewHolder.mItemsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesClickListener.onMessageItemClick(position, messageDetail);
            }
        });
        mItemViewHolder.mMessageTime.setText(getMessageTime(messageDetail.getMessageTime()));
    }

    private String getMessageTime(String messageTime) {
        String time = null;
        try {
            SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm");
            Date date = parseFormat.parse(messageTime);
            time = getElapsedTime(messageTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    @Override
    public int getItemCount() {
        if (mListItems != null) {
            return mListItems.size();
        }
        return 0;
    }

    // Define listener member variable
    private OnMessagesClickListener messagesClickListener;

    public void updateAdapter(ArrayList<CPCMessageDetails> mStudentMessagesList) {
        mListItems = mStudentMessagesList;
        notifyDataSetChanged();
    }

    // Define the listener interface
    public interface OnMessagesClickListener {
        void onMessageItemClick(int position, CPCMessageDetails messageDetails);

        void onFavouritesClick(int position, CPCMessageDetails messageDetails);

        void onDeleteMessageClick(int position, CPCMessageDetails messageDetails);

    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnMessagesClickListener(CPCUserMessagesAdapter.OnMessagesClickListener listener) {
        this.messagesClickListener = listener;
    }

    class MessagesItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.message_student_name)
        TextView mNameTextView;

        @BindView(R.id.message_content)
        TextView mMessageTextView;

        @BindView(R.id.message_time)
        RoboTextView mMessageTime;

        @BindView(R.id.message_content_lyt)
        RelativeLayout mItemsLayout;

        @BindView(R.id.message_item_lyt)
        RelativeLayout mMessageItemLyt;

        @BindView(R.id.favourite_icon)
        ImageView mFavouriteIcon;

        @BindView(R.id.delete_icon)
        ImageView mDeleteIcon;

        public MessagesItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mItemsLayout.getLayoutParams().height = mGridUnit * 4;
            mMessageItemLyt.getLayoutParams().height = mGridUnit * 3;

        }
    }
}
