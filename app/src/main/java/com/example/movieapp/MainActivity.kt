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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.ui.movieDetail.sort_selection.SortAdapter
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

        // ✅ יצירת binding והצגת התצוגה הראשית
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ קריאה לפונקציה שתגדיר את רשימת אפשרויות המיון
        setupSortRecyclerView()

        // ✅ ביצוע קוד לאחר טעינת ה- UI
        binding.root.post {
            try {
                // ✅ השגת ה- NavController פעם אחת בלבד
                navController =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
                        it as? androidx.navigation.fragment.NavHostFragment
                    }?.navController ?: throw IllegalStateException("NavController not found")

                Log.d("NavigationDebug", "NavController initialized successfully")
                binding.bottomNavigation.setupWithNavController(navController)

                // ✅ האזנה לשינויים במסכים (ניווט)
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    val isDetailFragment = destination.id == R.id.movieDetailFragment

                    // ✅ הצגת/הסתרת רכיבים בהתאם לעמוד בו נמצאים
                    binding.bottomNavigation.visibility = if (isDetailFragment) View.GONE else View.VISIBLE
                    binding.searchEditText.visibility = if (isDetailFragment) View.GONE else View.VISIBLE
                    binding.recyclerViewSort.visibility = if (isDetailFragment) View.GONE else View.VISIBLE

                    // ✅ כאשר המשתמש עובר דף – מאפסים את המיון ל-"Regular"
                    binding.recyclerViewSort.adapter?.let {
                        if (it is SortAdapter) {
                            Log.d("SortDebug", "Resetting sort to Regular on navigation change")
                            it.setSelectedSort("Regular")
                        } else {
                            Log.e("SortDebug", "Adapter is not SortAdapter!")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("NavigationDebug", "Error initializing NavController", e)
            }
        }

        // ✅ הגדרת פונקציית חיפוש (לחיצה על כפתור חיפוש במקלדת)
        binding.searchEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text.toString().trim()
                searchMovies(query)
                true
            } else {
                false
            }

        }

        // ✅ הגדרת חיפוש אוטומטי בזמן שהמשתמש מקליד
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchMovies(s.toString().trim())
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
                Log.d("SearchDebug", "Calling filterMovies in AllMoviesFragment with query: $query")
                currentFragment.filterMovies(query)
            }

            is UpcomingMoviesFragment -> {
                Log.d(
                    "SearchDebug",
                    "Calling filterMovies in UpcomingMoviesFragment with query: $query"
                )
                currentFragment.filterMovies(query)
            }

            is FavoriteMoviesFragment -> {
                Log.d(
                    "SearchDebug",
                    "Calling filterMovies in FavoriteMoviesFragment with query: $query"
                )
                currentFragment.filterMovies(query)
            }

            else -> {
                Log.e("SearchDebug", "No matching fragment found for search!")
            }
        }
    }

    private fun sortMovies(sortType: String) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? androidx.navigation.fragment.NavHostFragment
        val currentFragment = navHostFragment?.childFragmentManager?.fragments?.firstOrNull()

        when (currentFragment) {
            is AllMoviesFragment -> currentFragment.sortMovies(sortType)
            is UpcomingMoviesFragment -> currentFragment.sortMovies(sortType)
            is FavoriteMoviesFragment -> currentFragment.sortMovies(sortType)
            else -> Log.e("SortDebug", "No matching fragment found for sorting!")
        }
    }

    private fun setupSortRecyclerView() {
        val sortOptions = listOf("High Rate", "Low Rate", "Latest", "Oldest", "Regular")

        val sortAdapter = SortAdapter(sortOptions, object : SortAdapter.OnSortClickListener {
            override fun onSortClick(sortType: String) {
                Log.d("SortDebug", "User clicked on: $sortType") // ✅ בדיקה אם הלחיצה נקלטת
                sortMovies(sortType)
            }
        })

        binding.recyclerViewSort.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = sortAdapter
        }

        Log.d("SortDebug", "setupSortRecyclerView finished, calling resetSort()") // ✅ לוודא שהשורה מופעלת
        sortAdapter.resetSort() // ✅ הפעלה מיידית של Reset כדי שהבחירה תהיה "Regular"
    }


    override fun onResume() {
        super.onResume()

        if (binding.recyclerViewSort.adapter == null) {
            Log.e("SortDebug", "RecyclerViewSort adapter is NULL!") // ❌ בעיה - ה- Adapter לא נטען!
        } else {
            Log.d("SortDebug", "RecyclerViewSort adapter is loaded.") // ✅ הכל תקין
        }

        binding.recyclerViewSort.adapter?.let {
            if (it is SortAdapter) {
                Log.d("SortDebug", "Calling setSelectedSort(Regular)") // ✅ בדיקה אם זה מתבצע
                it.setSelectedSort("Regular") // ✅ איפוס לברירת מחדל
            } else {
                Log.e("SortDebug", "Adapter is not SortAdapter!") // ❌ בעיה - האדפטר לא נכון!
            }
        }
    }


}
