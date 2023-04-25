package kr.co.releasetech.kiosk.model.realm

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
open class Payment(
    @PrimaryKey
    var id: Int = 0,
    var orderId: Int = 0,
    var tranNo: String? = null,
    var tranType: String? = null,
    var cardNum: String? = null,
    var cardName: String? = null,
    var issuerCode: String? = null,
    var totalAmount: String? = null,
    var tax: String? = null,
    var tip: String? = null,
    var installment: String? = null,
    var resultCode: String? = null,
    var resultMsg: String? = null,
    var approvalNum: String? = null,
    var approvalDate: Long? = null,
    var acquirerCode: String? = null,
    var acquirerName: String? = null,
    var ad1: String? = null,
    var ad2: String? = null,
    var merchantNum: String? = null,
    var shopTid: String? = null,
    var shopBizNum: String? = null,
    var addField: String? = null,
    var notice: String? = null,
    var cashAmount: String? = null,
    var tpk: String? = null,
    var tranSerialNo: String? = null,
    var shopName: String? = null,
    var shopTel: String? = null,
    var shopAddress: String? = null,
    var shopOwner: String? = null,
    var isRefund: Boolean = false

): RealmObject(), Parcelable