package kr.co.releasetech.kiosk.view.activity.goodsdetail

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.releasetech.kiosk.model.realm.Cart
import kr.co.releasetech.kiosk.model.OptionItem
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.model.realm.Option
import kr.co.releasetech.kiosk.model.repository.OptionRepository
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.activity.option.OptionViewModel
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class GoodsDetailViewModel(val repository: OptionRepository) : BaseViewModel() {
    companion object {
        private const val TAG = "GoodsDetailViewModel"
    }

    var mCount = 1
    var mTotalPrice = 0
    var mPrice = 0


    val totalPriceField = ObservableField("${mTotalPrice}원")
    val quantityField = ObservableField("$mCount")



    private val _onPrice = MutableLiveData<Int>()
    val onPrice: LiveData<Int>
        get() = _onPrice

    private val _onReStartTimer = MutableLiveData<Unit>()
    val onRestartTimer: LiveData<Unit>
        get() = _onReStartTimer

    private val _onPauseTimer = MutableLiveData<Unit>()
    val onPauseTimer: LiveData<Unit>
        get() = _onPauseTimer


    private val _onShowOption = MutableLiveData<Unit>()
    val onShowOption: LiveData<Unit>
        get() = _onShowOption

    private val _onSelectComplete = MutableLiveData<Unit>()
    val onSelectComplete: LiveData<Unit>
        get() = _onSelectComplete


    private val _onSetCart = MutableLiveData<Cart?>()
    val onSetCart : LiveData<Cart?>
        get() = _onSetCart

    private val _onSetCount = MutableLiveData<Int>()
    val onSetCount: LiveData<Int>
        get() = _onSetCount

    fun setPrice(price: Int) {
        mPrice = price
        mTotalPrice = mPrice * mCount
    }

    fun getOptionList(optionCategorys: String, cart: Cart?) {
        if (optionCategorys.isNotEmpty()) {
            var optionIds = ""
            var optionNames = ""
            var optionPrices = ""
            val optionCategoryList = optionCategorys.split(",")
            optionCategoryList.map {
                val category = repository.getOptionCategory(mRealm, it.toInt())

                category?.let { category ->
                    DebugUtils.setLog(TAG, "category : ${category.name}")

                    if (category.isSingle) {
                        val singleOption = repository.getOption(mRealm, category.id)
                        singleOption[0]?.price?.let { price ->
                            optionPrices += price
                            mPrice += price
                        }
                        singleOption[0]?.name?.let { name -> optionNames += "$name," }
                        singleOption[0]?.id?.let { id -> optionIds += "$id," }
                    }
                }
            }

            if (optionIds.isNotEmpty()) optionIds = optionIds.substring(0, optionIds.length - 1)
            if (optionNames.isNotEmpty()) optionNames = optionNames.substring(0, optionNames.length - 1)
            if (optionPrices.isNotEmpty()) optionPrices = optionPrices.substring(0, optionPrices.length - 1)

            cart?.let { cart ->
                cart.optionIds = optionIds
                cart.optionNames = optionNames
                cart.optionPrice = optionPrices
                cart.price = mPrice

                mCount = cart.quantity
                quantityField.set(mCount.toString())

            }

            mTotalPrice = mPrice * mCount
            cart?.totalPrice = mTotalPrice

            _onSetCart.postValue(cart)
            _onPrice.postValue(mTotalPrice)
        }
    }


    fun modifyCart(cart: Cart?){
        _onPauseTimer.postValue(Unit)
        mCount = cart?.quantity!!
        quantityField.set(cart?.quantity.toString())
        _onPrice.postValue(cart?.totalPrice)
    }


    fun showOption(){
        _onPauseTimer.postValue(Unit)
        _onShowOption.postValue(Unit)
    }




    fun setCount(isPlus: Boolean) {
        _onReStartTimer.postValue(Unit)
        if (isPlus) ++mCount
        else {
            if (1 < mCount) --mCount
        }


        quantityField.set(mCount.toString())
        mTotalPrice = mPrice * mCount
        _onSetCount.postValue(mCount)
        _onPrice.postValue(mTotalPrice)
    }


    fun selectComplete(){
        _onSelectComplete.postValue(Unit)
    }



}