package com.randy.plantapp.ui.profile.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.randy.plantapp.databinding.FragmentEditProfileBinding
import com.randy.plantapp.model.User
import com.randy.plantapp.preferenes.UserPrefViewModel
import com.randy.plantapp.ui.dialog.DialogAvatarFragment
import com.randy.plantapp.utils.UiState
import com.randy.plantapp.utils.registerOnBackPressHandler
import com.randy.plantapp.utils.resetErrorOnTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditProfileViewModel by viewModels()
    private val userPrefViewModel: UserPrefViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerOnBackPressHandler()

        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.apply {
            etName.resetErrorOnTextChanged(txtLayName)
            etEmail.resetErrorOnTextChanged(txtLayEmail)
            etPassword.resetErrorOnTextChanged(txtLayPassword)
            etConfPassword.resetErrorOnTextChanged(txtLayConfPassword)



            userPrefViewModel.getUserInfo().observe(viewLifecycleOwner) { (userId, _) ->
                viewModel.getUserById(userId)

            }

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
                        if (data != "Avatar user berhasil diperbarui") {
                            Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
                        }
                        if (data == "Data user berhasil diperbarui") {
                            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                            findNavController().navigateUp()
                        }
                    }
                }
            }

            getUser()

            btnBack.setOnClickListener {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                findNavController().navigateUp()
            }

            bgEditAvatar.setOnClickListener {
                showAvatarDialog()
            }
        }

    }

    private fun getUser() {
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
                    binding.apply {
                        etName.setText(data.username)
                        etEmail.setText(data.email)
                        etEmail.isEnabled = false
                        etPassword.setText(data.password)
                        Glide.with(requireActivity())
                            .load(data.avatarUrl)
                            .into(ivProfile)

                        btnSave.setOnClickListener {
                            val username = etName.text.toString()
                            val email = etEmail.text.toString()
                            val password = etPassword.text.toString()
                            val confPassword = etConfPassword.text.toString()
//                            val avatarUrl = ivProfile

                            when{
                                username.isEmpty() -> {
                                    txtLayName.error = "Mohon masukkan nama anda"
                                }
//                                email.isEmpty() -> {
//                                    txtLayEmail.error = "Mohon masukkan email"
//                                }
//                                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
//                                    txtLayEmail.error = "Mohon masukkan email dengan benar"
//                                }
                                password.isEmpty() -> {
                                    txtLayPassword.error = "Mohon masukkan password"
                                }
                                password.length<6 -> {
                                    txtLayPassword.error = "Password minimal 6 karakter"
                                }
                                confPassword.isEmpty() -> {
                                    txtLayConfPassword.error = "Mohon masukkan password kembali"
                                }
//                                confPassword.length<6 -> {
//                                    txtLayConfPassword.error = "Password minimal 6 karakter"
//                                }
                                password!=confPassword -> {
                                    txtLayConfPassword.error = "Konfirmasi password tidak sesuai"
                                }
                                else -> {
                                    viewModel.updateUser(
                                        User(
                                            id = data.id,
                                            username = username,
                                            email = email,
                                            password = password,
                                            avatarUrl = data.avatarUrl

                                        )
                                    )
                                }
                            }
                        }
                    }



                }
            }
        }
    }

    private fun showAvatarDialog() {
         DialogAvatarFragment(
             onClickItem = { avatar ->
                 Glide.with(requireActivity())
                     .load(avatar)
                     .into(binding.ivProfile)
             }

         ).show(parentFragmentManager, "DialogAvatar")
    }

    override fun onResume() {
        super.onResume()
        getUser()
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