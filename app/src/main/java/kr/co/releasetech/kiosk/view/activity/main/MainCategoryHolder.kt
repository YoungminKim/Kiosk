package kr.co.releasetech.kiosk.view.activity.main

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ItemMainCategoryBinding
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.view.base.BaseViewHolder
import splitties.views.onClick

class MainCategoryHolder(val ctx: Context, val vm: MainViewModel, view: View) :
    BaseViewHolder<ItemMainCategoryBinding>(view) {
    companion object {
        const val MIN_SCALE = 0.85f
    }

    fun onBind(item: Category, isSelect: Boolean = false) {
        with(binding) {
            nameTv.setTextColor(
                if (isSelect) vm.BgColorField.get()!!
                else
                    ContextCompat.getColor(
                        ctx,
                        R.color.off_select_color
                    )
            )

            nameTv.text = item.name

            //lineV.visibility = if (isSelect) View.VISIBLE else View.GONE

            rl.onClick {
                vm.getGoodsList(item.id)
            }
            /*val adapter =  MainGoodsPagerAdapter(ctx, vm, item.goodsList)

            vp.offscreenPageLimit = 3

            val pageMarginPx =
                ctx.resources.getDimensionPixelOffset(R.dimen.item_goods_pager_horizontal_margin)
            val pagerWidth =
                ctx.resources.getDimensionPixelSize(R.dimen.item_goods_pager_width)


            val screenWidth = ctx.resources.displayMetrics.widthPixels
            val offsetPx = screenWidth - pageMarginPx - pagerWidth
            vp.setPageTransformer { page, position ->
                page.translationX = position * -offsetPx
                if( position < -1) return@setPageTransformer

                if( position <= 1){
                    val scaleFactor = max(MIN_SCALE, 1 - abs(position * getEase(abs(position))))
                    page.scaleX = scaleFactor
                    page.scaleY = scaleFactor
                }else{
                    page.scaleX = MIN_SCALE
                    page.scaleY = MIN_SCALE
                }


            }


            vp.adapter = adapter
            adapter.notifyDataSetChanged()*/
        }
    }


    private fun getEase(position: Float): Float {
        val sqt = position * position;
        return sqt / (2.0f * (sqt - position) + 1.0f)
    }
}