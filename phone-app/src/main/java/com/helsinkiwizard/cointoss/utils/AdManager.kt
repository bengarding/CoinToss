package com.helsinkiwizard.cointoss.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.preference.PreferenceManager
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.helsinkiwizard.cointoss.BuildConfig
import com.helsinkiwizard.cointoss.Constants.COIN_LIST_INTERSTITIAL_AD_ID
import com.helsinkiwizard.cointoss.Constants.CUSTOM_COIN_INTERSTITIAL_AD_ID
import com.helsinkiwizard.cointoss.Constants.DEBUG_BANNER_AD_ID
import com.helsinkiwizard.cointoss.Constants.DEBUG_INTERSTITIAL_AD_ID
import com.helsinkiwizard.cointoss.Constants.MAIN_INTERSTITIAL_AD_ID
import com.helsinkiwizard.cointoss.data.InterstitialAdData
import com.helsinkiwizard.core.theme.LocalActivity
import timber.log.Timber

private const val ONE_HOUR_IN_MILLIS = 3600000

object AdManager {

    private val interstitialAds = mutableMapOf<String, InterstitialAdData?>(
        MAIN_INTERSTITIAL_AD_ID to null,
        COIN_LIST_INTERSTITIAL_AD_ID to null,
        CUSTOM_COIN_INTERSTITIAL_AD_ID to null
    )

    fun updateConsentStatus(activity: Activity) {
        val consentInfo = UserMessagingPlatform.getConsentInformation(activity)

        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()

        consentInfo.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    formError?.let {
                        Timber.e("Error loading consent form: ${it.message}")
                    }
                }
            },
            { requestError ->
                Timber.e("Error requesting consent: ${requestError.message}")
            }
        )
    }

    fun loadInterstitialAds(context: Context) {
        val currentTime = System.currentTimeMillis()

        interstitialAds.forEach { (adId, adData) ->
            val isStale = currentTime - (adData?.timestamp ?: 0) > ONE_HOUR_IN_MILLIS
            if (isStale) {
                loadInterstitialAd(
                    context = context,
                    id = if (BuildConfig.DEBUG) DEBUG_INTERSTITIAL_AD_ID else adId,
                    onAdLoaded = { newAd ->
                        interstitialAds[adId] = InterstitialAdData(newAd, currentTime)
                    }
                )
            }
        }
    }

    fun clearLoadedAds() {
        interstitialAds.clear()
    }

    fun showConsentForm(activity: Activity) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
            formError?.let {
                Timber.e("Error loading consent form from settings: ${it.message}")
            }
        }
    }

    private fun getAdRequest(context: Context): AdRequest {
        val extras = Bundle().apply {
            if (canShowPersonalizedAds(context).not()) {
                putString("npa", "1")
            }
        }
        return AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()
    }

    private fun loadInterstitialAd(
        context: Context,
        id: String,
        onAdLoaded: (InterstitialAd) -> Unit
    ) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context, id, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    onAdLoaded(interstitialAd)
                }
            }
        )
    }

    // The following methods are from https://stackoverflow.com/a/68310602/19034973

    fun isGDPR(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        val gdpr = prefs.getInt("IABTCF_gdprApplies", 0)
        return gdpr == 1
    }

    private fun canShowPersonalizedAds(context: Context): Boolean {
        if (isGDPR(context).not()) return true

        val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

        //https://github.com/InteractiveAdvertisingBureau/GDPR-Transparency-and-Consent-Framework/blob/master/TCFv2/IAB%20Tech%20Lab%20-%20CMP%20API%20v2.md#in-app-details
        //https://support.google.com/admob/answer/9760862?hl=en&ref_topic=9756841

        val purposeConsent = prefs.getString("IABTCF_PurposeConsents", "") ?: ""
        val vendorConsent = prefs.getString("IABTCF_VendorConsents", "") ?: ""
        val vendorLI = prefs.getString("IABTCF_VendorLegitimateInterests", "") ?: ""
        val purposeLI = prefs.getString("IABTCF_PurposeLegitimateInterests", "") ?: ""

        val googleId = 755
        val hasGoogleVendorConsent = hasAttribute(vendorConsent, index = googleId)
        val hasGoogleVendorLI = hasAttribute(vendorLI, index = googleId)

        return hasConsentFor(listOf(1, 3, 4), purposeConsent, hasGoogleVendorConsent)
                && hasConsentOrLegitimateInterestFor(
            listOf(2, 7, 9, 10),
            purposeConsent,
            purposeLI,
            hasGoogleVendorConsent,
            hasGoogleVendorLI
        )
    }

    // Check if a binary string has a "1" at position "index" (1-based)
    private fun hasAttribute(input: String, index: Int): Boolean {
        return input.length >= index && input[index - 1] == '1'
    }

    // Check if consent is given for a list of purposes
    private fun hasConsentFor(purposes: List<Int>, purposeConsent: String, hasVendorConsent: Boolean): Boolean {
        return purposes.all { p -> hasAttribute(purposeConsent, p) } && hasVendorConsent
    }

    // Check if a vendor either has consent or legitimate interest for a list of purposes
    private fun hasConsentOrLegitimateInterestFor(
        purposes: List<Int>,
        purposeConsent: String,
        purposeLI: String,
        hasVendorConsent: Boolean,
        hasVendorLI: Boolean
    ): Boolean {
        return purposes.all { p ->
            (hasAttribute(purposeLI, p) && hasVendorLI) || (hasAttribute(purposeConsent, p) && hasVendorConsent)
        }
    }

    @Composable
    fun ShowInterstitialAd(
        adId: String,
        onAdDismissed: (() -> Unit)? = null
    ) {
        val activity = LocalActivity.current
        val interstitialAd = interstitialAds[adId]?.ad

        val onDismiss = {
            interstitialAds[adId] = null
            onAdDismissed?.invoke()
            loadInterstitialAds(activity)
        }

        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    onDismiss()
                }
            }
            ad.show(activity)
        } ?: onDismiss()
    }

    @Composable
    fun BannerAd(
        adId: String,
        modifier: Modifier = Modifier
    ) {
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    val displayMetrics = context.resources.displayMetrics
                    val screenWidth = (displayMetrics.widthPixels / displayMetrics.density).toInt()
                    setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidth))
                    adUnitId = if (BuildConfig.DEBUG) DEBUG_BANNER_AD_ID else adId
                    loadAd(getAdRequest(context))
                }
            }
        )
    }
}
