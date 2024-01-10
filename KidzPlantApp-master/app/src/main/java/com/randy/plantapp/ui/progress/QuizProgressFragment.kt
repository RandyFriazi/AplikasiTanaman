package com.randy.plantapp.ui.progress

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.randy.plantapp.databinding.FragmentQuizProgressBinding
import com.randy.plantapp.preferenes.UserPrefViewModel
import com.randy.plantapp.utils.UiState
import com.randy.plantapp.utils.registerOnBackPressHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizProgressFragment : Fragment() {

    private lateinit var listQuizResultAdapter: ListQuizResultAdapter

    private val viewModel: ProgressViewModel by viewModels()
    private val userPrefViewModel: UserPrefViewModel by viewModels()

    private var _binding: FragmentQuizProgressBinding? = null
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
        _binding = FragmentQuizProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerOnBackPressHandler()

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
                    listQuizResultAdapter.submitList(data)
                }
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun setupRecyclerView() {
        listQuizResultAdapter = ListQuizResultAdapter()
        binding.rvQuizResult.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listQuizResultAdapter
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