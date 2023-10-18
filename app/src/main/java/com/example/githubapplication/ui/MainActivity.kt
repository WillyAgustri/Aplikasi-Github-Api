package com.example.githubapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapplication.UserAdapter
import com.example.githubapplication.data.response.ItemsItem
import com.example.githubapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = UserAdapter()
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupRecyclerView()
        setupSearchView()
        observeUserData()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        binding.rvReview.adapter = adapter
    }

    private fun setupSearchView() {

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
}
