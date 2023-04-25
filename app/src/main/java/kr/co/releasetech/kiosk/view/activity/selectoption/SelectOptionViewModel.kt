package kr.co.releasetech.kiosk.view.activity.selectoption

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmResults
import kr.co.releasetech.kiosk.model.realm.OptionCategory
import kr.co.releasetech.kiosk.model.repository.OptionRepository
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class SelectOptionViewModel(private val repository: OptionRepository): BaseViewModel() {


    private val _optionCategoryList = MutableLiveData<RealmResults<OptionCategory>>()
    val optionCategoryList: LiveData<RealmResults<OptionCategory>>
        get() = _optionCategoryList

    private val _onSelectItem = MutableLiveData<OptionCategory>()
    val onSelectItem: LiveData<OptionCategory>
        get() = _onSelectItem

    fun getList(){
        _optionCategoryList.postValue(repository.getOptionCategorys(mRealm))
    }

    private val _onClose = MutableLiveData<Unit>()
    val onClose: LiveData<Unit>
        get() = _onClose

    fun close(){
        _onClose.postValue(Unit)
    }


    fun selectItem(item: OptionCategory){
        _onSelectItem.postValue(item)
    }
}