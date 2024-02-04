package com.randy.plantapp.ui.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.randy.plantapp.databinding.FragmentQuizResultBinding
import com.randy.plantapp.preferenes.UserPrefViewModel
import com.randy.plantapp.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizResultFragment : Fragment() {

    private var _binding: FragmentQuizResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by viewModels()
    private val userPrefViewModel: UserPrefViewModel by viewModels()

    private val args: QuizResultFragmentArgs by navArgs()

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
        _binding = FragmentQuizResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plantId = args.plantId
        val benar = args.benar
        val salah = args.salah

        activity?.onBackPressedDispatcher?.addCallback{
            userPrefViewModel.saveQuizState(true)
            userPrefViewModel.getQuizState().observe(viewLifecycleOwner){
                if (it){
                    findNavController().navigateUp()
                }
            }
        }

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
                    val quizResult = data.first { it.id == plantId }
                    var restAttempt = 3
                    restAttempt -= quizResult.scores.size
                    binding.tvRestAttempt.text = "Sisa Percobaan: $restAttempt"
                    if (quizResult.scores.size<3){
                        binding.btnRetry.visibility = View.VISIBLE
                    }
                    binding.apply {
                        tvScore.text = (benar * 10).toString()
                        tvJumlahBenar.text = benar.toString()
                        tvJumlahSalah.text = salah.toString()

                    }

                    Log.i("QUIZRESULT", "onViewCreated: $data")
                }
            }
        }

        binding.apply {

            btnOK.setOnClickListener {
                userPrefViewModel.saveQuizState(true)
                userPrefViewModel.getQuizState().observe(viewLifecycleOwner){
                    if (it){
                        findNavController().navigateUp()
                    }
                }
            }

            btnRetry.setOnClickListener {
                userPrefViewModel.saveQuizState(false)
                findNavController().navigateUp()
            }
        }

    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility = View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}