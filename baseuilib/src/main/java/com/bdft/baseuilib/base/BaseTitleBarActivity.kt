package com.bdft.baseuilib.base

import android.graphics.Color
import android.support.annotation.*
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.bdft.baselibrary.`interface`.OnDoubleClickListener
import com.bdft.baselibrary.base.BaseActivity
import com.bdft.baseuilib.R
import com.bdft.baseuilib.widget.common.TitleBar

/**
 * ${拥有titlebar的activity基类}
 * Created by spark_lizhy on 2017/10/25.
 */
abstract class BaseTitleBarActivity : BaseActivity() {
    protected var titleBar: TitleBar? = null

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        initTitlebar()
    }


    override fun setContentView(view: View?) {
        super.setContentView(view)
        initTitlebar()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        initTitlebar()
    }

    public fun setTitleText(@StringRes resId: Int) {
        if (titleBar != null && resId != 0) {
            titleBar?.setTitle(resId)
        }
    }

    public fun getTitleText() {
        titleBar?.titleView?.text.toString()
    }


    fun setTitleMode(@TitleBar.TitleBarMode mode: Int) {
        if (titleBar != null) {
            titleBar?.currentMode=mode
        }
    }

    public fun setTitleText(text: String) {
        if (titleBar != null && !TextUtils.isEmpty(text)) {
            titleBar?.setTitle(text)
        }
    }

    public fun setTitleLeftText(@StringRes resId: Int) {
        if (titleBar != null && resId != 0) {
            titleBar?.setLeftText(resId)
        }
    }

    public fun setTitleLeftText(text: String) {
        if (titleBar != null && !TextUtils.isEmpty(text)) {
            titleBar?.setLeftText(text)
        }
    }

    public fun setTitleRightText(@StringRes resId: Int) {
        if (titleBar != null && resId != 0) {
            titleBar?.setRightText(resId)
        }
    }

    public fun setTitleRightText(text: String) {
        if (titleBar != null && !TextUtils.isEmpty(text)) {
            titleBar?.setRightText(text)
        }
    }

    public fun setLeftTextColor(@ColorRes resId: Int) {
        if (titleBar != null && resId != 0) {
            titleBar?.setLeftTextColor(resId)
        }
    }

    public fun setRightTextColor(@ColorRes resId: Int) {
        if (titleBar != null && resId != 0) {
            titleBar?.setRigTextColor(resId)
        }
    }

    public fun setTitleLeftIcon(@DrawableRes resId: Int) {
        if (titleBar != null) {
            titleBar?.setLeftIcon(resId)
        }
    }

    public fun setTitleRightIcon(@DrawableRes resId: Int) {
        if (titleBar != null) {
            titleBar?.setRightIcon(resId)
        }
    }

    public fun setTitleBackground(@ColorRes resId: Int) {
        if (titleBar != null) {
            titleBar?.setTitleBarBackground(resId)
        }
    }

    public fun onTitleRightLongClick() {
    }

    public fun onTitleLeftLongClick() {
    }

    public fun onTitleRightClick() {
    }

    public fun onTitleLeftClick() {
        finish()
    }

    public fun onTitleDoubleClick() {
    }

    public fun onTitleSingleClick() {
    }

    private fun initTitlebar() {
        titleBar = findView(R.id.title_bar_view)

        if (titleBar != null) {
            titleBar?.setOnClickListener(object : OnDoubleClickListener() {
                override fun onSingleClick() {
                    onTitleSingleClick()
                }

                override fun onDoubleClick(view: View) {
                    onTitleDoubleClick()
                }

            })
            titleBar?.onTitleBarClickListener = mTitleBarClickListener
        }
    }

    private var mTitleBarClickListener = object : TitleBar.OnTitleBarClickListener {

        override fun onLeftClick(v: View?, isLongClick: Boolean): Boolean {
            if (isLongClick)
                onTitleLeftLongClick()
            else
                onTitleLeftClick()
            return false
        }

        override fun onRightClick(v: View?, isLongClick: Boolean): Boolean {
            if (isLongClick)
                onTitleRightLongClick()
            else
                onTitleRightClick()
            return false
        }
    }
}