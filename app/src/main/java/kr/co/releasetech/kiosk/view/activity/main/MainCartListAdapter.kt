package kr.co.releasetech.kiosk.view.activity.main

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.Cart
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.utils.TextUtils
import kr.co.releasetech.kiosk.view.base.BaseAdapter


class MainCartListAdapter(override val ctx: Context, private val vm: MainViewModel) : BaseAdapter() {
    companion object{
        const val TAG = "MainCartListAdapter"
    }
    private val mList = arrayListOf<Cart>()

    override fun onCreateBasicViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder{
        DebugUtils.setLog(TAG, "onCreateBasicViewHolder called!!!")
        return MainCartHolder(
            inflater.inflate(R.layout.item_main_cart, parent, false)
        )
    }

    override fun onBindBasicItemView(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as MainCartHolder
        holder.binding.item = mList[position]
        holder.binding.vm = vm
        holder.binding.position = position
        holder.onBind(mList[position])
    }

    fun addItem(item: Cart){
        mList.add(item)
        DebugUtils.setLog(TAG, "size : ${mList.size}")
        setBasicItemCount(mList.size)
        notifyAdapter()
    }

    fun getList():ArrayList<Cart> = mList

    fun removeCart(item: Cart){
        mList.remove(item)
        setBasicItemCount(mList.size)
        notifyAdapter()
    }

    fun allRemoveCart(){
        mList.clear()
        setBasicItemCount(mList.size)
        notifyAdapter()
    }

    fun modifyCart(cart: Cart, position: Int){
        mList[position] = cart
        var price = 0
        mList.map {
            price += it.totalPrice
        }
        vm.totalPriceField.set("${TextUtils.getMoneyComma(price)}${ctx.getString(R.string.currency)}")
        notifyItemChanged(position)
    }

    override fun notifyAdapter() {
        super.notifyAdapter()
        var price = 0
        mList.map {
            price += it.totalPrice
        }
        vm.totalPriceField.set("${TextUtils.getMoneyComma(price)}${ctx.getString(R.string.currency)}")
    }

}