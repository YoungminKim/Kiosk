package kr.co.releasetech.kiosk.view.activity.paymentdetail

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.releasetech.kiosk.kicc.KiccCallbackParam
import kr.co.releasetech.kiosk.model.TransData
import kr.co.releasetech.kiosk.model.realm.Cart
import kr.co.releasetech.kiosk.model.realm.Payment
import kr.co.releasetech.kiosk.model.repository.CartRepository
import kr.co.releasetech.kiosk.model.repository.PaymentRepository
import kr.co.releasetech.kiosk.utils.DateUtils
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class PaymentDetailViewModel(val repository: PaymentRepository, val cartRepository: CartRepository): BaseViewModel() {
    companion object{
        private const val TAG = "PaymentDetailViewModel"
    }

    private val _onClose = MutableLiveData<Unit>()
    val onClose: LiveData<Unit>
        get() = _onClose

    private val _onRefundFailed = MutableLiveData<String>()
    val onRefundFailed : LiveData<String>
        get() = _onRefundFailed


    private val _onRefundRequest = MutableLiveData<TransData>()
    val onRefundRequest: LiveData<TransData>
        get() = _onRefundRequest


    private val _onRefund = MutableLiveData<Boolean>()
    val onRefund : LiveData<Boolean>
        get() = _onRefund

    private val _onRefundSuccess = MutableLiveData<Unit>()
    val onRefundSuccess: LiveData<Unit>
        get() = _onRefundSuccess


    private val _onPrint = MutableLiveData<HashMap<String, Any>>()
    val onPrint: LiveData<HashMap<String, Any>>
        get() = _onPrint

    fun refundRequest(item: Payment){
        item?.let { item ->
            val transData = TransData(
                tranNo = DateUtils.getDateText(System.currentTimeMillis()),
                tranType = "D4",
                totalAmount = item.totalAmount.toString(),
                tax = "0",
                tip = "0",
                approvalNum= item.approvalNum!!,
                approvalDate= item.approvalDate.toString().substring(0, 6),
                TranSerialNo=item.tranSerialNo.toString(),
                cashAmount="00",
                installment="0",
                addField="",
                signFlag="Y"
            )

            _onRefundRequest.postValue(transData)
        }

    }


    fun saveResult(extras: Bundle){
        val keys = extras.keySet()

        val payment = Payment()
        for(key in keys){
            DebugUtils.setLog(TAG, "key : $key, : ${extras.get(key)}")

            when(key){
                KiccCallbackParam.TRAN_NO -> payment.tranNo = extras.get(key).toString()
                KiccCallbackParam.TRAN_TYPE -> payment.tranType = extras.get(key).toString()
                KiccCallbackParam.CARD_NUM -> payment.cardNum = extras.get(key).toString()
                KiccCallbackParam.CARD_NAME -> payment.cardName = extras.get(key).toString()
                KiccCallbackParam.ISSUER_CODE -> payment.issuerCode = extras.get(key).toString()
                KiccCallbackParam.TOTAL_AMOUNT -> payment.totalAmount = extras.get(key).toString()
                KiccCallbackParam.TAX -> payment.tax = extras.get(key).toString()
                KiccCallbackParam.TIP -> payment.tip = extras.get(key).toString()
                KiccCallbackParam.INSTALLMENT -> payment.installment = extras.get(key).toString()
                KiccCallbackParam.RESULT_CODE -> payment.resultCode = extras.get(key).toString()
                KiccCallbackParam.RESULT_MSG -> payment.resultMsg = extras.get(key).toString()
                KiccCallbackParam.APPROVAL_NUM -> payment.approvalNum = extras.get(key).toString()
                KiccCallbackParam.APPROVAL_DATE -> payment.approvalDate = extras.get(key).toString().toLongOrNull()
                KiccCallbackParam.ACQUIRER_CODE -> payment.acquirerCode = extras.get(key).toString()
                KiccCallbackParam.ACQUIRER_NAME -> payment.acquirerName = extras.get(key).toString()
                KiccCallbackParam.AD1 -> payment.ad1 = extras.get(key).toString()
                KiccCallbackParam.AD2 -> payment.ad2 = extras.get(key).toString()
                KiccCallbackParam.MERCHANT_NUM -> payment.merchantNum = extras.get(key).toString()
                KiccCallbackParam.SHOP_TID -> payment.shopTid = extras.get(key).toString()
                KiccCallbackParam.SHOP_BIZ_NUM -> payment.shopBizNum = extras.get(key).toString()
                KiccCallbackParam.ADD_FIELD -> payment.addField = extras.get(key).toString()
                KiccCallbackParam.NOTICE -> payment.notice = extras.get(key).toString()
                KiccCallbackParam.CASHAMOUNT -> payment.cashAmount = extras.get(key).toString()
                KiccCallbackParam.TPK -> payment.tpk = extras.get(key).toString()
                KiccCallbackParam.TRAN_SERIALNO -> payment.tranSerialNo = extras.get(key).toString()
                KiccCallbackParam.SHOP_NAME -> payment.shopName = extras.get(key).toString()
                KiccCallbackParam.SHOP_TEL -> payment.shopTel = extras.get(key).toString()
                KiccCallbackParam.SHOP_ADDRESS -> payment.shopAddress = extras.get(key).toString()
                KiccCallbackParam.SHOP_OWNER -> payment.shopOwner = extras.get(key).toString()
            }
        }
        repository.addPayment(mRealm, payment = payment){
            _onRefund.postValue(it)
        }

    }

    fun setInRefund(id: Int){
        repository.updateIsRefund(mRealm, id){
            if(it) _onRefundSuccess.postValue(Unit)
            else _onDbError.postValue(Unit)
        }
    }



    fun refundFailed(message: String){
        _onRefundFailed.postValue(message)
    }

    fun close(){
        _onClose.postValue(Unit)
    }

    fun print(item: Payment){
        val cartList = ArrayList<Cart>()
        /*cartRepository.getCartList(mRealm, item.orderId){
            cartList.addAll(it)
            cartList.map {
                DebugUtils.setLog(TAG, "cart : ${it.name}")
            }

            if(cartList.size > 0){
                val param = hashMapOf<String, Any>()
                param["cart"] = cartList
                param["payment"] = item
                //_onPrint.postValue(param)
            }

        }*/
        cartRepository.getCartList(mRealm, item.orderId).map {
            val cart = it
            cartList.add(cart)
        }


        //cartList.addAll(cartRepository.getCartList(mRealm, item.orderId))


        val param = hashMapOf<String, Any>()
        param["cart"] = cartList
        param["payment"] = item
        _onPrint.postValue(param)
    }
}