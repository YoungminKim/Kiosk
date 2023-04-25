package kr.co.releasetech.kiosk.view.activity.orderdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.Cart

class OrderDetailAdapter(val ctx: Context) : RecyclerView.Adapter<OrderDetailHolder>() {

    private val mList = arrayListOf<Cart>()

    val inflater: LayoutInflater by lazy {
        ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailHolder =
        OrderDetailHolder(
            inflater.inflate(
                R.layout.item_order_detail, parent, false
            )
        )

    override fun onBindViewHolder(holder: OrderDetailHolder, position: Int) {
        holder.binding.item = mList[position]
        holder.onBind(mList[position])
    }

    override fun getItemCount(): Int = mList.size

    fun addList(list: RealmResults<Cart>){
        mList.addAll(list)
    }
}