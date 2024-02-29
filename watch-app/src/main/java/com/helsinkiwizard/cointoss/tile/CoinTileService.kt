package com.helsinkiwizard.cointoss.tile

import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.tiles.DeviceParametersBuilders.DeviceParameters
import androidx.wear.tiles.DimensionBuilders
import androidx.wear.tiles.DimensionBuilders.dp
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.ModifiersBuilders
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TimelineBuilders
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import com.google.android.horologist.tiles.images.drawableResToImageResource
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.Repository.Companion.TILE_RESOURCE_VERSION
import com.helsinkiwizard.cointoss.coin.CoinType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalHorologistApi::class)
class CoinTileService : SuspendingTileService() {

    companion object {
        private const val SELECTED_COIN = "selected_coin"
    }

    private lateinit var repo: Repository
    private lateinit var tileStateFlow: Flow<CoinType>
    private lateinit var resourceVersionFlow: Flow<Int>

    override fun onCreate() {
        super.onCreate()
        repo = Repository(this)
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
        val dimens = requestParams.deviceParameters!!

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
            .setTimeline(singleTileTimeline)
            .build()
    }

    private fun tileLayout(deviceParams: DeviceParameters): LayoutElementBuilders.LayoutElement {
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