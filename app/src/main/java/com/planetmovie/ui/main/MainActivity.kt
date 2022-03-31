package com.planetmovie.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.planetmovie.R
import com.planetmovie.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // View Binding
    private lateinit var binding: ActivityMainBinding

    // Navigation Component
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                else -> showBottomNav()
            }
        }

        // Bottom Navigation Controller Setup
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movieFragment,
                R.id.tvFragment,
                R.id.favoriteFragment,
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