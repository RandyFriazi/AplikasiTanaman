package com.randy.plantapp.ui.splashscreen

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.randy.plantapp.R
import com.randy.plantapp.preferenes.UserPrefViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private val userPrefViewModel: UserPrefViewModel by viewModels()

    private val splashDelay: Long = 2000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            userPrefViewModel.getUserInfo()
                .observe(viewLifecycleOwner) { (_, stateLogin, stateOnBoard) ->
                    val action = if (stateLogin) {
                        SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment()
                    } else {
                        if (stateOnBoard) {
                            SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
                        } else {
                            SplashScreenFragmentDirections.actionSplashScreenFragmentToOnBoardingFragment()
                        }
                    }
                    findNavController().navigate(action)
                }
        }, splashDelay)

    }


}