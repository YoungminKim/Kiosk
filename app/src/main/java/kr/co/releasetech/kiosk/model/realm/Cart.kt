package kr.co.releasetech.kiosk.model.realm

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
open class Cart(
    @PrimaryKey
    var id: Int = -1,
    var orderId: Int = -1,
    var goodsId: Int = -1,
    var goodsPrice: Int = 0,
    var price: Int = 0,
    var name : String = "",
    var imgUrl: String = "",
    var quantity: Int = 0,
    var optionIds: String = "",
    var optionNames: String = "",
    var optionPrice: String = "",
    var totalPrice: Int = 0
): RealmObject(), Parcelable