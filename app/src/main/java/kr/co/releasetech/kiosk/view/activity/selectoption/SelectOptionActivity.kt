package kr.co.releasetech.kiosk.view.activity.selectoption

import android.content.Intent
import android.os.Bundle
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivitySelectOptionBinding
import kr.co.releasetech.kiosk.model.realm.OptionCategory
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectOptionActivity: BaseActivity<ActivitySelectOptionBinding>(R.layout.activity_select_option) {
    val viewModel: SelectOptionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        val adapter = SelectOptionAdapter(this, viewModel)

        val optionCategorys = intent?.getParcelableArrayListExtra<OptionCategory>("optionCategorys")

        with(binding){
            rv.adapter = adapter
        }
        with(viewModel){
            getList()

            onSelectItem.observe(this@SelectOptionActivity){
                val intent = Intent()
                intent.putExtra("option", it)
                setResult(RESULT_OK, intent)
                finish()
            }

            optionCategoryList.observe(this@SelectOptionActivity){

                val list = ArrayList<OptionCategory>()
                list.addAll(it)

                var i = 0

                while (i < list.size){
                    val item = list[i]
                    optionCategorys?.map { optionCategorys ->
                        if(item.id == optionCategorys.id) {
                            list.remove(item)
                            --i
                        }
                    }
                    ++i
                }

                adapter.addList(list)
            }

            onClose.observe(this@SelectOptionActivity){
                finish()
            }
        }
    }

}