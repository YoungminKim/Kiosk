package kr.co.releasetech.kiosk.model.repository

import io.realm.Realm
import io.realm.RealmResults
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.utils.DebugUtils

class GoodsRepository {
    companion object{
        private const val TAG = "GoodsRepository"
        const val GOODS_ID = "id"
    }
    fun getGoodsList(realm: Realm): RealmResults<Goods> = realm.where(Goods::class.java).findAll().sort("categoryId")

    private fun getMaxId(realm: Realm): Int {
        val maxId = realm.where(Goods::class.java).max(GOODS_ID)
        return if (maxId == null) 0 else maxId.toInt() + 1
    }

    fun addGoods(realm: Realm, path: String, categoryId:Int, name: String, price: Int, description: String, optionCategoryIds: String, onSuccess: ((Boolean) -> Unit)){
        val id = getMaxId(realm)
        val goods = Goods(id = id, imgUrl = path, categoryId = categoryId, name = name, price = price, description= description, optionCategoryIds = optionCategoryIds)

        realm.executeTransactionAsync(
            {
                it.copyToRealm(goods)

            },
            {
                DebugUtils.setLog(TAG, "add onSuccess!!!")
                onSuccess.invoke(true)
            },
            {
                DebugUtils.setLog(TAG, "add onFailed!!!")
                onSuccess.invoke(false)
            })
    }

    fun deleteGoods(realm: Realm, goods: Goods, onSuccess: ((Boolean) -> Unit)){
        realm.executeTransactionAsync(
            {

                it.where(Goods::class.java).equalTo(GOODS_ID, goods.id).findFirst()?.let{ goods ->
                    DebugUtils.setLog(TAG, "delete goods : $goods")
                    goods.deleteFromRealm()
                }
            },
            {
                DebugUtils.setLog(TAG, "delete onSuccess!!!")
                onSuccess.invoke(true)
            },
            { e ->
                DebugUtils.setLog(TAG, "delete onFailed!!! ${e.localizedMessage}")
                onSuccess.invoke(false)
            })
    }



    fun deleteGoodsOption(realm: Realm, optionCategoryId: Int, onSuccess: ((Boolean) -> Unit)){
        realm.executeTransactionAsync(
            {
                it.where(Goods::class.java).findAll()?.let { goodsList ->
                    goodsList.map { goods ->
                        if(goods.optionCategoryIds.isNotEmpty()){
                            val optionCategoryIds = goods.optionCategoryIds.split(",")
                            var newOptionCategoryIds = ""
                            optionCategoryIds.filter { id -> id.toInt() != optionCategoryId }.map { str ->
                                newOptionCategoryIds += "$str,"
                            }
                            if(newOptionCategoryIds.isNotEmpty()) newOptionCategoryIds = newOptionCategoryIds.substring(0, newOptionCategoryIds.length - 1)
                            if(goods.optionCategoryIds != newOptionCategoryIds) goods.optionCategoryIds = newOptionCategoryIds
                        }
                    }

                }
            },
            {
                DebugUtils.setLog(TAG, "deleteGoodsOption onSuccess!!!")
                onSuccess.invoke(true)
            },
            { e ->
                DebugUtils.setLog(TAG, "deleteGoodsOption onFailed!!! ${e.localizedMessage}")
                onSuccess.invoke(false)
            })
    }

    fun updateGoods(realm: Realm, item: Goods, onSuccess: ((Boolean) -> Unit)){
        realm.executeTransactionAsync(
            {
                it.where(Goods::class.java).equalTo(GOODS_ID, item.id).findFirst()?.let{ goods ->
                    DebugUtils.setLog(TAG, "update goods : $goods")

                    goods.name = item.name
                    goods.imgUrl = item.imgUrl
                    goods.categoryId = item.categoryId
                    goods.optionCategoryIds = item.optionCategoryIds
                }
            },
            {
                DebugUtils.setLog(TAG, "update onSuccess!!!")
                onSuccess.invoke(true)
            },
            { e ->
                DebugUtils.setLog(TAG, "update onFailed!!! ${e.localizedMessage}")
                onSuccess.invoke(false)
            })
    }

}