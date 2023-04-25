package kr.co.releasetech.kiosk.view.activity.selectcategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmResults
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.model.repository.CategoryRepository
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class SelectCategoryViewModel(private val repository: CategoryRepository): BaseViewModel() {

    private val _categoryList = MutableLiveData<RealmResults<Category>>()
    val categoryList: LiveData<RealmResults<Category>>
        get() = _categoryList

    private val _onSelectItem = MutableLiveData<Category>()
    val onSelectItem: LiveData<Category>
        get()= _onSelectItem

    fun getCategoryList(){
        _categoryList.postValue(repository.getCategoryList(mRealm))
    }
    private val _onClose = MutableLiveData<Unit>()
    val onClose: LiveData<Unit>
        get() = _onClose

    fun close(){
        _onClose.postValue(Unit)
    }

    fun selectItem(item: Category){
        _onSelectItem.postValue(item)
    }
}