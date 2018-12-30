package com.crakac.ofutoon.ui.widget

import android.content.Context
import android.util.DisplayMetrics

class FastScrollLinearLayoutManager(context: Context) : androidx.recyclerview.widget.LinearLayoutManager(context) {
    val TAG: String = "FastScrollLinearLayoutManager"
    override fun smoothScrollToPosition(
        recyclerView: androidx.recyclerview.widget.RecyclerView,
        state: androidx.recyclerview.widget.RecyclerView.State,
        position: Int
    ) {
        val scroller = object : androidx.recyclerview.widget.LinearSmoothScroller(recyclerView.context) {
            private var isScrolled = false

            override fun updateActionForInterimTarget(action: Action) {
                if (isScrolled) {
                    action.jumpTo(targetPosition)
                } else {
                    super.updateActionForInterimTarget(action)
                    isScrolled = true
                }
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return 5f / displayMetrics.densityDpi
            }

        }
        scroller.targetPosition = position
        startSmoothScroll(scroller)
    }
}