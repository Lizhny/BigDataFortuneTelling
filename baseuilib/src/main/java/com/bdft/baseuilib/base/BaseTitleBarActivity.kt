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

    fun setTitleText(@StringRes resId: Int) {
        if (titleBar != null && resId != 0) {
            titleBar?.setTitle(resId)
        }
    }

    fun getTitleText() {
        titleBar?.titleView?.text.toString()
    }

    fun setTitleMode(@TitleBar.TitleBarMode mode: Int) {
        if (titleBar != null) {
            titleBar?.currentMode = mode
        }
    }

    fun setTitleText(text: String) {
        if (titleBar != null && !TextUtils.isEmpty(text)) {
            titleBar?.setTitle(text)
        }
    }

    fun setTitleLeftText(@StringRes resId: Int) {
        if (titleBar != null && resId != 0) {
            titleBar?.setLeftText(resId)
        }
    }

    fun setTitleLeftText(text: String) {
        if (titleBar != null && !TextUtils.isEmpty(text)) {
            titleBar?.setLeftText(text)
        }
    }

    fun setTitleRightText(@StringRes resId: Int) {
        if (titleBar != null && resId != 0) {
            titleBar?.setRightText(resId)
        }
    }

    fun setTitleRightText(text: String) {
        if (titleBar != null && !TextUtils.isEmpty(text)) {
            titleBar?.setRightText(text)
        }
    }

    fun setLeftTextColor(@ColorRes resId: Int) {
        if (titleBar != null && resId != 0) {
            titleBar?.setLeftTextColor(resId)
        }
    }

    fun setRightTextColor(@ColorRes resId: Int) {
        if (titleBar != null && resId != 0) {
            titleBar?.setRigTextColor(resId)
        }
    }

    fun setTitleTextColor(@ColorRes resId: Int) {
        if (titleBar != null && resId != 0) {
            titleBar?.setTitleTextColor(resId)
        }
    }

    fun setTitleLeftIcon(@DrawableRes resId: Int) {
        if (titleBar != null) {
            titleBar?.setLeftIcon(resId)
        }
    }

    fun setTitleRightIcon(@DrawableRes resId: Int) {
        if (titleBar != null) {
            titleBar?.setRightIcon(resId)
        }
    }

    fun setTitleBackground(@ColorRes resId: Int) {
        if (titleBar != null) {
            titleBar?.setTitleBarBackground(resId)
        }
    }

    fun onTitleRightLongClick() {
    }

    fun onTitleLeftLongClick() {
    }

    fun onTitleRightClick() {
    }

    fun onTitleLeftClick() {
        finish()
    }

    fun onTitleDoubleClick() {
    }

    fun onTitleSingleClick() {
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