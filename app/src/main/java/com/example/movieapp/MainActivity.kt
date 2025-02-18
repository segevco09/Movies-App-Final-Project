package com.example.movieapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.ui.movieList.AllMoviesFragment
import com.example.movieapp.ui.upcomingMovies.UpcomingMoviesFragment
import com.example.movieapp.ui.userFavorite.FavoriteMoviesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // חיפוש ה-NavController בצורה בטוחה דרך supportFragmentManager
        binding.root.post {
            try {
                navController =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
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

        // חיבור EditText לפונקציית החיפוש בתוך CardView
        binding.searchEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text.toString().trim()
                Log.d("SearchDebug", "Search submitted: $query") // ✅ בדיקת לוג
                searchMovies(query)
                true
            } else {
                false
            }
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                //Log.d("SearchDebug", "Search changed: $query") // ✅ בדיקת לוג
                searchMovies(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun searchMovies(query: String) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? androidx.navigation.fragment.NavHostFragment

        val currentFragment = navHostFragment?.childFragmentManager?.fragments?.firstOrNull()
        when (currentFragment) {
            is AllMoviesFragment -> {
                currentFragment.filterMovies(query)
            }
            is UpcomingMoviesFragment -> {
                currentFragment.filterMovies(query)
            }
            is FavoriteMoviesFragment -> {
                currentFragment.filterMovies(query)
            }
            else -> {
                Log.e("SearchDebug", "No matching fragment found for search!")
            }
        }
    }
}
