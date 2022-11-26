package com.helsinkiwizard.cointoss.tile

import androidx.wear.tiles.ActionBuilders
import androidx.wear.tiles.ModifiersBuilders
import com.helsinkiwizard.shared.Constants.EXTRA_START_FLIPPING
import com.helsinkiwizard.shared.Constants.PACKAGE_NAME
import com.helsinkiwizard.shared.BaseSplashActivity

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
        .setClassName(PACKAGE_NAME.plus(".").plus(BaseSplashActivity.TAG))
}