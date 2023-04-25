package kr.co.releasetech.kiosk.view.fragment.goods

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmResults
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.model.repository.CategoryRepository
import kr.co.releasetech.kiosk.model.repository.GoodsRepository
import kr.co.releasetech.kiosk.view.base.BaseViewModel

class GoodsViewModel(private val cateRepository: CategoryRepository, private val goodsRepository: GoodsRepository): BaseViewModel() {


    private val _onAddClick = MutableLiveData<Unit>()
    val onAddClick: LiveData<Unit>
        get() = _onAddClick

    private val _onDeleteClick = MutableLiveData<Goods>()
    val onDeleteClick: LiveData<Goods>
        get() = _onDeleteClick

    private val _onModifyClick = MutableLiveData<Goods>()
    val onModifyClick: LiveData<Goods>
        get() = _onModifyClick

    private val _goodsList = MutableLiveData<RealmResults<Goods>>()
    val goodsList: LiveData<RealmResults<Goods>>
        get() = _goodsList


    private val _onCategoryList = MutableLiveData<RealmResults<Category>>()
    val onCategoryList: LiveData<RealmResults<Category>>
        get() = _onCategoryList

    fun addGoods(){
        _onAddClick.postValue(Unit)
    }

    fun getCategoryList(){
        _onCategoryList.postValue(cateRepository.getCategoryList(mRealm))
    }

    fun getGoodsList(){
        _goodsList.postValue(goodsRepository.getGoodsList(mRealm))
    }

    fun deleteGoods(goods: Goods){
        _onDeleteClick.postValue(goods)
    }

    fun modifyGoods(goods: Goods){
        _onModifyClick.postValue(goods)
    }

    fun getCategory(categoryId: Int): Category?{
        return cateRepository.getCategory(mRealm, categoryId)
    }

}