package kr.co.releasetech.kiosk.receiver

import android.app.admin.DeviceAdminReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import kr.co.releasetech.kiosk.utils.DebugUtils

class MyDeviceAdminReceiver: DeviceAdminReceiver() {
    companion object{
        const val TAG = "MyDeviceAdminReceiver"

        fun getComponentName(context: Context) : ComponentName = ComponentName(context.applicationContext, MyDeviceAdminReceiver::class.java)
    }

    override fun onLockTaskModeEntering(context: Context, intent: Intent, pkg: String) {
        super.onLockTaskModeEntering(context, intent, pkg)
        DebugUtils.setLog(TAG, "onLockTaskModeEntering")
    }

    override fun onLockTaskModeExiting(context: Context, intent: Intent) {
        super.onLockTaskModeExiting(context, intent)
        DebugUtils.setLog(TAG, "onLockTaskModeExiting")
    }
}