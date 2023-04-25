package kr.co.releasetech.kiosk.view.activity.adminsetting

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.releasetech.kiosk.model.repository.AdminRepository
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class AdminSettingViewModel(val repository: AdminRepository): BaseViewModel() {

    val nowPassField = ObservableField("")
    val updatePassField = ObservableField("")

    private val _wrongPass = MutableLiveData<Unit>()
    val wrongPass: LiveData<Unit>
        get() = _wrongPass

    private val _emptyPass = MutableLiveData<Unit>()
    val emptyPass: LiveData<Unit>
        get() = _emptyPass

    private val _samePass = MutableLiveData<Unit>()
    val samePass: LiveData<Unit>
        get() = _samePass

    val adminPassModel = repository.adminPreferencesFlow.asLiveData()

    private val masterPass = MutableLiveData<String>()
    private val staffPass = MutableLiveData<String>()

    fun passUpdate(){
        if(nowPassField.get() ==  masterPass.value || nowPassField.get() == staffPass.value) {

            if(staffPass.value == updatePassField.get()){
                _samePass.postValue(Unit)
            }else{
                viewModelScope.launch {
                    updatePassField.get()?.let { repository.updateStaffPass(it) }
                }
            }

        }else if(nowPassField.get().isNullOrEmpty() || updatePassField.get().isNullOrEmpty()){
            _emptyPass.postValue(Unit)
        }else{
            _wrongPass.postValue(Unit)
        }
    }


    fun setPass(master: String, staff: String){
        masterPass.postValue(master)
        staffPass.postValue(staff)
    }
}