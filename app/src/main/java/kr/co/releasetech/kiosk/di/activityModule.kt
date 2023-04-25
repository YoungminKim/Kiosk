package kr.co.releasetech.kiosk.di


import kr.co.releasetech.kiosk.view.activity.addOption.AddOptionViewModel
import kr.co.releasetech.kiosk.view.activity.addcategory.AddCategoryViewModel
import kr.co.releasetech.kiosk.view.activity.addgoods.AddGoodsViewModel
import kr.co.releasetech.kiosk.view.activity.menuSetting.MenuSettingViewModel
import kr.co.releasetech.kiosk.view.activity.adminlogin.AdminLoginViewModel
import kr.co.releasetech.kiosk.view.activity.adminsetting.AdminSettingViewModel
import kr.co.releasetech.kiosk.view.activity.colorpicker.ColorPickerViewModel
import kr.co.releasetech.kiosk.view.activity.delete.DeleteViewModel
import kr.co.releasetech.kiosk.view.activity.editscreen.EditScreenViewModel
import kr.co.releasetech.kiosk.view.activity.goodsdetail.GoodsDetailViewModel
import kr.co.releasetech.kiosk.view.activity.main.MainViewModel
import kr.co.releasetech.kiosk.view.activity.manager.ManagerViewModel
import kr.co.releasetech.kiosk.view.activity.option.OptionViewModel
import kr.co.releasetech.kiosk.view.activity.order.OrderViewModel
import kr.co.releasetech.kiosk.view.activity.orderdetail.OrderDetailViewModel
import kr.co.releasetech.kiosk.view.activity.paymentdetail.PaymentDetailViewModel
import kr.co.releasetech.kiosk.view.activity.selectcategory.SelectCategoryViewModel
import kr.co.releasetech.kiosk.view.activity.selectoption.SelectOptionViewModel
import kr.co.releasetech.kiosk.view.activity.selectorder.SelectOrderViewModel
import kr.co.releasetech.kiosk.view.activity.selectprint.SelectPrintViewModel
import kr.co.releasetech.kiosk.view.activity.splash.SplashViewModel
import kr.co.releasetech.kiosk.view.activity.standby.StandbyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val activityModule  = module{
    viewModel { SplashViewModel(get(), get()) }
    viewModel { StandbyViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { AdminLoginViewModel(get()) }
    viewModel { MenuSettingViewModel() }
    viewModel { AddCategoryViewModel(get()) }
    viewModel { SelectCategoryViewModel(get()) }
    viewModel { AddGoodsViewModel(get(), get()) }
    viewModel { DeleteViewModel(get(), get()) }
    viewModel { ManagerViewModel(get()) }
    viewModel { OrderViewModel(get()) }
    viewModel { AddOptionViewModel(get()) }
    viewModel { AdminSettingViewModel(get()) }
    viewModel { SelectOptionViewModel(get()) }
    viewModel { GoodsDetailViewModel(get()) }
    viewModel { EditScreenViewModel(get()) }
    viewModel { SelectOrderViewModel() }
    viewModel { PaymentDetailViewModel(get(), get()) }
    viewModel { OrderDetailViewModel(get()) }
    viewModel { ColorPickerViewModel() }
    viewModel { OptionViewModel(get()) }
    viewModel { SelectPrintViewModel() }
}