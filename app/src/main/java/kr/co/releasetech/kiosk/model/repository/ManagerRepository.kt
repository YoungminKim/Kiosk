package kr.co.releasetech.kiosk.model.repository

import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kr.co.releasetech.kiosk.model.realm.*
import org.koin.core.KoinComponent

class ManagerRepository : KoinComponent {

    fun getPaymentList(realm: Realm, startDate: Long, endDate: Long): RealmResults<Payment> =
        realm.where(Payment::class.java).between("approvalDate", startDate, endDate)
            .findAll()

    fun getOrderList(realm: Realm, startDate: Long, endDate: Long): RealmResults<Order> =
        realm.where(Order::class.java).between("time", startDate, endDate).findAll()

    fun getCategoryList(realm: Realm): RealmResults<Category> =
        realm.where(Category::class.java).sort("id", Sort.ASCENDING).findAll()

    fun getGoodsList(realm: Realm): RealmResults<Goods> =
        realm.where(Goods::class.java).sort("id", Sort.ASCENDING).findAll()


    fun getOptionCategoryList(realm: Realm): RealmResults<OptionCategory> =
        realm.where(OptionCategory::class.java).sort("id", Sort.ASCENDING).findAll()

    fun getOptionList(realm: Realm): RealmResults<Option> =
        realm.where(Option::class.java).sort("id", Sort.ASCENDING).findAll()

    fun getEditScreenStandbyBg(realm: Realm): RealmResults<EditScreenStandbyBg> =
        realm.where(EditScreenStandbyBg::class.java).sort("id", Sort.ASCENDING).findAll()


    fun getScreenSetting(realm: Realm): ScreenSetting? = realm.where(ScreenSetting::class.java).findFirst()

    fun deleteDataAll(realm: Realm, onSuccess: ((Boolean) -> Unit)) {
        realm.executeTransactionAsync(
            {
                it.deleteAll()
            },
            {
                onSuccess.invoke(true)
            },
            {
                onSuccess.invoke(false)
            })
    }

    fun setCategory(realm: Realm, list: ArrayList<Category>) {
        realm.executeTransactionAsync {
            it.copyToRealm(list)
        }
    }

    fun setGoods(realm: Realm, list: ArrayList<Goods>) {
        realm.executeTransactionAsync {
            it.copyToRealm(list)
        }
    }

    fun setOptionCategory(realm: Realm, list: ArrayList<OptionCategory>) {
        realm.executeTransactionAsync {
            it.copyToRealm(list)
        }
    }

    fun setOption(realm: Realm, list: ArrayList<Option>) {
        realm.executeTransactionAsync {
            it.copyToRealm(list)
        }
    }


    fun setEditScreenStandbyBg(realm: Realm, list: ArrayList<EditScreenStandbyBg>) {
        realm.executeTransactionAsync {
            it.copyToRealm(list)
        }
    }

    fun setScreenSetting(realm: Realm, item: ScreenSetting) {
        realm.executeTransactionAsync {
            it.copyToRealm(item)
        }
    }
}