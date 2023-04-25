package kr.co.releasetech.kiosk.model.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class LogReport(
    @PrimaryKey
    var id: Int = 0,
    var date: String = "",
    var time: Long = 0,
    var discription: String = ""
): RealmObject()