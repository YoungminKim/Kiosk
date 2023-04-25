package kr.co.releasetech.kiosk.model

data class OptionDummy(
    var id: Int = -1,
    var index: Int = 0,
    var optionCategoryId: Int = 0,
    var name: String = "",
    var price: Int = 0,
    var valid: Boolean = true
)