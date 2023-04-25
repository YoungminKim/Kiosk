package kr.co.releasetech.kiosk.model.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class EditScreenStandbyBg(
    @PrimaryKey
    var id: Int = 0,
    var index: Int = 0,
    var fileName: String = "",
    var isVideo: Boolean = false
): RealmObject()