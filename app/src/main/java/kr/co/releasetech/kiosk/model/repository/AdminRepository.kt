package kr.co.releasetech.kiosk.model.repository

import android.content.Context
import androidx.datastore.core.DataStore
import org.koin.core.KoinComponent
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class AdminRepository(val context: Context): KoinComponent {
    companion object{
        private const val ADMIN_PASS_DATA_STORE_NAME = "adminPass"
        val MASTER_PASS = stringPreferencesKey("masterPass")
        val STAFF_PASS = stringPreferencesKey("staffPass")
    }


    data class AdminPreferences(
            val masterPass: String,
            val staffPass: String
    )

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = ADMIN_PASS_DATA_STORE_NAME)

    val adminPreferencesFlow: Flow<AdminPreferences> = context.dataStore.data
            .catch{ e ->
                if(e is IOException){
                    emit(emptyPreferences())
                }else {
                    throw e
                }
            }
            .map { preferences ->
                val masterPass = preferences[MASTER_PASS] ?: "1111"
                val staffPass = preferences[STAFF_PASS] ?: "1111"
                AdminPreferences(masterPass, staffPass)
            }

    suspend fun updateMasterPass(value: String){
        context.dataStore.edit{
            it[MASTER_PASS] = value
        }
    }


    suspend fun updateStaffPass(value: String){
        context.dataStore.edit{
            it[STAFF_PASS] = value
        }
    }
}