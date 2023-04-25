package kr.co.releasetech.kiosk.view.activity.editscreen

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.releasetech.kiosk.R

class EditScreenThemeAdapter(val ctx: Context, val vm: EditScreenViewModel) : RecyclerView.Adapter<EditScreenThemeHolder>() {

    private val mList = ctx.resources.getStringArray(R.array.theme_arr)
    private var mSelectPosition = 0

    val inflater: LayoutInflater by lazy {
        ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditScreenThemeHolder =
        EditScreenThemeHolder(ctx, inflater.inflate(R.layout.item_edit_screen_theme, parent, false))

    override fun onBindViewHolder(holder: EditScreenThemeHolder, position: Int) {
        holder.binding.position = position
        holder.binding.vm = vm
        holder.onBind(position, mList[position], mSelectPosition)
    }

    override fun getItemCount() = mList.size

    fun selectItem(position: Int){
        mSelectPosition = position
        notifyDataSetChanged()
    }
}