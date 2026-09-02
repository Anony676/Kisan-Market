package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import com.example.data.local.KisanMarketDatabase
import com.example.data.repository.KisanMarketRepository
import com.example.data.session.SessionManager
import com.example.ui.KisanMarketApp
import com.example.ui.theme.KisanMarketTheme
import com.example.ui.viewmodel.KisanMarketViewModel
import com.example.ui.viewmodel.KisanMarketViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: KisanMarketViewModel by viewModels {
        val app = application as? KisanMarketApplication
        val repo = app?.repository ?: run {
            val database = KisanMarketDatabase.getDatabase(applicationContext)
            val sessionManager = SessionManager(applicationContext)
            KisanMarketRepository(
                cropListingDao = database.cropListingDao(),
                marketPriceDao = database.marketPriceDao(),
                orderDao = database.orderDao(),
                farmerProfileDao = database.farmerProfileDao(),
                notificationDao = database.notificationDao(),
                userProfileDao = database.userProfileDao(),
                sessionManager = sessionManager
            )
        }
        KisanMarketViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KisanMarketTheme {
                KisanMarketApp(
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                )
            }
        }
    }
}
