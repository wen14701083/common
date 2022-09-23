package com.xhd.android.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xhd.android.base.BaseFragment

/**
 * Create by wk on 2021/7/9
 */
class FragmentPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = ArrayList<BaseFragment<*>>()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun addFragment(fragment: BaseFragment<*>) {
        fragments.add(fragment)
    }

    /**
     * 获取指定fragment
     */
    fun getItemFragment(position: Int): BaseFragment<*>? {
        if (position >= fragments.size) {
            return null
        }
        return fragments[position]
    }

    /**
     * 清除fragment
     */
    fun cleanFragment() {
        fragments.clear()
    }
}
