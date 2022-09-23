package com.xhd.android.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * Content: 引导页适配器
 */
class GuideAdapter(private val activity: Activity, private val pages: List<View>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = pages[position]
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
