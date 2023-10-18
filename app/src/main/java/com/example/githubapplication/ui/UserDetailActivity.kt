package com.example.githubapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapplication.R
import com.example.githubapplication.SectionPagerAdapter
import com.example.githubapplication.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private val viewModel: UserDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Detail"
        setupViewPager()
        setupActionBar()
        username = intent.getStringExtra(EXTRA_USER).toString()
        showViewModel(username)
        observeLoading()
    }

    private fun setupViewPager() {
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun showViewModel(username: String) {
        viewModel.detailUser(username)
        viewModel.getUserDetail.observe(this) { detailUser ->
            Glide.with(this)
                .load(detailUser.avatarUrl)
                .skipMemoryCache(true)
                .into(binding.imgAvatar)

            binding.tvName.text = detailUser.name
            binding.tvUsername.text = detailUser.login
            binding.tvRepository.text = detailUser.publicRepos.toString()
            binding.tvCompany.text = detailUser.company
            binding.tvLocation.text = detailUser.location
            binding.tvFollower.text = detailUser.followers.toString()
            binding.tvFollowing.text = detailUser.following.toString()
        }
    }

    private fun observeLoading() {
        viewModel.getIsLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        var username = String()

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}
