package com.bdft.baseuilib.widget.pullrecyclerview.wrapperadapter

import android.app.ActionBar
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.text.method.ReplacementTransformationMethod
import android.view.View
import android.view.ViewGroup
import com.bdft.baseuilib.widget.pullrecyclerview.wrapperadapter.FixedViewInfo.Companion.ITEM_VIEW_TYPE_FOOTER_START
import com.bdft.baseuilib.widget.pullrecyclerview.wrapperadapter.FixedViewInfo.Companion.ITEM_VIEW_TYPE_HEADER_START
import com.socks.library.KLog


/**
 * ${}
 * Created by spark_lizhy on 2017/11/15.
 */
class HeaderViewWrapperAdapter(recyclerView: RecyclerView,
                               @NonNull wrappedAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
                               headerViewInfos: ArrayList<FixedViewInfo>,
                               footerViewInfos: ArrayList<FixedViewInfo>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), WrapperRecyclerAdapter {

    var mWrappedAdapter: RecyclerView.Adapter<*>? = null
    var mRecyclerView: RecyclerView? = null

    private var mHeaderViewInfos: ArrayList<FixedViewInfo>? = null
    private var mFooterViewInfos: ArrayList<FixedViewInfo>? = null

    companion object {
        val EMPTY_INFO_LIST: ArrayList<FixedViewInfo> = ArrayList()

        class HeaderOrFooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private val mDataObserver: RecyclerView.AdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            val headerViewCount = getHeadersCount()
            notifyItemRangeChanged(fromPosition + headerViewCount, toPosition + headerViewCount, toPosition + itemCount)
        }
    }

    init {
        this.mRecyclerView = recyclerView
        this.mWrappedAdapter = wrappedAdapter
        try {
            mWrappedAdapter?.registerAdapterDataObserver(mDataObserver)
        } catch (e: Exception) {
            KLog.w(e)
        }
        if (mHeaderViewInfos == null) {
            this.mHeaderViewInfos = EMPTY_INFO_LIST
        } else {
            this.mHeaderViewInfos = headerViewInfos
        }
        if (mFooterViewInfos == null) {
            this.mFooterViewInfos = EMPTY_INFO_LIST
        } else {
            this.mFooterViewInfos = footerViewInfos
        }

    }

    fun getHeadersCount(): Int = mHeaderViewInfos?.size ?: 0
    fun getFootersCount(): Int = mFooterViewInfos?.size ?: 0

    fun removeHeader(headerView: View) {
        for (i in mHeaderViewInfos!!.indices) {
            val info = mHeaderViewInfos!![i]
            if (info.view == headerView) {
                mHeaderViewInfos!!.removeAt(i)
            }
        }
    }

    fun removeFooter(footerView: View) {
        for (i in mFooterViewInfos!!.indices) {
            val info = mFooterViewInfos!![i]
            if (info.view == footerView) {
                mFooterViewInfos!!.removeAt(i)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        //header
        if (onCreateHeaderViewHolder(viewType)) {
            val headerPosition = getHeaderPosition(viewType)
            val headerView = mHeaderViewInfos!![headerPosition].view
            checkAndSetRecyclerViewLayoutParams(headerView)
            return HeaderOrFooterViewHolder(headerView!!)
        } else if (onCreateFooterViewHolder(viewType)) {
            val footerPosition = getFooterPosition(viewType)
            val footerView = mFooterViewInfos!![footerPosition].view
            checkAndSetRecyclerViewLayoutParams(footerView)
            return HeaderOrFooterViewHolder(footerView!!)
        }

        return mWrappedAdapter?.onCreateViewHolder(parent, viewType)!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val numHeaders = getHeadersCount()
        val adapterCpunt = mWrappedAdapter?.itemCount!!
        if (position < numHeaders) {
            //header
            return
        } else if (position > (numHeaders + adapterCpunt - 1)) {
            //footer
            return
        } else {
            val adjustPostion = position - numHeaders
            if (adjustPostion < adapterCpunt) {
                //TODO 咋写这玩意？
//                mWrappedAdapter?.onBindViewHolder(holder, adjustPostion)
            }
        }
    }

    private fun checkAndSetRecyclerViewLayoutParams(child: View?) {
        if (child == null) return
        val p = child.layoutParams
        var params: ViewGroup.LayoutParams? = null
        if (p == null) {
            params = ViewGroup.LayoutParams(ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)))
        } else {
            if (p !is RecyclerView.LayoutParams) {
                params = mRecyclerView?.layoutManager?.generateLayoutParams(p)
            }
        }
        child.layoutParams = params
    }

    override fun getItemCount(): Int {
        return if (mWrappedAdapter != null) {
            getHeadersCount() + getFootersCount() + mWrappedAdapter!!.itemCount
        } else {
            getHeadersCount() + getFootersCount()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val numHeaders = getHeadersCount()
        if (mWrappedAdapter == null) return -1
        val adjustPos = position - numHeaders
        val adapterItemCount = mWrappedAdapter!!.itemCount
        if (position >= numHeaders) {
            if (adjustPos < adapterItemCount) {
                //如果是adpter返回的范围内，则取adapter的ItemViewType
                return mWrappedAdapter!!.getItemViewType(adjustPos)
            }
        } else if (position < numHeaders) {
            return mHeaderViewInfos?.get(position)?.itemViewType!!
        }
        return mFooterViewInfos?.get(position)?.itemViewType!!
    }

    private fun getHeaderPosition(viewType: Int): Int =
            Math.abs(viewType) - Math.abs(ITEM_VIEW_TYPE_HEADER_START)

    private fun getFooterPosition(viewType: Int): Int =
            Math.abs(viewType) - Math.abs(ITEM_VIEW_TYPE_FOOTER_START)

    fun findHeaderPosition(headerView: View?): Int {
        if (headerView == null) return -1
        for (i in 0..mHeaderViewInfos?.size!!) {
            val info = mHeaderViewInfos?.get(i)
            if (info?.view == headerView) return i
        }
        return -1
    }

    fun findFooterPosition(footerView: View?): Int {
        if (footerView == null) return -1
        for (i in 0..mFooterViewInfos?.size!!) {
            val info = mFooterViewInfos?.get(i)
            if (info?.view == footerView) {
                return getHeadersCount() + mWrappedAdapter?.itemCount!! + i
            }
        }
        return -1
    }

    private fun onCreateHeaderViewHolder(viewType: Int): Boolean =
            mHeaderViewInfos!!.size > 0 && viewType <= ITEM_VIEW_TYPE_HEADER_START && viewType >= ITEM_VIEW_TYPE_FOOTER_START

    private fun onCreateFooterViewHolder(viewType: Int): Boolean =
            mFooterViewInfos!!.size > 0 && viewType <= ITEM_VIEW_TYPE_HEADER_START && viewType >= ITEM_VIEW_TYPE_FOOTER_START

    override fun getWrappedAdapter(): RecyclerView.Adapter<*> = mWrappedAdapter!!

}

