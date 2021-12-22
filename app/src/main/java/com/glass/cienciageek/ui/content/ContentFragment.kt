@file:Suppress("DEPRECATION")
package com.glass.cienciageek.ui.content

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
     */
    private fun setUpWebView() {
        rootView.findViewById<WebView>(R.id.webView).also {
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
        val urlFacebook = "https://www.facebook.com/pokecoord/"
        val urlTwitter = "https://twitter.com/pokecoord"
        val urlYoutube = "https://youtube.com/channel/UCkkiDorjnq6HleT-oW467rg"

        with(rootView.findViewById<ConstraintLayout>(R.id.parentLayout)) {
            onClickButton(findViewById(R.id.btnFacebook), urlFacebook)
            onClickButton(findViewById(R.id.btnTwitter), urlTwitter)
            onClickButton(findViewById(R.id.btnYoutube), urlYoutube)
        }
    }

    private fun onClickButton(v: ImageView, url: String) {
        v.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }
}