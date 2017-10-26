package com.bdft.baseuilib.widget.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.lang.reflect.Constructor
import java.util.jar.Attributes

/**
 * ${}
 * Created by spark_lizhy on 2017/10/25.
 */
class TitleBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener, View.OnLongClickListener {

    lateinit var ll_left: LinearLayout
    lateinit var tv_left: TextView
    lateinit var iv_left: ImageView
    lateinit var tv_title: TextView
    lateinit var ll_right: LinearLayout
    lateinit var tv_right: TextView
    lateinit var iv_right: ImageView


    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLongClick(v: View?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}