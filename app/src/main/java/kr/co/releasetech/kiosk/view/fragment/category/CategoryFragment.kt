package kr.co.releasetech.kiosk.view.fragment.category

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.FragmentCategoryBinding
import kr.co.releasetech.kiosk.model.realm.Category
import kr.co.releasetech.kiosk.utils.DebugUtils
import kr.co.releasetech.kiosk.view.ItemTouchHelperCallback
import kr.co.releasetech.kiosk.view.activity.addcategory.AddCategoryActivity
import kr.co.releasetech.kiosk.view.activity.delete.DeleteActivity
import kr.co.releasetech.kiosk.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.fragments.start


class CategoryFragment : BaseFragment<FragmentCategoryBinding>(R.layout.fragment_category) {
    companion object {
        const val TAG = "CategoryFragment"
    }

    val viewModel: CategoryViewModel by viewModel()


    lateinit var adapter: CategoryListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            vm = viewModel
            adapter = CategoryListAdapter(requireContext(), viewModel)

            rv.adapter = adapter
            val helper = ItemTouchHelper(ItemTouchHelperCallback(adapter, false))
            helper.attachToRecyclerView(rv)
        }


        with(viewModel) {

            onAddClick.observe(viewLifecycleOwner) {
                DebugUtils.setLog(TAG, "onAddClick")
                start<AddCategoryActivity> {
                    putExtra("item", it)
                    putExtra("index", adapter.itemCount)
                }
            }



            onRemoveItemClick.observe(viewLifecycleOwner) { item ->
                item?.let {
                    start<DeleteActivity> {
                        putExtra("isCategory", true)
                        putExtra("title", it.name)
                        putExtra("item", it)
                    }
                }

            }

            categoryList.observe(viewLifecycleOwner) { list ->
                DebugUtils.setLog(TAG, "list : ${list.size}")
                adapter.addList(list)
            }

            onMoveItem.observe(viewLifecycleOwner) {
                updateIndex(adapter.mList)


            }

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCategoryList()
        Handler(Looper.getMainLooper()).post {
            //viewModel.getCategoryList()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}