package com.example.githubapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapplication.FollowAdapter
import com.example.githubapplication.data.response.FollowResponseItem
import com.example.githubapplication.databinding.FragmentFollowerBinding

class FollowerFragment : Fragment() {

    private val viewModel: UserDetailViewModel by viewModels()
    private val adapters = FollowAdapter()
    private lateinit var binding: FragmentFollowerBinding
    private val _binding get() = binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showViewModel()
        showRecyclerView()
        viewModel.getIsLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showViewModel() {
        viewModel.getFollower(UserDetailActivity.username)
        viewModel.getFollowers.observe(viewLifecycleOwner) { followers ->
            if (followers.isNotEmpty()) {
                adapters.setData(followers)
            } else {
                binding.tvUserNotFound.visibility = View.VISIBLE
            }
        }
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        _binding.rvFollower.adapter = adapters
        _binding.rvFollower.layoutManager = layoutManager
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
