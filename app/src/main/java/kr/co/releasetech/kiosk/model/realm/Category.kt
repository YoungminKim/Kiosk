package kr.co.releasetech.kiosk.model.realm

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
open class Category(
    @PrimaryKey
    var id: Int = 0,
    var index: Int = 0,
    var name: String = ""
): RealmObject(), Parcelable