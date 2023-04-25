package kr.co.releasetech.kiosk.utils

import android.os.Build
import android.text.Html
import android.text.Spanned
import java.lang.NumberFormatException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**

 * @author:young
 * @CreatedDate : 2021-03-21 오후 3:38
 * @PackageName : kr.co.unboxer.utils
 * @ClassName: TextUtils
 * @Description:

 */
object TextUtils {
    fun getMoneyComma(value: Int): String = DecimalFormat("##,###").format(value)

    fun setHtmlText(content: String?): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(content)
        }
    }

    fun getCountDownTimer(value: Long) : String{
        val date = Date(value)
        return SimpleDateFormat("m:ss").format(date)
    }



}