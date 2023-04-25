package kr.co.releasetech.kiosk.view.activity.main

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kr.co.releasetech.kiosk.databinding.ItemMainGoodsBinding
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.utils.TextUtils
import kr.co.releasetech.kiosk.view.base.BaseViewHolder

class MainGoodsHolder(private val ctx: Context, view: View): BaseViewHolder<ItemMainGoodsBinding>(view) {

    fun onBindGoods(item: Goods){
        binding.priceTv.text = TextUtils.getMoneyComma(item.price) + "원"

        Glide.with(ctx)
            .load(item.imgUrl)
            .transform(RoundedCorners(12))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.iv)

    }

}