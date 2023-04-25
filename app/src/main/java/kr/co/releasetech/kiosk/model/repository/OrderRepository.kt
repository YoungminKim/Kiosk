package kr.co.releasetech.kiosk.model.repository

import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kr.co.releasetech.kiosk.model.realm.Order
import org.koin.core.KoinComponent

class OrderRepository: KoinComponent {

    fun getOrderList(realm: Realm): RealmResults<Order> {
        return realm.where(Order::class.java).sort("id", Sort.DESCENDING).findAll()
    }


    fun getSearchOrderList(realm: Realm, startDate: Long, endDate: Long, searchWord: String): RealmResults<Order> {
        return if(searchWord.isNotEmpty()) realm.where(Order::class.java).beginsWith("name", searchWord).between("time", startDate, endDate).sort("id", Sort.DESCENDING).findAll()
        else realm.where(Order::class.java).between("time", startDate, endDate).sort("id", Sort.DESCENDING).findAll()
    }
}