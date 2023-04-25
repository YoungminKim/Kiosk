package kr.co.releasetech.kiosk.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import kr.co.releasetech.kiosk.databinding.DialogOneButtonBinding
import kr.co.releasetech.kiosk.view.base.BaseDialog
import splitties.views.onClick

class OneButtonDialog(val ctx: Context, val content: String, private val onConfirmFunc: (() -> Unit)): BaseDialog(ctx) {

    lateinit var binding : DialogOneButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogOneButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            contentTv.text = content
            confirmTv.onClick{
                onConfirmFunc.invoke()
                dismiss()
            }

        }
    }

    override fun onBackPressed() {
    }

    override fun setOnCancelListener(listener: DialogInterface.OnCancelListener?) {
    }
}