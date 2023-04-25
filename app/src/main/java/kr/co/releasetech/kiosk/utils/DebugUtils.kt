package kr.co.releasetech.kiosk.utils

import android.util.Log
import kr.co.releasetech.kiosk.AppConst
import splitties.toast.toast

object DebugUtils {
    fun setLog(TAG : String, msg: String){
        if(AppConst.IS_TEST) Log.e(TAG, msg)
    }

    fun setToast(msg: String){
        if(AppConst.IS_TEST) toast(msg)
    }
}