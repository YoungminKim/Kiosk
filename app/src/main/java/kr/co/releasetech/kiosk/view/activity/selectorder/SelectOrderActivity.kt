package kr.co.releasetech.kiosk.view.activity.selectorder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.releasetech.kiosk.AppConst
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivitySelectOrderBinding
import kr.co.releasetech.kiosk.model.realm.ScreenSetting
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.activity.adminlogin.AdminLoginActivity
import kr.co.releasetech.kiosk.view.activity.main.MainActivity
import kr.co.releasetech.kiosk.view.activity.manager.ManagerActivity
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.activities.start

class SelectOrderActivity :
    BaseActivity<ActivitySelectOrderBinding>(R.layout.activity_select_order) {
    companion object{
        private const val TAG = "SelectOrderActivity"
    }


    val viewModel: SelectOrderViewModel by viewModel()



    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                start<ManagerActivity> {  }
                finish()
            }
        }
    }


    private var mCountDownTimer: CountDownTimer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this


        binding.extra = intent.getParcelableExtra<ScreenSetting>("setting")

        binding.extra?.let { extra ->
            mCountDownTimer = object : CountDownTimer(
                extra.orderScreenWaitSecond * 1000L,
                AppConst.CLOSE_ORDER_SCREEN_INTERVAL
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    DebugUtils.setLog(TAG, "onTick")
                }

                override fun onFinish() {
                    finish()
                }

            }
            if(extra.useStandbyScreen) mCountDownTimer?.start()

            extra.logoImage?.let { logoImage ->
                binding.logoIv?.let { logoIv ->
                    logoIv.visibility = View.VISIBLE
                    Glide.with(this@SelectOrderActivity)
                        .load(logoImage)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(logoIv)
                }

            }

        }
        with(viewModel) {
            onIsTakeout.observe(this@SelectOrderActivity) {
                binding.extra?.let { extra ->
                    start<MainActivity> {
                        putExtra("setting", extra)
                        putExtra("isTakeout", it)
                    }
                    if(extra.useStandbyScreen) finish()
                }

            }

            stopLockTask.observe(this@SelectOrderActivity) {
                resultLauncher.launch(Intent(this@SelectOrderActivity, AdminLoginActivity::class.java))
            }
        }

    }

    override fun onPause() {
        super.onPause()
        mCountDownTimer?.cancel()
    }

    override fun onBackPressed() {

    }
}