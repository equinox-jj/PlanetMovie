package com.planetmovie.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.planetmovie.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }
}