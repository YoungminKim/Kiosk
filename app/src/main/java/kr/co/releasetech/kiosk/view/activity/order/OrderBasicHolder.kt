package kr.co.releasetech.kiosk.view.activity.order

import android.content.Context
import android.view.View
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ItemOrderBasicBinding
import kr.co.releasetech.kiosk.model.realm.Order
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewHolder

class OrderBasicHolder(val ctx: Context, view: View): BaseViewHolder<ItemOrderBasicBinding>(view) {
    companion object{
        private const val TAG = "OrderBasicHolder"
    }

    fun onBind(item: Order){
        with(binding){
            dateTv.text = item.date
            nameTv.text = item.name


            if(item.isSuccess){
                successTv.text  = ctx.getString(R.string.payment_success)
                failedLl.visibility = View.GONE
            }else{
                successTv.text = ctx.getString(R.string.payment_failed)
                failedLl.visibility = View.VISIBLE
                failedTv.text = item.resultMsg
            }

            takeoutTv.text = if(item.isTakeout) ctx.getString(R.string.takeout) else ctx.getString(R.string.store)
            DebugUtils.setLog(TAG, "date : ${item.time}")
            orderNumberTv.text = item.id.toString()
        }
    }
}