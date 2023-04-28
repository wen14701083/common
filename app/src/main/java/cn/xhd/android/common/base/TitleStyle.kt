package cn.xhd.android.common.base

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import cn.xhd.android.common.R
import com.hjq.bar.TitleBarSupport
import com.hjq.bar.style.CommonBarStyle
import com.xhd.android.utils.ResourcesUtils

/**
 * @date created on 2022/7/14
 */
class TitleStyle : CommonBarStyle() {

    override fun getTitleBarBackground(context: Context?): Drawable {
        return ColorDrawable(ResourcesUtils.getColor(R.color.white))
    }

    override fun getLeftTitleBackground(context: Context?): Drawable {
        return ColorDrawable(ResourcesUtils.getColor(R.color.transparent))
    }

    override fun getRightTitleBackground(context: Context?): Drawable {
        return ColorDrawable(ResourcesUtils.getColor(R.color.transparent))
    }

    override fun getBackButtonDrawable(context: Context?): Drawable {
        return TitleBarSupport.getDrawable(context, R.drawable.icon_back)
    }

    override fun getTitleColor(context: Context?): ColorStateList {
        return ColorStateList.valueOf(ResourcesUtils.getColor(R.color.black))
    }

    override fun getLeftTitleColor(context: Context?): ColorStateList {
        return ColorStateList.valueOf(ResourcesUtils.getColor(R.color.text_blue))
    }

    override fun getRightTitleColor(context: Context?): ColorStateList {
        return ColorStateList.valueOf(ResourcesUtils.getColor(R.color.text_blue))
    }

    override fun getLineDrawable(context: Context?): Drawable {
        return ColorDrawable(ResourcesUtils.getColor(R.color.line))
    }

    override fun getTitleStyle(context: Context?): Int {
        return Typeface.BOLD
    }
}
