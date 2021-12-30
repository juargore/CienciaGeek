package com.glass.cienciageek.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.glass.cienciageek.R
import com.glass.cienciageek.entities.Language
import kotlinx.android.synthetic.main.item_spinner_language.view.*

/**
 * Adapter that help us to show the items on the language spinner.
 * You can customize the views of each item and header on its own R.layout...
 */
class LanguageAdapter(context: Context): ArrayAdapter<Language>(
    context,
    0,
    listOf(
        Language(context.resources.getString(R.string.popup_language_spanish), R.drawable.ic_mexico_flag),
        Language(context.resources.getString(R.string.popup_language_english), R.drawable.ic_usa_flag)
    )
) {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.item_spinner_language, parent, false)
        getItem(position)?.let { setItemForLanguage(view, it) }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (position == 0) {
            view = layoutInflater.inflate(R.layout.header_spinner_language, parent, false)
        } else {
            view = layoutInflater.inflate(R.layout.item_spinner_language, parent, false)
            getItem(position)?.let { setItemForLanguage(view, it) }
        }

        return view
    }

    override fun getItem(position: Int): Language? {
        if (position == 0) return null
        return super.getItem(position - 1)
    }

    override fun getCount() = super.getCount() + 1

    override fun isEnabled(position: Int) = position != 0

    private fun setItemForLanguage(view: View, language: Language) {
        view.txtLanguage.text = language.language
        view.imgLanguage.setBackgroundResource(language.icon)
    }
}