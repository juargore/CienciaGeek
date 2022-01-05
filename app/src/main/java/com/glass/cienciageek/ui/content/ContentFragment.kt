@file:Suppress("DEPRECATION")
package com.glass.cienciageek.ui.content

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.glass.cienciageek.R
import com.glass.cienciageek.entities.UrlEspEng
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_content.*

/**
 * This fragment is the only one responsible to display the webView and its content.
 * Here the Ads from Adsense are setting up and displayed on top banner as well.
 * The rest of operations (menu, bottom sheet, slide menu, etc) are managed on MainActivity.
 */
class ContentFragment : Fragment() {

    private lateinit var mAdView : AdView
    private lateinit var links: UrlEspEng
    private lateinit var rootView: View

    /**
     * Get the url to be displayed on webView that comes
     * from onNavigationItemSelected() on MainActivity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            links = it.getSerializable("links") as UrlEspEng
        }
    }

    /**
     * Simplest code to display the url on the webView.
     * Here the Ads are setting up and displayed on screen.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_content, container, false)

        MobileAds.initialize(requireContext()) {}
        mAdView = rootView.findViewById(R.id.adView)

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        setUpWebView()
        setUpBottomSheet()

        return rootView
    }

    /**
     * load the url on webView + show/hide progress animation on screen.
     * Also enable javascript to display content correctly on webView.
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        rootView.findViewById<WebView>(R.id.webView).also {

            with(it.settings) {
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
            }

            it.loadUrl(links.url)
            it.webViewClient = object : WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    progress.visibility = View.VISIBLE
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    Handler().postDelayed({
                        progress.visibility = View.GONE
                    }, 1000)
                    super.onPageFinished(view, url)
                }
            }
        }
    }

    private fun setUpBottomSheet() {
        safeWith(rootView.findViewById<ConstraintLayout>(R.id.parentLayout), requireContext()) { lay, c ->
            onClickButton(lay.findViewById(R.id.btnFacebook), c.resources.getString(R.string.url_facebook))
            onClickButton(lay.findViewById(R.id.btnTwitter), c.resources.getString(R.string.url_twitter))
            onClickButton(lay.findViewById(R.id.btnYoutube), c.resources.getString(R.string.url_youtube))
            onClickButton(lay.findViewById(R.id.btnCien), c.resources.getString(R.string.url_cien))
        }
    }

    private fun onClickButton(v: ImageView, url: String) {
        v.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    private inline fun <T1: Any, T2: Any, R: Any> safeWith(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
        return if (p1 != null && p2 != null) block(p1, p2) else null
    }
}