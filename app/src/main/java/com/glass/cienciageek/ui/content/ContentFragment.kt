package com.glass.cienciageek.ui.content

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.glass.cienciageek.R
import com.glass.cienciageek.entities.UrlEspEng
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class ContentFragment : Fragment() {

    private lateinit var mAdView : AdView
    private lateinit var links: UrlEspEng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let{
            links = it.getSerializable("links") as UrlEspEng
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_content, container, false)

        MobileAds.initialize(requireContext()) {}
        mAdView = rootView.findViewById(R.id.adView)

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        rootView.findViewById<WebView>(R.id.webView).apply {
            loadUrl(links.url)
        }

        return rootView
    }
}