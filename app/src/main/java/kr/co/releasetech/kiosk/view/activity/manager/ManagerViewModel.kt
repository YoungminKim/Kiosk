package kr.co.releasetech.kiosk.view.activity.manager

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.releasetech.kiosk.model.MenuData
import kr.co.releasetech.kiosk.model.realm.*
import kr.co.releasetech.kiosk.model.repository.ManagerRepository
import kr.co.releasetech.kiosk.utils.DateUtils.getApprovalDate
import kr.co.releasetech.kiosk.utils.DateUtils.getDateText
import kr.co.releasetech.kiosk.utils.DateUtils.getDateTime
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewModel
import java.util.*
import kotlin.collections.ArrayList

class ManagerViewModel(val repository: ManagerRepository) : BaseViewModel() {
    companion object {
        private const val TAG = "ManagerViewModel"
    }

    val totalSaleField = ObservableField("")
    val totalOrderCountField = ObservableField("")
    val totalRefundCountField = ObservableField("")
    val totalRefundField = ObservableField("")

    private val _totalSale = MutableLiveData<Int>()
    val totalSale: LiveData<Int>
        get() = _totalSale

    private val _totalRefundCount = MutableLiveData<Int>()
    val totalRefundCount: LiveData<Int>
        get() = _totalRefundCount


    private val _totalRefund = MutableLiveData<Int>()
    val totalRefund: LiveData<Int>
        get() = _totalRefund

    private val _totalOrderCount = MutableLiveData<Int>()
    val totalOrderCount: LiveData<Int>
        get() = _totalOrderCount

    private val _onOpenStore = MutableLiveData<ScreenSetting>()
    val onOpenStore: LiveData<ScreenSetting>
        get() = _onOpenStore

    private val _onMenuSetting = MutableLiveData<Unit>()
    val onMenuSetting: LiveData<Unit>
        get() = _onMenuSetting

    private val _onManagerSetting = MutableLiveData<Unit>()
    val onManagerSetting: LiveData<Unit>
        get() = _onManagerSetting

    private val _onSettingDownload = MutableLiveData<MenuData>()
    val onSettingDownload: LiveData<MenuData>
        get() = _onSettingDownload

    private val _onSettingUpload = MutableLiveData<Unit>()
    val onSettingUpload: LiveData<Unit>
        get() = _onSettingUpload

    private val _onOrderList = MutableLiveData<Unit>()
    val onOrderList: LiveData<Unit>
        get() = _onOrderList

    private val _onPaymentList = MutableLiveData<Unit>()
    val onPaymentList: LiveData<Unit>
        get() = _onPaymentList

    private val _onEditScreen = MutableLiveData<Unit>()
    val onEditScreen: LiveData<Unit>
        get() = _onEditScreen

    private val _onAppFinished = MutableLiveData<Unit>()
    val onAppFinished: LiveData<Unit>
        get() = _onAppFinished

    private val _deleteData = MutableLiveData<Unit>()
    val deleteData: LiveData<Unit>
        get() = _deleteData

    private val _uploadSuccess = MutableLiveData<Unit>()
    val uploadSuccess: LiveData<Unit>
        get() = _uploadSuccess


    fun getSummary(cal: Calendar) {

        val startApprovalDate =
            "${getApprovalDate(cal.timeInMillis)}000000${cal.get(Calendar.DAY_OF_WEEK) - 1}".toLong()
        val endApprovalDate =
            "${getApprovalDate(cal.timeInMillis)}235959${cal.get(Calendar.DAY_OF_WEEK) - 1}".toLong()
        val paymentList = repository.getPaymentList(mRealm, startApprovalDate, endApprovalDate)

        var totalAmount = 0
        var totalRefundCount = 0
        var totalRefund = 0
        paymentList.map {

            if(it.tranType == "D4"){
                it.totalAmount?.let { amount ->
                    totalAmount -= amount.toInt()
                    ++totalRefundCount
                    totalRefund += amount.toInt()
                }
            }else{
                it.totalAmount?.let { amount -> totalAmount += amount.toInt() }
            }
        }

        _totalSale.postValue(totalAmount)
        _totalRefund.postValue(totalRefund)
        _totalRefundCount.postValue(totalRefundCount)

        val dateText = getDateText(cal.timeInMillis)
        val startDate = getDateTime("$dateText 00:00:00")
        val endDate = getDateTime("$dateText 23:59:59")
        val orderList = repository.getOrderList(mRealm, startDate, endDate)

        _totalOrderCount.postValue(orderList.size)
    }

    fun openStore() {
        repository.getScreenSetting(mRealm)?.let {
            _onOpenStore.postValue(it)
        }

    }

    fun menuSetting() {
        _onMenuSetting.postValue(Unit)
    }

    fun managerSetting() {
        _onManagerSetting.postValue(Unit)
    }

