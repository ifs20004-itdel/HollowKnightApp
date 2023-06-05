package com.example.hollowknightapp.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hollowknightapp.R
import com.example.hollowknightapp.data.remote.response.HeroResponse
import com.example.hollowknightapp.databinding.FragmentMainBinding

class HeroAdapter(private val heroList: List<HeroResponse>) :
    RecyclerView.Adapter<HeroAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.hero_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvUsername.text = heroList[position].name
        holder.gender.text = heroList[position].gender
        Glide.with(holder.itemView.context)
            .load(heroList[position].photoUrl)
            .into(holder.imgAvatar)
    }

    override fun getItemCount(): Int {
        return heroList.size
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val imgAvatar: ImageView = view.findViewById(R.id.img_user_avatar)
        val gender: TextView = view.findViewById(R.id.tvGender)
    }
}
