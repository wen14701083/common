package com.xhd.android.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by wk on 2021/7/5
 */
object TimeUtils {

    const val SECOND = 1000
    const val MINUTE = 60 * SECOND
    const val HOUR = 60 * MINUTE
    const val DAY = 24 * HOUR

    const val FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_YMD = "yyyy-MM-dd"
    const val FORMAT_YMD_CN = "yyyy年MM月dd日"
    const val FORMAT_HMS = "HH:mm:ss"
    fun formatTime(time: Long, rule: String): String? {
        val date = Date(time)
        val format = SimpleDateFormat(rule, Locale.getDefault())
        return format.format(date)
    }

    fun formatTime(time: String, rule: String): String? {
        return formatTime(time, FORMAT_DEFAULT, rule)
    }

    fun formatTime(time: String, oldRule: String, newRule: String): String? {
        try {
            val sf1 = SimpleDateFormat(oldRule, Locale.getDefault())
            val date = sf1.parse(time) ?: return ""
            val format = SimpleDateFormat(newRule, Locale.getDefault())
            return format.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 将int值转换成时长
     * @return 时长
     */
    fun formatVideoTime(durations: Long, alwaysShowHours: Boolean = false): String {
        val time = durations / 1000
        val sb = StringBuffer()
        var temp = time / 3600
        if (alwaysShowHours || temp != 0L) { // 时长不超过一小时则不添加
            sb.append(if (temp < 10) "0$temp:" else "$temp:")
        }
        temp = time % 3600 / 60
        sb.append(if (temp < 10) "0$temp:" else "$temp:")
        temp = time % 3600 % 60
        sb.append(if (temp < 10) "0$temp" else "" + temp)
        return sb.toString()
    }

    /**
     * 计算距离今日天数
     * [time] yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd
     */
    fun distanceDay(time: String): Int {
        val format = if (time.length == FORMAT_DEFAULT.length) {
            SimpleDateFormat(FORMAT_DEFAULT, Locale.getDefault())
        } else {
            SimpleDateFormat(FORMAT_YMD, Locale.getDefault())
        }
        try {
            val date = format.parse(time) ?: return 0
            return distanceDay(date.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return distanceDay(0)
    }

    /**
     * 计算距离今日天数
     * [time] 时间戳
     */
    fun distanceDay(time: Long): Int {
        val distance = System.currentTimeMillis() - time
        return (distance / DAY).toInt() + 1
    }

    private val weekNames = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")

    /**
     * 新航道app消息列表显示规则
     * 不同年：yyyy-MM-dd HH:mm:ss
     * 当天： 今天 HH:mm:ss
     * 昨天： 昨天 HH:mm:ss
     * 同一周： 周x HH:mm:ss
     * 其他： yyyy-MM-dd HH:mm:ss
     * [time] yyyy-MM-dd HH:mm:ss
     */
    fun getMessageTime(time: String): String {
        try {
            val curCalendar = Calendar.getInstance()
            val sf1 = SimpleDateFormat(FORMAT_DEFAULT, Locale.getDefault())
            val date = sf1.parse(time) ?: return ""
            val calendar = Calendar.getInstance()
            calendar.time = date

            if (calendar[Calendar.YEAR] != curCalendar[Calendar.YEAR]) {
                return time
            }

            when (curCalendar[Calendar.DAY_OF_YEAR] - calendar[Calendar.DAY_OF_YEAR]) {
                0 -> return "今天 ${formatTime(calendar.timeInMillis, FORMAT_HMS)}"
                1 -> return "昨天 ${formatTime(calendar.timeInMillis, FORMAT_HMS)}"
                2, 3, 4, 5, 6 -> {
                    val calendarWeek = calendar[Calendar.WEEK_OF_YEAR]
                    val currentWeek = curCalendar[Calendar.WEEK_OF_YEAR]
                    if (calendarWeek == currentWeek) { // 同一周
                        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
                        if (dayOfWeek != 1) {
                            return "${weekNames[dayOfWeek - 1]} ${formatTime(calendar.timeInMillis, FORMAT_HMS)}"
                        }
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            return time
        }
        return time
    }
}
