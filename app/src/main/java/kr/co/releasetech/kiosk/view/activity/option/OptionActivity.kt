package kr.co.releasetech.kiosk.view.activity.option

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import kr.co.releasetech.kiosk.AppConst
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityOptionBinding
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.model.realm.ScreenSetting
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.activity.main.MainActivity
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class OptionActivity: BaseActivity<ActivityOptionBinding>(R.layout.activity_option) {
    companion object{
        private const val TAG = "OptionActivity"
    }
    val viewModel: OptionViewModel by viewModel()

    private var mCountDownTimer: CountDownTimer? = null



    private val mAdapter: OptionAdapter by lazy{
        OptionAdapter(this, viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val setting = intent.getParcelableExtra<ScreenSetting>("setting")
        setting?.let { setting ->
            setCountDownTimer(setting.orderScreenWaitSecond * 1000L)
            binding.addTv.setBackgroundResource(resources.getIdentifier("theme_btn_${setting.themeType}", "drawable", packageName))
        }

        with(binding){
            lifecycleOwner = this@OptionActivity
            vm = viewModel
            goods = intent?.getParcelableExtra<Goods>("goods")
            cart = intent?.getParcelableExtra("cart")
            rv.adapter = mAdapter

        }

        with(viewModel){

            binding.goods?.optionCategoryIds?.let {
                binding.cart?.let { cart -> getOptionList(it, cart)  }
            }


            onCancel.observe(this@OptionActivity){
                setResult(RESULT_CANCELED)
                finish()
            }

            onOptionList.observe(this@OptionActivity){
                mAdapter.addList(it)
            }

            onAddCart.observe(this@OptionActivity){
                val intent = Intent()
                intent.putExtra("cart", it)
                setResult(RESULT_OK, intent)
                finish()
            }

            onRestartTimer.observe(this@OptionActivity){
                mCountDownTimer?.cancel()
                mCountDownTimer?.start()
            }

            onPauseTimer.observe(this@OptionActivity){
                mCountDownTimer?.cancel()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        mCountDownTimer?.start()
    }


    override fun onPause() {
        super.onPause()
        mCountDownTimer?.cancel()


    }


    private fun setCountDownTimer(millisInFuture: Long){
        mCountDownTimer?.cancel()
        mCountDownTimer = object : CountDownTimer(
            millisInFuture,
            AppConst.CLOSE_ORDER_SCREEN_INTERVAL
        ) {
            override fun onTick(millisUntilFinished: Long) {
                DebugUtils.setLog(TAG, "onTick")
            }

            override fun onFinish() {
                setResult(TIME_OUT)
                finish()
            }

        }

    }
}