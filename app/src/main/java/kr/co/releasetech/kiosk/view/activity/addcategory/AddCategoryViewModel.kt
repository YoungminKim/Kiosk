package kr.co.releasetech.kiosk.view.activity.addcategory

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.model.repository.CategoryRepository
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class AddCategoryViewModel(val repository: CategoryRepository): BaseViewModel() {
    companion object{
        private const val TAG = "AddCategoryViewModel"
    }

    val nameField = ObservableField("")

    private val _onAddCategory = MutableLiveData<Int>()
    val onAddCategory: LiveData<Int>
        get() = _onAddCategory

    fun addCategory(category: Category?, index: Int){
        var message = -1
        var name = nameField.get()
        if(name.isNullOrEmpty()){
            message = R.string.plz_input_category_name
        }else{
            if(category == null) repository.addCategory(mRealm, index, name){ isSuccess ->
                message = if(isSuccess) message else R.string.db_error_message
            }
            else {
                repository.updateCategory(mRealm, category, name){ isSuccess ->
                    message = if(isSuccess) message else R.string.db_error_message
                }
            }

        }

        _onAddCategory.postValue(message)
    }

}