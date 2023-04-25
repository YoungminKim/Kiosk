package kr.co.releasetech.kiosk.model.repository

import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kr.co.releasetech.kiosk.model.realm.*
import kr.co.releasetech.kiosk.utils.DebugUtils
import org.koin.core.KoinComponent

class MainRepository : KoinComponent {
    companion object {
        private const val TAG = "MainRepository"
    }


    fun getCategoryList(realm: Realm): ArrayList<Category> {
        val list = arrayListOf<Category>()
        realm.where(Category::class.java).sort("index", Sort.ASCENDING).findAll()
            ?.let { categoryList ->
                list.addAll(categoryList)
            }

        return list
    }

    fun getGoodsList(realm: Realm, cateId: Int): ArrayList<Goods> {
        val list = arrayListOf<Goods>()
        realm.where(Goods::class.java).equalTo("categoryId", cateId).findAll()
            ?.let { goodsList ->
                list.addAll(goodsList)
            }
        return list
    }

    fun getGoods(realm: Realm, goodsId: Int): Goods?
        = realm.where(Goods::class.java).equalTo("id", goodsId).findFirst()

    private fun getPaymentMaxId(realm: Realm): Int {
        val maxId = realm.where(Payment::class.java).max("id")
        return if (maxId == null) 0 else maxId.toInt() + 1
    }

    fun getOrderMaxId(realm: Realm): Int {
        val maxId = realm.where(Order::class.java).max("id")
        return if (maxId == null) 0 else maxId.toInt() + 1
    }

    fun getCartMaxId(realm: Realm): Int {
        val maxId = realm.where(Cart::class.java).max("id")
        return if (maxId == null) 0 else maxId.toInt() + 1
    }

    fun addOrder(realm: Realm, order: Order, onSuccess: ((Boolean) -> Unit)) {
        realm.executeTransactionAsync(
            {
                it.copyToRealm(order)
            },
            {
                DebugUtils.setLog(TAG, "addOrder success")
                onSuccess.invoke(true)
            },
            {
                DebugUtils.setLog(TAG, "addOrder failed")
                onSuccess.invoke(false)
            }
        )
    }

    fun addPayment(realm: Realm, payment: Payment, onSuccess: ((Boolean) -> Unit)) {
        payment.id = getPaymentMaxId(realm)
        val orderId = getOrderMaxId(realm) - 1
        payment.orderId = orderId

        realm.executeTransactionAsync(
            {
                it.where(Order::class.java).equalTo("id", orderId).findFirst()?.let { order ->
                    order.isSuccess = true
                }
                it.copyToRealm(payment)
            },
            {
                DebugUtils.setLog(TAG, "addPayment success")
                onSuccess.invoke(true)
            },
            {
                DebugUtils.setLog(TAG, "addPayment failed")
                onSuccess.invoke(false)
            }
        )
    }

    fun addCartList(realm: Realm, list: ArrayList<Cart>, onSuccess: ((Boolean) -> Unit)){
        realm.executeTransactionAsync(
            {
                it.copyToRealm(list)
            },
            {
                DebugUtils.setLog(TAG, "addCartList success")
                onSuccess.invoke(true)
            },
            {

                DebugUtils.setLog(TAG, "addCartList failed")
                onSuccess.invoke(false)
            }
        )
    }


    fun failedOrder(realm: Realm, msg: String, onSuccess: ((Boolean) -> Unit)) {
        val orderId = getOrderMaxId(realm) - 1
        DebugUtils.setLog(TAG, "orderId : $orderId")
        realm.executeTransactionAsync(
            {
                it.where(Order::class.java).equalTo("id", orderId).findFirst()?.let { order ->
                    order.resultMsg = msg
                }
            },
            {
                DebugUtils.setLog(TAG, "failedOrder success")
                onSuccess.invoke(true)
            },
            {
                DebugUtils.setLog(TAG, "failedOrder failed")
                onSuccess.invoke(false)
            }
        )
    }

}