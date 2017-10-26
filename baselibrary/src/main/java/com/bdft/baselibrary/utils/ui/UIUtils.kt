package com.bdft.baselibrary.utils.ui

import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorRes
import android.support.annotation.IdRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.bdft.baselibrary.base.AppContext
import com.socks.library.KLog

/**
 * ${}
 * Created by spark_lizhy on 2017/10/26.
 */

object UIUtils {

    fun dip2Px(dip: Float): Int =
            (dip * AppContext.getResources().displayMetrics.density + 0.5f).toInt()

    fun px2Dip(px: Float): Int =
            (px / AppContext.getResources().displayMetrics.density + 0.5f).toInt()

    fun sp2Px(sp: Float): Int =
            (sp * AppContext.getResources().displayMetrics.scaledDensity + 0.5f).toInt()

    fun getScreenWidthPix(context: Context): Int =
            context.resources.displayMetrics.widthPixels

    fun getScreenHeightPix(context: Context): Int =
            context.resources.displayMetrics.heightPixels

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) result = context.resources.getDimensionPixelSize(resourceId)
        return result
    }

    fun hideInputMethod(view: View) {
        try {
            val imm: InputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
            KLog.e(e)
        }
    }

    fun showInputMethod(view: View) {
        (view as? EditText)?.requestFocus()
        val imm: InputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val success = imm.showSoftInput(view, 0)
        KLog.i("showSoftKeyboard", " isSuccess   >>>   " + success)
    }

    fun showInputMethod(context: Context) {
        val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 多少时间后显示软键盘
     */
    fun showInputMethod(view: View, delayMillis: Long) {
        view.postDelayed(Runnable { }, delayMillis)
    }

    fun ToastMessage(msg: String) {
        Toast.makeText(AppContext.getAppContext(), msg, Toast.LENGTH_LONG).show()
    }

    fun getResourceColor(@ColorRes colorResId: Int): Int {
        return try {
            ContextCompat.getColor(AppContext.getAppContext(), colorResId)
        } catch (e: Exception) {
            Color.TRANSPARENT
        }
    }
}