package kr.co.releasetech.kiosk.manager

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import androidx.datastore.preferences.protobuf.ByteString
import com.printsdk.PrintSerializable
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.Cart
import kr.co.releasetech.kiosk.model.realm.Payment
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.utils.TextUtils
import okio.ByteString.Companion.encodeUtf8
import splitties.toast.toast
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.CharsetEncoder
import kotlin.concurrent.thread

class ReceiptPrinterManager(val ctx: Context, val intent: Intent? = null, isSuccess:(() -> Unit)? = null) {
    companion object{
        private const val TAG = "ReceiptPrinterManager"
        private const val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"

        private const val VENDOR_ID = 10473


        private const val RETRY_COUNT_MAX = 3

    }
    private val usbManager: UsbManager by lazy {
        ctx.getSystemService(Context.USB_SERVICE) as UsbManager
    }

    private val mPrinter = PrintSerializable()
    var reConnectCount = 0

    fun init(){
        var usbDevice: UsbDevice? = getUsbDevice()
        //val usbDevice:UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
        DebugUtils.setLog(TAG, "device : ${usbDevice}")

        if(usbDevice == null){
            toast(R.string.not_found_printer)
        }else{
            try {
                ctx.unregisterReceiver(usbReceiver)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                val permissionIntent = PendingIntent.getBroadcast(ctx, 0, Intent(ACTION_USB_PERMISSION), 0)
                val filter = IntentFilter(ACTION_USB_PERMISSION)
                ctx.registerReceiver(usbReceiver, filter)
                usbManager.requestPermission(usbDevice, permissionIntent)
            }
        }

    }


    fun getUsbDevice() : UsbDevice?{
        var usbDevice: UsbDevice? = null
        usbManager.deviceList.filter { it.value.vendorId == VENDOR_ID }.map {
            DebugUtils.setLog(TAG, "key : ${it.key}")
            DebugUtils.setLog(TAG, "value : ${it.value.vendorId}")
            usbDevice = it.value
        }

        return usbDevice
    }

    fun isConnect(isConnectFunc:((Boolean) -> Unit)) {
        thread {
            val usbDevice = getUsbDevice()
            if(mPrinter.state == PrintSerializable.CONN_CLOSED){
                mPrinter.open(usbManager, usbDevice)
                Thread.sleep(1000)
            }


            val result = mPrinter.state == PrintSerializable.CONN_SUCCESS && usbDevice != null
            isConnectFunc.invoke(result)
        }
    }

    fun kitchenPrint(isTakeout: Boolean, cartList: ArrayList<Cart>, number: Int){
        val usbDevice =  getUsbDevice()

        when(mPrinter.state){
            PrintSerializable.CONN_SUCCESS ->{
                DebugUtils.setLog(TAG, "init")
                mPrinter.init()
                DebugUtils.setLog(TAG, "printing")
                mPrinter.setFont(1, 2, 1, 0)
                print("${ctx.getString(R.string.waiting_number)} : $number")
                if(isTakeout) print("${ctx.getString(R.string.takeout)}")
                else print("${ctx.getString(R.string.store)}")

                cartList.map {
                    print(it.name)
                    val optionNames = it.optionNames.split(",")
                    optionNames.map { optionName ->
                        print("   + $optionName")
                    }

                    print("${ctx.getString(R.string.quantity)} : ${it.quantity}")
                }

                mPrinter.Cutter()
            }
            PrintSerializable.CONN_CLOSED ->{
                mPrinter.open(usbManager, usbDevice)
                Thread.sleep(1000)
                if(reConnectCount < RETRY_COUNT_MAX){
                    reConnectCount++
                    kitchenPrint(isTakeout, cartList, number)
                }
            }
        }
    }

    fun waitingNumberPrint(cartList: ArrayList<Cart>, number: Int){

        val usbDevice =  getUsbDevice()

        when(mPrinter.state){
            PrintSerializable.CONN_SUCCESS ->{
                DebugUtils.setLog(TAG, "init")
                mPrinter.init()
                DebugUtils.setLog(TAG, "printing")
                mPrinter.setFont(1, 2, 1, 0)
                mPrinter.wrapLines(1)
                print("${ctx.getString(R.string.waiting_number)} : $number")

                mPrinter.setFont(0, 0, 0, 0)
                var orderStr = "주문 : ${cartList[0].name}"
                if(cartList.size > 1) orderStr += " 외 ${cartList.size - 1}개"
                print(orderStr)

                mPrinter.Cutter()
            }
            PrintSerializable.CONN_CLOSED ->{
                mPrinter.open(usbManager, usbDevice)
                Thread.sleep(1000)
                if(reConnectCount < RETRY_COUNT_MAX){
                    reConnectCount++
                    waitingNumberPrint(cartList, number)
                }
            }
        }
    }


