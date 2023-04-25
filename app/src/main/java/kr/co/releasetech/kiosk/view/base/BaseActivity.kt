package kr.co.releasetech.kiosk.view.base

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import splitties.toast.toast
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.utils.WindowUtils.setImmersiveMode

abstract class BaseActivity<B : ViewDataBinding>(@LayoutRes private val layoutResId: Int) :
    AppCompatActivity() {

    companion object{
        const val TIME_OUT = 8888
    }



    lateinit var binding: B

    override fun onStart() {
        super.onStart()
        DebugUtils.setLog(this.localClassName, "onStart called!!!")
    }

    override fun onRestart() {
        super.onRestart()
        DebugUtils.setLog(this.localClassName, "onRestart called!!!")
    }

    override fun onResume() {
        super.onResume()
        DebugUtils.setLog(this.localClassName, "onResume called!!!")
        setImmersiveMode(window)
    }

    override fun onPause() {
        super.onPause()
        DebugUtils.setLog(this.localClassName, "onPause called!!!")
    }

    override fun onDestroy() {
        super.onDestroy()
        DebugUtils.setLog(this.localClassName, "onDestroy called!!!")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        DebugUtils.setLog(this.localClassName, "onSaveInstanceState called!!!")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        DebugUtils.setLog(this.localClassName, "onRestoreInstanceState called!!!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DebugUtils.setLog(this.localClassName, "onCreate called!!!")
        binding = DataBindingUtil.setContentView(this, layoutResId)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun showToast(message: Int) {
        toast(message)
    }

    fun showToast(message: String) {
        toast(message)
    }
}