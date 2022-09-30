package com.helsinkiwizard.coinflip

import androidx.annotation.DrawableRes

enum class CoinType(
    @DrawableRes val heads: Int,
    @DrawableRes val tails: Int
) {
    BITCOIN(R.drawable.bitcoin_heads, R.drawable.bitcoin_tails),
    CANADA(R.drawable.canada_heads, R.drawable.canada_tails),
    CHINA(R.drawable.china_heads, R.drawable.china_tails),
    ENGLAND(R.drawable.england_heads, R.drawable.england_tails),
    EURO(R.drawable.euro_heads, R.drawable.euro_tails),
    INDIA(R.drawable.india_heads, R.drawable.india_tails),
    JAPAN(R.drawable.japan_heads, R.drawable.japan_tails),
    THAILAND(R.drawable.thailand_heads, R.drawable.thailand_tails),
    UKRAINE(R.drawable.ukraine_heads, R.drawable.ukraine_tails),
    USA(R.drawable.usa_heads, R.drawable.usa_tails)
}