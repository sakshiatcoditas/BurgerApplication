package com.example.burgerapp.ui.presentation.payment_screen

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// DataStore property delegate
private val Context.dataStore by preferencesDataStore("payment_prefs")

@Singleton
class PaymentPreferencesHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        val SELECTED_CARD_KEY = intPreferencesKey("selected_card_index")
    }

    // Save selected card index
    suspend fun saveSelectedCard(index: Int) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_CARD_KEY] = index
        }
    }

    // Flow to read selected card index; default 0
    val selectedCard: Flow<Int> = context.dataStore.data
        .map { prefs ->
            prefs[SELECTED_CARD_KEY] ?: 0
        }
}