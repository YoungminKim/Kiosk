package kr.co.releasetech.kiosk.view.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import splitties.toast.toast

abstract class BaseAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object{
        const val TYPE_HEADER = Integer.MIN_VALUE
        const val TYPE_FOOTER = Integer.MIN_VALUE + 1
        const val TYPE_BASIC = Integer.MIN_VALUE + 2
        const val TYPE_ADAPTEE_OFFSET = 0

        const val TYPE_GROUP = -1
        const val TYPE_CHILD = -2
    }

    abstract val ctx: Context
    val inflater: LayoutInflater by lazy {
        ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> onCreateHeaderViewHolder(parent, viewType)!!
            TYPE_FOOTER -> onCreateFooterViewHolder(parent, viewType)!!
            TYPE_ADAPTEE_OFFSET -> onCreateBasicViewHolder(parent, viewType)
            else -> onCreateBasicViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0 && holder.itemViewType == TYPE_HEADER) {
            onBindHeaderItemView(holder, position)
        } else if (position == itemCount - 1 && holder.itemViewType == TYPE_FOOTER) {
            onBindFooterItemView(holder, position)
        } else {
            var basicPosition = position - itemCount + basicItemCount
            if(useFooter())basicPosition += 1
            onBindBasicItemView(holder, basicPosition)
        }
    }

    override fun getItemCount(): Int {
        var itemCount = basicItemCount
        if (useHeader()) {
            itemCount += 1
        }
        if (useFooter()) {
            itemCount += 1
        }
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0 && useHeader()) {
            return TYPE_HEADER
        }
        if (position == itemCount - 1  && useFooter()) {
            return TYPE_FOOTER
        }

        if (getBasicItemType(position) != Integer.MAX_VALUE - TYPE_ADAPTEE_OFFSET) {
            IllegalStateException("HeaderRecyclerViewAdapter offsets your BasicItemType by ${TYPE_ADAPTEE_OFFSET}.")
        }

        return getBasicItemType(position) + TYPE_ADAPTEE_OFFSET
    }

    /**
     * 헤더 추가시 true
     */
    open fun useHeader() = false
    open fun onCreateHeaderViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? = null
    open fun onBindHeaderItemView(holder: RecyclerView.ViewHolder, position: Int){}

    /**
     * Footer 추가시 true
     */

    open fun useFooter() = false
    open fun onCreateFooterViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? = null
    open fun onBindFooterItemView(holder: RecyclerView.ViewHolder, position: Int){}


    abstract fun onCreateBasicViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    abstract fun onBindBasicItemView(holder: RecyclerView.ViewHolder?, position: Int)


    private var basicItemCount: Int = 0


    fun setBasicItemCount(count: Int){
        basicItemCount = count
    }

    fun getBasicItemCount() = basicItemCount

    open fun getBasicItemType(position: Int): Int = position


    fun setToast(msgInt: Int) {

        if(msgInt > - 1) toast(msgInt)
    }

    fun setToast(msgStr: String) {
        msgStr?.let { toast(msgStr) }
    }


    open fun notifyAdapter() {
        notifyDataSetChanged()
    }

}