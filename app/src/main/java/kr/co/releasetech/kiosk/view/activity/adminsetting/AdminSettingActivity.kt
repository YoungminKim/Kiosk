package kr.co.releasetech.kiosk.view.activity.adminsetting

import android.os.Bundle
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityAdminSettingBinding
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdminSettingActivity: BaseActivity<ActivityAdminSettingBinding>(R.layout.activity_admin_setting) {
    companion object{
        const val TAG = "AdminSettingActivity"
    }
    val viewModel: AdminSettingViewModel by  viewModel()

    private var isInit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        binding.lifecycleOwner = this

        with(viewModel){
            adminPassModel.observe(this@AdminSettingActivity){
                DebugUtils.setLog(TAG, "adminPassModel called!!!")
                if(isInit){
                    setPass(it.masterPass, it.staffPass)
                    isInit = false
                }else{
                    showToast(R.string.update_pass_success)
                    finish()

                }


            }

            samePass.observe(this@AdminSettingActivity){
                showToast(R.string.same_pass)
            }

            emptyPass.observe(this@AdminSettingActivity){
                showToast(R.string.plz_input_pass)
            }

            wrongPass.observe(this@AdminSettingActivity){
                showToast(R.string.input_wrong_pass)
            }
        }
    }
}