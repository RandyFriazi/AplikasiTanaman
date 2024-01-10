package com.randy.plantapp.ui.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.randy.plantapp.databinding.ItemQuizBinding
import com.randy.plantapp.model.QuizResult

class ListQuizAdapter(
    private val onClick: (quizResult: QuizResult) -> Unit,
) : RecyclerView.Adapter<ListQuizAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListQuizAdapter.ViewHolder {
        val binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    private var diffCallback = object : DiffUtil.ItemCallback<QuizResult>() {
        override fun areItemsTheSame(oldItem: QuizResult, newItem: QuizResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: QuizResult, newItem: QuizResult): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(quizResult: List<QuizResult>) = differ.submitList(quizResult)

    override fun onBindViewHolder(holder: ListQuizAdapter.ViewHolder, position: Int) {
        val quizResult = differ.currentList[position]

        holder.bind(quizResult, holder)
    }

    inner class ViewHolder(private val binding: ItemQuizBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(quizResult: QuizResult, holder: ListQuizAdapter.ViewHolder) {

            var lastScore = 0
            var restAttempt = 3

            if (quizResult.scores.isNotEmpty()) {
                restAttempt -= quizResult.scores.size
                if (quizResult.scores.size==3){
                    binding.btnStart.visibility = View.GONE
                }
                quizResult.scores.forEach {
                    lastScore = it.score
                }
            }

            binding.apply {
                tvTitle.text = quizResult.title
                tvLastScore.text = "Nilai Terakhir: $lastScore"
                tvRestAttempt.text = "Sisa Percobaan: $restAttempt"
                Glide.with(holder.itemView.context)
                    .load(quizResult.iconUrl)
                    .into(icPlant)
                btnStart.setOnClickListener { onClick(quizResult) }
            }

        }
    }

    override fun getItemCount(): Int = differ.currentList.size

}