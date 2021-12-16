package com.glass.cienciageek.entities

import java.io.Serializable

/**
 * Class to manage the urls displayed on every webView.
 * Since you need to pass the data between Main screen and fragment,
 * make sure to implement the serializable interface.
 */
data class UrlEspEng(val title: String, val url: String) : Serializable