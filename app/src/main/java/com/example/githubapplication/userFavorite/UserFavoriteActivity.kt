package com.example.githubapplication.userFavorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapplication.data.database.UserEntity
import com.example.githubapplication.databinding.ActivityUserFavoriteBinding
import com.example.githubapplication.ui.FavoriteViewModel
import com.example.githubapplication.ui.UserDetailActivity

class UserFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupFavoriteData()
    }

    private fun setupFavoriteData() {
        val favoriteViewModel = obtainViewModel(this@UserFavoriteActivity)
        favoriteViewModel.getAllFavoriteData().observe(this) { userEntities ->
            if (userEntities.isEmpty()) {
                showNotFound(true)
                showLoading(false)
            } else {
                showNotFound(false)
                showLoading(false)
                setFavoriteData(userEntities)
            }
        }
    }

    private fun setFavoriteData(userEntities: List<UserEntity>) {
        val items = userEntities.toMutableList()
        val adapter = FavoriteAdapter(items)
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity) {
                startActivity(
                    Intent(this@UserFavoriteActivity, UserDetailActivity::class.java)
                        .putExtra(UserDetailActivity.EXTRA_USER, data.username)
                )
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModel.ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showNotFound(isFound: Boolean) {
        binding.tvUserNotFound.visibility = if (isFound) View.VISIBLE else View.GONE
    }
}
