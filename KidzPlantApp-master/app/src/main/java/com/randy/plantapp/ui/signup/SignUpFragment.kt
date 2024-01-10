package com.randy.plantapp.ui.signup

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
import com.randy.plantapp.databinding.FragmentSignUpBinding
import com.randy.plantapp.preferenes.UserPrefViewModel
import com.randy.plantapp.ui.login.LoginFragmentDirections
import com.randy.plantapp.utils.UiState
import com.randy.plantapp.utils.registerOnBackPressHandler
import com.randy.plantapp.utils.resetErrorOnTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModels()
    private val userPrefViewModel: UserPrefViewModel by viewModels()

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerOnBackPressHandler()

        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        viewModel.response.observe(viewLifecycleOwner) { uiState ->
            when(uiState){
                is UiState.Error -> {
                    isLoading(false)
                }
                is UiState.Loading -> {
                    isLoading(true)
                }
                is UiState.Success -> {
                    isLoading(false)
                    val data = uiState.data
                    Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()

//                        userPrefViewModel.saveOnBoardState(true)
                        userPrefViewModel.saveUserInfo(data.user.id, true)
                        userPrefViewModel.getUserInfo()
                            .observe(viewLifecycleOwner) { (_, state, _) ->
                                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                                if (state) {
                                    val action = SignUpFragmentDirections.actionSignUpFragmentToHomeFragment()
                                    findNavController().navigate(action)
                                }
                            }
//                        findNavController().navigateUp()

                }
            }
        }

        binding.apply {
            btnBack.setOnClickListener {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                findNavController().navigateUp()
            }

            etName.resetErrorOnTextChanged(txtLayName)
            etEmail.resetErrorOnTextChanged(txtLayEmail)
            etPassword.resetErrorOnTextChanged(txtLayPassword)
            etConfPassword.resetErrorOnTextChanged(txtLayConfPassword)

            btnSignUp.setOnClickListener {
                val username = etName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val confPassword = etConfPassword.text.toString()

                when{
                    username.isEmpty() -> {
                        txtLayName.error = "Mohon masukkan nama anda"
                    }
                    email.isEmpty() -> {
                        txtLayEmail.error = "Mohon masukkan email"
                    }
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        txtLayEmail.error = "Mohon masukkan email dengan benar"
                    }
                    password.isEmpty() -> {
                        txtLayConfPassword.error = "Mohon masukkan password"
                    }
                    password.length<6 -> {
                        txtLayConfPassword.error = "Password minimal 6 karakter"
                    }
                    confPassword.isEmpty() -> {
                        txtLayConfPassword.error = "Mohon masukkan password kembali"
                    }
                    password!=confPassword -> {
                        txtLayConfPassword.error = "Konfirmasi password tidak sesuai"
                    }
                    else -> {
                        viewModel.registerUser(username, email, password)
                    }
                }

            }

//            tvLogin.setOnClickListener {
//                findNavController().navigateUp()
//            }
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