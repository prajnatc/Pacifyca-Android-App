
package com.thinkpace.pacifyca.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.thinkpace.pacifyca.R;

public class RoboTextView extends TextView {

    public RoboTextView(Context context) {
        super(context);
        init();
    }

    public RoboTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttribute(context, attrs);
    }

    public RoboTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAttribute(context, attrs);
    }

    private void setAttribute(Context context, AttributeSet attrs) {
        int font;
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.RoboTextStyle);
        try {
            font = ta.getInteger(R.styleable.RoboTextStyle_fontType, 0);
        } finally {
            ta.recycle();
        }
        if (font != 0) {
            switch (font) {

                case 1:
                    if (getOSVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/OpenSans-Bold.ttf"));
                    } else {
                        setTypeface(null, Typeface.NORMAL);
                    }
                    break;

                case 2:
                    if (getOSVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/OpenSans-Light.ttf"));
                    } else {
                        setTypeface(null, Typeface.NORMAL);
                    }
                    break;

                case 3:
                    if (getOSVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/OpenSans-Regular.ttf"));
                    } else {
                        setTypeface(null, Typeface.BOLD);
                    }
                    break;

                case 4:
                    if (getOSVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/OpenSans-Semibold.ttf"));
                    } else {
                        setTypeface(null, Typeface.BOLD);
                    }
                    break;
            }
        }
    }

    private void init() {
        if (!isInEditMode()) {
            try {
                if (getOSVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/OpenSans-Bold.ttf.ttf"));
                } else {
                    setTypeface(null, Typeface.BOLD);
                }
            } catch (Exception e) {
            }
        }
    }

    public static int getOSVersion() {
        return Build.VERSION.SDK_INT;
    }
}

