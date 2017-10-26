package com.future.telling.listener

import android.view.View

import com.bdft.baselibrary.thirdpart.WeakHandler
import com.socks.library.KLog

/**
 * ${This class definition for a callback to be invoked when a view is double clicked.}
 * Created by spark_lizhy on 2017/10/24.
 */

abstract class OnDoubleClickListener : View.OnClickListener {
    private var isDouble = false
    private val handler = WeakHandler()

    private val runnable = object : Runnable {
        override fun run() {
            KLog.d("=====", "单击" + isDouble)
            isDouble = false
            handler.removeCallbacks(this)
        }
    }

    override fun onClick(v: View) {
        if (isDouble) {
            isDouble = false
            handler.removeCallbacks(runnable)
            onDoubleClick(v)
            KLog.d("=====", "双击" + isDouble)
        } else {
            isDouble = true
            handler.postDelayed(runnable, DelayedTime.toLong())
        }
    }

    abstract fun onDoubleClick(view: View)

    companion object {
        private val DelayedTime = 300
    }
}
