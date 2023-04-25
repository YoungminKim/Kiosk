package kr.co.releasetech.kiosk.manager

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager

object AppManager {
    fun isServiceRunning(ctx: Context, serviceName: String): Boolean{
        val am = ctx.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager

        for (service in am.getRunningServices(Int.MAX_VALUE)){
            if(serviceName == service.service.className){
                return true
            }
        }

        return false

    }

    fun checkAppInstall(ctx: Context, packageName: String): Boolean{
        return ctx.packageManager.getLaunchIntentForPackage(packageName) != null
    }
}