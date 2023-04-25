package kr.co.releasetech.kiosk.view.activity.selectprint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class SelectPrintViewModel : BaseViewModel() {

    private val _onCancel = MutableLiveData<Unit>()
    val onCancel: LiveData<Unit>
        get() = _onCancel

    private val _onConfirm = MutableLiveData<Unit>()
    val onConfirm: LiveData<Unit>
        get() = _onConfirm

    fun cancel(){
        _onCancel.postValue(Unit)
    }

    fun confirm(){
        _onConfirm.postValue(Unit)
    }
}