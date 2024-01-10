package com.randy.plantapp.ui.detail

import android.app.ActionBar
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.randy.plantapp.R
import com.randy.plantapp.databinding.FragmentDetailSubPlantBinding

class DetailSubPlantFragment : Fragment() {

    private var _binding: FragmentDetailSubPlantBinding? = null
    private val binding get() = _binding!!

    private val args: DetailSubPlantFragmentArgs by navArgs()

    private var player: ExoPlayer? = null
    private var isFullscreen = false
    private lateinit var fullScreenButton: ImageView
    private lateinit var params: ConstraintLayout.LayoutParams
    private lateinit var parentParams: ConstraintLayout.LayoutParams
    private lateinit var mediaPlayer: MediaPlayer
    private var displayDesinty: Float = 0f

//    private val audioUrl =
//        "https://randypriatma.000webhostapp.com/sound/Audio%20Bunga%20Lavender.mp3"
    private var isPlaying = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailSubPlantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer()

//        tts = TextToSpeech(requireContext(), this@DetailSubPlantFragment)

        val title = args.title

        val subPlant = args.subPlant

        fullScreenButton =
            binding.exoPlayerView.findViewById(R.id.exo_fullscreen_icon)
        params = binding.cvExoPlayer.layoutParams as ConstraintLayout.LayoutParams
        parentParams = binding.layContent.layoutParams as ConstraintLayout.LayoutParams
        displayDesinty = requireContext().resources.displayMetrics.density
        initializePlayer(subPlant.videoUrl)

        activity?.onBackPressedDispatcher?.addCallback {
            if (isFullscreen) {
                closeFullScreen()
            } else {
                findNavController().navigateUp()
            }
        }

        binding.apply {
            tvAppBar.text = "Macam $title"

            Glide.with(requireActivity())
                .load(subPlant.imageUrl1)
                .into(ivPlant1)
            Glide.with(requireActivity())
                .load(subPlant.imageUrl2)
                .into(ivPlant2)
            Glide.with(requireActivity())
                .load(subPlant.imageUrl3)
                .into(ivPlant3)

            cvImage1.setBackgroundResource(R.drawable.bg_card_white)
            cvImage2.setBackgroundResource(R.drawable.bg_card_white)
            cvImage3.setBackgroundResource(R.drawable.bg_card_white)

            Glide.with(requireActivity())
                .load(subPlant.iconUrl)
                .into(icSubPlant)

            cvImage1.setOnClickListener {
                openDialogImage(subPlant.imageUrl1)
            }

            cvImage2.setOnClickListener {
                openDialogImage(subPlant.imageUrl2)
            }

            cvImage3.setOnClickListener {
                openDialogImage(subPlant.imageUrl3)
            }

            tvTitleSubPlant.text = subPlant.title
            tvListBenefit.text = subPlant.benefit

            mediaPlayer.setOnPreparedListener {
                // Audio siap untuk diputar
//                println("Audio dimulai")
                isPlaying = true
                mediaPlayer.start()
            }

            mediaPlayer.setOnCompletionListener {
                // Audio selesai diputar
//                println("Audio selesai")
                icSpeaker.setImageResource(R.drawable.ic_speaker)
                isPlaying = false
            }

//            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
//                override fun onStart(utteranceId: String?) {
//                    Log.i("TTSPLANT", "onStart: true")
//                }
//
//                override fun onStop(utteranceId: String?, interrupted: Boolean) {
//                    super.onStop(utteranceId, interrupted)
//                    Log.i("TTSPLANT", "onStop: true")
//                }
//
//                override fun onDone(utteranceId: String?) {
//                    Log.i("TTSPLANT", "onDone: true")
//                    icSpeaker.setImageResource(R.drawable.ic_speaker)
//                }
//
//                override fun onError(utteranceId: String?) {
//                }
//
//            })

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            icSpeaker.setOnClickListener {
                if (!isPlaying){
                    icSpeaker.setImageResource(R.drawable.ic_stop)
                    mediaPlayer.apply {
                        reset()
                        setDataSource(subPlant.audioUrl)
                        prepareAsync()
                    }
                } else {
                    icSpeaker.setImageResource(R.drawable.ic_speaker)
                    mediaPlayer.stop()
                    isPlaying = false
                }
//                if (mediaPlayer.isPlaying) {
//                    try {
//                        mediaPlayer.setDataSource(audioUrl)
//                        mediaPlayer.prepareAsync()
//                        mediaPlayer.setOnPreparedListener {
//                            // Audio siap untuk diputar
//                            mediaPlayer.start()
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                } else {
//                    mediaPlayer.release()
//                    icSpeaker.setImageResource(R.drawable.ic_stop)
//                }
//                if (tts.isSpeaking) {
//                    tts.stop()
//                    icSpeaker.setImageResource(R.drawable.ic_speaker)
//                } else {
//                    speak(subPlant)
//                    icSpeaker.setImageResource(R.drawable.ic_stop)
//                }
            }
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
        params.width = ActionBar.LayoutParams.MATCH_PARENT
        params.height = ActionBar.LayoutParams.MATCH_PARENT
        params.setMargins(0, 0, 0, 0)
        parentParams.setMargins(0, 0, 0, 0)
        binding.apply {
            cvExoPlayer.layoutParams = params
            layContent.layoutParams = parentParams
            layContent.setBackgroundResource(R.color.black)

            icSubPlant.visibility = View.GONE
            tvTitleSubPlant.visibility = View.GONE
            tvVideo.visibility = View.GONE
            tvImage.visibility = View.GONE
            cvImage1.visibility = View.GONE
            cvImage2.visibility = View.GONE
            cvImage3.visibility = View.GONE
            tvBenefit.visibility = View.GONE
            tvListBenefit.visibility = View.GONE
            appbar.visibility = View.GONE
        }

        isFullscreen = true
    }

