package kr.co.releasetech.kiosk.model.realm

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
open class Option(
    @PrimaryKey
    var id: Int = 0,
    var index: Int = 0,
    var optionCategoryId: Int = 0,
    var name: String = "",
    var price: Int = 0
): RealmObject(), Parcelable