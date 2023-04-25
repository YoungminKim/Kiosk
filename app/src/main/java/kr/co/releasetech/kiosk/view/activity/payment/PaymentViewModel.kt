package kr.co.releasetech.kiosk.view.activity.payment

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmResults
import kr.co.releasetech.kiosk.model.realm.Payment
import kr.co.releasetech.kiosk.model.repository.PaymentRepository
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class PaymentViewModel(val repository: PaymentRepository): BaseViewModel() {
    companion object{
        private const val TAG = "PaymentViewModel"
    }

    val isApprovalFiled = ObservableField<Boolean>(true)

    private val _paymentList = MutableLiveData<RealmResults<Payment>>()
    val paymentList: LiveData<RealmResults<Payment>>
        get() = _paymentList

    private val _onPaymentDetail = MutableLiveData<Payment>()
    val onPaymentDetail: LiveData<Payment>
        get() = _onPaymentDetail

    private val _onIsApproval = MutableLiveData<Boolean>()
    val onIsApproval: LiveData<Boolean>
        get() = _onIsApproval

    fun getList(){
        _paymentList.postValue(repository.getPaymentList(mRealm))
    }

    fun search(startDate: Long, endDate: Long){
        DebugUtils.setLog(TAG, "startDate : $startDate , endDate : $endDate")

        var transType = "D1"
        isApprovalFiled.get()?.let { if(!it) transType = "D4" }
        _paymentList.postValue(repository.getPaymentList(mRealm, startDate, endDate, transType))
    }

    fun goDetail(item: Payment){
        _onPaymentDetail.postValue(item)
    }


    fun choiceType(isApproval: Boolean){
        isApprovalFiled.set(isApproval)
        _onIsApproval.postValue(isApproval)
    }
}