    private fun closeFullScreen() {
        fullScreenButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),
            R.drawable.ic_fullscreen_open))

        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        params.width = ActionBar.LayoutParams.MATCH_PARENT
        params.height = (180 * displayDesinty).toInt()
        params.setMargins((20 * displayDesinty).toInt())
        parentParams.setMargins((20 * displayDesinty).toInt())
        binding.apply {
            cvExoPlayer.layoutParams = params
            layContent.layoutParams = parentParams
            layContent.setBackgroundResource(R.drawable.bg_card_cream)


            icSubPlant.visibility = View.VISIBLE
            tvTitleSubPlant.visibility = View.VISIBLE
            tvVideo.visibility = View.VISIBLE
            tvImage.visibility = View.VISIBLE
            cvImage1.visibility = View.VISIBLE
            cvImage2.visibility = View.VISIBLE
            cvImage3.visibility = View.VISIBLE
            tvBenefit.visibility = View.VISIBLE
            tvListBenefit.visibility = View.VISIBLE
            appbar.visibility = View.VISIBLE
        }

        isFullscreen = false
    }

    private fun openDialogImage(imageUrl: String) {
        DialogDetailImageFragment(
            imageUrl = imageUrl
        ).show(parentFragmentManager, "DialogImage")
    }


//    override fun onInit(status: Int) {
//        if (status == TextToSpeech.SUCCESS) {
//            val result = tts.setLanguage(Locale("id"))
//            checkEngine()
//
//            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                Toast.makeText(
//                    requireContext(),
//                    "The Languange Specified is not Supported!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        } else {
//            Toast.makeText(requireContext(), "Initilization Failed!", Toast.LENGTH_SHORT).show()
//        }
//    }

//    private fun speak(subPlant: SubPlant) {
//        val benefitTextFiltered = StringUtil.filterText(subPlant.benefit)
//
//        val text = "Manfaat ${subPlant.title} yaitu sebagai berikut. $benefitTextFiltered"
//
//        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
//    }

//    private fun checkEngine() {
//        if (!tts.defaultEngine.equals("com.google.android.tts")) {
//            Toast.makeText(
//                requireContext(),
//                "Mohon menginstal dan mengatur "
//                        + "Google TexttoSpeech sebagai default untuk kualitas lebih baik",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//        tts.setSpeechRate(0.7f)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        player?.stop()
        mediaPlayer.stop()
//        tts.stop()
//        tts.shutdown()
        _binding = null
    }

}