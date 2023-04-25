package kr.co.releasetech.kiosk

import android.Manifest
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import io.realm.DynamicRealm
import io.realm.RealmChangeListener
import io.realm.RealmMigration
import io.realm.RealmModel
import io.realm.RealmResults

object AppConst {
    const val IS_TEST = true

    const val CLOSE_ORDER_SCREEN_INTERVAL: Long = 1000




    const val SCHEMA_VERSION :  Long = 1

    class MyRealmMigration : RealmMigration {
        override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
            realm.schema.let {
            }
        }
    }


    const val REQUEST_PERMISSION = 1001
    val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
        , Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    const val DATA_TEXT_FILE_NAME = "ReleasetechKioskData.txt"
    const val ZIP_FILE_NAME = "ReleasetechKioskSetting.zip"

    val VIDEO_EXTENSIONS = arrayOf("mp4", "mpeg", "mpg", "mpe", "qt", "mov", "avi", "movie")
    val IMG_EXTENSIONS = arrayOf("jpeg", "jpg", "jpe", "png", "gif")
/*
    class LiveRealmData<T: RealmModel>(private val realmResult: RealmResults<T>): LiveData<RealmResults<T>>(){
        private val listener = RealmChangeListener<RealmResults<T>>{ value = it }
        override fun onActive() = realmResult.addChangeListener(listener)
        override fun onInactive() = realmResult.removeChangeListener(listener)

    }

    @JvmName("DB_Helper")
    fun <T: RealmModel> RealmResults<T>.asLiveData() = LiveRealmData<T>(this)*/

}