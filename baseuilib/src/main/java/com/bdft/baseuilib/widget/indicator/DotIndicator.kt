package com.bdft.baseuilib.widget.indicator

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.bdft.baselibrary.utils.ui.UIUtils
import com.socks.library.KLog

/**
 * ${}
 * Created by spark_lizhy on 2017/10/27.
 */
class DotIndicator @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val TAG = "DotIndicator"
    val DEAFULT_DOT_NUM = 0
    val MAX_DOT_NUM = 9
    val DOT_SIZE = 8

    var mCurrentSelection = 0
    var mDotNum = DEAFULT_DOT_NUM

    val mDotViews: ArrayList<DotView>
        get() = arrayListOf()

    fun init(context: Context, dotNum: Int): DotIndicator {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        mDotNum = if (dotNum == 0) {
            DEAFULT_DOT_NUM
        } else {
            dotNum
        }
        buildDotView(context)
        visibility = if (dotNum < 1) {
            View.GONE
        } else {
            View.VISIBLE
        }
        setCurrentSelection(0)
        return this
    }

    private fun buildDotView(context: Context) {
        for (i in 0..mDotNum) {
            val dotView = DotView(context)
            dotView.isSelected = false
            val params = LayoutParams(UIUtils.px2Dip(DOT_SIZE.toFloat()), UIUtils.px2Dip(DOT_SIZE.toFloat()))
            if (i == 0) {
                params.leftMargin = 0
            } else {
                params.leftMargin = UIUtils.dip2PxToInt(6f)
            }
            addView(dotView, params)
            mDotViews.add(dotView)
        }
    }

    private fun getCurrentSelecttion(): Int = mCurrentSelection

    public fun setCurrentSelection(selection: Int) {
        mCurrentSelection = selection
        for (dotView: DotView in mDotViews) {
            dotView.isSelected = false
        }
        if (selection >= 0 && selection < mDotViews.size) {
            mDotViews[selection].isSelected = true
        } else {
            KLog.e(TAG, "the selection can not over mDotViews size")
        }

    }

    public fun getDotNum(): Int = mDotNum

    fun setDotViewNum(num: Int) {
        if (num < 1 || num > MAX_DOT_NUM) {
            KLog.e(TAG, "num必须在1~" + MAX_DOT_NUM + "之间哦")
        }

        if (num <= 1) {
            removeAllViews()
            visibility = View.GONE
        }

        if (mDotNum != num) {
            mDotNum = num
            removeAllViews()
            mDotViews.clear()
            buildDotView(context)
            setCurrentSelection(0)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mDotViews.clear()
        KLog.i(TAG, "清除DotIndicator引用")
    }

}