package com.randy.plantapp.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.randy.plantapp.R
import com.randy.plantapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // StatusBarColor
                statusBarColor = ContextCompat.getColor(context, R.color.white)

                // NavigationBarColor (jika ingin mengubah warna navigasi)
                navigationBarColor = ContextCompat.getColor(context, R.color.white)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        }

        val rootDestinationId = listOf(
            R.id.homeFragment,
            R.id.quizFragment,
            R.id.profileFragment
        )

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController

        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_nav_menu)
        val menu = popupMenu.menu
        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.setupWithNavController(menu, navController)
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            if (destination.id == R.id.homeFragment) {
                navController.graph.setStartDestination(R.id.homeFragment)
            }
            if (destination.id in rootDestinationId) {
                bottomNavigation.visibility = View.VISIBLE
            } else {
                bottomNavigation.visibility = View.GONE
            }
        }

    }
}