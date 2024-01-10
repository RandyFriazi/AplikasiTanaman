package com.randy.plantapp.utils

import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.registerOnBackPressHandler() {
    registerOnBackPressHandler {
        findNavController().navigateUp()
    }
}

fun Fragment.registerOnBackPressHandler(
    onBackPressed: OnBackPressedCallback.() -> Unit
) {
    activity?.onBackPressedDispatcher?.addCallback { onBackPressed() }
}