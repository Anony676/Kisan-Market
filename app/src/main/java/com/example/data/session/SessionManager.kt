package com.example.data.session

import android.content.Context
import android.content.SharedPreferences
import com.example.data.sample.SampleData
import com.example.ui.viewmodel.NavigationTab
import com.example.ui.viewmodel.SortOption

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    var selectedRegionId: String
        get() = prefs.getString(KEY_SELECTED_REGION_ID, SampleData.MANDI_REGIONS.first().id)
            ?: SampleData.MANDI_REGIONS.first().id
        set(value) = prefs.edit().putString(KEY_SELECTED_REGION_ID, value).apply()

    var selectedTab: NavigationTab
        get() {
            val name = prefs.getString(KEY_SELECTED_TAB, NavigationTab.MARKETPLACE.name)
            return try {
                NavigationTab.valueOf(name ?: NavigationTab.MARKETPLACE.name)
            } catch (_: Exception) {
                NavigationTab.MARKETPLACE
            }
        }
        set(value) = prefs.edit().putString(KEY_SELECTED_TAB, value.name).apply()

    var selectedCategory: String
        get() = prefs.getString(KEY_SELECTED_CATEGORY, "All") ?: "All"
        set(value) = prefs.edit().putString(KEY_SELECTED_CATEGORY, value).apply()

    var selectedSort: SortOption
        get() {
            val name = prefs.getString(KEY_SELECTED_SORT, SortOption.RECENT.name)
            return try {
                SortOption.valueOf(name ?: SortOption.RECENT.name)
            } catch (_: Exception) {
                SortOption.RECENT
            }
        }
        set(value) = prefs.edit().putString(KEY_SELECTED_SORT, value.name).apply()

    var isOrganicOnly: Boolean
        get() = prefs.getBoolean(KEY_ORGANIC_ONLY, false)
        set(value) = prefs.edit().putBoolean(KEY_ORGANIC_ONLY, value).apply()

    var isVerifiedOnly: Boolean
        get() = prefs.getBoolean(KEY_VERIFIED_ONLY, false)
        set(value) = prefs.edit().putBoolean(KEY_VERIFIED_ONLY, value).apply()

    var buyerName: String
        get() {
            val saved = prefs.getString(KEY_BUYER_NAME, "Your Name")
            return if (saved == null || saved == "Sunil Deshmukh") "Your Name" else saved
        }
        set(value) = prefs.edit().putString(KEY_BUYER_NAME, value).apply()

    var buyerPhone: String
        get() {
            val saved = prefs.getString(KEY_BUYER_PHONE, "+91 00000 00000")
            return if (saved == null || saved.contains("98234")) "+91 00000 00000" else saved
        }
        set(value) = prefs.edit().putString(KEY_BUYER_PHONE, value).apply()

    var deliveryAddress: String
        get() {
            val saved = prefs.getString(KEY_DELIVERY_ADDRESS, "Your Location")
            return if (saved == null || saved.contains("APMC Market Gate 2")) "Your Location" else saved
        }
        set(value) = prefs.edit().putString(KEY_DELIVERY_ADDRESS, value).apply()

    var selectedFarmerId: String?
        get() = prefs.getString(KEY_SELECTED_FARMER_ID, null)
        set(value) = prefs.edit().putString(KEY_SELECTED_FARMER_ID, value).apply()

    var isNotificationsInitialized: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_INITIALIZED, false)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS_INITIALIZED, value).apply()

    var isNotificationsCleared: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_CLEARED, false)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS_CLEARED, value).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    var userEmail: String
        get() = prefs.getString(KEY_USER_EMAIL, "") ?: ""
        set(value) = prefs.edit().putString(KEY_USER_EMAIL, value).apply()

    var isWelcomeCompleted: Boolean
        get() = prefs.getBoolean(KEY_WELCOME_COMPLETED, false)
        set(value) = prefs.edit().putBoolean(KEY_WELCOME_COMPLETED, value).apply()

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "kisan_market_session"
        private const val KEY_SELECTED_REGION_ID = "key_selected_region_id"
        private const val KEY_SELECTED_TAB = "key_selected_tab"
        private const val KEY_SELECTED_CATEGORY = "key_selected_category"
        private const val KEY_SELECTED_SORT = "key_selected_sort"
        private const val KEY_ORGANIC_ONLY = "key_organic_only"
        private const val KEY_VERIFIED_ONLY = "key_verified_only"
        private const val KEY_BUYER_NAME = "key_buyer_name"
        private const val KEY_BUYER_PHONE = "key_buyer_phone"
        private const val KEY_DELIVERY_ADDRESS = "key_delivery_address"
        private const val KEY_SELECTED_FARMER_ID = "key_selected_farmer_id"
        private const val KEY_NOTIFICATIONS_INITIALIZED = "key_notifications_initialized"
        private const val KEY_NOTIFICATIONS_CLEARED = "key_notifications_cleared"
        private const val KEY_WELCOME_COMPLETED = "key_welcome_completed"
        private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
        private const val KEY_USER_EMAIL = "key_user_email"
    }
}
