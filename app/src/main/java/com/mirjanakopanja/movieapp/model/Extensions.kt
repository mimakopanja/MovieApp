package com.mirjanakopanja.movieapp.model

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: ((View) -> Unit)? = null,
    length: Int = Snackbar.LENGTH_INDEFINITE
){
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}