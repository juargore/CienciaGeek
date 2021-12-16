package com.glass.cienciageek.entities

import com.glass.cienciageek.R

/**
 * Class to manage the options displayed on popup language
 * If you need more languages, make sure to add them here.
 */
enum class Language(val language: String, val icon: Int) {
    SPANISH("Spanish", R.drawable.ic_mexico_flag),
    ENGLISH("English", R.drawable.ic_usa_flag)
}