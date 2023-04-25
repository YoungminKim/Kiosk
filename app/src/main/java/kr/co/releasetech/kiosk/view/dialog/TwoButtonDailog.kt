package kr.co.releasetech.kiosk.view.dialog

import android.content.Context
import android.os.Bundle
import kr.co.releasetech.kiosk.databinding.DialogTwoButtonBinding
import kr.co.releasetech.kiosk.view.base.BaseDialog
import splitties.views.onClick

class TwoButtonDailog(val ctx: Context, val content: String, val onConfirmFunc: (() -> Unit)) : BaseDialog(ctx) {

    lateinit var binding: DialogTwoButtonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogTwoButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            contentTv.text = content
            confirmTv.onClick{
                onConfirmFunc.invoke()
                dismiss()
            }

            cancelTv.onClick{
                dismiss()
            }
        }
    }


}