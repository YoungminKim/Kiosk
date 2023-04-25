package kr.co.releasetech.kiosk.model.realm

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
open class ScreenSetting(
    @PrimaryKey
    var id: Int = 0,
    var useSelectOrderScreen: Boolean = true,
    var useStandbyScreen: Boolean = true,
    var standbyScreenImageSecond: Int = 10,
    var orderScreenWaitSecond: Int = 30,
    var useEventScreen: Boolean = false,
    var bgColor: Int = -1,
    var bgHexCode: String = "#FFFFFFFF",
    var logoImage: String? = null,
    var themeType: Int = 0
): RealmObject(), Parcelable