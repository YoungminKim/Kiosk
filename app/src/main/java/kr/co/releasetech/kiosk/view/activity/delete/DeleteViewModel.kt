package kr.co.releasetech.kiosk.view.activity.delete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.model.repository.CategoryRepository
import kr.co.releasetech.kiosk.model.repository.GoodsRepository
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class DeleteViewModel(private val cateRepository: CategoryRepository, private val goodsRepository: GoodsRepository): BaseViewModel() {
    companion object{
        const val TAG = "DeleteViewModel"
    }
    private val _onConfirmClick = MutableLiveData<Boolean>()
    val onConfirmClick : LiveData<Boolean>
    get() = _onConfirmClick

    private val _onCancelClick = MutableLiveData<Unit>()
    val onCancelClick: LiveData<Unit>
    get() = _onCancelClick

    fun removeCategory(category: Category?){
        category?.let {
            cateRepository.deleteCategory(mRealm, category){
                _onConfirmClick.postValue(it)
            }
        }

    }

    fun removeGoods(goods: Goods?){
        goods?.let { goods ->
            DebugUtils.setLog(TAG, "goods: ${goods.name}")
            goodsRepository.deleteGoods(mRealm, goods){
                _onConfirmClick.postValue(it)
            }
        }

    }


    fun onClose(){
        _onCancelClick.postValue(Unit)
    }
}