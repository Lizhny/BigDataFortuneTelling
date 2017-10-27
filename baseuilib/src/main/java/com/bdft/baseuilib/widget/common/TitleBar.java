package com.bdft.baseuilib.widget.common;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdft.baselibrary.base.AppContext;
import com.bdft.baselibrary.utils.ui.UIUtils;
import com.bdft.baseuilib.R;
import com.socks.library.KLog;

import org.w3c.dom.Text;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ${}
 * Created by spark_lizhy on 2017/10/26.
 */

public class TitleBar extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {
    private LinearLayout ll_left;
    private ImageView iv_left;
    private TextView tv_left;
    private LinearLayout ll_right;
    private ImageView iv_right;
    private TextView tv_right;
    private TextView title;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_LEFT, MODE_RIGHT, MODE_BOTH, MODE_TITLE})
    public @interface TitleBarMode {

    }

    public static final int MODE_LEFT = 0x11;
    public static final int MODE_RIGHT = 0x12;
    public static final int MODE_BOTH = 0x13;
    public static final int MODE_TITLE = 0x14;

    private int currentMode = MODE_LEFT;

    public TitleBar(@NonNull Context context) {
        this(context, null);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_title_bar, this);
        ll_left = (LinearLayout) findViewById(R.id.ll_title_bar_left);
        iv_left = (ImageView) findViewById(R.id.ic_title_bar_left);
        tv_left = (TextView) findViewById(R.id.tx_title_bar_left);

        ll_right = (LinearLayout) findViewById(R.id.ll_title_bar_right);
        iv_right = (ImageView) findViewById(R.id.ic_title_bar_right);
        tv_right = (TextView) findViewById(R.id.tx_title_bar_right);

        title = (TextView) findViewById(R.id.tx_title);
        this.setBackgroundColor(UIUtils.INSTANCE.getResourceColor(R.color.action_bar_bg));

        ll_left.setOnClickListener(this);
    }

    public int getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(@TitleBarMode int currentMode) {
        if (this.currentMode == currentMode) return;
        this.currentMode = currentMode;
        switch (currentMode) {
            case MODE_LEFT:
                ll_left.setVisibility(VISIBLE);
                ll_left.setOnClickListener(this);
                ll_left.setOnLongClickListener(this);
                ll_right.setOnClickListener(null);
                ll_right.setOnLongClickListener(null);
                ll_right.setVisibility(INVISIBLE);
                break;
            case MODE_RIGHT:
                ll_left.setOnClickListener(null);
                ll_left.setOnLongClickListener(null);
                ll_left.setVisibility(INVISIBLE);
                ll_right.setVisibility(VISIBLE);
                ll_right.setOnClickListener(this);
                ll_right.setOnLongClickListener(this);
                break;
            case MODE_BOTH:
                ll_left.setVisibility(VISIBLE);
                ll_left.setOnLongClickListener(this);
                ll_right.setVisibility(VISIBLE);
                ll_right.setOnLongClickListener(this);
//                ViewUtil.setViewsClickListener(this, ll_left, ll_right);
                break;
            case MODE_TITLE:
                ll_left.setVisibility(GONE);
                ll_left.setOnClickListener(null);
                ll_left.setOnLongClickListener(null);
                ll_right.setVisibility(GONE);
                ll_right.setOnClickListener(null);
                ll_right.setOnLongClickListener(null);
                break;
            default:
                break;
        }
    }

    public void setTitleBarBackground(@ColorRes int colorRes) {
        if (colorRes != -1) {
            this.setBackgroundColor(UIUtils.INSTANCE.getResourceColor(colorRes));
        }
    }

    public TextView getTitleView() {
        return title;
    }

    public void setTitle(String titleStr) {
        if (TextUtils.isEmpty(titleStr)) {
            title.setText("");
        } else {
            title.setText(titleStr);
        }
    }

    public void setTitle(@StringRes int stringResId) {
        if (stringResId > 0) {
            title.setText("");
        }
    }

    public void setLeftIcon(int resId) {
        try {
            iv_left.setImageResource(resId);
            setShowLeftIcon(resId != 0);
        } catch (Exception e) {
            KLog.e(e);
        }
    }

    public void setRightIcon(int resId) {
        try {
            iv_right.setImageResource(resId);
            setShowRightIcon(resId != 0);
        } catch (Exception e) {
            KLog.e(e);
        }
    }

    public void setLeftText(String leftText) {
        if (TextUtils.isEmpty(leftText)) {
            tv_left.setText("");
        } else {
            tv_left.setText(leftText);
        }
    }

    public void setLeftText(@StringRes int resId) {
        if (resId > 0) {
            tv_left.setText(resId);
        }
    }

    public void setRightText(String rightText) {
        if (TextUtils.isEmpty(rightText)) {
            tv_right.setText("");
        } else {
            tv_right.setText(rightText);
        }
    }

    public void setRightText(@StringRes int resId) {
        if (resId > 0) {
            tv_right.setText(resId);
        }
    }

    public void setLeftTextColor(@ColorRes int resId) {
        tv_left.setTextColor(UIUtils.INSTANCE.getResourceColor(resId));
    }

    public void setRigTextColor(@ColorRes int resId) {
        tv_left.setTextColor(UIUtils.INSTANCE.getResourceColor(resId));
    }


    public void setShowLeftIcon(boolean isShow) {
        iv_left.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setShowRightIcon(boolean isShow) {
        iv_right.setVisibility(isShow ? VISIBLE : GONE);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
