package com.xhd.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import java.lang.reflect.Field
import kotlin.Throws

/**
 * Content: 首页惯性下滑时RecyclerView上滑抖动冲突
 * Create by wk on 2020/8/12
 */
class CustomBehavior(context: Context?, attrs: AttributeSet?) :
    AppBarLayout.Behavior(context, attrs) {

    private val lastPagerShow = false
    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> stopAppbarLayoutFling(child) // 手指触摸屏幕的时候停止fling事件
            else -> {}
        }
        return super.onInterceptTouchEvent(parent, child, ev)
    } // 可能是28及以上版本// support design 27及以下版本

    /**
     * 反射获取私有的flingRunnable 属性，考虑support 28以后变量名修改的问题
     * @return Field
     */
    @get:Throws(NoSuchFieldException::class)
    private val flingRunnableField: Field
        get() = try {
            // support design 27及以下版本
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass
            headerBehaviorType.getDeclaredField("mFlingRunnable")
        } catch (e: NoSuchFieldException) {
            // 可能是28及以上版本
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass.superclass
            headerBehaviorType.getDeclaredField("flingRunnable")
        } // 可能是28及以上版本// support design 27及以下版本

    /**
     * 反射获取私有的scroller 属性，考虑support 28以后变量名修改的问题
     * @return Field
     */
    @get:Throws(NoSuchFieldException::class)
    private val scrollerField: Field
        get() = try {
            // support design 27及以下版本
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass
            headerBehaviorType.getDeclaredField("mScroller")
        } catch (e: NoSuchFieldException) {
            // 可能是28及以上版本
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass.superclass
            headerBehaviorType.getDeclaredField("scroller")
        }

    private fun stopContentFling(parent: CoordinatorLayout) {}

    /**
     * 停止appbarLayout的fling事件
     * @param appBarLayout
     */
    private fun stopAppbarLayoutFling(appBarLayout: AppBarLayout) {
        // 通过反射拿到HeaderBehavior中的flingRunnable变量
        try {
            val flingRunnableField = flingRunnableField
            val scrollerField = scrollerField
            flingRunnableField.isAccessible = true
            scrollerField.isAccessible = true
            val flingRunnable = flingRunnableField[this]
            val overScroller = scrollerField[this]
            flingRunnable?.let {
                appBarLayout.removeCallbacks(it as Runnable)
                flingRunnableField[this] = null
            }
            overScroller?.let {
                (it as OverScroller).abortAnimation()
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        stopAppbarLayoutFling(child)
        return if (directTargetChild is AppBarLayout && type == TYPE_FLING) {
            false
        } else super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type)
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        // type返回1时，表示当前target处于非touch的滑动，
        // 该bug的引起是因为appbar在滑动时，CoordinatorLayout内的实现NestedScrollingChild2接口的滑动子类还未结束其自身的fling
        // 所以这里监听子类的非touch时的滑动，然后block掉滑动事件传递给AppBarLayout
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed
        )
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        abl: AppBarLayout,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
    }

    companion object {
        private const val TAG = "CustomBehavior"
        private const val TYPE_FLING = 1
    }
}
