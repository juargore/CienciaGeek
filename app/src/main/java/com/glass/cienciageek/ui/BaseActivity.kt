package com.glass.cienciageek.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.glass.cienciageek.R
import com.glass.cienciageek.utils.ContextUtils
import com.glass.cienciageek.utils.General.getLanguageApp
import java.util.*

open class BaseActivity : AppCompatActivity() {

    /** Update the language on the App. Called on Top Menu / Language */
    override fun attachBaseContext(context: Context) {
        val localeToSwitchTo = getLanguageApp(context)

        val localeUpdatedContext = ContextUtils.updateLocale(context,
            if (localeToSwitchTo.equals(context.resources.getString(R.string.popup_language_spanish), true)) {
                Locale("es")
            } else {
                Locale("en")
            }
        )

        super.attachBaseContext(localeUpdatedContext)
    }
}