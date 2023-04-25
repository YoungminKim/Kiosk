package kr.co.releasetech.kiosk.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import splitties.toast.toast


abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes private val layoutResId: Int) :Fragment() {

    lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }


    fun showToast(message: Int){
        toast(message)
    }

    fun showToast(message: String){
        toast(message)
    }

}