    fun receiptPrint(cartList: ArrayList<Cart>, payment: Payment){

        //thread(start = true) {
            DebugUtils.setLog(TAG, "state : ${mPrinter.state}")

            val usbDevice =  getUsbDevice()

            when(mPrinter.state){
                PrintSerializable.CONN_SUCCESS ->{
                    //mPrinter.printText("Hello")
                    DebugUtils.setLog(TAG, "init")
                    mPrinter.init()
                    DebugUtils.setLog(TAG, "printing")
                    mPrinter.setFont(0, 0, 0, 0)
                    print(ctx.getString(R.string.receipt))

                    linePrint()
                    print("  상품명    단가  수량    금액")
                    linePrint()

                    for (i in cartList.indices) {
                        val goods = cartList[i]
                        val goodsStr = "${String.format("%02d", i + 1)}  ${goods.name}  ${TextUtils.getMoneyComma(goods.goodsPrice)}"
                        print(goodsStr)
                        val optionNames = goods.optionNames.split(",")
                        val optionPrice = goods.optionPrice.split(",")
                        for(j in optionNames.indices){
                            val optionStr = "     +${optionNames[j]}  ${TextUtils.getMoneyComma(optionPrice[j].toInt())}"
                            print(optionStr)
                        }
                        mPrinter.setAlign(2)
                        print("${TextUtils.getMoneyComma(goods.price)} * ${goods.quantity}  ${TextUtils.getMoneyComma(goods.totalPrice)}")
                        mPrinter.setAlign(0)
                    }

                    linePrint()

                    val totalPrice = if(payment.totalAmount.isNullOrEmpty()) "0" else TextUtils.getMoneyComma(payment.totalAmount!!.toInt())
                    val totalPriceStr = "${ctx.getString(R.string.total_amount)} : $totalPrice"
                    print(totalPriceStr)

                    val tax = if(payment.tax.isNullOrEmpty()) "0" else TextUtils.getMoneyComma(payment.tax!!.toInt())
                    val taxStr = "${ctx.getString(R.string.tax)} : $tax"
                    print(taxStr)

                    val cardNameStr = "${ctx.getString(R.string.card_name)} : ${payment.cardName}"
                    print(cardNameStr)

                    val cardNumStr = "${ctx.getString(R.string.card_num)} : ${payment.cardNum}"
                    print(cardNumStr)

                    val installment = if(payment.installment == "0") ctx.getString(R.string.single_payment) else String.format(payment.installment.toString(), ctx.getString(R.string.how_month))
                    val installmentStr = "${ctx.getString(R.string.installment)}  $installment"
                    print(installmentStr)
                    
                    val transTypeStr = "거래 구분 : " + if(payment.tranType == "D4") "환불" else "승인"
                    print(transTypeStr)

                    linePrint()

                    val approvalDate = "${ctx.getString(R.string.approval_date)} : ${payment.approvalDate}"
                    print(approvalDate)

                    val approvalNum  = "${ctx.getString(R.string.approval_num)} : ${payment.approvalNum}"
                    print(approvalNum)

                    val shopName = "${ctx.getString(R.string.shop_name)} : ${payment.shopName}"
                    print(shopName)

                    val shopTel = "${ctx.getString(R.string.shop_tel)} : ${payment.shopTel}"
                    print(shopTel)

                    print(ctx.getString(R.string.shop_address))
                    print(payment.shopAddress)

                    val shopOwner = "${ctx.getString(R.string.shop_owner)} : ${payment.shopOwner}"
                    print(shopOwner)

                    DebugUtils.setLog(TAG, "cutter")
                    mPrinter.Cutter()
                    //mPrinter.close()
                }
                PrintSerializable.CONN_CLOSED ->{
                    mPrinter.open(usbManager, usbDevice)
                    Thread.sleep(1000)
                    if(reConnectCount < RETRY_COUNT_MAX){
                        reConnectCount++
                        receiptPrint(cartList, payment)
                    }
                }

            }

        //}

    }

    private fun print(str: String){
        mPrinter.sendByteData(str.encodeUtf8().toByteArray())
        mPrinter.wrapLines(1)
    }

    private fun linePrint(){
        mPrinter.printText("================================")
        mPrinter.wrapLines(1)
    }
    private val usbReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            if (ACTION_USB_PERMISSION == intent.action) {
                synchronized(this) {
                    DebugUtils.setLog(TAG, "onReceive ACTION_USB_PERMISSION")
                    val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        device?.apply {
                            DebugUtils.setLog(TAG, "permission granted  $device")

                            mPrinter.open(usbManager, getUsbDevice())

                            mPrinter.init()
                            DebugUtils.setLog(TAG, "state :   ${mPrinter.state}")
                            when(mPrinter.state){
                                PrintSerializable.CONN_SUCCESS -> { //연결성공
                                    //mPrinter.printSelf()
                                    isSuccess?.invoke()
                                }
                                
                                PrintSerializable.CONN_FAILED -> { //연결 실패
                                    toast(R.string.printer_connect_failed)
                                }
                                
                                PrintSerializable.CONN_CLOSED ->{ //닫기
                                    toast(R.string.printer_connect_close)
                                }
                            }
                        }
                    } else {
                        toast(R.string.permision_denied_printer)
                        DebugUtils.setLog(TAG, "permission denied for device $device")
                    }
                }
            }
        }
    }
}