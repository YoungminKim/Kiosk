package kr.co.releasetech.kiosk.view.activity.addgoods

import android.view.View
import kr.co.releasetech.kiosk.databinding.ItemAddGoodsOptionBinding
import kr.co.releasetech.kiosk.model.realm.OptionCategory
import kr.co.releasetech.kiosk.view.base.BaseViewHolder

class AddGoodsOptionHolder(view: View): BaseViewHolder<ItemAddGoodsOptionBinding>(view) {
    fun onBind(item: OptionCategory, isLast: Boolean){
        with(binding){
            nameTv.text = item.name
            emptyLl.visibility = if (isLast) View.VISIBLE else View.GONE

        }
    }
}