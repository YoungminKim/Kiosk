package kr.co.releasetech.kiosk.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import kr.co.releasetech.kiosk.manager.ReceiptPrinterManager
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.activity.splash.SplashActivity


class BootCompleteReceiver: BroadcastReceiver() {
    companion object{
        private const val TAG = "BootCompleteReceiver"
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val action = it.action

            if(Intent.ACTION_BOOT_COMPLETED == action){
                DebugUtils.setLog(TAG, "action : $action")

                val printerManager = ReceiptPrinterManager(context?.applicationContext!!){
                }

                printerManager.isConnect { isConnect ->
                    if(!isConnect) printerManager.init()
                }

                Handler(Looper.getMainLooper()).postDelayed(
                    {

                        DebugUtils.setLog(TAG, "start Intent : $context")
                        val i = Intent(context, SplashActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context?.startActivity(i)
                    },
                    1000
                )

                /*if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){

                }*/
            }
        }
    }
}