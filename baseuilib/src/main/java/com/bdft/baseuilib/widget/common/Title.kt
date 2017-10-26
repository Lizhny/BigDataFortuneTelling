package com.bdft.baseuilib.widget.common

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

/**
 * ${}
 * Created by spark_lizhy on 2017/10/25.
 */

class Title @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener, View.OnLongClickListener {

    override fun onClick(v: View) {

    }

    override fun onLongClick(v: View): Boolean {
        return false
    }
}
