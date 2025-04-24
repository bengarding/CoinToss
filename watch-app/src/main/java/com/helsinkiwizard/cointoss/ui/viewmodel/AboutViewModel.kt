package com.helsinkiwizard.cointoss.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.helsinkiwizard.cointoss.di.WearClients
import com.helsinkiwizard.cointoss.utils.isAppInstalledOnPhone
import com.helsinkiwizard.cointoss.utils.isConnectedToAnyNode
import com.helsinkiwizard.cointoss.utils.launchDeepLinkOnPhone
import com.helsinkiwizard.core.CoreConstants.PLAY_STORE_DEEPLINK
import com.helsinkiwizard.core.viewmodel.AbstractViewModel
import com.helsinkiwizard.core.viewmodel.BaseDialogType
import com.helsinkiwizard.core.viewmodel.BaseType
import com.helsinkiwizard.core.viewmodel.DialogState
import com.helsinkiwizard.core.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val wearClients: WearClients
) : AbstractViewModel() {

    init {
        viewModelScope.launch {
            val appInstalledOnPhone = isAppInstalledOnPhone(wearClients.capabilityClient)
            mutableUiStateFlow.value = UiState.ShowContent(
                AboutContent.LoadingComplete(showMobileAppButton = appInstalledOnPhone.not())
            )
        }
    }

    fun onDownloadButtonClicked() {
        viewModelScope.safeLaunch {
            val connectedToAnyNode = isConnectedToAnyNode(wearClients.nodeClient)

            if (connectedToAnyNode.not()) {
                mutableUiStateFlow.value = UiState.ShowContent(AboutContent.LoadingComplete(showMobileAppButton = true))
                mutableDialogStateFlow.value = DialogState.ShowContent(AboutDialogs.DownloadMobileApp)
                return@safeLaunch
            }

            val deepLinkLaunched = launchDeepLinkOnPhone(
                remoteActivityHelper = wearClients.remoteActivityHelper,
                deepLink = PLAY_STORE_DEEPLINK
            )

            mutableUiStateFlow.value = UiState.ShowContent(AboutContent.LoadingComplete(showMobileAppButton = true))
            mutableDialogStateFlow.value = if (deepLinkLaunched) {
                DialogState.ShowContent(AboutDialogs.OpenOnPhone)
            } else {
                DialogState.ShowContent(AboutDialogs.DownloadMobileApp)
            }
        }
    }
}

sealed interface AboutContent : BaseType {
    data class LoadingComplete(val showMobileAppButton: Boolean) : AboutContent
}

internal sealed interface AboutDialogs : BaseDialogType {
    data object OpenOnPhone : AboutDialogs
    data object DownloadMobileApp : AboutDialogs
}
