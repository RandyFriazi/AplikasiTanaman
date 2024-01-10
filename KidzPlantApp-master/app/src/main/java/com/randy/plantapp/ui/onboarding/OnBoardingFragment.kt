package com.randy.plantapp.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.randy.plantapp.R
import com.randy.plantapp.databinding.FragmentOnBoardingBinding
import com.randy.plantapp.model.onBoardingItem
import com.randy.plantapp.preferenes.UserPrefViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private lateinit var onBoardingAdapter: OnBoardingAdapter

    private val userPrefViewModel: UserPrefViewModel by viewModels()

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        binding.apply {

            btnLeft.setOnClickListener {
                position = if (position > 0) position - 1 else position
                vpOnBoard.setCurrentItem(position, true)
            }

            btnRight.setOnClickListener {
//                if (position == onBoardingItem.size - 1) {
//                    Log.i("ONBOARDINGGG", "LAST")
//                    userPrefViewModel.saveOnBoardState(true)
//                    val action =
//                        OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment()
//                    findNavController().navigate(action)
//                }
                position = if (position < onBoardingItem.size - 1) position + 1 else position
                vpOnBoard.setCurrentItem(position, true)
            }

            binding.btnLogin.setOnClickListener {
                val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment()
                findNavController().navigate(action)
            }

            binding.btnDaftar.setOnClickListener {
                val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToSignUpFragment()
                findNavController().navigate(action)
            }

            vpOnBoard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(pos: Int) {
                    super.onPageSelected(pos)
                    val positionBefore = position
                    position = pos
                    val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
                    val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
                    binding.btnLeft.visibility = if (position > 0) {
                        if (positionBefore == 0) {
                            binding.btnLeft.startAnimation(fadeIn)
                        }
                        View.VISIBLE
                    } else {
                        binding.btnLeft.startAnimation(fadeOut)
                        View.INVISIBLE
                    }

                    binding.btnRight.visibility = if (position < onBoardingItem.size - 1) {
                        if (positionBefore == onBoardingItem.size - 1) {
                            binding.btnRight.startAnimation(fadeIn)
                        }
                        View.VISIBLE
                    } else {
                        binding.btnRight.startAnimation(fadeOut)
                        View.INVISIBLE
                    }
                }
            })

        }

    }

    private fun setUpRecyclerView() {
        onBoardingAdapter = OnBoardingAdapter(
            list = onBoardingItem
        )
        binding.apply {
            vpOnBoard.adapter = onBoardingAdapter
            vpOnBoard.offscreenPageLimit = onBoardingItem.size
            vpOnBoard.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            wormDot.attachTo(vpOnBoard)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}