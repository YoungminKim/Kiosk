package kr.co.releasetech.kiosk.view.activity.order

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.Order
import kr.co.releasetech.kiosk.view.base.BaseAdapter


class OrderAdapter(override val ctx: Context, val vm: OrderViewModel) : BaseAdapter() {
    private val mList = arrayListOf<Order>()

    override fun useHeader(): Boolean = false

    override fun onCreateHeaderViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? = OrderHeaderHolder(
            inflater.inflate(R.layout.item_order_header, parent, false)
    )
    override fun onCreateBasicViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = OrderBasicHolder(ctx,
            inflater.inflate(R.layout.item_order_basic, parent, false)
    )

    override fun onBindBasicItemView(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as OrderBasicHolder
        holder.binding.vm = vm
        holder.binding.item = mList[position]
        holder.onBind(mList[position])
    }

    fun addList(list: RealmResults<Order>){
        mList.clear()
        mList.addAll(list)
        setBasicItemCount(mList.size)
        notifyAdapter()
    }
}