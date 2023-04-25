package kr.co.releasetech.kiosk.model.repository

import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kr.co.releasetech.kiosk.model.realm.EditScreenStandbyBg
import kr.co.releasetech.kiosk.model.realm.ScreenSetting
import kr.co.releasetech.kiosk.utils.DebugUtils
import org.koin.core.KoinComponent

class ScreenSettingRepository : KoinComponent {
    companion object {
        private const val TAG = "EditScreenRepository"
    }

    fun getEditScreenStandbyBgMaxId(realm: Realm): Int {
        val maxId = realm.where(EditScreenStandbyBg::class.java).max("id")
        val id = if (maxId == null) 0 else maxId.toInt() + 1
        return id
    }

    fun addStandbyBgItem(realm: Realm, item: EditScreenStandbyBg, onSuccess: ((Boolean) -> Unit)) {
        item.id = getEditScreenStandbyBgMaxId(realm)
        realm.executeTransactionAsync(
            {
                it.copyToRealm(item)
            },
            {
                DebugUtils.setLog(TAG, "add onSuccess!!!")
                onSuccess.invoke(true)
            },
            {
                DebugUtils.setLog(TAG, "add onFailed!!!")
                onSuccess.invoke(false)
            })
    }

    fun getStandbyBgList(realm: Realm): RealmResults<EditScreenStandbyBg> =
        realm.where(EditScreenStandbyBg::class.java).sort("index", Sort.ASCENDING).findAll()


    fun updateIndex(
        realm: Realm,
        list: ArrayList<EditScreenStandbyBg>,
        onSuccess: ((Boolean) -> Unit)
    ) {

        for (i in list.indices) {
            val id = list[i].id
            realm.executeTransactionAsync(
                { realm ->
                    realm.where(EditScreenStandbyBg::class.java).equalTo("id", id).findFirst()
                        ?.let {
                            it.index = i
                        }
                },
                {
                    DebugUtils.setLog(TAG, "updateIndex onSuccess!!!")
                    onSuccess.invoke(true)
                },
                {
                    DebugUtils.setLog(TAG, "updateIndex onFailed!!! : ${it.printStackTrace()}")
                    onSuccess.invoke(false)
                }
            )
        }


    }

    fun deleteStandbyBg(realm: Realm, id: Int, onSuccess: ((Boolean) -> Unit)) {
        realm.executeTransactionAsync(
            {
                it.where(EditScreenStandbyBg::class.java).equalTo("id", id).findFirst()?.let {
                    DebugUtils.setLog(TAG, "delete category : ${it.fileName}")
                    it.deleteFromRealm()
                }

            },
            {
                DebugUtils.setLog(TAG, "delete onSuccess!!!")
                onSuccess.invoke(true)
            },
            { e ->
                DebugUtils.setLog(TAG, "delete onFailed!!! ${e.localizedMessage}")
                onSuccess.invoke(false)
            })
    }


    fun addScreenSetting(realm: Realm) {
        val item = ScreenSetting(
            0,
            useSelectOrderScreen = true,
            useStandbyScreen = true,
            standbyScreenImageSecond = 10,
            orderScreenWaitSecond = 30,
            useEventScreen = false
        )
        realm.executeTransactionAsync {
            it.copyToRealm(item)
        }

    }

    fun getScreenSetting(realm: Realm): ScreenSetting? =
        realm.where(ScreenSetting::class.java).findFirst()

    fun updateScreenSettingUseStandby(
        realm: Realm,
        isChecked: Boolean,
        onSuccess: ((Boolean) -> Unit)
    ) {
        realm.executeTransactionAsync(
            {
                it.where(ScreenSetting::class.java).findFirst()?.let { result ->
                    result.useStandbyScreen = isChecked
                }

            },
            {
                DebugUtils.setLog(TAG, "updateScreenSettingUseStandby onSuccess!!!")
                onSuccess.invoke(true)
            },
            { e ->
                DebugUtils.setLog(
                    TAG,
                    "updateScreenSettingUseStandby onFailed!!! ${e.localizedMessage}"
                )
                onSuccess.invoke(false)
            })
    }

