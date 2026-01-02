package id.antasari.minda.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userPrefsDataStore by preferencesDataStore(name = "user_prefs")

class UserPrefsRepository(private val context: Context) {
    private object Keys {
        val USER_NAME = stringPreferencesKey("user_name")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    val userNameFlow: Flow<String?> = context.userPrefsDataStore.data.map { prefs ->
        prefs[Keys.USER_NAME]
    }

    val onboardingCompletedFlow: Flow<Boolean> = context.userPrefsDataStore.data.map { prefs ->
        prefs[Keys.ONBOARDING_COMPLETED] ?: false
    }

    val isDarkModeFlow: Flow<Boolean?> = context.userPrefsDataStore.data.map { prefs ->
        prefs[Keys.IS_DARK_MODE]
    }

    suspend fun saveUserName(name: String) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[Keys.USER_NAME] = name
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[Keys.ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setDarkMode(enable: Boolean) {
        context.userPrefsDataStore.edit { prefs ->
            prefs[Keys.IS_DARK_MODE] = enable
        }
    }
}