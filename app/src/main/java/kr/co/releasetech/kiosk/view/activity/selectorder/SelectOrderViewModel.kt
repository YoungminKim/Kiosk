package kr.co.releasetech.kiosk.view.activity.selectorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class SelectOrderViewModel: BaseViewModel() {
    private val _onIsTakeout = MutableLiveData<Boolean>()
    val onIsTakeout: LiveData<Boolean>
        get() = _onIsTakeout


    private val _stopLockTask = MutableLiveData<Unit>()
    val stopLockTask: LiveData<Unit>
        get() = _stopLockTask


    fun isTakeout(isTakeout: Boolean){
        _onIsTakeout.postValue(isTakeout)
    }


    fun stopLockTask(){
        _stopLockTask.postValue(Unit)
    }

}