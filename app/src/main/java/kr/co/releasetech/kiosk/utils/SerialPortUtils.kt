package kr.co.releasetech.kiosk.utils

import android_serialport_api.SerialPort
import android_serialport_api.SerialPortFinder
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.security.InvalidParameterException

object SerialPortUtils {
    const val TAG = "SerialPortUtils"
    const val BAUD_RATE = 38400

    val ports: ArrayList<SerialPort> = arrayListOf()



    fun findSerialPort(){
        val finder = SerialPortFinder()
        val devices = finder.allDevices
        val paths = finder.allDevicesPath
        DebugUtils.setLog(TAG, "devices size : ${devices.size}")

        for (device in  devices){
            DebugUtils.setLog(TAG, "device : $device")
            val index = devices.indexOf(device)
            DebugUtils.setLog(TAG, "path : ${paths[index]}")
        }


    }

    fun connectSerialPort(path: String, baudRate: Int): SerialPort?{
        var port: SerialPort? = null
        try{
            port = SerialPort(File(path), baudRate, 0)
        }catch (e: SecurityException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }catch (e: InvalidParameterException){
            e.printStackTrace()
        }

        return port
    }



    fun sendData(port: SerialPort?, writeBytes: ByteArray){
        try {
            port?.let {
                val outputStream = port.outputStream
                outputStream.write(writeBytes)
            }
        }catch (e: IOException){

        }
    }

    fun readData(port: SerialPort?){

        val inputStream = port?.let { it.inputStream }
        ReadThread(inputStream).start()
    }

    class ReadThread(private val inputStream: InputStream?) : Thread(){
        override fun run() {
            super.run()
            var size: Int
            while (!isInterrupted){
                try {
                    val buffer = byteArrayOf(64)

                    inputStream?.let {
                        size = it.read(buffer)

                        if(size > 0){

                            DebugUtils.setLog(TAG, "buffer : $buffer")

                            try{
                                sleep(100)
                            }catch (e: InterruptedException){
                                e.printStackTrace()
                            }
                        }
                    }
                }catch (e: IOException){

                }

            }
        }
    }



}