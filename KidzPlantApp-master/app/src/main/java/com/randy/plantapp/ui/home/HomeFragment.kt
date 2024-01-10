package com.randy.plantapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.randy.plantapp.databinding.FragmentHomeBinding
import com.randy.plantapp.preferenes.UserPrefViewModel
import com.randy.plantapp.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var listPlantAdapter: ListPlantAdapter

    private val viewModel: HomeViewModel by viewModels()
    private val userPrefViewModel: UserPrefViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPrefViewModel.getUserInfo().observe(viewLifecycleOwner) { (userId, _) ->
            viewModel.getUserById(userId)
            viewModel.getPlants(userId)
        }

        viewModel.listPlant.observe(viewLifecycleOwner) { uiState ->
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

                    Log.i("PLANTTTT", "onViewCreated: $data")
                    listPlantAdapter.submitList(data)
                }
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
                        tvName.text = data.username
                        ivProfile.visibility = View.VISIBLE
                        Glide.with(requireActivity())
                            .load(data.avatarUrl)
                            .into(ivProfile)
                    }
                }
            }

        }
    }

    private fun setupRecyclerView() {
        listPlantAdapter = ListPlantAdapter(
            onClick = { plant ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailPlantFragment(plant)
                findNavController().navigate(action)
            }
        )
        binding.apply {
            rvPlant.setHasFixedSize(true)
            rvPlant.layoutManager = LinearLayoutManager(requireContext())
            rvPlant.adapter = listPlantAdapter
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