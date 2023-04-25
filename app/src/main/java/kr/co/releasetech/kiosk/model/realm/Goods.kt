package kr.co.releasetech.kiosk.model.realm

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
open class Goods(
    @PrimaryKey
    var id: Int = 0,
    var index: Int = 0,
    var name: String = "",
    var description: String = "",
    var imgUrl: String = "",
    var categoryId: Int = 0,
    var price: Int = 0,
    var optionCategoryIds: String = "",

): RealmObject(), Parcelable