package kr.co.releasetech.kiosk.view.activity.addOption

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmResults
import kr.co.releasetech.kiosk.model.OptionDummy
import kr.co.releasetech.kiosk.model.realm.Option
import kr.co.releasetech.kiosk.model.realm.OptionCategory
import kr.co.releasetech.kiosk.model.repository.OptionRepository
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewModel
import java.sql.ResultSet

class AddOptionViewModel(val repository: OptionRepository): BaseViewModel() {
    companion object{
        private const val TAG = "AddOptionViewModel"
    }

    val categoryField = ObservableField("")

    private val _onSaved = MutableLiveData<Unit>()
    val onSaved: LiveData<Unit>
        get() = _onSaved

    private val _onSaveSuccess = MutableLiveData<Unit>()
    val onSaveSuccess: LiveData<Unit>
        get() = _onSaveSuccess

    private val _categoryNameEmpty = MutableLiveData<Unit>()
    val categoryNameEmpty: LiveData<Unit>
        get() = _categoryNameEmpty

    private val _isSingleChoice = MutableLiveData<Boolean>()
    val isSingleChoice: LiveData<Boolean>
        get() = _isSingleChoice

    private val _optionList = MutableLiveData<RealmResults<Option>>()
    val optionList: LiveData<RealmResults<Option>>
        get() = _optionList

    fun saved(){
        _onSaved.postValue(Unit)
    }


    fun modify(){

    }

    fun delete(item: Option){

    }

    fun addOption(isSingle: Boolean, options: ArrayList<OptionDummy>){
        if(categoryField.get().isNullOrEmpty()){
            _categoryNameEmpty.postValue(Unit)
        }else{
            val optionCategoryMaxId = repository.getOptionCategoryMaxId(mRealm)
            val optionCategory = OptionCategory(id = optionCategoryMaxId, name = categoryField.get()!!, isSingle = isSingle)


            repository.addOptionCategory(mRealm, optionCategory) { result ->
                if (result) {
                    var optionMaxId = repository.getOptionMaxId(mRealm)
                    val optionList = ArrayList<Option>()
                    for (i in options.indices){
                        val item = options[i]
                        optionList.add(Option(optionMaxId, i, optionCategoryMaxId, item.name, item.price))
                        ++optionMaxId
                    }


                    repository.addOption(mRealm, optionList) { result ->
                        if(result){
                            _onSaveSuccess.postValue(Unit)
                        }else _onDbError.postValue(Unit)
                    }

                }else _onDbError.postValue(Unit)
            }
        }
    }

    fun modify(id: Int, isSingle: Boolean, updateOptions: ArrayList<OptionDummy>, deleteOptions: ArrayList<OptionDummy>){
        if(categoryField.get().isNullOrEmpty()){
            _categoryNameEmpty.postValue(Unit)
        }else{
            repository.updateOptionCategory(mRealm, id, categoryField.get()!!, isSingle){
                if(it){

                    repository.deleteOption(mRealm, deleteOptions){ }

                    repository.updateOption(mRealm, updateOptions){ result ->
                        DebugUtils.setLog(TAG, "result: $result")
                        if(result){
                            _onSaveSuccess.postValue(Unit)
                        }else{
                            _onDbError.postValue(Unit)
                        }
                    }
                }else{
                    _onDbError.postValue(Unit)
                }

            }
        }
    }

    fun choiceType(value: Boolean){
        _isSingleChoice.postValue(value)
    }


    fun getList(id: Int){
        _optionList.postValue(repository.getOption(mRealm, id))
    }
}