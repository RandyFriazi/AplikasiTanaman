package com.randy.plantapp.ui.detail

import android.app.ActionBar.LayoutParams
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.randy.plantapp.R
import com.randy.plantapp.databinding.FragmentDetailPlantBinding
import com.randy.plantapp.preferenes.UserPrefViewModel
import com.randy.plantapp.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DetailPlantFragment : Fragment() {

    private lateinit var listDetailPlantAdapter: ListDetailPlantAdapter

    private val viewModel: DetailPlantViewModel by viewModels()
    private val userPrefViewModel: UserPrefViewModel by viewModels()

    private var _binding: FragmentDetailPlantBinding? = null
    private val binding get() = _binding!!

    private val args: DetailPlantFragmentArgs by navArgs()

    private var player: ExoPlayer? = null

    private var isFullscreen = false

    private lateinit var fullScreenButton: ImageView
    private lateinit var playVideoButton: ImageView
    private lateinit var params: ConstraintLayout.LayoutParams
    private var displayDesinty: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailPlantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val plant = args.plant

        getSubPlants(plant.id)

        binding.apply {
            tvAppBar.text = plant.title
            val text = plant.title.lowercase(Locale.getDefault())
            tvSubTitle.text = getString(R.string.sub_title_plant, text, text)
            tvKindOfPlant.text = getString(R.string.kind_of_plant, text)
            cvExoPlayer.setBackgroundResource(R.color.black)
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        playVideoButton = binding.exoPlayerView.findViewById(R.id.exo_play)
        fullScreenButton = binding.exoPlayerView.findViewById(R.id.exo_fullscreen_icon)
        params = binding.cvExoPlayer.layoutParams as ConstraintLayout.LayoutParams
        displayDesinty = requireContext().resources.displayMetrics.density
        initializePlayer(plant.videoUrl)
        playVideoButton.setOnClickListener {
            player?.play()
            userPrefViewModel.getUserInfo().observe(viewLifecycleOwner) { (userId, _) ->
                viewModel.setProgressMateriBySubPlantId(userId, plant.id, 5, true)
            }
            getSubPlants(plant.id)
        }

        viewModel.detailPlant.observe(viewLifecycleOwner){ uiState ->
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
                    if (data.videoIsCompleted){
                        binding.icCheckVideo.visibility = View.VISIBLE
                    }
                    listDetailPlantAdapter.submitList(data.subPlants)
                    Log.i("DETAILPLANT", "onViewCreated: $data")
                }
            }
        }

//        binding.apply {
//            tvAppBar.text = plant.title
//            val text = plant.title.lowercase(Locale.getDefault())
//            tvSubTitle.text = getString(R.string.sub_title_plant, text, text)
//            tvKindOfPlant.text = getString(R.string.kind_of_plant, text)
//            cvExoPlayer.setBackgroundResource(R.color.black)
//            btnBack.setOnClickListener {
//                findNavController().navigateUp()
//            }
//        }

        activity?.onBackPressedDispatcher?.addCallback{
            if (isFullscreen) {
                closeFullScreen()
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun getSubPlants(plantId: Int) {
        userPrefViewModel.getUserInfo().observe(viewLifecycleOwner){ (userId, _) ->
            viewModel.getSubPlantsById(userId, plantId)
        }
    }

    private fun setupRecyclerView() {
        listDetailPlantAdapter = ListDetailPlantAdapter(
            onClick = { subPlant ->
                userPrefViewModel.getUserInfo().observe(viewLifecycleOwner){ (userId, _) ->
                    viewModel.setProgressMateriBySubPlantId(userId, subPlant.plantId, subPlant.subPlantId, true)
                }
                val action =
                    DetailPlantFragmentDirections.actionDetailPlantFragmentToDetailSubPlantFragment(
                        subPlant,
                        binding.tvAppBar.text.toString().lowercase(Locale.getDefault()))
                findNavController().navigate(action)
            }
        )
//        listDetailPlantAdapter.submitList(previewOrnamentalPlants)
        binding.rvSubPlant.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = listDetailPlantAdapter
        }
    }

    private fun initializePlayer(videoUrl: String) {
        player = ExoPlayer.Builder(requireContext())
            .setSeekBackIncrementMs(10000)
            .setSeekForwardIncrementMs(10000)
            .build()

        val mediaItem = MediaItem.Builder()
            .setUri(videoUrl)
            .setMimeType(MimeTypes.APPLICATION_MP4)
            .build()

        val mediaSource = ProgressiveMediaSource.Factory(
            DefaultDataSource.Factory(requireContext())
        ).createMediaSource(mediaItem)

        player!!.addListener(object : Player.Listener {
            @Deprecated("Deprecated in Java")
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_READY) {
                    binding.tvDuration.visibility = View.VISIBLE
                    val minute = (player!!.duration / 1000) / 60
                    val sec = (player!!.duration / 1000)
                    binding.tvDuration.text = if (minute.toInt()==0) "$sec detik" else "$minute menit"
                }
            }
        })

        binding.tvDuration.setOnClickListener {
            Toast.makeText(requireContext(),
                "${(player!!.duration / 1000) / 60} menit",
                Toast.LENGTH_SHORT)
                .show()
        }



        fullScreenButton.setOnClickListener {
            if (isFullscreen) {
                closeFullScreen()
            } else {
                openFullScreen()

            }
        }

        player?.apply {
            setPlaybackSpeed(1f)
            setMediaSource(mediaSource)
            seekTo(0, 0L)
            prepare()
        }.also {
            binding.exoPlayerView.player = it
        }
    }

    private fun openFullScreen() {
        fullScreenButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),
            R.drawable.ic_fullscreen_close))

        activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        params.width = LayoutParams.MATCH_PARENT
        params.height = LayoutParams.MATCH_PARENT
        params.setMargins(0, 0, 0, 0)
        binding.apply {
            cvExoPlayer.layoutParams = params

            appbar.visibility = View.GONE
            contentLayout.visibility = View.GONE
        }

        isFullscreen = true
    }

    private fun closeFullScreen() {
        fullScreenButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),
            R.drawable.ic_fullscreen_open))

        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        params.width = LayoutParams.MATCH_PARENT
        params.height = (180 * displayDesinty).toInt()
        params.setMargins((20 * displayDesinty).toInt())
        binding.apply {
            cvExoPlayer.layoutParams = params
            appbar.visibility = View.VISIBLE
            contentLayout.visibility = View.VISIBLE
        }

        isFullscreen = false
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player?.stop()
        _binding = null
    }

}