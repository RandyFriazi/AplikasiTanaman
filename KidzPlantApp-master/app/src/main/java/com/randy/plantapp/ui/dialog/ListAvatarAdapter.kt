package com.randy.plantapp.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.randy.plantapp.databinding.ItemAvatarBinding
import com.randy.plantapp.response.AvatarResponse

class ListAvatarAdapter(
    private val onClick: (avatar: AvatarResponse) -> Unit,
) : RecyclerView.Adapter<ListAvatarAdapter.ViewHolder>() {

    private val listItem = ArrayList<AvatarResponse>()

    fun submitList(list: List<AvatarResponse>) {
        listItem.clear()
        listItem.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemAvatarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAvatarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val avatar = listItem[position]

        Glide.with(holder.itemView.context)
            .load(avatar.avatarUrl)
            .into(holder.binding.ivAvatar)
        holder.binding.root.setOnClickListener {
            onClick(avatar)
        }
    }

    override fun getItemCount(): Int = listItem.size

}