package com.helsinkiwizard.shared.coin

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.helsinkiwizard.cointoss.R

enum class CoinType(
    @DrawableRes val heads: Int,
    @DrawableRes val tails: Int,
    @StringRes val contentDesc: Int
) {
    BITCOIN(R.drawable.bitcoin_heads, R.drawable.bitcoin_tails, R.string.bitcoin),
    CANADA(R.drawable.canada_heads, R.drawable.canada_tails, R.string.canada),
    CHINA(R.drawable.china_heads, R.drawable.china_tails, R.string.china),
    EURO(R.drawable.euro_heads, R.drawable.euro_tails, R.string.euro),
    INDIA(R.drawable.india_heads, R.drawable.india_tails, R.string.india),
    JAPAN(R.drawable.japan_heads, R.drawable.japan_tails, R.string.japan),
    THAILAND(R.drawable.thailand_heads, R.drawable.thailand_tails, R.string.thailand),
    UKRAINE(R.drawable.ukraine_heads, R.drawable.ukraine_tails, R.string.ukraine),
    UNITED_KINGDOM(R.drawable.uk_heads, R.drawable.uk_tails, R.string.united_kingdom),
    UNITED_STATES(R.drawable.usa_heads, R.drawable.usa_tails, R.string.united_states);

    companion object {
        fun parse(value: Int): CoinType {
            return when (value) {
                0 -> BITCOIN
                1 -> CANADA
                2 -> CHINA
                3 -> EURO
                4 -> INDIA
                5 -> JAPAN
                6 -> THAILAND
                7 -> UKRAINE
                8 -> UNITED_KINGDOM
                else -> UNITED_STATES
            }
        }
    }
}