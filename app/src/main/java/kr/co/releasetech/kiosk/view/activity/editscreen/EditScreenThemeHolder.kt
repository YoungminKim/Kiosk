package kr.co.releasetech.kiosk.view.activity.editscreen

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ItemEditScreenThemeBinding
import kr.co.releasetech.kiosk.view.base.BaseViewHolder

class EditScreenThemeHolder(val ctx: Context, view: View): BaseViewHolder<ItemEditScreenThemeBinding>(view) {

    fun onBind(position: Int, resName: String, selectPosition: Int){

        val bgColor = ContextCompat.getColor(ctx, if(position == selectPosition) R.color.tab_select_on else R.color.white)
        binding.ll.setBackgroundColor(bgColor)

        val resId = ctx.resources.getIdentifier(resName, "drawable", ctx.packageName)
        Glide.with(ctx)
            .load(resId)
            .into(binding.iv)

    }
}