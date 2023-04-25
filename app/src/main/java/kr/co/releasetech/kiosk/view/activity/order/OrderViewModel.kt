package kr.co.releasetech.kiosk.view.activity.order

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmResults
import kr.co.releasetech.kiosk.model.realm.Order
import kr.co.releasetech.kiosk.model.repository.OrderRepository
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class OrderViewModel(private val repository: OrderRepository): BaseViewModel() {
    companion object{
        private const val TAG = "OrderViewModel"
    }

    val searchField = ObservableField("")

    private val _orderList = MutableLiveData<RealmResults<Order>>()
    val orderList: LiveData<RealmResults<Order>>
        get() = _orderList

    private val _onOderDetail = MutableLiveData<Int>()
    val onOderDetail: LiveData<Int>
        get() = _onOderDetail


    fun getList(){
        _orderList.postValue(repository.getOrderList(mRealm))
    }

    fun search(startDate: Long, endDate: Long){
        searchField.get()?.let {
            _orderList.postValue(repository.getSearchOrderList(mRealm, startDate, endDate, it))
        }
    }


    fun orderDetail(item: Order){
        DebugUtils.setLog(TAG, "item: ${item.id}")
        _onOderDetail.postValue(item.id)
    }

}