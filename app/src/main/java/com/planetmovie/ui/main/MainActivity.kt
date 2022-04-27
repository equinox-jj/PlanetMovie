package com.planetmovie.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.planetmovie.R
import com.planetmovie.databinding.ActivityMainBinding
import com.planetmovie.ui.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // View Binding
    private lateinit var binding: ActivityMainBinding

    // Navigation Component
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    // Splash Screen
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.isLoading.value }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHost) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchMovieFragment -> hideBottomNav()
                R.id.searchTvFragment -> hideBottomNav()
                R.id.movieDetailFragment -> hideBottomNav()
                R.id.tvDetailFragment -> hideBottomNav()
                R.id.favoriteMovieFragment -> hideBottomNav()
                R.id.favoriteTvFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }

        // Bottom Navigation Controller Setup
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movieFragment,
                R.id.tvFragment,
                R.id.aboutFragment
            )
        )
        binding.bottNavigation.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = this.findNavController(R.id.mainNavHost)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun showBottomNav() {
        binding.bottNavigation.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottNavigation.visibility = View.GONE
    }

}