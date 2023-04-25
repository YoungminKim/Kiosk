package kr.co.releasetech.kiosk.view.activity.menuSetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class MenuSettingViewModel: BaseViewModel() {

    private val _onClose = MutableLiveData<Unit>()
    val onClose: LiveData<Unit>
        get() = _onClose

    fun close(){
        _onClose.postValue(Unit)
    }
}