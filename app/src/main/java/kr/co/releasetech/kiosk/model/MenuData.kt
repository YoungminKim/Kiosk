package kr.co.releasetech.kiosk.model


data class MenuData(
    val version: Long,
    val category: ArrayList<Category>,
    val goods: ArrayList<Goods>,
    val optionCategory: ArrayList<OptionCategory>,
    val option: ArrayList<Option>,
    val editScreenStandbyBg: ArrayList<EditScreenStandbyBg>,
    val screenSetting: ScreenSetting
){
    data class Category(
        var id: Int = 0,
        var index: Int = 0,
        var name: String = ""
    )

    data class Goods(
        var id: Int = 0,
        var index: Int = 0,
        var name: String = "",
        var description: String = "",
        var imgUrl: String = "",
        var categoryId: Int = 0,
        var price: Int = 0,
        var optionCategoryIds: String = "",
    )

    data class OptionCategory(
        var id: Int = 0,
        var name: String = "",
        var isSingle: Boolean = false
    )

    data class Option(
        var id: Int = 0,
        var index: Int = 0,
        var optionCategoryId: Int = 0,
        var name: String = "",
        var price: Int = 0
    )

    data class EditScreenStandbyBg(
        var id: Int = 0,
        var index: Int = 0,
        var fileName: String = "",
        var isVideo: Boolean = false)


    data class ScreenSetting(
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

    )
}