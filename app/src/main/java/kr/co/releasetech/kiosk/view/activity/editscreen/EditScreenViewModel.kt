package kr.co.releasetech.kiosk.view.activity.editscreen

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmResults
import kr.co.releasetech.kiosk.AppConst
import kr.co.releasetech.kiosk.model.realm.EditScreenStandbyBg
import kr.co.releasetech.kiosk.model.realm.ScreenSetting
import kr.co.releasetech.kiosk.model.repository.ScreenSettingRepository
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class EditScreenViewModel(val repository: ScreenSettingRepository): BaseViewModel() {
    companion object{
        private const val TAG = "EditScreenViewModel"
    }

    val standbyImageSecondField = ObservableField("30")
    val orderScreenSecondField = ObservableField("60")
    val useStandbyField = ObservableField(true)
    val useSOrderField = ObservableField(true)
    val bgColorField = ObservableField("#FFFFFFFF")

    private val _onOpenGallery = MutableLiveData<Unit>()
    val onOpenGallery: LiveData<Unit>
        get() = _onOpenGallery

    private val _onAddStandbyBgItem = MutableLiveData<EditScreenStandbyBg>()
    val onAddStandbyBgItem: LiveData<EditScreenStandbyBg>
        get() = _onAddStandbyBgItem

    private val _onStandbyBgList = MutableLiveData<RealmResults<EditScreenStandbyBg>>()
    val onStandbyBgList: LiveData<RealmResults<EditScreenStandbyBg>>
        get() = _onStandbyBgList

    private val _onShowStandbyBg = MutableLiveData<EditScreenStandbyBg>()
    val onShowStandbyBg: LiveData<EditScreenStandbyBg>
        get() = _onShowStandbyBg


    private val _onMoveItem = MutableLiveData<Unit>()
    val onMoveItem: LiveData<Unit>
        get() = _onMoveItem

    private val _onEmptyStandbyBg = MutableLiveData<Unit>()
    val onEmptyStandbyBg: LiveData<Unit>
        get() = _onEmptyStandbyBg

    private val _onIsNotAllow = MutableLiveData<Unit>()
    val onIsNotAllow: LiveData<Unit>
        get() = _onIsNotAllow


    private val _isCheckedUseStandby = MutableLiveData<Boolean>()
    val isCheckedUseStandby: LiveData<Boolean>
        get() = _isCheckedUseStandby

    private val _onSelectBgColor = MutableLiveData<Unit>()
    val onSelectBgColor: LiveData<Unit>
        get() = _onSelectBgColor

    private val _onSelectLogo = MutableLiveData<Unit>()
    val onSelectLogo: LiveData<Unit>
        get() = _onSelectLogo

    private val _onLogoSaved = MutableLiveData<Unit>()
    val onLogoSaved : LiveData<Unit>
        get() = _onLogoSaved

    private val _onSetLogoImage = MutableLiveData<String?>()
    val onSetLogoImage : LiveData<String?>
        get () = _onSetLogoImage


    private val _onSelectTheme = MutableLiveData<Int>()
    val onSelectTheme: LiveData<Int>
        get() = _onSelectTheme

    fun openGallery() {
        _onOpenGallery.postValue(Unit)
    }

    fun addStandbyBgItem(fileName: String, index: Int) {


        val fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length)

        var isNotAllow = false
        var isVideo = false
        for (extension in AppConst.VIDEO_EXTENSIONS) {
            if (fileExtension == extension) {
                isVideo = true
                isNotAllow = true
                break
            }
        }

        for (extension in AppConst.IMG_EXTENSIONS){
            if(fileExtension == extension) {
                isNotAllow = true
                break
            }
        }

        if(isNotAllow){
            val item = EditScreenStandbyBg(isVideo = isVideo, index = index, fileName = fileName)
            repository.addStandbyBgItem(mRealm, item) {
                if (it) _onAddStandbyBgItem.postValue(item)
            }
        }else _onIsNotAllow.postValue(Unit)



    }

    fun getStandbyBgList() {
        _onStandbyBgList.postValue(repository.getStandbyBgList(mRealm))
    }

    fun showStandbyBg(item: EditScreenStandbyBg) {
        _onShowStandbyBg.postValue(item)
    }

    fun moveItem(list: ArrayList<EditScreenStandbyBg>) {
        repository.updateIndex(mRealm, list) {
            _onMoveItem.postValue(Unit)
        }

    }

    fun removeItem(item: EditScreenStandbyBg) {
        repository.deleteStandbyBg(mRealm, item.id) {
            if(!it) _onDbError.postValue(Unit)
        }
    }

    fun emptyStandbyBg() {
        _onEmptyStandbyBg.postValue(Unit)
    }



    fun getScreenSetting(){
        val result = repository.getScreenSetting(mRealm)
        if(result == null){
            repository.addScreenSetting(mRealm)
        }else{
            result.let {
                DebugUtils.setLog(TAG, "getScreenSetting : ${it.id}")
                useStandbyField.set(it.useStandbyScreen)
                useSOrderField.set(it.useSelectOrderScreen)
                orderScreenSecondField.set(it.orderScreenWaitSecond.toString())
                standbyImageSecondField.set(it.standbyScreenImageSecond.toString())
                bgColorField.set(it.bgHexCode)
                _onSetLogoImage.postValue(it.logoImage)
                _onSelectTheme.postValue(it.themeType)
            }
        }

    }

    fun isCheckedUseStandby(checked: Boolean){
        useStandbyField.set(checked)
        repository.updateScreenSettingUseStandby(mRealm, checked){
            if(it)_isCheckedUseStandby.postValue(checked)
            else _onDbError.postValue(Unit)
        }
    }

    fun isCheckedUseSelectOrder(checked: Boolean){
        useSOrderField.set(checked)
        repository.updateScreenSettingUseSelectOrder(mRealm, checked){
            if(!it) _onDbError.postValue(Unit)
        }
    }

    fun setImageStandby(){
        var second = 10
        standbyImageSecondField.get()?.let {
            second = it.toInt()
        }
        repository.updateScreenSettingImageDelay(mRealm, second){
            if (!it) _onDbError.postValue(Unit)
        }
    }

    fun setOrderScreenWait(){
        var second = 10
        orderScreenSecondField.get()?.let {
            second = it.toInt()
        }
        repository.updateScreenSettingOrderScreenWaitSecond(mRealm, second){
            if (!it) _onDbError.postValue(Unit)
        }
    }

    fun selectBgColor(){
        _onSelectBgColor.postValue(Unit)
    }

    fun setBgColor(color: Int, hexCode: String?){
        repository.updateBgColor(mRealm, color, hexCode){
            if(it) bgColorField.set(hexCode)
            else _onDbError.postValue(Unit)
        }
    }

    fun setLogo(){
        _onSelectLogo.postValue(Unit)
    }

    fun setLogoImage(img: String?){
        repository.updateLogoImage(mRealm, img){
            if(it) _onLogoSaved.postValue(Unit)
            else _onDbError.postValue(Unit)
        }
    }

    fun selectTheme(position: Int){
        repository.updateTheme(mRealm, position){
            if(it) _onSelectTheme.postValue(position)
            else _onDbError.postValue(Unit)
        }

    }
}