package kr.co.releasetech.kiosk.utils

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController

object WindowUtils {
    private const val TAG = "WindowUtils"
    fun setImmersiveMode(window: Window){
        DebugUtils.setLog(TAG, "setImmersiveMode called!!!")

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.setDecorFitsSystemWindows(false)
            val controller = window.insetsController
            controller?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }else{
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            window.decorView.systemUiVisibility = flags
        }

    }
}