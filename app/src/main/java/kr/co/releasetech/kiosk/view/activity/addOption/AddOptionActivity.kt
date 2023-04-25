package kr.co.releasetech.kiosk.view.activity.addOption

import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityAddOptionBinding
import kr.co.releasetech.kiosk.model.OptionDummy
import kr.co.releasetech.kiosk.model.realm.OptionCategory
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.ItemTouchHelperCallback
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.views.onClick

class AddOptionActivity: BaseActivity<ActivityAddOptionBinding>(R.layout.activity_add_option) {
    companion object{
        private const val TAG = "AddOptionActivity"
    }

    val viewModel: AddOptionViewModel by viewModel()
    var isUpdate = false
    val optionCategory: OptionCategory? by lazy { intent?.getParcelableExtra<OptionCategory>("optionCategory") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        binding.lifecycleOwner = this




        intent?.getBooleanExtra("isUpdate", false)?.let {
            isUpdate = it
        }


        val adapter = AddOptionAdapter(this, viewModel)

        with(binding){
            rv.adapter = adapter
            if(!isUpdate) adapter.addItem()


            val helper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
            helper.attachToRecyclerView(rv)

            singleBt.isSelected = true
        }


        with(viewModel){
            onSaved.observe(this@AddOptionActivity){
                if(isUpdate){
                    optionCategory?.let {
                        modify(it.id, binding.singleBt.isSelected, adapter.mList, adapter.mRemoveList)
                    }

                }else addOption(binding.singleBt.isSelected, adapter.mList)
            }

            onSaveSuccess.observe(this@AddOptionActivity){
                finish()
            }

            onDbError.observe(this@AddOptionActivity){
                showToast(R.string.db_error_message)
            }

            categoryNameEmpty.observe(this@AddOptionActivity){
                showToast(R.string.plz_input_category_name)
            }

            isSingleChoice.observe(this@AddOptionActivity){
                binding.singleBt.isSelected = it
                binding.multipleBt.isSelected = !it
            }

            optionList.observe(this@AddOptionActivity){
                val list = ArrayList<OptionDummy>()
                it.map { option ->
                    list.add(OptionDummy(option.id, option.index, option.optionCategoryId, option.name, option.price))
                }
                adapter.addList(list)
            }
        }

        optionCategory?.let {
            viewModel.categoryField.set(it.name)
            binding.singleBt.isSelected = it.isSingle
            binding.multipleBt.isSelected = !it.isSingle
            viewModel.getList(it.id)
        }
    }
}