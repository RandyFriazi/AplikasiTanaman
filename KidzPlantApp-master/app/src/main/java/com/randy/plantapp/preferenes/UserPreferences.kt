package com.randy.plantapp.preferenes

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences @Inject constructor(@ApplicationContext val context: Context) {

    private val dataStore = context.dataStore

    fun getUserInfo(): Flow<Triple<Int,Boolean, Boolean>> {
        return dataStore.data.map { preferences ->
            Triple(
                preferences[USER_ID_KEY] ?: 0,
                preferences[LOGIN_KEY] ?: false,
                preferences[QUIZ_STATE] ?: false
            )
        }
    }

    suspend fun saveUserInfo(userId: Int, loginState: Boolean){
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[LOGIN_KEY] = loginState
        }
    }

    fun getQuizState(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[QUIZ_STATE] ?: false
        }
    }

    suspend fun saveQuizState(quizState: Boolean) {
        dataStore.edit { preferences ->
            preferences[QUIZ_STATE] = quizState
        }
    }

//    fun getOnBoardState(): Flow<Boolean> {
//        return dataStore.data.map { preferences ->
//            preferences[ON_BOARD_STATE] ?: false
//        }
//    }

    suspend fun saveOnBoardState(onBoardState: Boolean) {
        dataStore.edit { preferences ->
            preferences[ON_BOARD_STATE] = onBoardState
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = false
        }
    }

    companion object {

        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val LOGIN_KEY = booleanPreferencesKey("state")
        private val QUIZ_STATE = booleanPreferencesKey("quiz_state")
        private val ON_BOARD_STATE = booleanPreferencesKey("on_board_state")
    }

}