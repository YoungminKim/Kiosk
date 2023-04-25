package kr.co.releasetech.kiosk.view.activity.main

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.view.base.BaseAdapter

class MainGoodsListAdapter(override val ctx: Context, val vm: MainViewModel) : BaseAdapter(){


    private val mList = arrayListOf<Goods>()

    override fun onCreateBasicViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = MainGoodsHolder(
        ctx,
        inflater.inflate(R.layout.item_main_goods, parent, false)
    )

    override fun onBindBasicItemView(holder: RecyclerView.ViewHolder?, position: Int) {
        holder as MainGoodsHolder
        holder.binding.item = mList[position]
        holder.binding.vm = vm

        holder.onBindGoods(mList[position])
    }

    fun addList(list: ArrayList<Goods>){
        mList.clear()
        mList.addAll(list)
        setBasicItemCount(mList.size)
        notifyAdapter()
    }


}