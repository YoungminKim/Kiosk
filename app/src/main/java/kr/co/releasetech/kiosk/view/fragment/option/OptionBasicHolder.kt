package kr.co.releasetech.kiosk.view.fragment.option

import android.view.View
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ItemOptionBasicBinding
import kr.co.releasetech.kiosk.model.realm.OptionCategory
import kr.co.releasetech.kiosk.view.base.BaseViewHolder
import splitties.views.onClick

class OptionBasicHolder(view: View): BaseViewHolder<ItemOptionBasicBinding>(view) {

    fun onBind(item: OptionCategory){
        with(binding){
            typeTv.setText(if(item.isSingle) R.string.single_choice else R.string.multiple_choice)

        }
    }
}