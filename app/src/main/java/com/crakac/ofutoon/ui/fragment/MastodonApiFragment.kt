package com.crakac.ofutoon.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.crakac.ofutoon.R
import com.crakac.ofutoon.api.Link
import com.crakac.ofutoon.api.Paging
import com.crakac.ofutoon.api.entity.Identifiable
import com.crakac.ofutoon.ui.adapter.RefreshableAdapter
import com.crakac.ofutoon.ui.widget.FastScrollLinearLayoutManager
import com.crakac.ofutoon.ui.widget.SwipeRefreshRecyclerView
import kotlinx.android.synthetic.main.fragment_swiperefresh.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class MastodonApiFragment<AdapterClass : Identifiable, ResponseClass> : Fragment(),
    SwipeRefreshLayout.OnRefreshListener, SwipeRefreshRecyclerView.OnLoadMoreListener {
    lateinit var prevPaging: Paging
    lateinit var nextPaging: Paging
    val prev: Map<String, String> get() = prevPaging.q
    val next: Map<String, String> get() = nextPaging.q
    var isLoadingNext = false

    val recyclerView: RecyclerView get() = swipeRefresh.recyclerView
    lateinit var adapter: RefreshableAdapter<AdapterClass>
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_swiperefresh, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = createAdapter()
        recyclerView.adapter = adapter
        layoutManager = FastScrollLinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager
        val divider =
            androidx.recyclerview.widget.DividerItemDecoration(requireActivity(), layoutManager.orientation).apply {
                setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.divider)!!)
            }
        recyclerView.addItemDecoration(divider)
        swipeRefresh.setOnRefreshListener(this)
        swipeRefresh.setOnLoadMoreListener(this)

        prevPaging = Paging()
        nextPaging = Paging()
        onRefresh()
        swipeRefresh.isRefreshing = true
    }

    override fun onStart() {
        super.onStart()
        refreshItem()
    }

    abstract fun createAdapter(): RefreshableAdapter<AdapterClass>

    abstract fun refreshCall(): Call<ResponseClass>
    abstract fun onRefreshSuccess(response: ResponseClass)
    override fun onRefresh() {
        refreshCall().enqueue(object : Callback<ResponseClass> {
            override fun onFailure(call: Call<ResponseClass>, t: Throwable) {
                swipeRefresh.isRefreshing = false
                refreshItem()
            }

            override fun onResponse(call: Call<ResponseClass>, response: Response<ResponseClass>) {
                swipeRefresh.isRefreshing = false
                refreshItem()
                if (!response.isSuccessful) {
                    return
                }
                response.body()?.let {
                    onRefreshSuccess(it)
                }
                Link.parse(response.headers()["link"])?.let {
                    prevPaging = it.prevPaging()
                    if (nextPaging.maxId == null) {
                        nextPaging = it.nextPaging()
                    }
                }
            }
        })
    }


    abstract fun loadMoreCall(): Call<ResponseClass>
    abstract fun onLoadMoreSuccess(response: ResponseClass)
    override fun onLoadMore() {
        if (isLoadingNext || nextPaging.maxId == null) {
            return
        }
        isLoadingNext = true
        loadMoreCall().enqueue(object : Callback<ResponseClass> {
            override fun onFailure(call: Call<ResponseClass>, t: Throwable) {
                isLoadingNext = false
            }

            override fun onResponse(call: Call<ResponseClass>, response: Response<ResponseClass>) {
                isLoadingNext = false
                if (!response.isSuccessful) {
                    return
                }
                response.body()?.let {
                    onLoadMoreSuccess(it)
                }
                Link.parse(response.headers()["link"])?.let {
                    nextPaging = it.nextPaging()
                }
            }
        })
    }

    private var firstVisibleItem: AdapterClass? = null
    private var firstVisibleOffset = 0

    private fun savePosition() {
        if (adapter.isEmpty || recyclerView.getChildAt(0) == null) {
            return
        }
        firstVisibleItem = adapter.getItem(layoutManager.findFirstVisibleItemPosition())
        firstVisibleOffset = recyclerView.getChildAt(0).top
    }

    private fun restorePosition() {
        firstVisibleItem?.let {
            val pos = adapter.getPosition(it)
            layoutManager.scrollToPositionWithOffset(pos, firstVisibleOffset)
        }
    }

    fun insertQuietly(items: List<AdapterClass>) {
        savePosition()
        adapter.addTop(items)
        restorePosition()
    }

    fun scrollToTop() {
        if (!isAdded) return
        recyclerView.scrollToPosition(0)
    }

    fun refreshItem() {
        if (!isAdded) return
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val holder = recyclerView.getChildViewHolder(child) as RefreshableAdapter.Refreshable
            holder.refresh()
        }
    }
}