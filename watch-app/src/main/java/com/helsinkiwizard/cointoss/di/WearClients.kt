package com.helsinkiwizard.cointoss.di

import android.content.Context
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WearClients @Inject constructor(
    @ApplicationContext context: Context
) {
    val capabilityClient: CapabilityClient = Wearable.getCapabilityClient(context)
    val nodeClient: NodeClient = Wearable.getNodeClient(context)
    val remoteActivityHelper: RemoteActivityHelper = RemoteActivityHelper(context)
}