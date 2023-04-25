package kr.co.releasetech.kiosk.view.activity.splash

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.releasetech.kiosk.model.repository.AppSettingRepository
import kr.co.releasetech.kiosk.model.repository.ScreenSettingRepository
import kr.co.releasetech.kiosk.utils.SerialPortUtils
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class SplashViewModel(private val repository: AppSettingRepository, val screenSettingRepository: ScreenSettingRepository): BaseViewModel() {
    companion object{
        const val TAG = "SplashViewModel"
    }
    val appSettingModel = repository.appSettingPreferencesFlow.asLiveData()

    fun checkAppFirstActive(isFirstActive: Boolean){
        viewModelScope.launch{
            if(isFirstActive){
                screenSettingRepository.addScreenSetting(mRealm)
                repository.disableFirstActive()
            }
        }
    }

    fun findPort(){
        SerialPortUtils.findSerialPort()
    }

}