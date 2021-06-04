package com.mirjanakopanja.movieapp.extensions

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: ((View) -> Unit)? = null,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    val ourSnackBar = Snackbar.make(this, text, length)
    action?.let {
        ourSnackBar.setAction(actionText, it)
    }
    ourSnackBar.show()


}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}