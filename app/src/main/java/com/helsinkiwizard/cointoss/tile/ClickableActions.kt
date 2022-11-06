package com.helsinkiwizard.cointoss.tile

import androidx.wear.tiles.ActionBuilders
import androidx.wear.tiles.ModifiersBuilders
import com.helsinkiwizard.cointoss.Constants.EXTRA_START_FLIPPING
import com.helsinkiwizard.cointoss.Constants.PACKAGE_NAME
import com.helsinkiwizard.cointoss.SplashActivity

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
        EXTRA_START_FLIPPING,
        ActionBuilders.booleanExtra(true)
    )
    .build()

private fun ActionBuilders.AndroidActivity.Builder.setMessagingActivity(): ActionBuilders.AndroidActivity.Builder {
    return setPackageName(PACKAGE_NAME)
        .setClassName(PACKAGE_NAME.plus(".").plus(SplashActivity.TAG))
}