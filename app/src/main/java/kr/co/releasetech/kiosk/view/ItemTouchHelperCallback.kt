package kr.co.releasetech.kiosk.view

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import kr.co.releasetech.kiosk.utils.DebugUtils

class ItemTouchHelperCallback(val listener: ItemTouchHelperListener, private val isSwipe: Boolean = true): ItemTouchHelper.Callback() {
    companion object{
       private const val TAG = "ItemTouchHelperCallback"
    }

    interface ItemTouchHelperListener{
        fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
        fun onItemSwipe(position: Int)
        fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int)

    }


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if(isSwipe) makeMovementFlags(UP or DOWN, LEFT or RIGHT)
        else makeMovementFlags(UP or DOWN, ACTION_STATE_IDLE)
    }


    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        listener.onSelectedChanged(viewHolder, actionState)
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = listener.onItemMove(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onItemSwipe(viewHolder.bindingAdapterPosition)
    }


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        if (actionState == ACTION_STATE_SWIPE){

        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}