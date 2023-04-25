/*
package kr.co.releasetech.kiosk.view.activity.goodsdetail

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.IncludeItemGoodsOption01Binding
import kr.co.releasetech.kiosk.databinding.IncludeItemGoodsOption02Binding
import kr.co.releasetech.kiosk.databinding.ItemGoodsDetailBasicBinding
import kr.co.releasetech.kiosk.model.OptionItem
import kr.co.releasetech.kiosk.model.realm.Option
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewHolder
import splitties.views.inflate
import splitties.views.onClick

class GoodsDetailBasicHolder(val ctx: Context, view: View): BaseViewHolder<ItemGoodsDetailBasicBinding>(view) {
    companion object{
        private const val TAG = "GoodsDetailBasicHolder"
    }
    fun onBind(item: OptionItem){
        with(binding){
            nameTv.text = item.category.name
            DebugUtils.setLog(TAG, "name: ${item.category.name}")
            if(item.category.isSingle){
                setSingleChoiceView(item.category.id, item.options)
            }else{
                setMultipleChoiceView(item.options)
            }

        }
    }

    private fun setSingleChoiceView(categoryId : Int, items: ArrayList<Option>){
        val checkImages = arrayOfNulls<ImageView>(items.size)
        for (i in items.indices){
            val option = items[i]
            val optionBinding = IncludeItemGoodsOption01Binding.bind(ctx.inflate(R.layout.include_item_goods_option_01))
            optionBinding.apply {
                nameTv.text = option.name
                priceTv.text = option.price.toString()
                if(i == 0) choiceIv.isSelected = true
                checkImages[i] = choiceIv
                checkImages[i]?.onClick{
                    for (j in items.indices)  {
                        checkImages[j]?.isSelected = i == j
                        if(checkImages[j]?.isSelected == true) binding.vm?.setSingleMap(categoryId, items[j])
                    }
                }
            }

            binding.optionLl.addView(optionBinding.root)

        }
    }

    private fun setMultipleChoiceView(items: ArrayList<Option>){

        items.map { item ->
            val optionBinding = IncludeItemGoodsOption02Binding.bind(ctx.inflate(R.layout.include_item_goods_option_02))
            optionBinding.apply {
                nameTv.text = item.name
                priceTv.text = item.price.toString()
                choiceIv.onClick{
                    it.isSelected = !it.isSelected
                    binding.vm?.setMultipleOptionPrice(item, it.isSelected)
                }
            }

            binding.optionLl.addView(optionBinding.root)
        }
    }
}*/
