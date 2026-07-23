package com.helsinkiwizard.cointoss.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.ui.theme.LocalNavController
import com.helsinkiwizard.cointoss.ui.viewmodel.RemoveAdsContent
import com.helsinkiwizard.cointoss.ui.viewmodel.RemoveAdsViewModel
import com.helsinkiwizard.core.viewmodel.UiState
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.models.StoreTransaction
import com.revenuecat.purchases.ui.revenuecatui.PaywallDialog
import com.revenuecat.purchases.ui.revenuecatui.PaywallDialogOptions
import com.revenuecat.purchases.ui.revenuecatui.PaywallListener

@Composable
fun RemoveAdsScreen(
    viewModel: RemoveAdsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val navController = LocalNavController.current

    when (val state = viewModel.uiState.collectAsState().value) {
        is UiState.ShowContent -> {
            when (val type = state.type as RemoveAdsContent) {
                is RemoveAdsContent.ShowDialog -> {
                    RemoveAdsDialog(
                        onPurchaseCompleted = { isRestored -> viewModel.onPurchaseCompleted(isRestored) }
                    )
                }

                is RemoveAdsContent.PurchaseComplete -> {
                    Toast.makeText(context, R.string.purchase_success, Toast.LENGTH_LONG).show()
                    if (type.isRestored) {
                        navController.popBackStack()
                    }
                }
            }
        }

        else -> {}
    }
}

@Composable
fun RemoveAdsDialog(
    onPurchaseCompleted: (isRestored: Boolean) -> Unit
) {
    val navController = LocalNavController.current
    PaywallDialog(
        PaywallDialogOptions.Builder()
            .setDismissRequest {
                navController.popBackStack()
            }
            .setListener(
                object : PaywallListener {
                    override fun onPurchaseCompleted(customerInfo: CustomerInfo, storeTransaction: StoreTransaction) {
                        super.onPurchaseCompleted(customerInfo, storeTransaction)
                        onPurchaseCompleted(false)
                    }

                    override fun onRestoreCompleted(customerInfo: CustomerInfo) {
                        super.onRestoreCompleted(customerInfo)
                        onPurchaseCompleted(true)
                    }
                }
            )
            .build()
    )
}
