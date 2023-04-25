package kr.co.releasetech.kiosk.kicc

import android.content.ComponentName
import android.content.Intent
import kr.co.releasetech.kiosk.kicc.MakePrintMessage
import kr.co.releasetech.kiosk.kicc.MakePrintMessage.receiptPrint
import kr.co.releasetech.kiosk.model.TransData
import java.io.UnsupportedEncodingException


const val KICC_PACKAGE_NAME = "kr.co.kicc.easycarda"
const val KICC_CLASS_NAME = "kr.co.kicc.easycarda.CallPopup"
var printMessage: MakePrintMessage? = null

fun setTransData(data: TransData): Intent {

    val tran_types = data.tranType
    val compName = ComponentName(KICC_PACKAGE_NAME, KICC_CLASS_NAME)

    val intent = Intent(Intent.ACTION_MAIN)

    intent.putExtra("TRAN_NO", data.tranNo)
    intent.putExtra("TRAN_TYPE", tran_types)
    intent.putExtra("SIGN_FLAG", data.signFlag)
    /*if ("M3" == tran_types || "M4" == tran_types || "M8" == tran_types) {
        intent.putExtra("TERMINAL_TYPE", dongleflag.getText().toString())
        intent.putExtra("TEXT_DECLINE", "포인트 조회가 거절되었습니다")
    } else */
    intent.putExtra("TERMINAL_TYPE", "40")
    intent.putExtra("TOTAL_AMOUNT", data.totalAmount)
    intent.putExtra("TAX", data.tax)
    intent.putExtra("TAX_OPTION", "A")
    intent.putExtra("TIP", "0")
    //intent.putExtra("TIP_OPTION","N");
    //intent.putExtra("TIP_OPTION","N");
    if ("D4" == tran_types || "B2" == tran_types || "B4" == tran_types) {
        intent.putExtra("APPROVAL_NUM", data.approvalNum)
        intent.putExtra("APPROVAL_DATE", data.approvalDate)
        intent.putExtra("TRAN_SERIALNO", data.TranSerialNo)
    }
    if ("B1" == tran_types || "B2" == tran_types || "B3" == tran_types || "B4" == tran_types) {
        intent.putExtra("CASHAMOUNT", "00")
    } else {
        intent.putExtra("INSTALLMENT", data.installment)
    }
    if ("PT" == tran_types) {
        val printmsg: String = receiptPrint(
            "160516120000",
            "IC신용구매",
            false,
            false,
            1004,
            91,
            0,
            0,
            "1234-56**-****-1234",
            "테스트점",
            "1234567890",
            "홍길동",
            "02-1234-5678",
            "1234567",
            "서울 테스트구 테스트동",
            "발급사",
            "00001111",
            "",
            "",
            "12345678",
            "매입사",
            "",
            "",
            true
        )
        try {
            intent.putExtra("PRINT_DATA", printmsg.toByteArray(charset("EUC-KR")))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }
    intent.putExtra("ADD_FIELD", data.addField)
    intent.putExtra("TIMEOUT", "30")

    intent.putExtra("TEXT_PROCESS", "결제 진행중입니다")
    intent.putExtra("TEXT_COMPLETE", "결제가 완료되었습니다")
//		intent.putExtra("TEXT_FALLBACK", "카드의 마그네틱부분으로\n읽어주세요");
//
//		intent.putExtra("TEXT_MAIN_SIZE", 60);
//		intent.putExtra("IMG_BG_WIDTH", 1000);
//		intent.putExtra("IMG_CARD_WIDTH", 600);
//		intent.putExtra("IMG_CLOSE_WIDTH", 100);

//		intent.putExtra("FALLBACK_FLAG","N");


    //		intent.putExtra("TEXT_FALLBACK", "카드의 마그네틱부분으로\n읽어주세요");
//
//		intent.putExtra("TEXT_MAIN_SIZE", 60);
//		intent.putExtra("IMG_BG_WIDTH", 1000);
//		intent.putExtra("IMG_CARD_WIDTH", 600);
//		intent.putExtra("IMG_CLOSE_WIDTH", 100);

//		intent.putExtra("FALLBACK_FLAG","N");
    /*if (rb2.isChecked()) {
        intent.putExtra("IMG_BG_PATH", "/sdcard/kicc/background_gray.png")
        intent.putExtra("TEXT_MAIN_SIZE", 25)
        intent.putExtra("TEXT_MAIN_COLOR", "#FFFFFF")
        intent.putExtra("TEXT_SUB1_SIZE", 15)
        //			intent.putExtra("TEXT_SUB1_COLOR", "#FFFF00");
        intent.putExtra("TEXT_SUB1_COLOR", "#00000000")
        intent.putExtra("TEXT_SUB2_SIZE", 12)
        intent.putExtra("TEXT_SUB2_COLOR", "#00FFFF")
        intent.putExtra("TEXT_SUB3_SIZE", 20)
        intent.putExtra("TEXT_SUB3_COLOR", "#FF00FF")
        intent.putExtra("BLUR_BACKGROUND", false)
    }

    if (rb3.isChecked()) {
        intent.putExtra("IMG_BG_PATH", "/sdcard/kicc/background_kicc.png")
        intent.putExtra("IMG_CARD_PATH", "/sdcard/kicc/card_kicc.png")
        intent.putExtra("IMG_CLOSE_PATH", "/sdcard/kicc/close_kicc.png")
        intent.putExtra("TEXT_MAIN_SIZE", 18)
        intent.putExtra("TEXT_MAIN_COLOR", "#303030")
        intent.putExtra("TEXT_SUB1_SIZE", 12)
        //			intent.putExtra("TEXT_SUB1_COLOR", "#ff752a");
        intent.putExtra("TEXT_SUB1_COLOR", "#00000000")
        intent.putExtra("TEXT_SUB2_SIZE", 10)
        intent.putExtra("TEXT_SUB2_COLOR", "#ff752a")
        intent.putExtra("TEXT_SUB3_SIZE", 16)
        intent.putExtra("TEXT_SUB3_COLOR", "#909090")
        intent.putExtra("BLUR_BACKGROUND", "1")
    }*/

//		if(barcode.getText().toString().isEmpty()==false)
//		{
//			intent.putExtra("DONGLE_FLAG",this.dongleflag.getText().toString());
//			intent.putExtra("BARCODE",this.barcode.getText().toString());
//
//		}


//		if(barcode.getText().toString().isEmpty()==false)
//		{
//			intent.putExtra("DONGLE_FLAG",this.dongleflag.getText().toString());
//			intent.putExtra("BARCODE",this.barcode.getText().toString());
//
//		}
    intent.component = compName
    return intent
}

object KiccCallbackParam{
    const val TRAN_NO = "TRAN_NO"
    const val TRAN_TYPE = "TRAN_TYPE"
    const val CARD_NUM = "CARD_NUM"
    const val CARD_NAME = "CARD_NAME"
    const val ISSUER_CODE = "ISSUER_CODE"
    const val TOTAL_AMOUNT = "TOTAL_AMOUNT"
    const val TAX = "TAX"
    const val TIP = "TIP"
    const val INSTALLMENT = "INSTALLMENT"
    const val RESULT_CODE = "RESULT_CODE"
    const val RESULT_MSG = "RESULT_MSG"
    const val APPROVAL_NUM = "APPROVAL_NUM"
    const val APPROVAL_DATE = "APPROVAL_DATE"
    const val ACQUIRER_CODE = "ACQUIRER_CODE"
    const val ACQUIRER_NAME = "ACQUIRER_NAME"
    const val AD1 = "AD1"
    const val AD2 = "AD2"
    const val MERCHANT_NUM = "MERCHANT_NUM"
    const val SHOP_TID = "SHOP_TID"
    const val SHOP_BIZ_NUM = "SHOP_BIZ_NUM"
    const val ADD_FIELD = "ADD_FIELD"
    const val NOTICE = "NOTICE"
    const val CASHAMOUNT = "CASHAMOUNT"
    const val TPK = "TPK"
    const val TRAN_SERIALNO = "TRAN_SERIALNO"
    const val SHOP_NAME = "SHOP_NAME"
    const val SHOP_TEL = "SHOP_TEL"
    const val SHOP_ADDRESS = "SHOP_ADDRESS"
    const val SHOP_OWNER = " SHOP_OWNER"
}


