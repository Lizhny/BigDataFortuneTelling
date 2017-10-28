package com.bdft.baseuilib.widget.indicator

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout

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
     * GRAVITY_LEF_CENTER 左边居中
     * <p/>
     * GRAVITY_RIGHT_CENTER 右边居中
     * <p/>
     */
    public enum class DotGravity {
        GRAVITY_TOP_LEFT, GRAVITY_TOP_RIGHT, GRAVITY_BOTTOM_LEFT, GRAVITY_BOTTOM_RIGHT,
        GRAVITY_CENTER, GRAVITY_CENTER_VERTICAL, GRAVITY_CENTER_HORIZONTAL, GRAVITY_LEF_CENTER,
        GRAVITY_RIGHT_CENTER
    }

    lateinit private var mDotGravity: DotGravity

    public enum class Mode {
        ROUND_RECT, CIRCLE
    }

    private var mode = Mode.CIRCLE

    private var dotColor = DEAFULT_DOT_COLOR
    private var dotSize = DEAFULT_DOT_SIZE
    private var dotRadius = DEAFULT_DOT_REDIUS
    private var dotTextColor = DEAFULT_DOT_TEXT_COLOR
    private var dotTextSize = 0

    private lateinit var mDotBackGround: ShapeDrawable
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
        mFadeIn = AlphaAnimation(0f,1f)
        mFadeIn.setInterpolator(DecelerateInterpolator())
        mFadeIn.duration=450
        mFadeIn.setAnimationListener(mAnimListener)

        mFadeOut = AlphaAnimation(0f,1f)
        mFadeOut.setInterpolator(DecelerateInterpolator())
        mFadeOut.duration=450
        mFadeOut.setAnimationListener(mAnimListener)
    }

    var mAnimListener =object :Animation.AnimationListener{
        override fun onAnimationRepeat(p0: Animation?) {

        }

        override fun onAnimationEnd(p0: Animation?) {
            mIsShowing=!mIsShowing
            visibility = if (mIsShowing){
                VISIBLE
            }else{
                GONE
            }

        }

        override fun onAnimationStart(p0: Animation?) {

        }

    }

}