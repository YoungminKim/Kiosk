package kr.co.releasetech.kiosk.view.activity.addgoods

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.OptionCategory
import kr.co.releasetech.kiosk.view.ItemTouchHelperCallback
import java.util.*
import kotlin.collections.ArrayList

class AddGoodsOptionAdapter(val ctx: Context, val vm: AddGoodsViewModel) : RecyclerView.Adapter<AddGoodsOptionHolder>(), ItemTouchHelperCallback.ItemTouchHelperListener {

    val inflater: LayoutInflater by lazy {
        ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    val mList = ArrayList<OptionCategory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddGoodsOptionHolder =
        AddGoodsOptionHolder(
            inflater.inflate(
                R.layout.item_add_goods_option, parent, false
            )
        )

    override fun onBindViewHolder(holder: AddGoodsOptionHolder, position: Int) {
        holder.binding.vm = vm
        holder.onBind(mList[position], position == mList.size - 1)
    }

    override fun getItemCount(): Int = mList.size

    fun addItem(item: OptionCategory) {
        mList.add(item)
        notifyItemInserted(mList.size)
        notifyItemChanged(mList.size -2)

    }


    fun addList(list: ArrayList<OptionCategory>) {
        mList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if(fromPosition < toPosition){
            for(i in fromPosition until toPosition){
                Collections.swap(mList, i, i+1)
            }
        }else{
            for (i in fromPosition downTo toPosition + 1){
                Collections.swap(mList, i, i-1)
            }
        }

        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)
        notifyItemMoved(fromPosition, toPosition)

        return true
    }

    override fun onItemSwipe(position: Int) {
        notifyItemRemoved(position)
        mList.removeAt(position)
        notifyItemChanged(mList.size - 1)

        if(itemCount == 0) vm.emptyOptions()
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
    }
}