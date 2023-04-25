package kr.co.releasetech.kiosk.model

data class TransData(
    val tranNo: String,
    val tranType: String,
    val totalAmount: String,
    val tax: String,
    val tip: String = "0",
    val approvalNum: String,
    val approvalDate: String,
    val TranSerialNo: String,
    val cashAmount: String,
    val installment: String = "0",
    val addField: String,
    val signFlag: String = "Y"
)