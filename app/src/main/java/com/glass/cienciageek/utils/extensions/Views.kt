package com.glass.cienciageek.utils.extensions

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.EditText
import androidx.annotation.LayoutRes
import com.glass.cienciageek.R

fun EditText.getAsText() : String = this.text.toString().trim()

fun getDialog(c: Context, @LayoutRes layout: Int) =
    Dialog(c, R.style.FullDialogTheme).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(layout)
        show()
    }

