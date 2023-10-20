package com.example.githubapplication.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapplication.FollowAdapter
import com.example.githubapplication.data.response.FollowResponseItem
import com.example.githubapplication.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {

    private val viewModel: UserDetailViewModel by viewModels()
    private val adapters = FollowAdapter()
    private lateinit var binding: FragmentFollowingBinding
    private val _binding get() = binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showViewModel()
        showRecyclerView()
        viewModel.getIsLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return _binding.root
    }

    private fun showViewModel() {
        viewModel.getFollowing(UserDetailActivity.username)
        viewModel.getFollowing.observe(viewLifecycleOwner) { following ->
            if (following.isNotEmpty()) {
                adapters.setData(following)
            } else {
                binding.tvUserNotFound.visibility = View.VISIBLE
            }
        }
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        _binding.rvFollowing.adapter = adapters
        _binding.rvFollowing.layoutManager = layoutManager
        adapters.setOnItemClickCallback { data -> selectedUser(data) }
    }

    private fun selectedUser(user: FollowResponseItem) {
        val i = Intent(activity, UserDetailActivity::class.java)
        i.putExtra(UserDetailActivity.EXTRA_USER, user.login)
        startActivity(i)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.tvUserNotFound.visibility = if (isLoading) View.GONE else View.GONE
    }


}
