package kr.co.releasetech.kiosk.view.activity.editscreen

import android.view.View
import kr.co.releasetech.kiosk.AppConst
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ItemEditScreenStanbyBgBinding
import kr.co.releasetech.kiosk.model.realm.EditScreenStandbyBg
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewHolder
import splitties.views.onClick

class EditScreenStandbyBgHolder(view: View): BaseViewHolder<ItemEditScreenStanbyBgBinding>(view) {
    companion object{
        private const val TAG = "EditScreenStandbyBgHolder"
    }
    fun onBind(item: EditScreenStandbyBg, isLast: Boolean){
        with(binding){
            addLl.visibility = if(isLast) View.VISIBLE else View.GONE

            fileIv.setImageResource(if(item.isVideo) R.drawable.file_video else R.drawable.file_img)

            fileTv.text = item.fileName
        }


    }
}