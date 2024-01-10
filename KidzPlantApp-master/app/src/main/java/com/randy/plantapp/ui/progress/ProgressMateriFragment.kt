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
import com.randy.plantapp.databinding.FragmentProgressMateriBinding
import com.randy.plantapp.preferenes.UserPrefViewModel
import com.randy.plantapp.utils.UiState
import com.randy.plantapp.utils.registerOnBackPressHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProgressMateriFragment : Fragment() {

    private lateinit var listProgressMateriAdapter: ListProgressMateriAdapter

    private val viewModel: ProgressViewModel by viewModels()
    private val userPrefViewModel: UserPrefViewModel by viewModels()

    private var _binding: FragmentProgressMateriBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProgressMateriBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPrefViewModel.getUserInfo().observe(viewLifecycleOwner){ (userId, _) ->
            viewModel.getPlants(userId)
        }

        registerOnBackPressHandler()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
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
                    setupRecyclerView()
                    val data = uiState.data
                    Log.i("PLANTTTT", "onViewCreated: $data")
                    listProgressMateriAdapter.submitList(data)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        listProgressMateriAdapter = ListProgressMateriAdapter(
            onClick = { plant ->
                val action =
                    ProgressMateriFragmentDirections.actionProgressMateriFragmentToDetailPlantFragment(
                        plant)
                findNavController().navigate(action)
            }
        )
        binding.apply {
            rvProgressMateri.setHasFixedSize(true)
            rvProgressMateri.layoutManager = LinearLayoutManager(requireContext())
            rvProgressMateri.adapter = listProgressMateriAdapter
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