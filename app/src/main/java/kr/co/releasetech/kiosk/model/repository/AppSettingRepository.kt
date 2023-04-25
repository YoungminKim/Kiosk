package kr.co.releasetech.kiosk.model.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import java.io.IOException

class AppSettingRepository(val context: Context): KoinComponent {

    companion object{
        private const val  APP_SETTING_DATA_STORE_NAME = "app_setting"
        val APP_FIRST_ACTIVE = booleanPreferencesKey("app_first_active")
    }


    data class AppSettingPreferences(
        val isFirstActive: Boolean
    )
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_SETTING_DATA_STORE_NAME)

    val appSettingPreferencesFlow: Flow<AppSettingPreferences> = context.dataStore.data
        .catch{ e ->
            if(e is IOException){
                emit(emptyPreferences())
            }else {
                throw e
            }
        }
        .map { preferences ->
            val isFirstActive = preferences[APP_FIRST_ACTIVE] ?: true
            AppSettingPreferences(isFirstActive)
        }


    suspend fun disableFirstActive(){
        context.dataStore.edit {
            it[APP_FIRST_ACTIVE] = false
        }
    }

}