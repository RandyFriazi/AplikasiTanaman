package com.randy.plantapp.ui.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.randy.plantapp.databinding.ItemOnboardingBinding
import com.randy.plantapp.model.OnBoardingDataClass

class OnBoardingAdapter(
    private val list: List<OnBoardingDataClass>,
) : RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load(list[position].image)
                .into(ivOnboard)
            tvOnboard1.text = list[position].text1
            tvOnboard2.text = list[position].text2
        }
    }

    class ViewHolder(val binding: ItemOnboardingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = list.size
}