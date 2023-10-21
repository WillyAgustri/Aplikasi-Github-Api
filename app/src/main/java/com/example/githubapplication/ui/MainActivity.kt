package com.example.githubapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapplication.R
import com.example.githubapplication.R.id.theme
import com.example.githubapplication.R.menu.main_menu
import com.example.githubapplication.UserAdapter
import com.example.githubapplication.data.response.ItemsItem
import com.example.githubapplication.databinding.ActivityMainBinding
import com.example.githubapplication.theme.ConfigurationPreferences
import com.example.githubapplication.theme.DarkActivity
import com.example.githubapplication.theme.dataStore
import com.google.android.material.search.SearchView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel
    private val adapter = UserAdapter()
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        initializeThemeAndViewModel()
        setupSearchView()
        observeUserData()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu) // Ganti "main_menu" dengan ID yang sesuai dari XML menu Anda
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.Favorite -> {
//                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
//                startActivity(intent)
//                return true
//            }
            R.id.theme -> {
                val intent = Intent(this@MainActivity, DarkActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initializeThemeAndViewModel() {
        val modePreferences = ConfigurationPreferences.getInstance(dataStore)
        val modeViewModel = ViewModelProvider(this, UserViewModel.ViewModelFactory(modePreferences))
            .get(UserViewModel::class.java)

        modeViewModel.getTheme().observe(this) { isDarkModeActive ->
            val nightMode = if (isDarkModeActive) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }

        viewModel = ViewModelProvider(this, UserViewModel.ViewModelFactory(modePreferences)).get(UserViewModel::class.java)
    }



    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        binding.rvReview.adapter = adapter
    }


    private fun setupSearchView() {
        viewModel.preloadInitialQuery()
        Toast.makeText(this@MainActivity, "Create by Willy Agustri Djabar", Toast.LENGTH_SHORT).show()
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.text = searchView.text
                searchView.hide()

                val query = searchView.text.toString()
                if (query.isNotBlank()) {
                    viewModel.searchUsers(query)
                } else {
                    Toast.makeText(this@MainActivity, "Create by Willy Agustri Djabar", Toast.LENGTH_SHORT).show()
                }
                false
            }
        }
    }

    private fun observeUserData() {
        viewModel.userList.observe(this) { userList ->
            val isDataEmpty = userList.isEmpty()
            binding.tvUserNotFound.visibility = if (isDataEmpty) View.VISIBLE else View.GONE
            binding.rvReview.visibility = if (isDataEmpty) View.GONE else View.VISIBLE
            adapter.submitList(userList)
            adapter.setOnItemClickCallback { data -> selectedUser(data) }
        }

        viewModel.getIsLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun selectedUser(user: ItemsItem) {
        val intent = Intent(this, UserDetailActivity::class.java)
        intent.putExtra(UserDetailActivity.EXTRA_USER, user.login)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.tvUserNotFound.visibility = if (isLoading) View.GONE else View.GONE
    }


    override fun onResume() {
        super.onResume()
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            val themeMenuItem = menu?.findItem(R.id.theme)
            themeMenuItem?.setIcon(R.drawable.baseline_brightness_4_24)
            val favoriteMenuItem = menu?.findItem(R.id.menu_favorite)
            favoriteMenuItem?.setIcon(R.drawable.baseline_volunteer_activism_white)
        } else {
            val themeMenuItem = menu?.findItem(R.id.theme)
            themeMenuItem?.setIcon(R.drawable.baseline_brightness_7_24)
            val favoriteMenuItem = menu?.findItem(R.id.menu_favorite)
            favoriteMenuItem?.setIcon(R.drawable.baseline_volunteer_activism_black)
        }
    }

}
