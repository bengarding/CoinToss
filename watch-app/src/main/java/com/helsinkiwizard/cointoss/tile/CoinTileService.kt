package com.helsinkiwizard.cointoss.tile

import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders.Tile
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import com.google.android.horologist.tiles.images.drawableResToImageResource
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.Repository.Companion.TILE_RESOURCE_VERSION
import com.helsinkiwizard.core.coin.CoinType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalHorologistApi::class)
@AndroidEntryPoint
class CoinTileService : SuspendingTileService() {

    companion object {
        private const val SELECTED_COIN = "selected_coin"
    }

    @Inject
    lateinit var repo: Repository

    private lateinit var tileStateFlow: Flow<CoinType>
    private lateinit var resourceVersionFlow: Flow<Int>

    override fun onCreate() {
        super.onCreate()
        tileStateFlow = repo.getCoinType
            .map { CoinType.parse(it) }
        resourceVersionFlow = repo.getResourceVersion
    }

    override suspend fun resourcesRequest(
        requestParams: ResourcesRequest
    ): ResourceBuilders.Resources {
        val headsRes = latestTileState().heads
        val resourceVersion = getResourceVersion()
        return ResourceBuilders.Resources.Builder().setVersion(resourceVersion.toString())
            .addIdToImageMapping(
                SELECTED_COIN,
                drawableResToImageResource(headsRes)
            )
            .build()
    }

    override suspend fun tileRequest(requestParams: TileRequest): Tile {
        val dimens = requestParams.deviceConfiguration

        val singleTileTimeline = TimelineBuilders.Timeline.Builder()
            .addTimelineEntry(
                TimelineBuilders.TimelineEntry.Builder()
                    .setLayout(
                        LayoutElementBuilders.Layout.Builder()
                            .setRoot(tileLayout(dimens))
                            .build()
                    )
                    .build()
            )
            .build()

        // Bump the version number to refresh the coin image
        var resourceVersion = getResourceVersion()
        resourceVersion++
        repo.saveIntPreference(TILE_RESOURCE_VERSION, resourceVersion)

        return Tile.Builder()
            .setResourcesVersion(resourceVersion.toString())
            .setTileTimeline(singleTileTimeline)
            .build()
    }

    private fun tileLayout(deviceParams: DeviceParametersBuilders.DeviceParameters): LayoutElementBuilders.LayoutElement {
        val clickable = launchActivityClickable("coin_button", openCoin())
        return LayoutElementBuilders.Box.Builder()
            .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .addContent(
                LayoutElementBuilders.Image.Builder()
                    .apply {
                        setResourceId(SELECTED_COIN)
                        setModifiers(
                            ModifiersBuilders.Modifiers.Builder().setClickable(clickable).build()
                        )
                        setHeight(dp(deviceParams.screenHeightDp.toFloat()))
                        setWidth(dp(deviceParams.screenWidthDp.toFloat()))
                    }.build()
            )
            .build()
    }

    private suspend fun latestTileState() = tileStateFlow.filterNotNull().first()
    private suspend fun getResourceVersion() = resourceVersionFlow.filterNotNull().first()
}
