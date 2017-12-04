package com.bdft.baseuilib.widget.pullrecyclerview.wrapperadapter

import android.support.v7.widget.RecyclerView

/**
 * ${}
 * Created by spark_lizhy on 2017/11/16.
 */
interface WrapperRecyclerAdapter {
    fun getWrappedAdapter(): RecyclerView.Adapter<*>
}