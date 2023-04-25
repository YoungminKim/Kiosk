package kr.co.releasetech.kiosk.view.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager

abstract class BaseDialog
    : Dialog {

    constructor(ctx: Context) : super(ctx, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)

    constructor(ctx: Context, theme: Int) : super(
        ctx,
        android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
    )

    protected constructor(
        ctx: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener
    ) : super(ctx, cancelable, cancelListener)


    companion object {
        private const val DIM_AMOUNT = 0.8f
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lpWindow = WindowManager.LayoutParams()
        lpWindow.apply {
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dimAmount = DIM_AMOUNT
        }
        window!!.attributes = lpWindow

    }

    override fun dismiss() {
        super.dismiss()
    }
}