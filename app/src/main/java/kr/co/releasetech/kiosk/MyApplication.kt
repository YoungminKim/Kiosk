package kr.co.releasetech.kiosk

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import kr.co.releasetech.kiosk.AppConst.SCHEMA_VERSION
import kr.co.releasetech.kiosk.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.io.File

class MyApplication: Application() {

    companion object{
        private const val VIMEO_CLIENT_ID = "1bd1c8966f0f8739c8b727d612d01b55f14ab865"
        private const val VIMEO_CLIENT_SECRETS = "w7Eo+twf5NwbnIbpbt0O/uFjWsmEoqg1nC6/zgrDNNR81SFvXZzx5XI+5hE5b93gH/nxcthzColNvegMhOUP+l+cCEwu+/EM52nx+Kt+A9vwph6cv0CpD4qOTsdEhu0a"
        private const val VIMEO_SCOPE = "private public create edit delete interact"
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                appModule,
                activityModule,
                fragmentModule,
                networkModule,
                apiModule
            )
        }

        Realm.init(this)

        Realm.setDefaultConfiguration(getDefaultRealmConfig())
        RealmConfiguration.Builder().allowQueriesOnUiThread(true)


    }

    private fun getDefaultRealmConfig(): RealmConfiguration = RealmConfiguration.Builder()
        .schemaVersion(SCHEMA_VERSION)
        //.directory(File(getExternalFilesDir(null)?.absolutePath, getString(R.string.app_name)))
        //.deleteRealmIfMigrationNeeded()
        .migration(
        AppConst.MyRealmMigration()

        ).build()
}