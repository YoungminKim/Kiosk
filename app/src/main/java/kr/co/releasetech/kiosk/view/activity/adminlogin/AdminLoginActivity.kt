package kr.co.releasetech.kiosk.view.activity.adminlogin

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityAdminLoginBinding
import kr.co.releasetech.kiosk.view.activity.manager.ManagerActivity
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.activities.start


class AdminLoginActivity: BaseActivity<ActivityAdminLoginBinding>(R.layout.activity_admin_login) {
    val viewModel: AdminLoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.et, 0)

        with(viewModel){
            onLogin.observe(this@AdminLoginActivity) {
                setResult(RESULT_OK)
                finish()
            }

            adminPassModel.observe(this@AdminLoginActivity) {
                setPass(it.masterPass, it.staffPass)
            }

            wrongPass.observe(this@AdminLoginActivity){
                showToast(R.string.input_wrong_pass)
            }

            emptyPass.observe(this@AdminLoginActivity){
                showToast(R.string.plz_input_pass)
            }
        }
    }
}