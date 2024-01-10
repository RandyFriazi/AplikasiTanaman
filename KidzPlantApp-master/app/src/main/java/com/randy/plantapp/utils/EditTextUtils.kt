package com.randy.plantapp.utils

import android.util.Patterns
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout

fun EditText.validateEmail(textInputLayout: TextInputLayout) {
    this.doOnTextChanged { text, _, _, _ ->
        val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()
        textInputLayout.error = if (text.isNullOrEmpty()) {
            null
        } else if (!isValidEmail) {
            "Mohon masukkan email dengan benar"
        } else {
            null
        }
    }
}

fun EditText.resetErrorOnTextChanged(textInputLayout: TextInputLayout) {
    this.doOnTextChanged { _, _, _, _ ->
        textInputLayout.error = null
    }
}