package kr.co.releasetech.kiosk.view.activity.payment

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.Payment
import kr.co.releasetech.kiosk.view.base.BaseAdapter

class PaymentAdapter(override val ctx: Context, private val vm: PaymentViewModel) : BaseAdapter() {
    private val mList = arrayListOf<Payment>()
    override fun onCreateBasicViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = PaymentHolder(
            inflater.inflate(R.layout.item_payment, parent, false)
    )

    override fun onBindBasicItemView(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as PaymentHolder
        holder.binding.item = mList[position]
        holder.binding.vm = vm
    }

    fun addList(list: RealmResults<Payment>){
        mList.clear()
        mList.addAll(list)
        setBasicItemCount(mList.size)
        notifyAdapter()
    }
}