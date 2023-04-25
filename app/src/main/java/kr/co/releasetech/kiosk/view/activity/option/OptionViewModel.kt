package kr.co.releasetech.kiosk.view.activity.option

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.releasetech.kiosk.model.OptionItem
import kr.co.releasetech.kiosk.model.realm.Cart
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.model.realm.Option
import kr.co.releasetech.kiosk.model.repository.OptionRepository
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.activity.goodsdetail.GoodsDetailViewModel
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class OptionViewModel(val repository: OptionRepository) : BaseViewModel() {
    companion object {
        private const val TAG = "OptionViewModel"
    }

    private val _onOptionList = MutableLiveData<ArrayList<OptionItem>>()
    val onOptionList: LiveData<ArrayList<OptionItem>>
        get() = _onOptionList


    private val mOptionList = ArrayList<Option>()

    private val _onCancel = MutableLiveData<Unit>()
    val onCancel: LiveData<Unit>
        get() = _onCancel

    private val _onAddCart = MutableLiveData<Cart?>()
    val onAddCart: LiveData<Cart?>
        get() = _onAddCart

    private val _onReStartTimer = MutableLiveData<Unit>()
    val onRestartTimer : LiveData<Unit>
        get() = _onReStartTimer

    private val _onPauseTimer = MutableLiveData<Unit>()
    val onPauseTimer : LiveData<Unit>
        get() = _onPauseTimer

    private val selectMultipleOptions = hashMapOf<Int, Boolean>()
    private val singleOptions = hashMapOf<Int, Option>()



    fun setSingleMap(categoryId: Int, item: Option) {
        _onReStartTimer.postValue(Unit)
        singleOptions[categoryId] = item

    }

    fun setMultipleOptionPrice(item: Option, isSelected: Boolean) {
        _onReStartTimer.postValue(Unit)
        selectMultipleOptions[item.id] = isSelected
    }

    fun cancel() {
        _onPauseTimer.postValue(Unit)
        _onCancel.postValue(Unit)
    }


    fun addCart(goods: Goods, cart: Cart?) {
        _onPauseTimer.postValue(Unit)
        var updateOptionIds = ""
        var updateOptionNames = ""
        var updateOptionPrice = ""
        var singlePrice = 0
        var multiPrice = 0
        singleOptions.map {
            DebugUtils.setLog(TAG, "single : ${it.value.name}")
            updateOptionIds += "${it.value.id},"
            updateOptionNames += "${it.value.name},"
            updateOptionPrice += "${it.value.price},"
            singlePrice += it.value.price

        }

        selectMultipleOptions.filter { it.value }.map { map ->
            updateOptionIds += "${map.key},"

            mOptionList.filter { it.id == map.key }.map {
                updateOptionNames += "${it.name},"
                updateOptionPrice += "${it.price},"
                multiPrice += it.price
            }

        }


        if (updateOptionIds.isNotEmpty()) updateOptionIds = updateOptionIds.substring(0, updateOptionIds.length - 1)
        if (updateOptionNames.isNotEmpty()) updateOptionNames = updateOptionNames.substring(0, updateOptionNames.length - 1)
        if(updateOptionPrice.isNotEmpty()) updateOptionPrice = updateOptionPrice.substring(0, updateOptionPrice.length - 1)




        cart?.apply {
            optionIds = updateOptionIds
            optionNames = updateOptionNames
            optionPrice = updateOptionPrice

            price = singlePrice + multiPrice + goods.price
            totalPrice = (singlePrice + multiPrice + goods.price) * quantity
        }

        _onAddCart.postValue(cart)
    }

    fun getOptionList(optionCategorys: String, cart: Cart) {
        if (optionCategorys.isNotEmpty()) {
            DebugUtils.setLog(TAG, "optionCategorys : $optionCategorys")
            val list = ArrayList<OptionItem>()

            val optionCategoryList = optionCategorys.split(",")
            val cartIds = cart.optionIds.split(",")

            DebugUtils.setLog(TAG, "optionIds : ${cart.optionIds}")

            optionCategoryList.map {
                val category = repository.getOptionCategory(mRealm, it.toInt())

                category?.let { category ->
                    val optionList = ArrayList<Option>()
                    optionList.addAll(repository.getOption(mRealm, category.id))
                    mOptionList.addAll(optionList)



                    if (category.isSingle) {

                        //singleOptions[category.id] = optionList[0]
                        var selectPosition = 0
                        for (i in optionList.indices){
                            val option = optionList[i]
                            cartIds.filter { cartId -> option.id == cartId.toInt() }.map {
                                singleOptions[category.id] = option
                                selectPosition = i
                            }
                        }
                        val optionItem = OptionItem(category, optionList, singlePosition = selectPosition)
                        list.add(optionItem)

                    } else {
                        optionList.map { item ->
                            selectMultipleOptions[item.id] = false
                            cartIds.filter { cartId -> item.id == cartId.toInt() }.map {
                                selectMultipleOptions[item.id] = true
                            }
                        }
                        val optionItem = OptionItem(category, optionList, selectMultipleOptions = selectMultipleOptions)
                        list.add(optionItem)

                    }
                }
            }

            _onOptionList.postValue(list)

        }
    }
}