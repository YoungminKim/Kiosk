package kr.co.releasetech.kiosk.view.fragment.category

import android.view.View
import kr.co.releasetech.kiosk.databinding.ItemCategoryBinding
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.view.base.BaseViewHolder

class CategoryHolder(view: View): BaseViewHolder<ItemCategoryBinding>(view) {

    fun onBind(item: Category, position: Int){
        with(binding){
            indexTv.text = "${ position + 1 }."
        }
    }
}