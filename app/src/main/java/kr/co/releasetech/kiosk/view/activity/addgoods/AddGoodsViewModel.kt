package kr.co.releasetech.kiosk.view.activity.addgoods


import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.model.realm.OptionCategory
import kr.co.releasetech.kiosk.model.repository.GoodsRepository
import kr.co.releasetech.kiosk.model.repository.OptionRepository
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewModel
import java.io.File

class AddGoodsViewModel(private val goodsRepository: GoodsRepository, private val optionRepository: OptionRepository): BaseViewModel() {
    companion object{
        private const val TAG = "AddGoodsViewModel"
    }
    private val _onAddGoods = MutableLiveData<Int>()
    val onAddGoods: LiveData<Int>
        get() = _onAddGoods

    private val _onOpenGallery = MutableLiveData<Unit>()
    val onOpenGallery: LiveData<Unit>
        get() = _onOpenGallery

    private val _onShowSelectCategory = MutableLiveData<Unit>()
    val onShowSelectCategory: LiveData<Unit>
        get() = _onShowSelectCategory


    private val _onShowSelectOption = MutableLiveData<Unit>()
    val onShowSelectOption: LiveData<Unit>
        get() = _onShowSelectOption

    private val _optionList = MutableLiveData<ArrayList<OptionCategory>>()
    val optionList: LiveData<ArrayList<OptionCategory>>
        get() = _optionList

    private val _emptyOptions = MutableLiveData<Unit>()
    val emptyOptions: LiveData<Unit>
        get() = _emptyOptions

    val categoryNameField = ObservableField("")
    val nameField = ObservableField("")
    val priceField = ObservableField("0")
    val descriptionField = ObservableField("")

    fun addGoods(categoryId:Int, optionCategoryIds: String, file: File?){
        var message = -1
        val name = nameField.get()
        val price = priceField.get()
        var description = descriptionField.get()!!
        when {
            name.isNullOrEmpty() -> {
                message = R.string.plz_input_goods_name
                _onAddGoods.postValue(message)
            }
            price.isNullOrEmpty() -> {
                message = R.string.plz_input_hot_price
                _onAddGoods.postValue(message)
            }
            file == null -> {
                message = R.string.plz_input_goods_img
                _onAddGoods.postValue(message)
            }
            else -> {
                goodsRepository.addGoods(mRealm, file.absolutePath, categoryId, name, price.toInt(), description, optionCategoryIds) { isSuccess ->
                    message = if(isSuccess){
                        message
                    }else{
                        R.string.db_error_message
                    }

                    _onAddGoods.postValue(message)
                }
            }
        }


    }


    fun openGallery(){
        _onOpenGallery.postValue(Unit)
    }


    fun modifyGoods(goods: Goods){

        var message = -1
        val name = nameField.get()
        val price = priceField.get()
        var description = descriptionField.get()!!

        when {
            name.isNullOrEmpty() -> {
                message = R.string.plz_input_goods_name
                _onAddGoods.postValue(message)
            }
            price.isNullOrEmpty() -> {
                message = R.string.plz_input_hot_price
                _onAddGoods.postValue(message)
            }

            else -> {
                goods.name = name
                goods.price = price.toInt()
                goods.description = description
                goodsRepository.updateGoods(mRealm, goods) { isSuccess ->
                    message = if(isSuccess) message else R.string.db_error_message
                    _onAddGoods.postValue(message)
                }
            }
        }


    }


    fun showSelectCategory(){
        _onShowSelectCategory.postValue(Unit)
    }

    fun showSelectOption(){
        _onShowSelectOption.postValue(Unit)
    }

    fun getOptionList(optionCategoryIds: String){
        DebugUtils.setLog(TAG, "optionCategoryIds : $optionCategoryIds")
        val ids = optionCategoryIds.split(",")

        val list = ArrayList<OptionCategory>()
        ids.map {
            optionRepository.getOptionCategory(mRealm, it.toInt())?.let { category -> list.add(category) }
        }

        _optionList.postValue(list)
    }

    fun emptyOptions(){
        _emptyOptions.postValue(Unit)
    }
}