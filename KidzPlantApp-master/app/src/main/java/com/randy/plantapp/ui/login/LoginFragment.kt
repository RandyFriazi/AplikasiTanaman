package com.randy.plantapp.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.randy.plantapp.databinding.FragmentLoginBinding
import com.randy.plantapp.preferenes.UserPrefViewModel
import com.randy.plantapp.utils.UiState
import com.randy.plantapp.utils.registerOnBackPressHandler
import com.randy.plantapp.utils.resetErrorOnTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private val userPrefViewModel: UserPrefViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerOnBackPressHandler()

        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

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
                    if (data.id > 0) {
                        Toast.makeText(
                            requireContext(),
                            "Selamat datang ${data.username}",
                            Toast.LENGTH_SHORT
                        ).show()
//                        userPrefViewModel.saveOnBoardState(true)
                        userPrefViewModel.saveUserInfo(data.id, true)
                        userPrefViewModel.getUserInfo()
                            .observe(viewLifecycleOwner) { (_, state, _) ->
                                if (state) {
                                    val action =
                                        LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                                    findNavController().navigate(action)
                                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                                }
                            }
                    }

                }
            }
        }

        binding.apply {
            btnBack.setOnClickListener {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                findNavController().navigateUp()
            }

            etEmail.resetErrorOnTextChanged(txtLayEmail)
            etPassword.resetErrorOnTextChanged(txtLayPassword)
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                when {
                    email.isEmpty() -> {
                        txtLayEmail.error = "Mohon masukkan email"
                    }
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        txtLayEmail.error = "Mohon masukkan email dengan benar"
                    }
                    password.isEmpty() -> {
                        txtLayPassword.error = "Mohon masukkan password"
                    }
                    else -> {
                        viewModel.loginUser(email, password)
                    }
                }

            }
//            tvSignUp.setOnClickListener {
//                val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
//                findNavController().navigate(action)
//            }

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