package kr.co.releasetech.kiosk.view.fragment.goods

import android.os.Bundle
import android.view.View
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.FragmentGoodsBinding
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.activity.addgoods.AddGoodsActivity
import kr.co.releasetech.kiosk.view.activity.delete.DeleteActivity
import kr.co.releasetech.kiosk.view.activity.selectcategory.SelectCategoryActivity
import kr.co.releasetech.kiosk.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.fragments.start


class GoodsFragment: BaseFragment<FragmentGoodsBinding>(R.layout.fragment_goods) {
    companion object{
        const val TAG = "GoodsFragment"
    }
    val viewModel: GoodsViewModel by viewModel()
    val adapter: GoodsAdapter by lazy { GoodsAdapter(requireContext(), viewModel) }

    private var isNotEmptyCategory = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.rv.adapter = adapter
        viewModel.run {

            onCategoryList.observe(viewLifecycleOwner){
                isNotEmptyCategory = it.size > 0
                adapter.setCategoryList(it)
                getGoodsList()
            }

            onAddClick.observe(viewLifecycleOwner) {
                if (isNotEmptyCategory) start<SelectCategoryActivity> { } else showToast(R.string.empty_category_list)
            }

            onDeleteClick.observe(viewLifecycleOwner) {
                start<DeleteActivity> {
                    putExtra("isCategory", false)
                    putExtra("title", it.name)
                    putExtra("item", it)
                }
            }

            onModifyClick.observe(viewLifecycleOwner) {
                it?.let { goods ->
                    val category = getCategory(goods.categoryId)
                    start<AddGoodsActivity> {
                        putExtra("category", category)
                        putExtra("goods", goods)
                    }
                }

            }

            goodsList.observe(viewLifecycleOwner) {
                DebugUtils.setLog(TAG, "list : ${it.size}")
                adapter.addList(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCategoryList()

    }

}