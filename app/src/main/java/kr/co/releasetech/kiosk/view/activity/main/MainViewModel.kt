package kr.co.releasetech.kiosk.view.activity.main

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.releasetech.kiosk.kicc.KiccCallbackParam
import kr.co.releasetech.kiosk.model.realm.Cart
import kr.co.releasetech.kiosk.model.TransData
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.model.realm.Order
import kr.co.releasetech.kiosk.model.realm.Payment
import kr.co.releasetech.kiosk.model.repository.MainRepository
import kr.co.releasetech.kiosk.utils.DateUtils.getDateText
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class MainViewModel(private val repository: MainRepository): BaseViewModel() {
    companion object{
        private const val TAG = "MainViewModel"
    }


    val BgColorField = ObservableField(-1)


    val totalPriceField = ObservableField<String>()

    private val _cateList = MutableLiveData<ArrayList<Category>>()
    val cateList: LiveData<ArrayList<Category>>
        get() = _cateList

    private val _selectCate = MutableLiveData<Int>()
    val selectCate: LiveData<Int>
        get() = _selectCate


    private val _goodsList = MutableLiveData<ArrayList<Goods>>()
    val goodsList: LiveData<ArrayList<Goods>>
        get() = _goodsList

    private val _onShowSelect = MutableLiveData<Goods>()
    val onShowSelect: LiveData<Goods>
        get() = _onShowSelect

    private val _onOrder = MutableLiveData<TransData>()
    val onOrder: LiveData<TransData>
        get() = _onOrder

    private val _onRemoveCart = MutableLiveData<Cart>()
    val onRemoveCart: LiveData<Cart>
        get() = _onRemoveCart

    private val _onPayment = MutableLiveData<Payment>()
    val onPayment: LiveData<Payment>
        get() = _onPayment

    private val _freeOrder = MutableLiveData<Order>()
    val freeOrder: LiveData<Order>
        get() = _freeOrder

    private val _onReStartTimer = MutableLiveData<Unit>()
    val onRestartTimer : LiveData<Unit>
        get() = _onReStartTimer

    private val _onPauseTimer = MutableLiveData<Unit>()
    val onPauseTimer : LiveData<Unit>
        get() = _onPauseTimer

    private val _onAllRemoveCart = MutableLiveData<Unit>()
    val onAllRemoveCart: LiveData<Unit>
        get() = _onAllRemoveCart

    private val _onModifyCart = MutableLiveData<HashMap<String, Any?>>()
    val onModifyCart: LiveData<HashMap<String, Any?>>
        get() = _onModifyCart

    fun getCateList(){
        _cateList.postValue(repository.getCategoryList(mRealm))
    }

    fun getGoodsList(id: Int){
        _onReStartTimer.postValue(Unit)
        _selectCate.postValue(id)
        _goodsList.postValue(repository.getGoodsList(mRealm, id))
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
            if(it)_onPayment.postValue(payment)
            else _onDbError.postValue(Unit)
        }

        _onReStartTimer.postValue(Unit)
    }

    fun order(list: ArrayList<Cart>, isTakeOut: Boolean){
        _onPauseTimer.postValue(Unit)

        var totalAmount = 0
        var names = ""

        val nowTime = System.currentTimeMillis()
        val date = getDateText(nowTime)
        val orderId = repository.getOrderMaxId(mRealm)
        var cartId = repository.getCartMaxId(mRealm)

        for (i in list.indices){
            list[i].id = cartId++
            list[i].orderId = orderId
            val item = list[i]
            totalAmount += item.totalPrice
            names += item.name
            if(i < list.size -1) names += ", "
        }

        val order = Order(id = orderId, name =  names, quantity = list.size, totalAmount = totalAmount, time = nowTime, date = date, isTakeout = isTakeOut)

        if(totalAmount == 0){
            _freeOrder.postValue(order)
        }else{

            repository.addOrder(mRealm, order){ isSuccess ->
                if(isSuccess){
                    repository.addCartList(mRealm, list){
                        if(it){
                            val item = TransData(
                                tranNo = nowTime.toString(),
                                tranType = "D1",
                                totalAmount = totalAmount.toString(),
                                tax = "0",
                                tip = "0",
                                approvalNum="",
                                approvalDate="",
                                TranSerialNo="",
                                cashAmount="00",
                                installment="0",
                                addField="",
                                signFlag="Y"
                            )
                            _onOrder.postValue(item)
                        }else{
                            _onDbError.postValue(Unit)
                        }
                    }
                }else{
                    _onDbError
                }
            }
        }
    }


    fun showSelect(goods: Goods){
        _onReStartTimer.postValue(Unit)
        _onShowSelect.postValue(goods)
    }


    fun removeCartItem(item: Cart){
        _onReStartTimer.postValue(Unit)
        _onRemoveCart.postValue(item)
    }

    fun orderFailed(resultMsg: String){
        _onReStartTimer.postValue(Unit)
        repository.failedOrder(mRealm, resultMsg){
            if(!it) _onDbError.postValue(Unit)
        }
    }

    fun allRemoveCart(){
        _onAllRemoveCart.postValue(Unit)
    }

    fun modifyCart(cart: Cart, position: Int){
        val map = hashMapOf<String, Any?>()
        val goods = repository.getGoods(mRealm, cart.goodsId)
        map["goods"] = goods
        map["cart"] = cart
        map["position"] = position

        _onModifyCart.postValue(map)
    }
}