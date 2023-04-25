package kr.co.releasetech.kiosk.di

import kr.co.releasetech.kiosk.view.fragment.category.CategoryViewModel
import kr.co.releasetech.kiosk.view.fragment.goods.GoodsViewModel
import kr.co.releasetech.kiosk.view.fragment.option.OptionViewModel
import kr.co.releasetech.kiosk.view.activity.payment.PaymentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val fragmentModule  = module{
    viewModel { GoodsViewModel(get(), get()) }
    viewModel { CategoryViewModel(get()) }

    viewModel { PaymentViewModel(get()) }
    viewModel { OptionViewModel(get(), get()) }
}