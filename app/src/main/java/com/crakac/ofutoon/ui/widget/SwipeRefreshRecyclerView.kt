package com.crakac.ofutoon.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.crakac.ofutoon.R

class SwipeRefreshRecyclerView : androidx.swiperefreshlayout.widget.SwipeRefreshLayout {
    companion object {
        val TAG: String = "SwipeRefreshListView"
        val VISIBLE_THRESHOLD = 5
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    val recyclerView: androidx.recyclerview.widget.RecyclerView

    init {
        val v = View.inflate(context, R.layout.swipe_refresh_recycler_view, this)
        recyclerView = v.findViewById(R.id.recyclerView)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        recyclerView.addOnScrollListener(scrollListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        recyclerView.removeOnScrollListener(scrollListener)
    }

    private var mLoadMoreListener: OnLoadMoreListener? = null
    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        mLoadMoreListener = listener
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    val scrollListener = object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
            // bail out if scrolling upward or already loading data
            // if (dy < 0 || dataLoading.isDataLoading()) return

            val visibleItemCount = recyclerView.childCount
            val layoutManager = recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

            if (totalItemCount - visibleItemCount <= firstVisibleItem + VISIBLE_THRESHOLD) {
                mLoadMoreListener?.onLoadMore()
            }
        }
    }
}