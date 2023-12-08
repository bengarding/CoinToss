package com.helsinkiwizard.cointoss.coin

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.helsinkiwizard.cointoss.R

enum class CoinType(
    val value: Int,
    @DrawableRes val heads: Int,
    @DrawableRes val tails: Int,
    @StringRes val nameRes: Int
) {
    BITCOIN(1, R.drawable.bitcoin_heads, R.drawable.bitcoin_tails, R.string.bitcoin),
    CANADA(2, R.drawable.canada_heads, R.drawable.canada_tails, R.string.canada),
    CHINA(3, R.drawable.china_heads, R.drawable.china_tails, R.string.china),
    EURO(4, R.drawable.euro_heads, R.drawable.euro_tails, R.string.euro),
    INDIA(5, R.drawable.india_heads, R.drawable.india_tails, R.string.india),
    JAPAN(6, R.drawable.japan_heads, R.drawable.japan_tails, R.string.japan),
    THAILAND(7, R.drawable.thailand_heads, R.drawable.thailand_tails, R.string.thailand),
    TURKEY(8, R.drawable.turkey_heads, R.drawable.turkey_tails, R.string.turkey),
    UKRAINE(9, R.drawable.ukraine_heads, R.drawable.ukraine_tails, R.string.ukraine),
    UNITED_KINGDOM(10, R.drawable.uk_heads, R.drawable.uk_tails, R.string.united_kingdom),
    UNITED_STATES(11, R.drawable.usa_heads, R.drawable.usa_tails, R.string.united_states),
    UZBEKISTAN(12, R.drawable.uzbekistan_heads, R.drawable.uzbekistan_tails, R.string.uzbekistan),
    DIAPER(13, R.drawable.diaper_heads, R.drawable.diaper_tails, R.string.diaper_duty);

    companion object {
        fun parse(value: Int): CoinType {
            return CoinType.entries.firstOrNull { it.value == value } ?: BITCOIN
        }
    }
}
