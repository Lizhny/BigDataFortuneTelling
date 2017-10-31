package com.bdft.baselibrary.utils.ui

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import com.socks.library.KLog
import java.util.*

/**
 * ${}
 * Created by spark_lizhy on 2017/10/31.
 */
object ViewUtil {
    fun setViewsClickListener(listener: View.OnClickListener, vararg views: View) {
        for (view in views) {
            view.setOnClickListener { listener }
        }
    }

    fun setViewsVisible(visible: Int, vararg views: View) {
        for (view in views) {
            view.visibility = visible
        }
    }

    fun setViewsEnable(enable: Boolean, vararg views: View) {
        for (view in views) {
            view.isEnabled = enable
        }
    }

    fun setViewsEnableAndClickble(enable: Boolean, clickable: Boolean, vararg views: View) {
        for (view in views) {
            view.isEnabled = enable
            view.isClickable = clickable
        }
    }

    /**
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围（ABSListView无效）
     * 延迟300毫秒执行，使该方法可以在onCreate里面使用
     */
    fun expandViewTouchDelegate(view: View?, left: Int, top: Int, right: Int, bottom: Int) {
        if (view == null) return
        val parent: View? = view.parent as View
        parent?.postDelayed({
            val bounds = Rect()
            view.isEnabled = true
            view.getHitRect(bounds)
            bounds.left -= left
            bounds.top -= top
            bounds.right += right
            bounds.bottom += bottom

            KLog.d("bouns", "rect - top" + bounds.top + "  - right" + bounds.right)
            val touchDelegate = TouchDelegate(bounds, view)
            if (view.parent is View) {
                parent.touchDelegate = touchDelegate
            }
        }, 300)
    }

    fun restoreViewTouchDelegate(view: View?) {
        if (view == null) return
        val parent: View? = view.parent as View

        parent?.postDelayed({
            val bounds = Rect()
            bounds.setEmpty()
            val touchDelegate = TouchDelegate(bounds, view)
            if (view.parent is View) {
                parent.touchDelegate = touchDelegate
            }
        }, 300)

    }
}