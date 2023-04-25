package kr.co.releasetech.kiosk.view.activity.main

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.view.base.BaseAdapter


class MainCategoryListAdapter(override val ctx: Context, val vm: MainViewModel) :
    BaseAdapter() {

    private val mList = arrayListOf<Category>()

    private var selectPosition = 0

    override fun getItemCount(): Int = mList.size

    override fun onCreateBasicViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = MainCategoryHolder(ctx, vm,
        inflater.inflate(R.layout.item_main_category, parent, false)
    )

    override fun onBindBasicItemView(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as MainCategoryHolder
        holder.onBind(mList[position], selectPosition == position)
    }

    fun addList(list: ArrayList<Category>){
        mList.addAll(list)
        setBasicItemCount(mList.size)
        notifyAdapter()
    }

    fun onSelect(cateId: Int){
        for (i in mList.indices){
            val item = mList[i]
            if(item.id == cateId){
                selectPosition = i
                break
            }
        }

        notifyAdapter()

    }



}