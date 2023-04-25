package kr.co.releasetech.kiosk.view.fragment.goods

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.view.ItemTouchHelperCallback
import kr.co.releasetech.kiosk.view.base.BaseAdapter

class GoodsAdapter(val ctx: Context, val vm: GoodsViewModel) : RecyclerView.Adapter<GoodsHolder>() {

    private val mList = arrayListOf<Goods>()
    private val mCategoryList = arrayListOf<Category>()

    val inflater: LayoutInflater by lazy {
        ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getItemCount(): Int  = mList.size



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsHolder = GoodsHolder(
        inflater.inflate(R.layout.item_goods, parent, false)
    )

    override fun onBindViewHolder(holder: GoodsHolder, position: Int) {
        holder.binding.item = mList[position]
        holder.binding.vm = vm

        var isSame = false
        if(position > 0 && mList[position - 1].categoryId == mList[position].categoryId){
            isSame = true
        }

        var categoryName: String = ""
        mCategoryList.map {
            if(it.id == mList[position].categoryId) categoryName = it.name
        }

        holder.onBind(mList[position], categoryName, isSame)
    }

    fun addList(list : RealmResults<Goods>){
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun setCategoryList(categoryList: RealmResults<Category>){
        mCategoryList.clear()
        mCategoryList.addAll(categoryList)
    }



}