package com.dilan.kamuda.customerapp.views.activities.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dilan.kamuda.customerapp.ActBase
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.databinding.ActivityMainBinding
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.viewmodels.main.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ActBase() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    var latestOrderDetail: OrderDetail? = null

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        kamuDaSecurePreference.clearSharedPrefKeys(this)

        binding.lytCommonErrorScreenIncluded.findViewById<MaterialButton>(R.id.mbtnCommonErrorScreen)
            .setOnClickListener {
                binding.navView.visibility = View.VISIBLE
                viewModel.getFoodHouseDetails()
                binding.lytCommonErrorScreenIncluded.visibility = View.GONE
            }

        viewModel.showLoader.observe(this, Observer {
            showProgress(it)
        })

        viewModel.showErrorPage.observe(this) {
            if (it) {
                showCommonErrorScreen()
            }
        }

        viewModel.foodHouseDetail.observe(this, Observer {

        })

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.foodHouseFragment2, R.id.viewOrderFragment2
            )
        )

        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    private fun showCommonErrorScreen() {
        binding.navView.visibility = View.GONE
        binding.lytCommonErrorScreenIncluded.visibility = View.VISIBLE
    }
}