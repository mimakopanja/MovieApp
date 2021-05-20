package com.mirjanakopanja.movieapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.mirjanakopanja.movieapp.MainActivity
import com.mirjanakopanja.movieapp.R

const val SPLASH_DISPLAY_LENGTH : Long = 1000;

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

        }, SPLASH_DISPLAY_LENGTH)
    }
}