package com.randy.plantapp.ui.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.randy.plantapp.databinding.ItemQuizResultBinding
import com.randy.plantapp.model.QuizResult

class ListQuizResultAdapter : RecyclerView.Adapter<ListQuizResultAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListQuizResultAdapter.ViewHolder {
        val binding = ItemQuizResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    override fun onBindViewHolder(holder: ListQuizResultAdapter.ViewHolder, position: Int) {
        val quizResult = differ.currentList[position]

        holder.bind(quizResult, holder)
    }

    inner class ViewHolder(private val binding: ItemQuizResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(quizResult: QuizResult, holder: ListQuizResultAdapter.ViewHolder) {

            var lastScore = 0
            var restAttempt = 3
            var score1 = 0
            var score2 = 0
            var score3 = 0

            quizResult.scores.take(3).forEachIndexed { index, score ->
                when (index) {
                    0 -> score1 = score.score
                    1 -> score2 = score.score
                    2 -> score3 = score.score
                }
                lastScore = score.score
            }

            restAttempt -= quizResult.scores.size

            binding.apply {
                tvTitle.text = quizResult.title
                tvLastScoreInput.text = lastScore.toString()
                tvAttemp1Input.text = score1.toString()
                tvAttemp2Input.text = score2.toString()
                tvAttemp3Input.text = score3.toString()
                Glide.with(holder.itemView)
                    .load(quizResult.iconUrl)
                    .into(icPlant)
            }

        }
    }

    override fun getItemCount(): Int = differ.currentList.size

}