package com.example.movieapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.movieapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  חיפוש ה-NavController בצורה בטוחה דרך supportFragmentManager
        binding.root.post {
            try {
                navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
                    it as? androidx.navigation.fragment.NavHostFragment
                }?.navController ?: throw IllegalStateException("NavController not found")

                Log.d("NavigationDebug", "NavController initialized successfully")
                binding.bottomNavigation.setupWithNavController(navController)

                navController.addOnDestinationChangedListener { _, destination, _ ->
                    binding.bottomNavigation.visibility =
                        if (destination.id == R.id.movieDetailFragment) View.GONE else View.VISIBLE
                }
            } catch (e: Exception) {
                Log.e("NavigationDebug", "Error initializing NavController", e)
            }
        }
    }


}
