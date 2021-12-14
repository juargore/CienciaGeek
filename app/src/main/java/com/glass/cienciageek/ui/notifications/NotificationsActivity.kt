package com.glass.cienciageek.ui.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import com.glass.cienciageek.R
import kotlinx.android.synthetic.main.activity_notifications.*

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        setupSpinners()
    }

    private fun setupSpinners() {
        val listTypes = listOf("Youtube")
        val typesAdapter = ArrayAdapter(this, R.layout.spinner_item_simple, listTypes)

        val listLanguages = listOf(resources.getString(R.string.popup_language_spanish), resources.getString(R.string.popup_language_english))
        val languagesAdapter = ArrayAdapter(this, R.layout.spinner_item_simple, listLanguages)

        spinnerType.adapter = typesAdapter
        spinnerLanguage.adapter = languagesAdapter
    }
}