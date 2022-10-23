package com.helsinkiwizard.coinflip.tile

import androidx.wear.tiles.ActionBuilders
import androidx.wear.tiles.ModifiersBuilders
import com.helsinkiwizard.coinflip.Constants.EXTRA_JOURNEY
import com.helsinkiwizard.coinflip.Constants.EXTRA_JOURNEY_START_FLIPPING
import com.helsinkiwizard.coinflip.Constants.PACKAGE_NAME
import com.helsinkiwizard.coinflip.SplashActivity

/**
 * Creates a Clickable that can be used to launch an activity.
 */
internal fun launchActivityClickable(
    clickableId: String,
    androidActivity: ActionBuilders.AndroidActivity
) = ModifiersBuilders.Clickable.Builder()
    .setId(clickableId)
    .setOnClick(
        ActionBuilders.LaunchAction.Builder()
            .setAndroidActivity(androidActivity)
            .build()
    )
    .build()

internal fun openCoin() = ActionBuilders.AndroidActivity.Builder()
    .setMessagingActivity()
    .addKeyToExtraMapping(
        EXTRA_JOURNEY,
        ActionBuilders.stringExtra(EXTRA_JOURNEY_START_FLIPPING)
    )
    .build()

private fun ActionBuilders.AndroidActivity.Builder.setMessagingActivity(): ActionBuilders.AndroidActivity.Builder {
    return setPackageName(PACKAGE_NAME)
        .setClassName(PACKAGE_NAME.plus(".").plus(SplashActivity.TAG))
}