package com.glass.cienciageek.utils

import android.content.Context

object General {

    const val TOPIC_SPANISH = "Spanish"
    const val TOPIC_ENGLISH = "English"
    private const val LANGUAGE = "language"

    /**
     * Save and Get on SharedPreferences the default language on the entire App.
     * @param language is a string resource to define the language (spanish or english).
     * The first time on the registration process, it's saved as English by default.
     */
    fun saveLanguageApp(c: Context, language: String) {
        with(c.getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE)){
            edit().putString(LANGUAGE, language).apply()
        }
    }

    fun getLanguageApp(c: Context) : String {
        val prefs = c.getSharedPreferences(LANGUAGE, Context.MODE_PRIVATE)
        return prefs.getString(LANGUAGE, "").toString()
    }

}