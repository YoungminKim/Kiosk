package kr.co.releasetech.kiosk.view.activity.editscreen

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import io.realm.RealmSet
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.EditScreenStandbyBg
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.ItemTouchHelperCallback
import kr.co.releasetech.kiosk.view.activity.addOption.AddOptionAdapter
import java.util.*
import kotlin.collections.ArrayList

class EditScreenStandbyBgAdapter(val ctx: Context, val vm: EditScreenViewModel): RecyclerView.Adapter<EditScreenStandbyBgHolder>(), ItemTouchHelperCallback.ItemTouchHelperListener {
    companion object{
        private const val TAG = "EditScreenStandbyBgAdapter"
    }
    val mList = ArrayList<EditScreenStandbyBg>()
    var isDrag = false

    val inflater: LayoutInflater by lazy {
        ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditScreenStandbyBgHolder = EditScreenStandbyBgHolder(inflater.inflate(
        R.layout.item_edit_screen_stanby_bg, parent, false))

    override fun onBindViewHolder(holder: EditScreenStandbyBgHolder, position: Int) {
        holder.binding.vm = vm
        holder.binding.item = mList[position]
        holder.onBind(mList[position], mList.size - 1 == position)
    }

    override fun getItemCount() = mList.size


    fun addList(list: RealmResults<EditScreenStandbyBg>){
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun addItem(item: EditScreenStandbyBg){
        mList.add(item)
        notifyItemRangeChanged(mList.size - 2, mList.size - 1)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {

        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(mList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(mList, i, i - 1)
            }
        }


        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)

        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemSwipe(position: Int) {
        vm.removeItem(mList[position])
        mList.removeAt(position)
        notifyItemRemoved(position)
        if (position < itemCount) notifyItemRangeChanged(position, itemCount)
        else notifyItemChanged(itemCount - 1)

        if (itemCount == 0) vm.emptyStandbyBg()
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG -> {
                isDrag = true
            }
            ItemTouchHelper.ACTION_STATE_IDLE -> {
                if (isDrag) {

                    mList.map {
                        vm.moveItem(mList)
                    }

                    isDrag = false
                    //notifyDataSetChanged()
                }
            }
        }
    }
}