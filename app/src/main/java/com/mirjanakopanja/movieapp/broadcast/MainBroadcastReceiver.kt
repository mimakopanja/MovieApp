package com.mirjanakopanja.movieapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.mirjanakopanja.movieapp.files.SYSTEM_MESSAGE

class MainBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append(SYSTEM_MESSAGE)
            append("Action: ${intent.action}")
             }
        }
    }