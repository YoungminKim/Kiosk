package kr.co.releasetech.kiosk.view.activity.adminlogin

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kr.co.releasetech.kiosk.model.repository.AdminRepository
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class AdminLoginViewModel(private val repository: AdminRepository): BaseViewModel() {
    companion object{
        private const val TAG = "AdminLoginViewModel"
    }
    val passField =  ObservableField("")

    private val masterPass = MutableLiveData<String>()
    private val staffPass = MutableLiveData<String>()

    private val _onLogin = MutableLiveData<Unit>()
    val onLogin:LiveData<Unit>
        get() = _onLogin


    private val _wrongPass = MutableLiveData<Unit>()
    val wrongPass: LiveData<Unit>
        get() = _wrongPass

    private val _emptyPass = MutableLiveData<Unit>()
    val emptyPass: LiveData<Unit>
        get() = _emptyPass

    val adminPassModel = repository.adminPreferencesFlow.asLiveData()

    fun openAdminLogin(){
        DebugUtils.setLog(TAG, "passField : ${passField.get()}")

        if(passField.get() ==  masterPass.value || passField.get() == staffPass.value){
            _onLogin.postValue(Unit)
        }else if(passField.get().isNullOrEmpty()){
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