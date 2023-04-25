package kr.co.releasetech.kiosk.view.activity.option

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.IncludeItemGoodsOption01Binding
import kr.co.releasetech.kiosk.databinding.IncludeItemGoodsOption02Binding
import kr.co.releasetech.kiosk.databinding.ItemOptionBinding
import kr.co.releasetech.kiosk.model.OptionItem
import kr.co.releasetech.kiosk.model.realm.Option
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.utils.TextUtils.getMoneyComma
import kr.co.releasetech.kiosk.view.base.BaseViewHolder
import splitties.views.inflate
import splitties.views.onClick

class OptionHolder(private val ctx: Context, view: View): BaseViewHolder<ItemOptionBinding>(view) {
    companion object{
        private const val TAG = "OptionHolder"
    }
    fun onBind(item: OptionItem){
        with(binding){
            nameTv.text = item.category.name
            DebugUtils.setLog(TAG, "name: ${item.category.name}")
            if(item.category.isSingle){
                setSingleChoiceView(item.category.id, item.options, item.singlePosition)
            }else{
                setMultipleChoiceView(item.options, item.selectMultipleOptions)
            }

        }
    }

    private fun setSingleChoiceView(categoryId : Int, items: ArrayList<Option>, selectPosition: Int = 0){
        val checkImages = arrayOfNulls<ImageView>(items.size)


        for (i in items.indices){
            val option = items[i]
            val optionBinding = IncludeItemGoodsOption01Binding.bind(ctx.inflate(R.layout.include_item_goods_option_01))
            optionBinding.apply {
                nameTv.text = option.name
                priceTv.text = getMoneyComma(option.price) + "원"
                if(i == selectPosition) choiceIv.isSelected = true
                checkImages[i] = choiceIv
                checkImages[i]?.onClick{
                    for (j in items.indices)  {
                        checkImages[j]?.isSelected = i == j
                        if(checkImages[j]?.isSelected == true) binding.vm?.setSingleMap(categoryId, items[j])
                    }
                }
                fl.onClick{
                    for (j in items.indices)  {
                        checkImages[j]?.isSelected = i == j
                        if(checkImages[j]?.isSelected == true) binding.vm?.setSingleMap(categoryId, items[j])
                    }
                }
            }

            binding.ll.addView(optionBinding.root)

        }
    }

    private fun setMultipleChoiceView(items: ArrayList<Option>, selectMultipleOptions: HashMap<Int, Boolean>){

        items.map { item ->
            val optionBinding = IncludeItemGoodsOption02Binding.bind(ctx.inflate(R.layout.include_item_goods_option_02))
            optionBinding.apply {
                nameTv.text = item.name
                priceTv.text = getMoneyComma(item.price) + "원"
                selectMultipleOptions[item.id]?.let { choiceIv.isSelected = it }

                choiceIv.onClick{
                    it.isSelected = !it.isSelected
                    binding.vm?.setMultipleOptionPrice(item, it.isSelected)
                }

                fl.onClick{
                    it.isSelected = !it.isSelected
                    binding.vm?.setMultipleOptionPrice(item, it.isSelected)
                }
            }

            binding.ll.addView(optionBinding.root)
        }
    }
}