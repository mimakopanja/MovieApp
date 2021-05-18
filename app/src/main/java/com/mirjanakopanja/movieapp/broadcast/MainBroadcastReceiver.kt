package com.mirjanakopanja.movieapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.mirjanakopanja.movieapp.ui.main.MainFragment

class MainBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append("SYSTEM MESSAGE\n")
            append("Action: ${intent.action}")
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            } }
        }
    }