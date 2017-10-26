package com.bdft.baselibrary.base

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.alibaba.android.arouter.launcher.ARouter
import com.bdft.baselibrary.helper.PermissionHelper
import com.socks.library.KLog


/**
 * ${}
 * Created by spark_lizhy on 2017/8/9.
 */

abstract class BaseActivity : AppCompatActivity() {
    private var permissionHelper: PermissionHelper? = null
    protected var isAppInBackground = false

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KLog.i("===", "activity onCreate: " + this.javaClass.simpleName)
        if (permissionHelper == null) {
            permissionHelper = PermissionHelper(this)
        }
        ARouter.getInstance().inject(this)
        onHandleIntent(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper?.handlePermissionsResult(requestCode, permissions, grantResults)
    }

    protected fun getPermissionHelper(): PermissionHelper? = permissionHelper

    protected fun <T : View> findView(@IdRes id: Int): T = super.findViewById(id) as T

    protected fun hideStatusBar() {
        val sdkVer = Build.VERSION.SDK_INT
        if (sdkVer < 16) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = uiOptions
            this.actionBar?.hide()
        }
    }

    override fun onStop() {
        super.onStop()
        if (AppContext.isAppBackground()) {
            isAppInBackground = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (isAppInBackground) {
            isAppInBackground = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionHelper?.handleDestroy()
    }

    /**
     * run in [BaseActivity.onCreate] but before [AppCompatActivity.setContentView]
     *
     * 如果有intent，则需要处理这个intent（该方法在onCreate里面执行，但在setContentView之前调用）
     *
     * @param intent
     * @return false:关闭activity
     */
    abstract fun onHandleIntent(intent: Intent)

}
