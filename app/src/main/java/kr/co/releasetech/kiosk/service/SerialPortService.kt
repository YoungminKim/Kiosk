package kr.co.releasetech.kiosk.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android_serialport_api.SerialPort
import android_serialport_api.SerialPortFinder
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.utils.SerialPortUtils
import kr.co.releasetech.kiosk.utils.SerialPortUtils.BAUD_RATE
import java.io.IOException
import java.io.InputStream
import java.security.InvalidParameterException

abstract class SerialPortService: Service() {
    companion object{
        const val TAG = "SerialPortService"
        const val PATH = "/dev/ttyS4"
    }

    var mSerialPort: SerialPort? = null
    var mInputStream: InputStream? = null

    var mReadThread: ReadThread? = null

    abstract fun onDataReceived(buffer: ByteArray, size: Int)

    override fun onCreate() {
        super.onCreate()
        DebugUtils.setLog(TAG, "onCreate called!!!")

        try{
            var isFind = false
            val finder = SerialPortFinder()
            for (path in finder.allDevicesPath){
                if(path == PATH){
                    isFind = true
                    break
                }
            }
            if(isFind){
                mSerialPort = SerialPortUtils.connectSerialPort(PATH, BAUD_RATE)
                mInputStream = mSerialPort?.inputStream
            }

        }catch (e: SecurityException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }catch (e: InvalidParameterException){
            e.printStackTrace()
        }


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        mReadThread = ReadThread()
        mReadThread?.start()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null


    override fun onDestroy() {
        super.onDestroy()
        mSerialPort?.close()
    }

    inner class ReadThread: Thread(){
        override fun run() {
            super.run()
            var size: Int
            while (!isInterrupted){
                try {
                    val buffer = ByteArray(64)

                    mInputStream?.let {
                        size = it.read(buffer)
                        if(size > 0){
                            onDataReceived(buffer, size)
                            try{
                               sleep(1000)
                            }catch (e: InterruptedException){
                                e.printStackTrace()
                            }
                        }
                    }
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }
        }
    }
}