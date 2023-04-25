package kr.co.releasetech.kiosk.view.activity.delete

import android.os.Bundle
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityDeleteBinding
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.model.realm.Goods
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeleteActivity : BaseActivity<ActivityDeleteBinding>(R.layout.activity_delete){
    val viewModel: DeleteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.onCancelClick.observe(this) {
            finish()
        }

        viewModel.onConfirmClick.observe(this) {
            if (it) finish() else showToast(R.string.db_error_message)
        }
        with(binding){
            val isCategory = intent.getBooleanExtra("isCategory", true)
            contentTv.text = String.format(getString(R.string.do_u_want_delete), intent.getStringExtra("title"))
            confirmBt.setOnClickListener {
                if(isCategory) viewModel.removeCategory(intent.getParcelableExtra<Category>("item"))
                else viewModel.removeGoods(intent.getParcelableExtra<Goods>("item"))
            }
        }


    }
}