package kr.co.releasetech.kiosk.view.fragment.goods

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.releasetech.kiosk.databinding.ItemGoodsBinding
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.utils.ImageUtils
import kr.co.releasetech.kiosk.view.base.BaseViewHolder
import splitties.views.imageBitmap
import java.io.File

class GoodsHolder(view: View): BaseViewHolder<ItemGoodsBinding>(view) {
    companion object{
        private const val TAG = "GoodsHolder"
    }
    fun onBind(item: Goods, categoryName: String, isPreCategorySame: Boolean){
        binding.run {
            categoryTv.visibility = if(isPreCategorySame) View.GONE else View.VISIBLE
            categoryTv.text = categoryName
        }

    }
}