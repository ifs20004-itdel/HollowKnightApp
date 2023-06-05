package com.example.hollowknightapp.view.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hollowknightapp.data.remote.response.HeroResponse
import com.example.hollowknightapp.databinding.HeroItemBinding

class MainAdapter : ListAdapter<HeroResponse,MainAdapter.MyViewHolder>(
    DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HeroItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hero = getItem(position)
        holder.bind(hero)
    }

    class MyViewHolder(
        private val binding:HeroItemBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(hero: HeroResponse) {
            binding.tvUsername.text = hero.name
            binding.tvGender.text = hero.gender
            Glide.with(itemView.context)
                .load(hero.photoUrl)
                .into(binding.imgUserAvatar)
        }
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<HeroResponse>() {
                override fun areItemsTheSame(
                    oldUser: HeroResponse,
                    newUser: HeroResponse
                ): Boolean {
                    return oldUser.name == newUser.name
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldUser: HeroResponse,
                    newUser: HeroResponse
                ): Boolean {
                    return oldUser == newUser
                }
            }
    }
}