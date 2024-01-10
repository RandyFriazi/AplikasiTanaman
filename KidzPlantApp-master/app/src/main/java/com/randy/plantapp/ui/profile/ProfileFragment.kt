package com.randy.plantapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.randy.plantapp.databinding.FragmentProfileBinding
import com.randy.plantapp.preferenes.UserPrefViewModel
import com.randy.plantapp.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private val userPrefViewModel: UserPrefViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPrefViewModel.getUserInfo().observe(viewLifecycleOwner) { (userId, state, _) ->
            viewModel.getUserById(userId)
            if (!state) {
                val action = ProfileFragmentDirections.actionProfileFragmentToOnBoardingFragment()
                findNavController().navigate(action)
            }
        }

        binding.apply {

            viewModel.user.observe(viewLifecycleOwner) { uiState ->
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
                        tvNameProfile.text = data.username
                        tvEmailProfile.text = data.email
                        ivProfile.visibility = View.VISIBLE
                        Glide.with(requireActivity())
                            .load(data.avatarUrl)
                            .into(ivProfile)
                    }
                }
            }

            btnProgressMateri.setOnClickListener {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToProgressMateriFragment()
                findNavController().navigate(action)
            }

            btnProgressQuiz.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToQuizProgressFragment()
                findNavController().navigate(action)
            }

            ivEditAvatar.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
                findNavController().navigate(action)
            }

            btnLogout.setOnClickListener {
                userPrefViewModel.logout()
            }
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