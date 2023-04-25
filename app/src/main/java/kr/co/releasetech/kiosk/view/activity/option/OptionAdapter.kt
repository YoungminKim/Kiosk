package kr.co.releasetech.kiosk.view.activity.option

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.model.OptionItem
import kr.co.releasetech.kiosk.utils.DebugUtils

class OptionAdapter(private val ctx: Context, private val vm: OptionViewModel) : RecyclerView.Adapter<OptionHolder>() {
    companion object{
        private const val TAG = "OptionAdapter"
    }

    private val mList = ArrayList<OptionItem>()



    val inflater: LayoutInflater by lazy {
        ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder = OptionHolder(
        ctx,
        inflater.inflate(
            R.layout.item_option, parent, false
        )

    )

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        holder.binding.vm = vm
        holder.onBind(mList[position],)
    }

    override fun getItemCount(): Int = mList.size


    fun addList(list: ArrayList<OptionItem>) {
        mList.addAll(list)
        notifyDataSetChanged()
    }
}