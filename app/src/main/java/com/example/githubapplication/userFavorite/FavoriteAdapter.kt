package com.example.githubapplication.userFavorite

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapplication.data.database.UserEntity
import com.example.githubapplication.databinding.ItemUserBinding
import de.hdodenhof.circleimageview.CircleImageView

class FavoriteAdapter(private val listFavorite: List<UserEntity>) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: UserEntity)
    }

    inner class ViewHolder(binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvusername: TextView = binding.tvUsername
        val tvUrl: TextView = binding.tvUrlGithub
        val tvphoto: CircleImageView = binding.imgAvatar

    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listFavorite.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvusername.text = listFavorite[position].username
        holder.tvUrl.text = listFavorite[position].urlHtml
        Glide.with(holder.itemView.context)
            .load(listFavorite[position].avatar)
            .into(holder.tvphoto)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listFavorite[holder.absoluteAdapterPosition])
        }
    }
}