package kr.co.releasetech.kiosk.view.activity.addOption

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.OptionDummy
import kr.co.releasetech.kiosk.model.realm.Option
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.ItemTouchHelperCallback
import java.util.*
import kotlin.collections.ArrayList

class AddOptionAdapter(val ctx: Context, val vm: AddOptionViewModel) :
    RecyclerView.Adapter<AddOptionBasicHolder>(),
    ItemTouchHelperCallback.ItemTouchHelperListener {
    companion object {
        private const val TAG = "AddOptionAdapter"
    }

    val mList = ArrayList<OptionDummy>()
    val mRemoveList = ArrayList<OptionDummy>()

    val inflater: LayoutInflater by lazy {
        ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    var isDrag = false
    var isSwipe = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOptionBasicHolder =
        AddOptionBasicHolder(
            inflater.inflate(R.layout.item_add_option_basic, parent, false)
        ) {
            addItem()
        }

    override fun onBindViewHolder(holder: AddOptionBasicHolder, position: Int) {
        holder.onBind(mList[position], position, mList.size - 1 == position) { name, price ->
            if (!isDrag && !isSwipe) {
                DebugUtils.setLog(TAG, "price : $price")

                mList[position].name = name
                mList[position].price = if (price.isEmpty()) 0 else price.toInt()
            }

        }
    }

    override fun getItemCount(): Int = mList.size


    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        DebugUtils.setLog(
            TAG,
            "onItemMove $fromPosition : ${mList[fromPosition].name} , $toPosition : ${mList[toPosition].name}"
        )
        //Collections.swap(mList, fromPosition, toPosition)


        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(mList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(mList, i, i - 1)
            }
        }

        mList[fromPosition].index = toPosition
        mList[toPosition].index = fromPosition

        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)

        DebugUtils.setLog(
            TAG,
            "onItemMove $fromPosition : ${mList[fromPosition].name} , $toPosition : ${mList[toPosition].name}"
        )
        DebugUtils.setLog(TAG, "onItemMove ==========================================")
        notifyItemMoved(fromPosition, toPosition)


        return true
    }

    override fun onItemSwipe(position: Int) {
        mList[position].valid = false
        mRemoveList.add(mList[position])
        mList.removeAt(position)
        for (i in mList.indices) {
            DebugUtils.setLog(TAG, "i : $i name : ${mList[i].name}")
            mList[i].index = i
        }

        notifyItemRemoved(position)
        DebugUtils.setLog(TAG, "onItemSwipe position : $position , itemCount : $itemCount")
        if (position < itemCount) notifyItemRangeChanged(position, itemCount)
        else notifyItemChanged(itemCount - 1)

    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_SWIPE -> isSwipe = true
            ItemTouchHelper.ACTION_STATE_DRAG -> {
                isDrag = true
            }
            ItemTouchHelper.ACTION_STATE_IDLE -> {
                if (isDrag) {
                    isDrag = false
                    isSwipe = false
                    mList.map {
                        DebugUtils.setLog(TAG, "onItemMove ${it.name}")
                    }
                    //notifyDataSetChanged()
                }
            }
        }
    }

    fun addItem() {
        if (mList.size == 0 || mList[mList.size - 1].name.isNotEmpty()) {
            val optionDummy = OptionDummy()
            optionDummy.index = mList.size
            optionDummy.name = ""
            optionDummy.price = 0
            if (mList.size > 0) optionDummy.optionCategoryId = mList[0].optionCategoryId
            mList.add(optionDummy)

            notifyItemInserted(mList.size)
            notifyItemChanged(mList.size - 2)
        } else {

        }
    }


    fun addList(list: ArrayList<OptionDummy>) {
        mList.addAll(list)
        notifyDataSetChanged()
    }


}