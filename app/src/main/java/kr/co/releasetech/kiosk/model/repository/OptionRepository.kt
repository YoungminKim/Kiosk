package kr.co.releasetech.kiosk.model.repository

import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kr.co.releasetech.kiosk.model.OptionDummy
import kr.co.releasetech.kiosk.model.realm.Option
import kr.co.releasetech.kiosk.model.realm.OptionCategory
import kr.co.releasetech.kiosk.utils.DebugUtils
import org.koin.core.KoinComponent

class OptionRepository : KoinComponent {
    companion object{
        private const val TAG = "OptionRepository"
        private const val OPTION_CATEGORY_ID = "id"
        private const val OPTION_ID = "id"
    }

    fun getOptionMaxId(realm: Realm): Int {
        val maxId = realm.where(Option::class.java).max("id")
        return if (maxId == null) 0 else maxId.toInt() + 1
    }

    fun getOptionCategoryMaxId(realm: Realm): Int {
        val maxId = realm.where(OptionCategory::class.java).max("id")
        return if (maxId == null) 0 else maxId.toInt() + 1
    }


    fun addOptionCategory(realm: Realm, optionCategory: OptionCategory, onSuccess: ((Boolean) -> Unit)){
        realm.executeTransactionAsync(
            {
                it.copyToRealm(optionCategory)
            },
            { onSuccess.invoke(true) },
            {
                DebugUtils.setLog(TAG, "e : ${it.printStackTrace()}")
                onSuccess.invoke(false)
            }
        )
    }


    fun addOption(realm: Realm, option: ArrayList<Option>, onSuccess: ((Boolean) -> Unit)){
        realm.executeTransactionAsync(
            {
                it.copyToRealm(option)
            },
            { onSuccess.invoke(true) },
            {
                DebugUtils.setLog(TAG, "e : ${it.printStackTrace()}")
                onSuccess.invoke(false)
            }
        )
    }

    fun updateOptionCategory(realm: Realm, id: Int, name: String, isSingle: Boolean, onSuccess: (Boolean) -> Unit){
        realm.executeTransactionAsync(
            { realm ->
                realm.where(OptionCategory::class.java).equalTo(OPTION_CATEGORY_ID, id).findFirst().let { category ->
                    category?.let {
                        it.name = name
                        it.isSingle = isSingle
                    }
                }
            },
            {
                DebugUtils.setLog(CategoryRepository.TAG, "updateOptionCategory onSuccess!!!")
                onSuccess.invoke(true)
            },
            {
                it.printStackTrace()
                DebugUtils.setLog(CategoryRepository.TAG, "updateOptionCategory onFailed!!! e: ${it.localizedMessage}")
                onSuccess.invoke(false)
            })
    }

    fun deleteOption(realm: Realm, items: ArrayList<OptionDummy>, onSuccess: ((Boolean) -> Unit)){
        for (i in items.indices) {
            if(!items[i].valid) {
                realm.executeTransactionAsync(
                    { realm ->
                        realm.where(Option::class.java).equalTo(OPTION_ID, items[i].id).findFirst()
                            .let { option ->
                                option?.deleteFromRealm()
                            }
                    },
                    {
                        DebugUtils.setLog(TAG, "deleteOption onSuccess!!!")
                        if(i == items.size - 1) onSuccess.invoke(true)
                    },
                    {
                        it.printStackTrace()
                        DebugUtils.setLog(TAG, "deleteOption onFailed!!! e: ${it.localizedMessage}")
                        onSuccess.invoke(false)
                    })

            }
        }
    }

    fun updateOption(realm: Realm, items: ArrayList<OptionDummy>, onSuccess: ((Boolean) -> Unit)){

        for (i in items.indices) {
            val item = items[i]

            DebugUtils.setLog(TAG, "name : ${item.name}")
            DebugUtils.setLog(TAG, "id : ${item.id}")
            realm.executeTransactionAsync(
                { realm ->
                    realm.where(Option::class.java).equalTo(OPTION_ID, item.id).findFirst().let { option ->

                        if(option == null){
                            var optionMaxId = getOptionMaxId(realm)
                            realm.copyToRealm(Option(optionMaxId, item.index, item.optionCategoryId, item.name, item.price))
                        }else{
                            option.name = item.name
                            option.index = i
                            option.price = item.price
                        }

                    }
                },
                {
                    DebugUtils.setLog(TAG, "updateOption onSuccess!!!")
                    if(i == items.size - 1) onSuccess.invoke(true)
                },
                {
                    it.printStackTrace()
                    DebugUtils.setLog(TAG, "updateOption onFailed!!! e: ${it.localizedMessage}")
                    onSuccess.invoke(false)
                })
        }
    }

    fun deleteOptionCategoryAll(realm: Realm, categoryId: Int, onSuccess: (Boolean) -> Unit){
        realm.executeTransactionAsync(
            {
                it.where(Option::class.java).equalTo("optionCategoryId", categoryId).findAll()?.let { options ->
                    options.map { option ->
                        option.deleteFromRealm()
                    }
                }

                it.where(OptionCategory::class.java).equalTo(OPTION_CATEGORY_ID, categoryId).findFirst()?.let{ category ->
                    category.deleteFromRealm()
                }

            },
            {
                DebugUtils.setLog(TAG, "deleteOptionCategoryAll onSuccess!!!")
                onSuccess.invoke(true)
            },
            {
                it.printStackTrace()
                DebugUtils.setLog(TAG, "deleteOptionCategoryAll onFailed!!! e: ${it.localizedMessage}")
                onSuccess.invoke(false)
            }
        )
    }

    fun getOptionCategorys(realm: Realm): RealmResults<OptionCategory>{
        return realm.where(OptionCategory::class.java).findAll()
    }

    fun getOptionCategory(realm: Realm, id: Int): OptionCategory?{
        return realm.where(OptionCategory::class.java).equalTo(OPTION_CATEGORY_ID, id).findFirst()
    }



    fun getOption(realm: Realm, cateId: Int): RealmResults<Option> {
        return realm.where(Option::class.java).equalTo("optionCategoryId", cateId).sort("index", Sort.ASCENDING).findAll()
    }
}