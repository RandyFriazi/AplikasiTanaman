package com.randy.plantapp.ui.quiz

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.randy.plantapp.R
import com.randy.plantapp.databinding.FragmentQuizPageBinding
import com.randy.plantapp.model.Quiz
import com.randy.plantapp.preferenes.UserPrefViewModel
import com.randy.plantapp.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizPageFragment : Fragment() {

    private val viewModel: QuizViewModel by viewModels()
    private val userPrefViewModel: UserPrefViewModel by viewModels()

    private var _binding: FragmentQuizPageBinding? = null
    private val binding get() = _binding!!

    private var position = 0

    private var listQuiz: List<Quiz> = listOf()

    private val args: QuizPageFragmentArgs by navArgs()

    override fun onResume() {
        super.onResume()
        userPrefViewModel.getQuizState().observe(viewLifecycleOwner){
            if (it){
                Log.i("OKEEEEE", "onResume: K+KOO")
                findNavController().navigateUp()
            } else {
                position = 0
                clearChecked()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentQuizPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback {
            showAlertBack()
        }

        binding.btnBack.setOnClickListener {
            showAlertBack()
        }

        val quizResult = args.quizResult
        viewModel.getQuizByPlantId(quizResult.id)

        viewModel.listQuiz.observe(viewLifecycleOwner) { uiState ->
            when(uiState){
                is UiState.Error -> {
                    Log.e("ERRORRRQUIZZZ", "${uiState.exception}")
                    isLoading(false)
                }
                is UiState.Loading -> {
                    isLoading(true)
                }
                is UiState.Success -> {
                    isLoading(false)
                    binding.layContent.visibility = View.VISIBLE
                    val data = uiState.data
                    Log.i("QUIZZSSA", "$data")
                    listQuiz = data
                    setupRadioGroupOptions()
                }
            }
        }

        binding.btnPrevQuestion.setOnClickListener {
            position = if (position>0) position-1 else position
            clearChecked()
            setupRadioGroupOptions()
            Log.i("questionTypeExample", "questionType: $position")
        }

        binding.btnNextQuestion.setOnClickListener {
            if (position==listQuiz.size-1){
                Log.i("questionTypeExample", "LAST")
                var benar = 0
                listQuiz.forEach { quiz ->
                    if (quiz.correctAnswer==quiz.tempAnswer){
                        benar+=1
                    }
                }
                userPrefViewModel.getUserInfo().observe(viewLifecycleOwner) { (userId, _) ->
                    showAlertFinish(userId, quizResult.id, benar, listQuiz.size - benar)
                }
                Log.i("questionTypeExample", "BENAR = $benar")
            }
            position = if (position<listQuiz.size-1) position+1 else position
            clearChecked()
            setupRadioGroupOptions()

            Log.i("questionTypeExample", "questionType: $position")
        }
    }

    private fun showAlertFinish(userId: Int, plantId: Int,benar: Int, salah: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Peringatan!")
            .setMessage("Apakah anda sudah yakin dengan jawaban anda?")
            .setPositiveButton("Ya") { dialog, which ->
                viewModel.setQuizScore(userId, plantId, (benar*10))
                viewModel.message.observe(viewLifecycleOwner) { uiState ->
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
                            Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
                            if (data == "Berhasil menyimpan Hasil Quiz") {
                                val action = QuizPageFragmentDirections.actionQuizPageFragmentToQuizResultFragment(plantId, benar, salah)
                                findNavController().navigate(action)
                            }
                        }
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showAlertBack() {
        AlertDialog.Builder(requireContext())
            .setTitle("Peringatan!")
            .setMessage("Apakah anda yakin ingin keluar dari quiz? Jawaban anda akan tidak tersimpan")
            .setPositiveButton("Ya") { dialog, which ->
                findNavController().navigateUp()
            }
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun clearChecked() {
        binding.rdOptionText1.isChecked = false
        binding.rdOptionText2.isChecked = false
        binding.rdOptionText3.isChecked = false
        binding.rdOptionText4.isChecked = false

        binding.rdOptionImage1.isChecked = false
        binding.rdOptionImage2.isChecked = false
        binding.rdOptionImage3.isChecked = false
        binding.rdOptionImage4.isChecked = false
    }

    private fun setupRadioGroupOptions() {
        val quiz = listQuiz[position]
        Log.i("QUIZPAGEE", "data: $listQuiz")

        binding.btnPrevQuestion.visibility = if (position > 0) View.VISIBLE else View.GONE

        binding.btnNextQuestion.text = if (position == listQuiz.size - 1) {
            "Selesai"
        } else {
            "Lanjut"
        }

        binding.tvTitle.text = "Soal ${position+1}"
        binding.tvIndicator.text = "${position+1} / ${listQuiz.size}"

        binding.rgAnswerText.setOnCheckedChangeListener { groupManager, checkedId ->
            quiz.tempAnswer = when (checkedId) {
                R.id.rdOptionText1 -> 1
                R.id.rdOptionText2 -> 2
                R.id.rdOptionText3 -> 3
                R.id.rdOptionText4 -> 4
                else -> 0
            }
        }

        binding.rgAnswerImage.setOnCheckedChangeListener { groupManager, checkedId ->
            quiz.tempAnswer = when (checkedId) {
                R.id.rdOptionImage1 -> 1
                R.id.rdOptionImage2 -> 2
                R.id.rdOptionImage3 -> 3
                R.id.rdOptionImage4 -> 4
                else -> 0
            }
        }

        val radioButtonGroup = if (quiz.questionType != 3) {
            listOf(
                binding.rdOptionText1,
                binding.rdOptionText2,
                binding.rdOptionText3,
                binding.rdOptionText4
            )
        } else {
            listOf(
                binding.rdOptionImage1,
                binding.rdOptionImage2,
                binding.rdOptionImage3,
                binding.rdOptionImage4
            )
        }

        radioButtonGroup.getOrNull(quiz.tempAnswer - 1)?.isChecked = true

//        if (quiz.questionType!=3){
//            when(quiz.tempAnswer){
//                1 -> {
//                    binding.rdOptionText1.isChecked = true
//                }
//                2 -> {
//                    binding.rdOptionText2.isChecked = true
//                }
//                3 -> {
//                    binding.rdOptionText3.isChecked = true
//                }
//                4 -> {
//                    binding.rdOptionText4.isChecked = true
//                }
//            }
//        } else {
//            when(quiz.tempAnswer){
//                1 -> {
//                    binding.rdOptionImage1.isChecked = true
//                }
//                2 -> {
//                    binding.rdOptionImage2.isChecked = true
//                }
//                3 -> {
//                    binding.rdOptionImage3.isChecked = true
//                }
//                4 -> {
//                    binding.rdOptionImage4.isChecked = true
//                }
//            }
//        }

        val params = binding.tvQuestion.layoutParams as ConstraintLayout.LayoutParams
        when (quiz.questionType) {
            1 -> {
                binding.cvImageQuestion.visibility = View.GONE

                binding.rgAnswerImage.visibility = View.GONE
                binding.rgAnswerText.visibility = View.VISIBLE

                params.setMargins(
                    0,
                    42,
                    0,
                    42,
                )

                binding.tvQuestion.text = quiz.textQuestion
                binding.tvQuestion.layoutParams = params
                binding.rdOptionText1.text = quiz.answerOptions.textOption1
                binding.rdOptionText2.text = quiz.answerOptions.textOption2
                binding.rdOptionText3.text = quiz.answerOptions.textOption3
                binding.rdOptionText4.text = quiz.answerOptions.textOption4
                binding.rdOptionText3.visibility = View.VISIBLE
                binding.rdOptionText4.visibility = View.VISIBLE
            }

            2 -> {
                binding.cvImageQuestion.visibility = View.VISIBLE

                binding.rgAnswerImage.visibility = View.GONE
                binding.rgAnswerText.visibility = View.VISIBLE

                params.setMargins(
                    0,
                    0,
                    0,
                    0,
                )
                binding.tvQuestion.layoutParams = params
                Glide.with(requireActivity())
                    .load(quiz.imageQuestionUrl)
                    .into(binding.ivQuestion)
                binding.tvQuestion.text = quiz.textQuestion
                binding.rdOptionText1.text = quiz.answerOptions.textOption1
                binding.rdOptionText2.text = quiz.answerOptions.textOption2
                binding.rdOptionText3.text = quiz.answerOptions.textOption3
                binding.rdOptionText4.text = quiz.answerOptions.textOption4
                binding.rdOptionText3.visibility = View.VISIBLE
                binding.rdOptionText4.visibility = View.VISIBLE
            }

            3 -> {
                binding.cvImageQuestion.visibility = View.GONE

                binding.rgAnswerImage.visibility = View.VISIBLE
                binding.rgAnswerText.visibility = View.GONE

                params.setMargins(
                    0,
                    42,
                    0,
                    42,
                )

                binding.tvQuestion.layoutParams = params
                binding.tvQuestion.text = quiz.textQuestion
                Glide.with(requireActivity()).load(quiz.answerOptions.imageOption1).into(binding.ivOption1)
                Glide.with(requireActivity()).load(quiz.answerOptions.imageOption2).into(binding.ivOption2)
                Glide.with(requireActivity()).load(quiz.answerOptions.imageOption3).into(binding.ivOption3)
                Glide.with(requireActivity()).load(quiz.answerOptions.imageOption4).into(binding.ivOption4)
            }
            4 -> {
                binding.cvImageQuestion.visibility = View.VISIBLE

                binding.rgAnswerImage.visibility = View.GONE
                binding.rgAnswerText.visibility = View.VISIBLE

                params.setMargins(
                    0,
                    0,
                    0,
                    0,
                )

                binding.tvQuestion.layoutParams = params
                Glide.with(requireActivity())
                    .load(quiz.imageQuestionUrl)
                    .into(binding.ivQuestion)
                binding.tvQuestion.text = quiz.textQuestion
                binding.rdOptionText1.text = quiz.answerOptions.textOption1
                binding.rdOptionText2.text = quiz.answerOptions.textOption2
                binding.rdOptionText3.visibility = View.GONE
                binding.rdOptionText4.visibility = View.GONE

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