    fun updateScreenSettingUseSelectOrder(
        realm: Realm,
        isChecked: Boolean,
        onSuccess: ((Boolean) -> Unit)
    ) {
        realm.executeTransactionAsync(
            {
                it.where(ScreenSetting::class.java).findFirst()?.let { result ->
                    result.useSelectOrderScreen = isChecked
                }

            },
            {
                DebugUtils.setLog(TAG, "updateScreenSettingUseStandby onSuccess!!!")
                onSuccess.invoke(true)
            },
            { e ->
                DebugUtils.setLog(
                    TAG,
                    "updateScreenSettingUseStandby onFailed!!! ${e.localizedMessage}"
                )
                onSuccess.invoke(false)
            })
    }


    fun updateScreenSettingImageDelay(
        realm: Realm,
        second: Int,
        onSuccess: ((Boolean) -> Unit)
    ) {
        realm.executeTransactionAsync(
            {
                it.where(ScreenSetting::class.java).findFirst()?.let { result ->
                    result.standbyScreenImageSecond = second
                }

            },
            {
                DebugUtils.setLog(TAG, "updateScreenSettingImageDelay onSuccess!!!")
                onSuccess.invoke(true)
            },
            { e ->
                DebugUtils.setLog(
                    TAG,
                    "updateScreenSettingImageDelay onFailed!!! ${e.localizedMessage}"
                )
                onSuccess.invoke(false)
            })
    }


    fun updateScreenSettingOrderScreenWaitSecond(
        realm: Realm,
        second: Int,
        onSuccess: ((Boolean) -> Unit)
    ) {
        realm.executeTransactionAsync(
            {
                it.where(ScreenSetting::class.java).findFirst()?.let { result ->
                    result.orderScreenWaitSecond = second
                }

            },
            {
                DebugUtils.setLog(TAG, "updateScreenSettingOrderScreenWaitSecond onSuccess!!!")
                onSuccess.invoke(true)
            },
            { e ->
                DebugUtils.setLog(
                    TAG,
                    "updateScreenSettingOrderScreenWaitSecond onFailed!!! ${e.localizedMessage}"
                )
                onSuccess.invoke(false)
            })
    }

    fun updateBgColor(
        realm: Realm,
        color: Int,
        hexCode: String?,
        onSuccess: ((Boolean) -> Unit)
    ) {
        realm.executeTransactionAsync(
            {
                it.where(ScreenSetting::class.java).findFirst()?.let { result ->
                    result.bgColor = color
                    hexCode?.let { result.bgHexCode = hexCode }
                }

            },
            {
                DebugUtils.setLog(TAG, "updateBgColor onSuccess!!!")
                onSuccess.invoke(true)
            },
            { e ->
                DebugUtils.setLog(
                    TAG,
                    "updateBgColor onFailed!!! ${e.localizedMessage}"
                )
                onSuccess.invoke(false)
            })
    }

    fun updateLogoImage(
        realm: Realm,
        logo: String?,
        onSuccess: ((Boolean) -> Unit)
    ) {
        realm.executeTransactionAsync(
            {
                it.where(ScreenSetting::class.java).findFirst()?.let { result ->
                    result.logoImage = logo
                }

            },
            {
                DebugUtils.setLog(TAG, "updateLogoImage onSuccess!!!")
                onSuccess.invoke(true)
            },
            { e ->
                DebugUtils.setLog(
                    TAG,
                    "updateLogoImage onFailed!!! ${e.localizedMessage}"
                )
                onSuccess.invoke(false)
            })
    }


    fun updateTheme(
        realm: Realm,
        themeType: Int,
        onSuccess: ((Boolean) -> Unit)
    ) {
        realm.executeTransactionAsync(
            {
                it.where(ScreenSetting::class.java).findFirst()?.let { result ->
                    result.themeType = themeType
                }

            },
            {
                DebugUtils.setLog(TAG, "updateTheme onSuccess!!!")
                onSuccess.invoke(true)
            },
            { e ->
                DebugUtils.setLog(
                    TAG,
                    "updateTheme onFailed!!! ${e.localizedMessage}"
                )
                onSuccess.invoke(false)
            })
    }

}