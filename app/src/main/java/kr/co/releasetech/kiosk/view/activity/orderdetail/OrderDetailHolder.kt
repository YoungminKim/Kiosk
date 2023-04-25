package kr.co.releasetech.kiosk.view.activity.orderdetail

import android.view.View
import kr.co.releasetech.kiosk.databinding.ItemOrderDetailBinding
import kr.co.releasetech.kiosk.model.realm.Cart
import kr.co.releasetech.kiosk.utils.TextUtils
import kr.co.releasetech.kiosk.view.base.BaseViewHolder

class OrderDetailHolder(view: View): BaseViewHolder<ItemOrderDetailBinding>(view) {
    fun onBind(item: Cart){
        with(binding){
            nameTv.text = item.name
            priceTv.text = "${TextUtils.getMoneyComma(item.price)}원"
            quantityTv.text = item.quantity.toString()
            optionTv.text = item.optionNames
        }
    }
}