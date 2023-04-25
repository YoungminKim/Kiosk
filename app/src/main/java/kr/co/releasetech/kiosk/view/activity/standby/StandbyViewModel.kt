package kr.co.releasetech.kiosk.view.activity.standby

import androidx.lifecycle.LiveData
import kr.co.releasetech.kiosk.view.base.BaseViewModel
import androidx.lifecycle.MutableLiveData
import kr.co.releasetech.kiosk.model.realm.EditScreenStandbyBg
import kr.co.releasetech.kiosk.model.repository.ScreenSettingRepository

class StandbyViewModel(val repository: ScreenSettingRepository): BaseViewModel() {

    private val _onMain = MutableLiveData<Unit>()
    val onMain: LiveData<Unit>
        get() = _onMain

    private val _stopLockTask = MutableLiveData<Unit>()
    val stopLockTask: LiveData<Unit>
        get() = _stopLockTask

    private val _onBgList = MutableLiveData<ArrayList<EditScreenStandbyBg>>()
    val onBgList: LiveData<ArrayList<EditScreenStandbyBg>>
        get() = _onBgList

    fun goMain(){
        _onMain.postValue(Unit)
    }


    fun stopLockTask(){
        _stopLockTask.postValue(Unit)
    }

    fun getBgList(){
        val list = arrayListOf<EditScreenStandbyBg>()
        list.addAll(repository.getStandbyBgList(mRealm))
        _onBgList.postValue(list)

    }
}