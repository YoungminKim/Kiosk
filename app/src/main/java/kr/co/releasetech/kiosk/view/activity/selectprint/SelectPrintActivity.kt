package kr.co.releasetech.kiosk.view.activity.selectprint

import android.content.Intent
import android.os.Bundle
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivitySelectPrintBinding
import kr.co.releasetech.kiosk.model.realm.Payment
import kr.co.releasetech.kiosk.model.realm.ScreenSetting
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectPrintActivity: BaseActivity<ActivitySelectPrintBinding>(R.layout.activity_select_print) {
    companion object{
        private const val TAG = "SelectPrintActivity"
    }
    val viewModel: SelectPrintViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.vm = viewModel

        val setting = intent.getParcelableExtra<ScreenSetting>("setting")

        DebugUtils.setLog(TAG, "setting : ${setting?.themeType}")

        setting?.let {
            binding.confirmTv.setBackgroundResource(resources.getIdentifier("theme_btn_${setting.themeType}", "drawable", packageName))
        }

        with(viewModel){
            onCancel.observe(this@SelectPrintActivity){
                finish()
            }

            onConfirm.observe(this@SelectPrintActivity){
                val intent = Intent()
                intent.putExtra("payment", this@SelectPrintActivity.intent.getParcelableExtra<Payment>("payment"))
                setResult(RESULT_OK, intent)
                finish()
            }
        }

    }
}