package com.dilan.kamuda.customerapp.views.activities.main

import android.os.Bundle
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
import com.dilan.kamuda.customerapp.util.KamuDaSecurePreference
import com.dilan.kamuda.customerapp.viewmodels.main.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ActBase() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    var latestOrderDetail: OrderDetail? = null

    companion object {
        var kamuDaSecurePreference = KamuDaSecurePreference()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel.showLoader.observe(this, Observer {
            showProgress(it)
        })

        viewModel.latestOrder.observe(this, Observer {
            latestOrderDetail = it
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

        //getLatestOrder()
    }

    fun getLatestOrder() {
        viewModel.getLatestOrderOfCustomer(kamuDaSecurePreference.getCustomerID(this).toInt())
    }
}