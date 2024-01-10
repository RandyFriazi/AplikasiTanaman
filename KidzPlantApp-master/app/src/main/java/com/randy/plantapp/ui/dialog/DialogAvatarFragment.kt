package com.randy.plantapp.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.randy.plantapp.databinding.FragmentDialogAvatarBinding
import com.randy.plantapp.preferenes.UserPrefViewModel
import com.randy.plantapp.ui.profile.edit.EditProfileViewModel
import com.randy.plantapp.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogAvatarFragment(
    private val onClickItem: (avatar: String) -> Unit,
) : DialogFragment() {

    private lateinit var listAvatarAdapter: ListAvatarAdapter

    private val viewModel: EditProfileViewModel by viewModels()
    private val userPrefViewModel: UserPrefViewModel by viewModels()

    private var _binding: FragmentDialogAvatarBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()

        dialog?.window?.apply {
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                width,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDialogAvatarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listAvatar.observe(viewLifecycleOwner) { uiState ->
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
                    setupRecyclerView()
                    listAvatarAdapter.submitList(data)
                }
            }
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
                    Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
                    if (data == "Avatar user berhasil diperbarui") {
                        dismiss()
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        listAvatarAdapter = ListAvatarAdapter(
            onClick = { avatar ->
                onClickItem(avatar.avatarUrl)
                userPrefViewModel.getUserInfo().observe(viewLifecycleOwner) { (userId, _) ->
                    viewModel.getUserById(userId)
                    viewModel.updateAvatarUser(userId, avatar.avatarUrl)
                }
            }
        )
        binding.rvAvatar.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = listAvatarAdapter
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