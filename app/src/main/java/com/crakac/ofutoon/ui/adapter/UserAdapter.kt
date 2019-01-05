package com.crakac.ofutoon.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.crakac.ofutoon.BR
import com.crakac.ofutoon.databinding.ListItemUserAccountBinding
import com.crakac.ofutoon.db.AppDatabase
import com.crakac.ofutoon.db.User

class UserAdapter(val context: Context) : BaseAdapter() {
    val TAG: String = "UserAdapter"
    private val users = ArrayList<User>()

    var onClickUserListener: ((User) -> Unit)? = null

    init {
        AppDatabase.execute {
            users.addAll(AppDatabase.instance.userDao().getAll())
            notifyDataSetChanged()
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = if (convertView != null) {
            ListItemUserAccountBinding.bind(convertView)
        } else {
            ListItemUserAccountBinding.inflate(LayoutInflater.from(context))
        }
        binding.setVariable(BR.user, users[position])
        binding.root.setOnClickListener {
            onClickUserListener?.invoke(getItem(position))
        }
        return binding.root

    }

    override fun getItem(position: Int) = users[position]
    override fun getItemId(position: Int) = getItem(position).id.toLong()
    override fun getCount() = users.size
}