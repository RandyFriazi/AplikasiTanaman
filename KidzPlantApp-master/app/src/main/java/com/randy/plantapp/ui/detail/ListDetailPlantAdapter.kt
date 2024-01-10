package com.randy.plantapp.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.randy.plantapp.databinding.ItemSubPlantBinding
import com.randy.plantapp.model.SubPlant

class ListDetailPlantAdapter(
    private val onClick: (subPlant: SubPlant) -> Unit,
) : RecyclerView.Adapter<ListDetailPlantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSubPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    private var diffCallback = object : DiffUtil.ItemCallback<SubPlant>() {
        override fun areItemsTheSame(oldItem: SubPlant, newItem: SubPlant): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SubPlant, newItem: SubPlant): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(subPlant: List<SubPlant>) = differ.submitList(subPlant)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subPlant = differ.currentList[position]

        holder.bind(subPlant, holder)
    }

    inner class ViewHolder(private val binding: ItemSubPlantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subPlant: SubPlant, holder: ViewHolder){
            val title = subPlant.title.replace(" ", "\n")
            binding.apply {
                tvTitle.text = title
                Glide.with(holder.itemView.context)
                    .load(subPlant.iconUrl)
                    .into(icSubPlant)
                if (subPlant.isCompleted){
                    icCheck.visibility = View.VISIBLE
                }
                root.setOnClickListener {
                    onClick(subPlant)
                }
            }
        }

    }

    override fun getItemCount(): Int = differ.currentList.size
}