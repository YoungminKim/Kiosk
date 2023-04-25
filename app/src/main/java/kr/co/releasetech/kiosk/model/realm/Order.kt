package kr.co.releasetech.kiosk.model.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Order(
    @PrimaryKey
    var id: Int = 0,
    var name: String = "",
    var quantity: Int = 0,
    var totalAmount: Int = 0,
    var date: String = "",
    var time: Long = 0,
    var isTakeout: Boolean = false,
    var isSuccess: Boolean = false,
    var resultMsg: String = ""

): RealmObject()