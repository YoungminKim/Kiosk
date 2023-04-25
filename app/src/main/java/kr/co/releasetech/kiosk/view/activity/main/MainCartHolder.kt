package kr.co.releasetech.kiosk.view.activity.main

import android.view.View
import kr.co.releasetech.kiosk.databinding.ItemMainCartBinding
import kr.co.releasetech.kiosk.model.realm.Cart
import kr.co.releasetech.kiosk.utils.TextUtils
import kr.co.releasetech.kiosk.view.base.BaseViewHolder

class MainCartHolder(view: View): BaseViewHolder<ItemMainCartBinding>(view) {

    fun onBind(item: Cart){
        binding.priceTv.text = TextUtils.getMoneyComma(item.totalPrice) + "원"

    }
}