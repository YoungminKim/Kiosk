package kr.co.releasetech.kiosk.view.fragment.category

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmResults
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.model.repository.CategoryRepository
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class CategoryViewModel(private val repository: CategoryRepository): BaseViewModel() {

    companion object{
        private const val TAG = "CategoryViewModel"
    }

    val isChangePosition = ObservableField<Boolean>(false)

    private val _onAddClick = MutableLiveData<Category?>()
    val onAddClick: LiveData<Category?>
        get() = _onAddClick


    private val _onRemoveItemClick = MutableLiveData<Category?>()
    val onRemoveItemClick: LiveData<Category?>
        get() = _onRemoveItemClick

    private val _categoryList = MutableLiveData<RealmResults<Category>>()
    val categoryList: LiveData<RealmResults<Category>>
        get() = _categoryList

    private val _onMoveItem = MutableLiveData<Unit>()
    val onMoveItem: LiveData<Unit>
        get() = _onMoveItem

    fun showAddDialog(){
        _onAddClick.postValue(null)
    }

    fun getCategoryList(){
        _categoryList.postValue(repository.getCategoryList(mRealm))
    }

    fun removeCategory(category: Category){
        _onRemoveItemClick.postValue(category)
    }


    fun modifyCategory(category: Category){
        _onAddClick.postValue(category)
    }

    fun moveItem(){
        _onMoveItem.postValue(Unit)
    }

    fun updateIndex(items: ArrayList<Category>){
        repository.updateCategoryIndex(mRealm, items){
            if(!it)_onDbError.postValue(Unit)
        }
    }


}