package com.example.movieapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.ui.adapter.SortAdapter
import com.example.movieapp.ui.allMovies.AllMoviesFragment
import com.example.movieapp.ui.upcomingMovies.UpcomingMoviesFragment
import com.example.movieapp.ui.userFavorite.FavoriteMoviesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var sortAdapter: SortAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupSearch()
        setupSortRecyclerView()
    }

    private fun setupNavigation() {
        binding.root.post {
            navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
                it as? androidx.navigation.fragment.NavHostFragment
            }?.navController ?: return@post

            binding.bottomNavigation.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                val isDetailFragment = destination.id == R.id.movieDetailFragment
                setMainUIVisibility(isDetailFragment)
                if (!isDetailFragment) {
                    sortAdapter?.setSelectedSort(getString(R.string.regular))
                    getCurrentFragment()?.let { fragment ->
                        when (fragment) {
                            is AllMoviesFragment -> fragment.sortMovies(getString(R.string.regular))
                            is UpcomingMoviesFragment -> fragment.sortMovies(getString(R.string.regular))
                            is FavoriteMoviesFragment -> fragment.sortMovies(getString(R.string.regular))
                        }
                    }
                }
            }
        }
    }

    private fun setMainUIVisibility(isDetailFragment: Boolean) {
        val visibility = if (isDetailFragment) View.GONE else View.VISIBLE
        binding.apply {
            bottomNavigation.visibility = visibility
            searchCardView.visibility = visibility
            recyclerViewSort.visibility = visibility
        }
    }

    private fun setupSearch() {
        binding.searchEditText.apply {
            setOnEditorActionListener { view, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchMovies(view.text.toString().trim())
                    true
                } else false
            }

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchMovies(s.toString().trim())
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun searchMovies(query: String) {
        getCurrentFragment()?.let { fragment ->
            when (fragment) {
                is AllMoviesFragment -> fragment.filterMovies(query)
                is UpcomingMoviesFragment -> fragment.filterMovies(query)
                is FavoriteMoviesFragment -> fragment.filterMovies(query)
            }
        }
    }

    private fun setupSortRecyclerView() {
        val sortOptions = listOf(
            getString(R.string.high_rate),
            getString(R.string.low_rate),
            getString(R.string.latest),
            getString(R.string.oldest),
            getString(R.string.regular)
        )

        sortAdapter = SortAdapter(
            sortOptions,
            object : SortAdapter.OnSortClickListener {
                override fun onSortClick(sortType: String) {
                    sortMovies(sortType)
                }
            }
        )

        binding.recyclerViewSort.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = sortAdapter
        }
    }

    private fun sortMovies(sortType: String) {
        getCurrentFragment()?.let { fragment ->
            when (fragment) {
                is AllMoviesFragment -> fragment.sortMovies(sortType)
                is UpcomingMoviesFragment -> fragment.sortMovies(sortType)
                is FavoriteMoviesFragment -> fragment.sortMovies(sortType)
            }
        }
    }

    private fun getCurrentFragment() = 
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?.let { it as? androidx.navigation.fragment.NavHostFragment }
            ?.childFragmentManager
            ?.fragments
            ?.firstOrNull()
}
