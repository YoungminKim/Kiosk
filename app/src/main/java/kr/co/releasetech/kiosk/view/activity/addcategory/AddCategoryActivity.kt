package kr.co.releasetech.kiosk.view.activity.addcategory

import android.os.Bundle
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityAddCategoryBinding
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddCategoryActivity: BaseActivity<ActivityAddCategoryBinding>(R.layout.activity_add_category) {
    companion object{
        private const val TAG = "AddCategoryActivity"
    }
    val viewModel: AddCategoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        val category = intent?.getParcelableExtra<Category>("item")
        val index = intent?.getIntExtra("index", 0)!!
        category?.let{
            viewModel.nameField.set(it.name)
        }

        binding.confirmBt.setOnClickListener {
            viewModel.addCategory(category, index)
        }

        viewModel.onAddCategory.observe(this) {
            if (it > 0) {
                showToast(it)
            } else {
                finish()
            }
        }
    }
}