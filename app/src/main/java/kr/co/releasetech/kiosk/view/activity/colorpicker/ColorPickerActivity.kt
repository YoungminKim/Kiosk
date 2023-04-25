package kr.co.releasetech.kiosk.view.activity.colorpicker

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityColorPickerBinding
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseActivity
import kr.co.releasetech.kiosk.view.base.BaseDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class ColorPickerActivity: BaseActivity<ActivityColorPickerBinding>(R.layout.activity_color_picker) {
    companion object{
        private const val TAG = "ColorPickerActivity"
    }

    val viewModel: ColorPickerViewModel by viewModel()

    var mColor = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lpWindow = WindowManager.LayoutParams()
        lpWindow.apply {
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dimAmount = 0.8f
        }
        window!!.attributes = lpWindow

        with(binding){
            lifecycleOwner = this@ColorPickerActivity
            vm = viewModel

            val hexCode = intent.getStringExtra("color")
            val bubbleFlag = BubbleFlag(this@ColorPickerActivity)
            bubbleFlag.flagMode = FlagMode.ALWAYS
            bubbleFlag.setBackgroundColor(Color.TRANSPARENT)
            colorPickerView.flagView = bubbleFlag
            colorPickerView.setInitialColor(Color.parseColor(hexCode))

            colorPickerView.setColorListener(object : ColorEnvelopeListener{
                override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                    envelope?.let {
                        viewModel.colorField.set("#${envelope.hexCode}")
                        mColor = envelope.color
                    }
                }

            })
            colorPickerView.attachAlphaSlider(alphaSlideBar)
            colorPickerView.attachBrightnessSlider(brightnessSlide)

            colorEt.addTextChangedListener { editable ->
                editable?.let {
                    if(editable.isEmpty()){
                        editable.insert(0, "#")
                    }
                }
            }
        }

        with(viewModel){
            onConfirm.observe(this@ColorPickerActivity){
                val intent = Intent()
                intent.putExtra("hexCode", colorField.get())
                intent.putExtra("color", mColor)
                intent.putExtra("type", getIntent().getStringExtra("type"))
                setResult(RESULT_OK, intent)
                finish()
            }

            onCancel.observe(this@ColorPickerActivity){
                setResult(RESULT_CANCELED)
                finish()
            }

            isNotHexCode.observe(this@ColorPickerActivity){
                showToast(R.string.not_color_hex_code)
            }
        }
    }


}