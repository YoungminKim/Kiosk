package kr.co.releasetech.kiosk.view.activity.addOption


import android.view.View
import androidx.core.widget.addTextChangedListener
import kr.co.releasetech.kiosk.databinding.ItemAddOptionBasicBinding
import kr.co.releasetech.kiosk.model.OptionDummy
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseViewHolder
import splitties.views.onClick

class AddOptionBasicHolder(
    view: View,
    private val onAddFunc: (() -> Unit)
) : BaseViewHolder<ItemAddOptionBasicBinding>(view) {
    companion object {
        private const val TAG = "AddOptionBasicHolder"
    }

    fun onBind(item: OptionDummy, position: Int, isLast: Boolean = false, onTextFunc: ((String, String) -> Unit)) {

        with(binding) {
            addLl.visibility = if (isLast) View.VISIBLE else View.GONE

            addIv.onClick {
                onAddFunc.invoke()
            }

            numTv.text = "${position + 1}."

            DebugUtils.setLog(TAG, "position: $position . item:${item.name}")
            nameEt.setText(item.name)
            priceEt.setText(item.price.toString())
            nameEt.addTextChangedListener { onTextFunc.invoke(nameEt.text.toString(), priceEt.text.toString()) }
            priceEt.addTextChangedListener {
                DebugUtils.setLog(TAG, "addTextChangedListener")
                onTextFunc.invoke(nameEt.text.toString(), priceEt.text.toString())
            }

        }
    }


}