package kr.co.releasetech.kiosk.view.fragment.option

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmResults
import io.realm.RealmSet
import kr.co.releasetech.kiosk.model.realm.OptionCategory
import kr.co.releasetech.kiosk.model.repository.GoodsRepository
import kr.co.releasetech.kiosk.model.repository.OptionRepository
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class OptionViewModel(private val optionRepository: OptionRepository, private val goodsRepository: GoodsRepository): BaseViewModel() {
    private val _onAddOption = MutableLiveData<OptionCategory?>()
    val onAddOption: LiveData<OptionCategory?>
        get() = _onAddOption

    private val _optionCategoryList = MutableLiveData<RealmResults<OptionCategory>>()
    val optionCategoryList:LiveData<RealmResults<OptionCategory>>
        get() = _optionCategoryList

    private val _removeItem = MutableLiveData<OptionCategory>()
    val removeItem:LiveData<OptionCategory>
        get() = _removeItem

    fun addOption(){
        _onAddOption.postValue(null)
    }

    fun addOption(item : OptionCategory? = null){
        _onAddOption.postValue(item)
    }

    fun getList(){
        _optionCategoryList.postValue(optionRepository.getOptionCategorys(mRealm))
    }

    fun removeOption(item: OptionCategory){
        val optionCategoryId = item.id

        goodsRepository.deleteGoodsOption(mRealm, optionCategoryId){ isSuccess ->

            if(isSuccess) {
                optionRepository.deleteOptionCategoryAll(mRealm, optionCategoryId){ isSuccess ->
                    if(isSuccess)_removeItem.postValue(item)
                    else _onDbError.postValue(Unit)
                }

            }
            else _onDbError.postValue(Unit)
        }
    }
}