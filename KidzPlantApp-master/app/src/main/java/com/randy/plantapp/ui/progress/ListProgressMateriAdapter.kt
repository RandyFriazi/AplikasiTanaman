package com.randy.plantapp.ui.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.percentlayout.widget.PercentRelativeLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.randy.plantapp.R
import com.randy.plantapp.databinding.ItemProgressMateriBinding
import com.randy.plantapp.model.Plant

class ListProgressMateriAdapter(
    private val onClick: (plant: Plant) -> Unit,
) : RecyclerView.Adapter<ListProgressMateriAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListProgressMateriAdapter.ViewHolder {
        val binding = ItemProgressMateriBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun submitList(Plant: List<Plant>) = differ.submitList(Plant)

    override fun onBindViewHolder(holder: ListProgressMateriAdapter.ViewHolder, position: Int) {
        val plant = differ.currentList[position]

        holder.bind(plant, holder)
    }

    inner class ViewHolder(private val binding: ItemProgressMateriBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plant: Plant, holder: ListProgressMateriAdapter.ViewHolder) {
            binding.apply {
                val countCompleted = ((plant.countCompleted*20) / 100.000)

                val params = progress.layoutParams as PercentRelativeLayout.LayoutParams
                params.percentLayoutInfo.widthPercent = countCompleted.toFloat()
                progress.layoutParams = params
                progress.scaleType = ImageView.ScaleType.MATRIX

                if (countCompleted == 1.0) {
                    layProgress.setBackgroundResource(R.drawable.progres)
                }

                tvTitle.text = plant.title
                tvTextInput.text = "${plant.countCompleted}/5"
                Glide.with(holder.itemView)
                    .load(plant.iconUrl)
                    .into(icPlant)
                btnLearn.setOnClickListener { onClick(plant) }

            }
        }

    }

    override fun getItemCount(): Int = differ.currentList.size

}