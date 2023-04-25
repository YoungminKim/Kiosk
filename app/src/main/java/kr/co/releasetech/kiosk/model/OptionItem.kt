package kr.co.releasetech.kiosk.model

import kr.co.releasetech.kiosk.model.realm.Option
import kr.co.releasetech.kiosk.model.realm.OptionCategory


data class OptionItem(
    var category: OptionCategory,
    var options: ArrayList<Option> = ArrayList(),
    var singlePosition: Int = 0,
    var selectMultipleOptions: HashMap<Int, Boolean> = hashMapOf()
)