package com.example.githubapplication.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapplication.R
import com.example.githubapplication.SectionPagerAdapter
import com.example.githubapplication.data.database.UserEntity
import com.example.githubapplication.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private val viewModel by viewModels<UserDetailViewModel> {
        UserDetailViewModel.ViewModelFactory.getInstance(application)
    }


    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.title = "Detail"
        setupViewPager()
        setupActionBar()

        showViewModel(username)
        username = intent.getStringExtra(EXTRA_USER).toString()
        val avatar = intent.getStringExtra(EXTRA_AVATAR) ?: ""
        val urlHtml = intent.getStringExtra(EXTRA_URL) ?: ""
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, username)
        observeLoading()

        viewModel.getDataByUsername(username).observe(this) {
            isFavorite = it.isNotEmpty()
            val favoriteUser = UserEntity(username, avatar, urlHtml)
            if (it.isEmpty()) {
                binding.iconFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.iconFavorite.context,
                        R.drawable.baseline_favorite_border_white
                    )
                )
                binding.iconFavorite.contentDescription = "sudah Ditambahkan"
            } else {
                binding.iconFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.iconFavorite.context,
                        R.drawable.baseline_favorite_24
                    )
                )
                binding.iconFavorite.contentDescription = "Dihapus"
            }

            binding.iconFavorite.setOnClickListener {
                if (isFavorite) {
                    viewModel.deleteDataUser(favoriteUser)
                    Toast.makeText(this, "Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.insertDataUser(favoriteUser)
                    Toast.makeText(this, "Berhasil Ditambah", Toast.LENGTH_SHORT).show()
                }
            }
        }
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

            detailUser.apply {
                binding.tvName.text = name
                binding.tvUsername.text = login
                binding.tvRepository.text = publicRepos.toString()
                binding.tvCompany.text = company
                binding.tvLocation.text = location
                binding.tvFollower.text = followers.toString()
                binding.tvFollowing.text = following.toString()
            }
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
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_URL = "extra_url"
        var username = String()

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}
