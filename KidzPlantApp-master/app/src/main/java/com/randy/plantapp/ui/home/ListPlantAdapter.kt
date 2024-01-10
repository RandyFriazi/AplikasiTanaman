package com.randy.plantapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.randy.plantapp.databinding.ItemPlantBinding
import com.randy.plantapp.model.Plant

class ListPlantAdapter(
    private val onClick: (plant: Plant) -> Unit,
) : RecyclerView.Adapter<ListPlantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    private var diffCallback = object : DiffUtil.ItemCallback<Plant>() {
        override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(plant: List<Plant>) = differ.submitList(plant)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plant = differ.currentList[position]

        holder.bind(plant, holder)
    }

    inner class ViewHolder(private val binding: ItemPlantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plant: Plant, holder: ViewHolder) {
            binding.apply {

                tvTitle.text = plant.title
                tvDesc.text = plant.desc
                Glide.with(holder.itemView)
                    .load(plant.iconUrl)
                    .into(icPlant)
                btnLearn.setOnClickListener { onClick(plant) }
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

}