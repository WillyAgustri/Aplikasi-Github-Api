package com.example.githubapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapplication.R
import com.example.githubapplication.UserAdapter
import com.example.githubapplication.data.response.ItemsItem
import com.example.githubapplication.databinding.ActivityMainBinding
import com.example.githubapplication.theme.ConfigurationPreferences
import com.example.githubapplication.theme.DarkActivity
import com.example.githubapplication.theme.dataStore
import com.example.githubapplication.userFavorite.UserFavoriteActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel
    private val adapter = UserAdapter()
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        val themeIcon =
            if (isDarkMode) R.drawable.baseline_brightness_4_24 else R.drawable.baseline_brightness_7_24
        val favoriteIcon =
            if (isDarkMode) R.drawable.baseline_volunteer_activism_white else R.drawable.baseline_volunteer_activism_black

        // Mengatur ikon pada item menu "theme"
        menu?.findItem(R.id.theme)?.setIcon(themeIcon)

        setupRecyclerView()
        initializeThemeAndViewModel()
        setupSearchView()
        observeUserData()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> {
                startActivity(UserFavoriteActivity::class.java)
                return true
            }

            R.id.theme -> {
                startActivity(DarkActivity::class.java)
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
        intent.putExtra(UserDetailActivity.EXTRA_AVATAR, user.avatarUrl)
        intent.putExtra(UserDetailActivity.EXTRA_URL, user.htmlUrl)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.tvUserNotFound.visibility = if (isLoading) View.GONE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        val themeIcon =
            if (isDarkMode) R.drawable.baseline_brightness_4_24 else R.drawable.baseline_brightness_7_24
        val favoriteIcon =
            if (isDarkMode) R.drawable.baseline_volunteer_activism_white else R.drawable.baseline_volunteer_activism_black

        menu?.findItem(R.id.theme)?.setIcon(themeIcon)
        menu?.findItem(R.id.menu_favorite)?.setIcon(favoriteIcon)
    }

    private fun startActivity(cls: Class<out AppCompatActivity>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }
}
