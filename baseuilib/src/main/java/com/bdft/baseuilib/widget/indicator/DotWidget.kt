package com.bdft.baseuilib.widget.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.Shape
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.bdft.baselibrary.utils.ui.UIUtils
import com.socks.library.KLog

/**
 * ${}
 * Created by spark_lizhy on 2017/10/28.
 */
class DotWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.textViewStyle
) : View(context, attrs, defStyleAttr) {
    private val TAG = "DotWidget"

    private val DEAFULT_DOT_COLOR = Color.RED
    private val DEAFULT_DOT_REDIUS = 10
    private val DEAFULT_DOT_SIZE = 10
    private val DEAFULT_DOT_TEXT_COLOR = Color.WHITE

    private lateinit var mTargetView: View
    private lateinit var mContainer: FrameLayout
    private lateinit var mMarginRect: Rect

    /**
     * 显示模式
     * <p/>
     * GRAVITY_TOP_LEFT 顶部居左
     * <p/>
     * GRAVITY_TOP_RIGHT 顶部居右
     * <p/>
     * GRAVITY_BOTTOM_LEFT 底部居左
     * <p/>
     * GRAVITY_BOTTOM_RIGHT 底部居右
     * <p/>
     * GRAVITY_CENTER 居中
     * <p/>
     * GRAVITY_CENTER_VERTICAL 垂直居中
     * <p/>
     * GRAVITY_CENTER_HORIZONTAL 水平居中
     * <p/>
     * GRAVITY_LEFT_CENTER 左边居中
     * <p/>
     * GRAVITY_RIGHT_CENTER 右边居中
     * <p/>
     */
    public enum class DotGravity {
        GRAVITY_TOP_LEFT, GRAVITY_TOP_RIGHT, GRAVITY_BOTTOM_LEFT, GRAVITY_BOTTOM_RIGHT,
        GRAVITY_CENTER, GRAVITY_CENTER_VERTICAL, GRAVITY_CENTER_HORIZONTAL, GRAVITY_LEFT_CENTER,
        GRAVITY_RIGHT_CENTER
    }

    lateinit private var mDotGravity: DotGravity

    public enum class Mode {
        ROUND_RECT, CIRCLE
    }

    private var mode = Mode.CIRCLE

    private var mDotColor = DEAFULT_DOT_COLOR
    private var mDotSize = DEAFULT_DOT_SIZE
    private var mDotRadius = DEAFULT_DOT_REDIUS
    private var mDotTextColor = DEAFULT_DOT_TEXT_COLOR
    private var mDotTextSize = 0

    private var mDotBackground: ShapeDrawable?=null
    private var mDotText = ""
    private var mIsShowing = false
    private var mNeedReDraw = false
    private lateinit var mFadeIn: AlphaAnimation
    private lateinit var mFadeOut: AlphaAnimation

    constructor(context: Context, target: View) : this(context) {
        initView(target)

    }

    private fun initView(target: View?) {
        if (target == null) return
        mTargetView = target
        mDotGravity = DotGravity.GRAVITY_TOP_RIGHT
        mMarginRect = Rect()
        mIsShowing = false

        buildDefaultAnim()

    }

    private fun buildDefaultAnim() {
        mFadeIn = AlphaAnimation(0f, 1f)
        mFadeIn.interpolator = DecelerateInterpolator()
        mFadeIn.duration = 450
        mFadeIn.setAnimationListener(mAnimListener)

        mFadeOut = AlphaAnimation(0f, 1f)
        mFadeOut.interpolator = DecelerateInterpolator()
        mFadeOut.duration = 450
        mFadeOut.setAnimationListener(mAnimListener)
    }

    private fun addDot(target: View) {
        val targetViewParams = target.layoutParams
        val newTargetViewParams = ViewGroup.LayoutParams(targetViewParams.width, targetViewParams.height)
        targetViewParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        targetViewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        //new 一个容器
        mContainer = FrameLayout(context)
        mContainer.clipToPadding = false

        val parent = target.parent as ViewGroup
        val index = parent.indexOfChild(target)
        //去掉目标View
        parent.removeView(target)
        parent.addView(mContainer, index, targetViewParams)

        //容器添加目标View
        mContainer.addView(target, newTargetViewParams)
        visibility = GONE
        //容器添加DotWidget（红点）
        mContainer.addView(this)
        parent.invalidate()
    }

    public fun show() {
        show(false)
    }

    public fun show(needAnim: Boolean) {
        show(needAnim, mFadeIn)
    }

    private fun show(needAnim: Boolean, animation: Animation?) {
        if (mIsShowing) return
        if (background == null || mNeedReDraw || mDotBackground == null) {
            mDotBackground = getDotBackground()
            background = mDotBackground
        }

        initDotParams(mDotSize)
        mTargetView.visibility = VISIBLE
        if (needAnim) {
            clearAnimation()
            startAnimation(animation)
        } else {
            alpha = 1F
            visibility = VISIBLE
            mIsShowing = true
        }
        mNeedReDraw = false
    }

    public fun hide() {
        hide(false)
    }

    public fun hide(needAnim: Boolean) {
        hide(needAnim, mFadeOut)
    }

    private fun hide(needAnim: Boolean, animation: Animation?) {
        if (!isShown) return
        if (needAnim) {
            clearAnimation()
            startAnimation(animation)
        } else {
            alpha = 0F
            visibility = GONE
            mIsShowing = false
        }
    }

    public fun toggle() {
        toggle(false)
    }

    public fun toggle(needAnim: Boolean) {
        toggle(needAnim, mFadeIn, mFadeOut)
    }

    private fun toggle(needAnim: Boolean, enterAnim: Animation?, exitAnim: Animation?) {
        if (isShown) {
            hide(needAnim && exitAnim != null, exitAnim)
        } else {
            show(needAnim && enterAnim != null, enterAnim)
        }
    }

    public fun setDotMatgin(left: Int, top: Int, right: Int, bottom: Int) {
        val absLeft = Math.abs(UIUtils.dip2Px(left)).toInt()
        val absTop = Math.abs(UIUtils.dip2Px(top)).toInt()
        val absRight = Math.abs(UIUtils.dip2Px(right)).toInt()
        val absBottom = Math.abs(UIUtils.dip2Px(bottom)).toInt()

        when (mDotGravity) {
            DotGravity.GRAVITY_TOP_LEFT -> applyDotMargin(absRight, absTop, 0, 0)
            DotGravity.GRAVITY_TOP_RIGHT -> applyDotMargin(0, absTop, absLeft, 0)
            DotGravity.GRAVITY_BOTTOM_LEFT -> applyDotMargin(absRight, 0, 0, absBottom)
            DotGravity.GRAVITY_BOTTOM_RIGHT -> applyDotMargin(0, 0, absLeft, absBottom)
            DotGravity.GRAVITY_LEFT_CENTER -> applyDotMargin(absRight, 0, 0, 0)
            DotGravity.GRAVITY_RIGHT_CENTER -> applyDotMargin(0, 0, absLeft, 0)
            else -> {
            }
        }
    }

    public fun getDotMargin(): Rect {
        val rect = Rect()
        rect.left = UIUtils.px2Dip(Math.abs(mMarginRect.left)).toInt()
        rect.top = UIUtils.px2Dip(Math.abs(mMarginRect.top)).toInt()
        rect.right = UIUtils.px2Dip(Math.abs(mMarginRect.right)).toInt()
        rect.bottom = UIUtils.px2Dip(Math.abs(mMarginRect.bottom)).toInt()

        KLog.d(TAG, "getDotMargin: \n" + rect.toString())
        return rect
    }

    public fun setDotGravity(dotGravity: DotGravity) {
        this.mDotGravity = dotGravity
    }

    public fun getDotGravity(): DotGravity {
        return mDotGravity
    }

    public fun setMode(mode: Mode) {
        this.mode = mode
    }

    public fun getMode(): Mode {
        return mode
    }

    public fun setDotColor(dotColor: Int) {
        this.mDotColor = dotColor
        mNeedReDraw = true
    }

    public fun getDotColor(): Int {
        return mDotColor
    }

    public fun setDotSize(dotSize: Int) {
        this.mDotSize = dotSize
        mNeedReDraw = true
    }

    public fun getDotSize(): Int {
        return mDotSize
    }

    public fun setDotRadius(dotRadius: Int) {
        this.mDotRadius = dotRadius
        mNeedReDraw = true
    }

    public fun getDotRadius(): Int {
        return mDotRadius
    }

    public fun setDotText(dotText: String) {
        this.mDotText = dotText
        mNeedReDraw = true
    }

    public fun getDotText(): String {
        return mDotText
    }

    public fun setDotTextColor(dotTextColor: Int) {
        this.mDotTextColor = dotTextColor
        mNeedReDraw = true
    }

    public fun getDotTextColor(): Int {
        return mDotTextColor
    }

    public fun setDotTextSize(dotTextSize: Int) {
        this.mDotTextSize = dotTextSize
        mNeedReDraw = true
    }

    public fun getDotTextSize(): Int {
        return mDotTextSize
    }


    /**
     * 更新dot和容器的大小
     * <p/>
     * 原理如下：
     * 当dot在左边，那么源控件就是相对于dot的右边，因此想移动dot一般而言都是设置dotMarginRight，然而容器container已经限制死了大小
     * 因此需要将container进行扩展，使用padding，同时clipToPadding设置为false，使dot可以正常显示。而dot因为其gravity，因此只需要marginLeft为负值即可
     */
    private fun applyDotMargin(left: Int, top: Int, right: Int, bottom: Int) {
        mMarginRect.left = -left
        mMarginRect.top = -top
        mMarginRect.right = -right
        mMarginRect.bottom = -bottom

        mContainer.setPadding(left, top, right, bottom)

        KLog.d(TAG, "applyDotMargin:\n" + mMarginRect.toString())
    }

    public fun getTargetView(): View {
        return mTargetView
    }

    public fun setTargetView(targetView: View) {
        this.mTargetView = targetView
    }


    private fun initDotParams(mDotSize: Int) {
        val dotWeightWidth = UIUtils.dip2Px(mDotSize).toInt()
        val dotParams = FrameLayout.LayoutParams(dotWeightWidth, dotWeightWidth)
        when (mDotGravity) {
            DotGravity.GRAVITY_TOP_LEFT -> dotParams.gravity = Gravity.TOP and Gravity.START
            DotGravity.GRAVITY_TOP_RIGHT -> dotParams.gravity = Gravity.TOP and Gravity.END
            DotGravity.GRAVITY_BOTTOM_LEFT -> dotParams.gravity = Gravity.BOTTOM and Gravity.START
            DotGravity.GRAVITY_BOTTOM_RIGHT -> dotParams.gravity = Gravity.BOTTOM and Gravity.END
            DotGravity.GRAVITY_CENTER -> dotParams.gravity = Gravity.CENTER
            DotGravity.GRAVITY_CENTER_VERTICAL -> dotParams.gravity = Gravity.CENTER_VERTICAL
            DotGravity.GRAVITY_CENTER_HORIZONTAL -> dotParams.gravity = Gravity.CENTER_HORIZONTAL
            DotGravity.GRAVITY_LEFT_CENTER -> dotParams.gravity = Gravity.CENTER_VERTICAL and Gravity.START
            DotGravity.GRAVITY_RIGHT_CENTER -> dotParams.gravity = Gravity.CENTER_VERTICAL and Gravity.END
        }
        dotParams.setMargins(mMarginRect.left, mMarginRect.top, mMarginRect.right, mMarginRect.bottom)
        this.layoutParams = dotParams
    }

    private fun getDotBackground(): ShapeDrawable {
        val drawable: ShapeDrawable
        when (mode) {
            Mode.ROUND_RECT -> {
                val radius = UIUtils.dip2Px(mDotRadius)
                val outerRect = floatArrayOf(
                        radius, radius, radius, radius, radius, radius, radius, radius)

                val rrs = RoundRectShape(outerRect, null, null)
                drawable = InnerShapeDrawableWithText(rrs, mDotText)
                drawable.paint.color = mDotColor
            }
            Mode.CIRCLE -> {
                val os = OvalShape()
                drawable = InnerShapeDrawableWithText(os, mDotText)
                drawable.paint.color = mDotColor
            }
        }
        return drawable
    }

    inner class InnerShapeDrawableWithText(s: Shape?) : ShapeDrawable(s) {
        private lateinit var text: String
        private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        constructor(s: Shape?, text: String) : this(s) {
            this.text = text
        }

        init {
            textPaint.color = mDotColor
            if (mDotTextSize != 0) {
                textPaint.textSize = mDotTextSize.toFloat()
            }
        }

        override fun onDraw(shape: Shape?, canvas: Canvas, paint: Paint?) {
            super.onDraw(shape, canvas, paint)
            if (!TextUtils.isEmpty(text)) {
                val rect = bounds
                if (mDotTextSize == 0) {
                    mDotTextSize = (rect.width() * 0.5).toInt()
                    textPaint.textSize = mDotTextSize.toFloat()
                }
                //保证文字居中
                val fontMetrics = textPaint.fontMetricsInt
                val baseLine = rect.top + (rect.bottom - rect.top + fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top
                textPaint.textAlign = Paint.Align.CENTER
                canvas.drawText(text, rect.centerX().toFloat(), baseLine.toFloat(), textPaint)
            }
        }

    }

    private var mAnimListener = object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {

        }

        override fun onAnimationEnd(p0: Animation?) {
            mIsShowing = !mIsShowing
            visibility = if (mIsShowing) {
                VISIBLE
            } else {
                GONE
            }
        }

        override fun onAnimationStart(p0: Animation?) {

        }

    }

}