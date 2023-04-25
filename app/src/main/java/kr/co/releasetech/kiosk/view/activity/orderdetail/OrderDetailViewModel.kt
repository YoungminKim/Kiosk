package kr.co.releasetech.kiosk.view.activity.orderdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmResults
import kr.co.releasetech.kiosk.model.realm.Cart
import kr.co.releasetech.kiosk.model.repository.CartRepository
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class OrderDetailViewModel(private val repository: CartRepository): BaseViewModel() {
    companion object{
        private const val TAG = "OrderDetailViewModel"
    }
    private val _onCartList = MutableLiveData<RealmResults<Cart>>()
    val onCartList: LiveData<RealmResults<Cart>>
        get() = _onCartList

    fun getCartList(orderId: Int){
        DebugUtils.setLog(TAG, "orderId : $orderId")
        _onCartList.postValue(repository.getCartList(mRealm, orderId))
    }
}