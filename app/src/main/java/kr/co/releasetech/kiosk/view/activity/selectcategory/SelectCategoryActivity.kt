package kr.co.releasetech.kiosk.view.activity.selectcategory

import android.content.Intent
import android.os.Bundle
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivitySelectCategoryBinding
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.activity.addgoods.AddGoodsActivity
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.activities.start


class SelectCategoryActivity: BaseActivity<ActivitySelectCategoryBinding>(R.layout.activity_select_category) {
    companion object{
        private const val TAG = "SelectCategoryActivity"
    }
    val viewModel: SelectCategoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        var adapter = SelectCategoryAdapter(this, viewModel)

        binding.rv.adapter = adapter

        viewModel.run {
            getCategoryList()
            categoryList.observe(this@SelectCategoryActivity) {
                DebugUtils.setLog(TAG, "list : ${it.size}")
                adapter.addList(list = it)
            }

            onClose.observe(this@SelectCategoryActivity) {
                finish()
            }

            onSelectItem.observe(this@SelectCategoryActivity) { category ->

                if (intent.getBooleanExtra("isUpdate", false)) {
                    val intent = Intent()
                    intent.putExtra("category", category)
                    setResult(RESULT_OK, intent)
                } else {
                    start<AddGoodsActivity> {
                        putExtra("category", category)
                    }
                }

                finish()
            }


        }

    }
}