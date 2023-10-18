package com.example.githubapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapplication.data.response.ItemsItem
import com.example.githubapplication.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private var listUser = ArrayList<ItemsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)

    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.tvUrlGithub.text = user.htmlUrl
            binding.tvUsername.text = user.login

            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .error(R.drawable.baseline_account_box_24)
                .skipMemoryCache(true)
                .into(binding.imgAvatar)
        }
    }

    fun submitList(user: List<ItemsItem>) {
        val diffCallback = DiffUtilCallback(listUser, user)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listUser.clear()
        listUser.addAll(user)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(
                listUser[position]

            )

        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    fun interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    class DiffUtilCallback(private val oldList: List<ItemsItem>, private val newList: List<ItemsItem>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.hashCode() == newItem.hashCode()
        }

        @Override
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}