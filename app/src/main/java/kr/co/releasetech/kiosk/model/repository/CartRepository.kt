package kr.co.releasetech.kiosk.model.repository

import io.realm.Realm
import io.realm.RealmResults
import kr.co.releasetech.kiosk.model.realm.Cart
import kr.co.releasetech.kiosk.utils.DebugUtils
import org.koin.core.KoinComponent

class CartRepository : KoinComponent {

    companion object{
        private const val TAG = "CartRepository"
    }
    fun getCartList(realm: Realm, orderId: Int, onSuccess: ((RealmResults<Cart>) -> Unit)){
        realm.executeTransactionAsync {
            val list = realm.where(Cart::class.java).equalTo("orderId", orderId).findAll()
            onSuccess.invoke(list)
        }
    }

    fun getCartList(realm: Realm, orderId: Int): RealmResults<Cart> = realm.where(Cart::class.java).equalTo("orderId", orderId).findAll()
}


