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

/**
 * ${}
 * Created by spark_lizhy on 2017/10/28.
 */
class DotWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.textViewStyle
) : View(context, attrs, defStyleAttr) {
    private val TAG = "DotWidget"
    public val DEBUG = true

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

    private lateinit var mDotBackground: ShapeDrawable
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

        buildDeafultAnim()

    }

    private fun buildDeafultAnim() {
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

    private fun show() {
        show(false)
    }

    private fun show(needAnim: Boolean) {
        show(needAnim, mFadeIn)
    }

    private fun show(needAnim: Boolean, animation: Animation) {
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

    private fun initDotParams(mDotSize: Int) {
        val dotWeightWidth = UIUtils.dip2PxToInt(mDotSize.toFloat())
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
                val radius = UIUtils.dip2PxToFloat(mDotRadius)
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

    var mAnimListener = object : Animation.AnimationListener {
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