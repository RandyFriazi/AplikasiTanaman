package com.randy.plantapp.ui.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.randy.plantapp.databinding.FragmentQuizBinding
import com.randy.plantapp.preferenes.UserPrefViewModel
import com.randy.plantapp.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment() {

    private lateinit var listQuizAdapter: ListQuizAdapter

    private val viewModel: QuizViewModel by viewModels()
    private val userPrefViewModel: UserPrefViewModel by viewModels()

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        userPrefViewModel.getUserInfo().observe(viewLifecycleOwner){ (userId, _) ->
            viewModel.getQuizResultByUserId(userId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.listQuizResult.observe(viewLifecycleOwner){ uiState ->
            when (uiState) {
                is UiState.Error -> {
                    isLoading(false)
                }
                is UiState.Loading -> {
                    isLoading(true)
                }
                is UiState.Success -> {
                    isLoading(false)
                    val data = uiState.data
                    Log.i("QUIZRESULT", "onViewCreated: $data")
                    listQuizAdapter.submitList(data)
                }
            }
        }

    }

    private fun setupRecyclerView() {
        listQuizAdapter = ListQuizAdapter(
            onClick = { quizResult ->
                userPrefViewModel.saveQuizState(false)
                val action = QuizFragmentDirections.actionQuizFragmentToQuizPageFragment(quizResult)
                findNavController().navigate(action)
            }
        )
//        listPlantAdapter.submitList(previewPlants)
        binding.apply {
            rvQuiz.setHasFixedSize(true)
            rvQuiz.layoutManager = LinearLayoutManager(requireContext())
            rvQuiz.adapter = listQuizAdapter
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}