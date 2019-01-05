package com.crakac.ofutoon.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.crakac.ofutoon.HasTitle

class MyFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    val fragments = ArrayList<Fragment>()
    override fun getItem(position: Int) = fragments[position]
    override fun getCount(): Int = fragments.size
    fun add(fragment: Fragment) {
        fragments.add(fragment)
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return (fragments[position] as HasTitle?)?.getTitle()
    }
}