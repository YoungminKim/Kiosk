package kr.co.releasetech.kiosk.view.fragment.option

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.FragmentOptionBinding
import kr.co.releasetech.kiosk.view.activity.addOption.AddOptionActivity
import kr.co.releasetech.kiosk.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.fragments.start

class OptionFragment: BaseFragment<FragmentOptionBinding>(R.layout.fragment_option) {

    val viewModel: OptionViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        val adapter = OptionListAdapter(requireActivity(), viewModel)
        with(binding){
            rv.adapter = adapter
        }
        with(viewModel){

            onAddOption.observe(viewLifecycleOwner){
                start<AddOptionActivity> {
                    putExtra("optionCategory", it)
                    putExtra("isUpdate", it != null)
                }
            }

            optionCategoryList.observe(viewLifecycleOwner){
                adapter.addList(it)
            }


            removeItem.observe(viewLifecycleOwner){
                adapter.removeItem(it)
            }


        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getList()
    }
}