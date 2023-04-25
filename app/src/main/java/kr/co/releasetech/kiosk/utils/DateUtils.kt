package kr.co.releasetech.kiosk.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getDateText(time: Long): String {
        val date = Date(time)
        return SimpleDateFormat("yyyy-MM-dd").format(date)
    }

    fun getDateTime(value: String): Long{
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value).time
    }

    fun getApprovalDate(value: Long): String{
        return SimpleDateFormat("yyMMdd").format(value)
    }
}