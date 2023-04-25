package kr.co.releasetech.kiosk.di

import kr.co.releasetech.kiosk.model.repository.*
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single { MainRepository() }
    single { CategoryRepository() }
    single { AppSettingRepository(androidApplication().applicationContext) }
    single { OrderRepository() }
    single { GoodsRepository() }
    single { AdminRepository(androidApplication().applicationContext) }
    single { PaymentRepository() }
    single { OptionRepository() }
    single { ManagerRepository() }
    single { ScreenSettingRepository() }
    single { CartRepository() }
}