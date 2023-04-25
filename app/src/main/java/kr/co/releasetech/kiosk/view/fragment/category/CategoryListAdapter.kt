package kr.co.releasetech.kiosk.view.fragment.category

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.ItemTouchHelperCallback
import kr.co.releasetech.kiosk.view.activity.addOption.AddOptionAdapter
import kr.co.releasetech.kiosk.view.base.BaseAdapter
import java.util.*
import kotlin.collections.ArrayList

class CategoryListAdapter(val ctx: Context, val vm: CategoryViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperCallback.ItemTouchHelperListener {

    companion object{
        private const val  TAG = "CategoryListAdapter"
    }

    val inflater: LayoutInflater by lazy {
        ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    val mList = arrayListOf<Category>()

    private var isDrag = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = CategoryHolder(
        inflater.inflate(R.layout.item_category, parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as CategoryHolder
        holder.binding.item = mList[position]
        holder.binding.vm = vm
        holder.onBind(mList[position], position)
    }

    override fun getItemCount(): Int = mList.size


    fun addList(list: RealmResults<Category>){
        mList.clear()
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

    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        when(actionState){
            ItemTouchHelper.ACTION_STATE_DRAG -> {
                isDrag = true
            }
            ItemTouchHelper.ACTION_STATE_IDLE -> {
                if(isDrag){
                    vm.moveItem()
                    isDrag = false
                }
            }
        }
    }


}