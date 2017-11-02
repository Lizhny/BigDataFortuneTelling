package com.future.telling.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.webkit.WebSettings
import com.bdft.baselibrary.`interface`.OnDoubleClickListener
import com.bdft.baseuilib.base.BaseTitleBarActivity
import com.bdft.baseuilib.widget.common.TitleBar
import com.bdft.telling.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseTitleBarActivity() {

    override fun onHandleIntent(intent: Intent) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitleMode(TitleBar.MODE_TITLE)
        setTitleText(R.string.title_main)
        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {

        fab.setOnClickListener(object : OnDoubleClickListener() {
            override fun onSingleClick() {

            }

            override fun onDoubleClick(view: View) {
                showSnackBar(view)
            }
        })
        val settings = main_web.settings
        settings.setSupportZoom(true)
        settings.defaultTextEncodingName = "utf-8"
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        main_web.loadUrl("https://www.bing.com/?wlexpsignin=1")
    }

    private fun showSnackBar(v: View) {
        Snackbar.make(v, "大数据算命，你值得拥有", Snackbar.LENGTH_LONG)
                .setAction("测一测") { v ->
                    Snackbar.make(v, "为何你不懂", Snackbar.LENGTH_LONG)
                            .setAction("有爱就有痛") { v -> showSnackBar(v) }.show()
                }.show()
    }

}
