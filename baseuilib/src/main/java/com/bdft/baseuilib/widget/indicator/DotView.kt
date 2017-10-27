package com.bdft.baseuilib.widget.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.bdft.baseuilib.R

/**
 * ${}
 * Created by spark_lizhy on 2017/10/27.
 */
class DotView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val TAG = "DotView"

    var mDotNormal: Drawable = context.getDrawable(R.drawable.ic_viewpager_dot_indicator_normal)
    var mDotSelected: Drawable = context.getDrawable(R.drawable.ic_viewpager_dot_indicator_selected)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isSelected) {
            mDotSelected.bounds.set(0, 0, width, height)
            mDotSelected.draw(canvas)
        } else {
            mDotNormal.bounds.set(0, 0, width, height)
            mDotNormal.draw(canvas)
        }
    }
}