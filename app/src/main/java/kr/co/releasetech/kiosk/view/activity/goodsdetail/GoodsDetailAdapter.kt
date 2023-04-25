/*
package kr.co.releasetech.kiosk.view.activity.goodsdetail

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.OptionItem
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseAdapter

class GoodsDetailAdapter(override val ctx: Context, val vm: GoodsDetailViewModel) : BaseAdapter() {
    companion object {
        private const val TAG = "GoodsDetailAdapter"
    }

    private val mList = ArrayList<OptionItem>()

    override fun onCreateBasicViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        GoodsDetailBasicHolder(ctx, inflater.inflate(R.layout.item_goods_detail_basic, parent, false))

    override fun onBindBasicItemView(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as GoodsDetailBasicHolder
        holder.binding.vm = vm
        holder.onBind(mList[position])
    }

    fun addList(list: ArrayList<OptionItem>) {

        mList.addAll(list)
        setBasicItemCount(mList.size)

        notifyAdapter()
    }
}*/
