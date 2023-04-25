package kr.co.releasetech.kiosk.view.activity.selectcategory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.view.base.BaseAdapter
import kr.co.releasetech.kiosk.view.fragment.category.CategoryViewModel

class SelectCategoryAdapter(override val ctx: Context, val vm: SelectCategoryViewModel) :
    BaseAdapter() {

    private val mList = arrayListOf<Category>()
    override fun onCreateBasicViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = SelectCategoryHolder(
        inflater.inflate(R.layout.item_select_category, parent, false)
    )

    override fun onBindBasicItemView(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as SelectCategoryHolder
        holder.binding.item = mList[position]
        holder.binding.vm = vm
    }

    fun addList(list: RealmResults<Category>) {
        mList.clear()
        mList.addAll(list)
        setBasicItemCount(mList.size)
        notifyAdapter()
    }
}