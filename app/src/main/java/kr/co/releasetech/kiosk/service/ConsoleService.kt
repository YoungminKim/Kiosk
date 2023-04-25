package kr.co.releasetech.kiosk.service

import android.content.Intent
import android.os.Handler
import android.os.Looper
import java.nio.ByteBuffer
import kotlin.concurrent.thread

import android.os.Message
import kr.co.releasetech.kiosk.utils.DebugUtils
import okio.ByteString.Companion.encodeUtf8


class ConsoleService: SerialPortService() {
    companion object{
        const val TAG = "ConsoleService"
        var isServiceRunning = false

        const val TO_PORT = 0

        const val STX = 0
        const val LEN = 1
        const val COM = 2
        const val D0 = 3
        const val D1 = 4
        const val D2 = 5
        const val D3 = 6
        const val D4 = 7
        const val D5 = 8
        const val D6 = 9
        const val D7 = 10
        const val D8 = 11
        const val D9 = 12
        const val D10 = 13
        const val D11 = 14
        const val D12 = 15
        const val ETX = 16
        const val CRC = 17
    }


    override fun onDataReceived(buffer: ByteArray, size: Int) {
        thread(start = true) {
            val byteArr: ByteBuffer = ByteBuffer.wrap(ByteArray(5000))

            if(size == 18){
                byteArr.put(buffer, 0 , size)

                val msg = Message.obtain()
                msg.obj = byteArr
                msg.arg1 = size

                mHandler.sendMessage(msg)


            }


            byteArr.clear()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isServiceRunning = true
        return super.onStartCommand(intent, flags, startId)
    }


    override fun stopService(name: Intent?): Boolean {
        DebugUtils.setLog(TAG, "stopService")
        isServiceRunning = false
        return super.stopService(name)
    }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            when(msg.what){
                TO_PORT -> {
                    val byteArr = msg.obj as ByteBuffer
                    val size = msg.arg1

                    val result = String(byteArr.array(), 0, size).encodeUtf8()
                    DebugUtils.setLog(TAG, "buffer : $result")
                    DebugUtils.setLog(TAG, "size : $size")
                    for (i in 0 until size){
                        val uByte = byteArr[i].toUByte().toInt()
                        val hex = Integer.toHexString(uByte)
                        val bin = Integer.toBinaryString(uByte)
                        val binStr = String.format("%8s", bin).replace(" ", "0")
                        DebugUtils.setLog(TAG, "$i hex : $hex")
                        DebugUtils.setLog(TAG, "$i bin : $binStr")

                        when(i){
                            STX -> DebugUtils.setLog(TAG, "STX : $hex")
                        }


                    }
                }
            }
        }
    }
}