    fun settingDownLoad() {

        val categoryList = arrayListOf<MenuData.Category>()
        repository.getCategoryList(mRealm).map {
            val item = MenuData.Category(id = it.id, index = it.index, name = it.name)
            categoryList.add(item)
        }
        //categoryList.addAll()

        val goodsList = arrayListOf<MenuData.Goods>()
        repository.getGoodsList(mRealm).map {
            val item = MenuData.Goods(
                id = it.id,
                categoryId = it.categoryId,
                index = it.index,
                name = it.name,
                description = it.description,
                optionCategoryIds = it.optionCategoryIds,
                imgUrl = it.imgUrl,
                price = it.price
            )
            goodsList.add(item)
        }

        val optionCategoryList = arrayListOf<MenuData.OptionCategory>()
        repository.getOptionCategoryList(mRealm).map {
            val item = MenuData.OptionCategory(
                id = it.id,
                name = it.name,
                isSingle = it.isSingle
            )
            optionCategoryList.add(item)

        }

        val optionList = arrayListOf<MenuData.Option>()
        repository.getOptionList(mRealm).map {
            val item = MenuData.Option(
                id = it.id,
                index = it.index,
                optionCategoryId = it.optionCategoryId,
                name = it.name,
                price = it.price
            )
            optionList.add(item)
        }

        val editScreenStandbyBgList = arrayListOf<MenuData.EditScreenStandbyBg>()
        repository.getEditScreenStandbyBg(mRealm).map {
            val item = MenuData.EditScreenStandbyBg(
                id = it.id,
                index = it.index,
                fileName = it.fileName,
                isVideo = it.isVideo
            )
            editScreenStandbyBgList.add(item)
        }

        val screenSetting = MenuData.ScreenSetting()
        repository.getScreenSetting(mRealm)?.let {
            screenSetting.id = it.id
            screenSetting.orderScreenWaitSecond = it.orderScreenWaitSecond
            screenSetting.standbyScreenImageSecond = it.standbyScreenImageSecond
            screenSetting.useEventScreen = it.useEventScreen
            screenSetting.useStandbyScreen = it.useStandbyScreen
            screenSetting.useSelectOrderScreen = it.useSelectOrderScreen
            screenSetting.bgColor = it.bgColor
            screenSetting.bgHexCode = it.bgHexCode
            screenSetting.logoImage = it.logoImage
            screenSetting.themeType = it.themeType
        }

        val setting = MenuData(
            version = mRealm.version,
            category = categoryList,
            goods = goodsList,
            optionCategory = optionCategoryList,
            option = optionList,
            editScreenStandbyBg = editScreenStandbyBgList,
            screenSetting = screenSetting
        )

        _onSettingDownload.postValue(setting)
    }

    fun settingUpload() {
        _onSettingUpload.postValue(Unit)
    }

    fun orderList() {
        _onOrderList.postValue(Unit)
    }

    fun paymentList() {
        _onPaymentList.postValue(Unit)
    }

    fun editScreen() {
        _onEditScreen.postValue(Unit)
    }

    fun appFinish() {
        _onAppFinished.postValue(Unit)
    }


    fun deleteRealm() {
        repository.deleteDataAll(mRealm) {
            if (it) _deleteData.postValue(Unit)
            else _onDbError.postValue(Unit)
        }
    }

    fun setMenuData(data: MenuData) {

        val categoryList = ArrayList<Category>()
        data.category.map {
            categoryList.add(Category(id = it.id, index = it.index, name = it.name))
        }

        repository.setCategory(mRealm, categoryList)

        val goodsList = ArrayList<Goods>()
        data.goods.map {

            goodsList.add(
                Goods(
                    id = it.id,
                    categoryId = it.categoryId,
                    index = it.index,
                    name = it.name,
                    optionCategoryIds = it.optionCategoryIds,
                    imgUrl = it.imgUrl,
                    price = it.price
                )
            )
        }

        repository.setGoods(mRealm, goodsList)

        val optionCategoryList = ArrayList<OptionCategory>()
        data.optionCategory.map {
            optionCategoryList.add(
                OptionCategory(
                    id = it.id,
                    name = it.name,
                    isSingle = it.isSingle
                )
            )
        }

        repository.setOptionCategory(mRealm, optionCategoryList)

        val optionList = arrayListOf<Option>()
        data.option.map {
            optionList.add(
                Option(
                    id = it.id,
                    index = it.index,
                    optionCategoryId = it.optionCategoryId,
                    name = it.name,
                    price = it.price
                )
            )
        }

        repository.setOption(mRealm, optionList)


        val editScreenStandbyBgList = arrayListOf<EditScreenStandbyBg>()
        data.editScreenStandbyBg.map {
            DebugUtils.setLog(TAG, "id : ${it}")
            editScreenStandbyBgList.add(
                EditScreenStandbyBg(
                    id = it.id,
                    index = it.index,
                    fileName = it.fileName,
                    isVideo = it.isVideo
                )
            )
        }

        repository.setEditScreenStandbyBg(mRealm, editScreenStandbyBgList)

        data.screenSetting.also {
            val screenSetting = ScreenSetting(
                id = it.id,
                useEventScreen = it.useEventScreen,
                useSelectOrderScreen = it.useSelectOrderScreen,
                useStandbyScreen = it.useStandbyScreen,
                standbyScreenImageSecond = it.standbyScreenImageSecond,
                orderScreenWaitSecond = it.orderScreenWaitSecond,
                bgColor = it.bgColor,
                bgHexCode = it.bgHexCode,
                logoImage = it.logoImage,
                themeType = it.themeType
                )
            repository.setScreenSetting(mRealm, screenSetting)
        }



        _uploadSuccess.postValue(Unit)

    }
}