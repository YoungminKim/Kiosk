package kr.co.releasetech.kiosk.model.repository

import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.delete
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.utils.DebugUtils
import org.koin.core.KoinComponent
import java.lang.Exception

class CategoryRepository : KoinComponent{
    companion object {
        const val TAG = "CategoryRepository"
        const val CATEGORY_ID = "id"
        const val CATEGORY_INDEX = "index"
    }

    fun getCategoryMaxId(realm: Realm): Int{
        val maxId = realm.where(Category::class.java).max(CATEGORY_ID)
        val id = if (maxId == null) 0 else maxId.toInt() + 1
        return id
    }

    fun getCategoryList(realm: Realm): RealmResults<Category> {
        return realm.where(Category::class.java).sort(CATEGORY_INDEX, Sort.ASCENDING).findAll()
    }


    fun getCategory(realm: Realm, id: Int): Category? {
        return realm.where(Category::class.java).equalTo(CATEGORY_ID, id).findFirst()
    }

    fun addCategory(realm: Realm, index: Int, name: String, onSuccess: ((Boolean) -> Unit)) {
        val id = getCategoryMaxId(realm)
        val category = Category(id, index, name)
        realm.executeTransactionAsync(
            {
                it.copyToRealm(category)
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

    fun deleteCategory(realm: Realm, item: Category, onSuccess: ((Boolean) -> Unit)) {
        realm.executeTransactionAsync(
            {
                it.where(Category::class.java).equalTo(CATEGORY_ID, item.id).findFirst()?.let { category ->
                    DebugUtils.setLog(TAG, "delete category : $category")
                    category.deleteFromRealm()
                }

                it.where(Goods::class.java).equalTo("categoryId", item.id).findAll()?.let{ goodsList ->
                    goodsList.map{ goods ->
                        DebugUtils.setLog(TAG, "delete goods : $goods")
                        goods.deleteFromRealm()
                    }
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

    fun updateCategory(realm: Realm, item: Category, name: String, onSuccess: ((Boolean) -> Unit)){

        realm.executeTransactionAsync(
            {
                it.where(Category::class.java).equalTo(CATEGORY_ID, item.id).findFirst()?.let { category ->
                    DebugUtils.setLog(TAG, "category $category")
                    DebugUtils.setLog(TAG, "item :  ${item.name}")
                    category.name = name
                }
            },
            {
                DebugUtils.setLog(TAG, "updateCategory onSuccess!!!")
                onSuccess.invoke(true)
                realm.close()
            },
            {
                DebugUtils.setLog(TAG, "updateCategory onFailed!!!")
                onSuccess.invoke(false)

            })
    }

    fun updateCategoryIndex(realm: Realm, items: ArrayList<Category>, onSuccess: ((Boolean) -> Unit)){
        DebugUtils.setLog(TAG, "updateCategoryIndex called!!!")

        for (i in items.indices) {
            val id = items[i].id
            realm.executeTransactionAsync(
                { realm ->
                    realm.where(Category::class.java).equalTo(CATEGORY_ID, id).findFirst().let { category ->
                        category?.let {
                            it.index = i
                        }
                    }
                },
                {
                    DebugUtils.setLog(TAG, "updateCategoryIndex onSuccess!!!")
                    onSuccess.invoke(true)
                },
                {
                    it.printStackTrace()
                    DebugUtils.setLog(TAG, "updateCategoryIndex onFailed!!! e: ${it.localizedMessage}")
                    onSuccess.invoke(false)
                })
        }
    }
}