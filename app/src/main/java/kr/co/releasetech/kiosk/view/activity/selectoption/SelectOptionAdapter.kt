package kr.co.releasetech.kiosk.view.activity.selectoption

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.OptionCategory
import kr.co.releasetech.kiosk.view.base.BaseAdapter

class SelectOptionAdapter(override val ctx: Context, val vm: SelectOptionViewModel) :
    BaseAdapter() {

    private val mList = arrayListOf<OptionCategory>()
    override fun onCreateBasicViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = SelectOptionHolder(
        inflater.inflate(R.layout.item_select_option, parent, false)
    )

    override fun onBindBasicItemView(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as SelectOptionHolder
        holder.binding.item = mList[position]
        holder.binding.vm = vm
    }

    fun addList(list: ArrayList<OptionCategory>) {
        mList.clear()
        mList.addAll(list)
        setBasicItemCount(mList.size)
        notifyAdapter()
    }
}