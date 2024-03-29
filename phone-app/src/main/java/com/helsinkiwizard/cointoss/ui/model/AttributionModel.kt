package com.helsinkiwizard.cointoss.ui.model

import com.helsinkiwizard.core.coin.CoinType

private const val UCoin = "uCoin.net"
private const val Amazon = "Amazon.com"

class AttributionModel(
    val coin: CoinType,
    val name: String,
    val source: String
)

object AttributionParams {
    val attributions = listOf(
        AttributionModel(
            coin = CoinType.DIAPER,
            name = "Huwane Us",
            source = Amazon
        ),
        AttributionModel(
            coin = CoinType.SWEDEN,
            name = "Jonathan(swe)",
            source = UCoin
        ),
        AttributionModel(
            coin = CoinType.SAUDI_ARABIA,
            name = "agpanich",
            source = UCoin
        ),
        AttributionModel(
            coin = CoinType.MEXICO,
            name = "Фанис 67",
            source = UCoin
        ),
        AttributionModel(
            coin = CoinType.RUSSIA,
            name = "Sirdare61",
            source = UCoin
        ),
        AttributionModel(
            coin = CoinType.SPAIN,
            name = "Фанис 67",
            source = UCoin
        ),
        AttributionModel(
            coin = CoinType.TURKEY,
            name = "Рожден в СССР",
            source = UCoin
        ),
    )
}
