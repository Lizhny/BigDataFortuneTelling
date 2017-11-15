package com.bdft.baseuilib.widget.pullrecyclerview.wrapperadapter

import android.view.View

/**
 * ${}
 * Created by spark_lizhy on 2017/11/14.
 */
class FixedViewInfo {

    /**
     * header:-2~-98
     */
    val ITEM_VIEW_TYPE_HEADER_START = -2

    /**
     * footer:-99~-∞
     */
    val ITEM_VIEW_TYPE_FOOTER_START = -99

    var view: View? = null

    var itemViewType: Int? = null

    constructor(view: View, itemViewType: Int) {
        this.view = view
        this.itemViewType = itemViewType
    }
}