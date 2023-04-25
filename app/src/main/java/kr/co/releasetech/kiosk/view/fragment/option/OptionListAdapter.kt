package kr.co.releasetech.kiosk.view.fragment.option

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.OptionCategory
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseAdapter

class OptionListAdapter(override val ctx: Context, val vm:  OptionViewModel) : BaseAdapter() {
    companion object{
        private const val TAG = "OptionListAdapter"
    }
    val mList = ArrayList<OptionCategory>()
    override fun onCreateBasicViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder  = OptionBasicHolder(inflater.inflate(R.layout.item_option_basic, parent, false))

    override fun onBindBasicItemView(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as OptionBasicHolder
        holder.binding.vm = vm
        holder.binding.item = mList[position]
        holder.onBind(mList[position])
    }

    fun addList(list: RealmResults<OptionCategory>){
        mList.clear()
        mList.addAll(list)
        setBasicItemCount(mList.size)
        DebugUtils.setLog(TAG, "size: ${getBasicItemCount()}")
        notifyDataSetChanged()
    }

    fun removeItem(item: OptionCategory){
        val position = mList.indexOf(item)
        mList.removeAt(position)
        setBasicItemCount(mList.size)
        notifyItemRemoved(position)
    }
}