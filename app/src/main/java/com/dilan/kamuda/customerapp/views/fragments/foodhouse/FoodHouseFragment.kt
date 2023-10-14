package com.dilan.kamuda.customerapp.views.fragments.foodhouse

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.databinding.FragmentFoodHouseBinding
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.model.specific.KamuDaPopup
import com.dilan.kamuda.customerapp.util.KamuDaSecurePreference
import com.dilan.kamuda.customerapp.viewmodels.foodhouse.FoodHouseViewModel
import com.dilan.kamuda.customerapp.views.activities.main.MainActivity
import com.dilan.kamuda.customerapp.views.adapters.ViewAllMealsAdapter
import com.dilan.kamuda.customerapp.views.adapters.ViewOrderedItemsAdapter
import com.google.android.material.divider.MaterialDivider
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class FoodHouseFragment : Fragment() {

    private lateinit var viewModel: FoodHouseViewModel
    private lateinit var binding: FragmentFoodHouseBinding
    private lateinit var adapter: ViewAllMealsAdapter
    private var latestOrderDetail: OrderDetail? = null
    private lateinit var mainActivity: MainActivity

    private val REQUEST_CALL_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodHouseBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[FoodHouseViewModel::class.java]
        binding.foodHouseVM = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = ViewAllMealsAdapter(this, object :
            ViewAllMealsAdapter.OnItemClickListener {

            override fun itemClick(item: FoodMenu) {

            }
        })

        binding.rvMeals.also {
            it.layoutManager = _layoutManager
            it.adapter = adapter
        }

        binding.imageCall.setOnClickListener {
            makePhoneCall()
        }

        viewModel.latestOrder.observe(viewLifecycleOwner) {
            latestOrderDetail = it
            manageLatestOrder()
        }

        viewModel.menuList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.showLoader.observe(viewLifecycleOwner) {
            mainActivity.showProgress(it)
        }

        viewModel.showErrorPopup.observe(viewLifecycleOwner) {
            showErrorPopup(it)
        }
    }

    /***
     * Make a phone call
     */
    private fun makePhoneCall() {

        val phoneNumber = kamuDaSecurePreference.getFoodHouseHotline(requireContext())

        // Check for CALL_PHONE permission
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(permission.CALL_PHONE),
                REQUEST_CALL_PERMISSION
            )
        } else {
            // Permission is granted, make the phone call
            initiatePhoneCall(phoneNumber)
        }
    }

    private fun initiatePhoneCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")

        try {
            startActivity(callIntent)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, make the phone call
                makePhoneCall()
            } else {
                // Permission denied, notify the user
                Toast.makeText(
                    requireContext(),
                    "Permission denied. Can't make a call.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /***
     * Manage the latest order of tthe customer
     */
    private fun manageLatestOrder() {
        if (latestOrderDetail == null) {
            binding.lytLatestOrderLoading.visibility = GONE
            binding.lytLatestOrderNot.visibility = VISIBLE
            binding.lytLatestOrder.visibility = GONE
        } else {
            binding.lytLatestOrder.findViewById<LinearLayout>(R.id.lytBtnActions).visibility = GONE
            binding.lytLatestOrderLoading.visibility = GONE
            binding.lytLatestOrderNot.visibility = GONE
            binding.lytLatestOrder.visibility = VISIBLE

            SimpleDateFormat("yyyy MMM dd").format(
                SimpleDateFormat("yyyy-MM-dd").parse(
                    latestOrderDetail!!.date
                )
            )
                .also {
                    binding.lytLatestOrder.findViewById<MaterialTextView>(R.id.mtvOrderDate).text =
                        it
                }
            binding.lytLatestOrder.findViewById<MaterialTextView>(R.id.mtvOrderTime).text =
                latestOrderDetail!!.createdAt
            binding.lytLatestOrder.findViewById<MaterialTextView>(R.id.mtvOrderTotal).text =
                "LKR ${latestOrderDetail!!.total}"
            binding.lytLatestOrder.findViewById<MaterialTextView>(R.id.mtvOrderedItemCount).text =
                "${latestOrderDetail!!.items.size} Items"
            binding.lytLatestOrder.findViewById<TextView>(R.id.tvOrderStatus).text =
                "${latestOrderDetail!!.status.uppercase()}"

            when (latestOrderDetail!!.status) {
                "pending" -> {
                    binding.lytLatestOrder.findViewById<MaterialDivider>(R.id.verticalDivider).dividerColor =
                        ContextCompat.getColor(
                            binding.lytLatestOrder.context,
                            R.color.yellow
                        )

                }

                else -> {
                    binding.lytLatestOrder.findViewById<MaterialDivider>(R.id.verticalDivider).dividerColor =
                        ContextCompat.getColor(
                            binding.lytLatestOrder.context,
                            R.color.green
                        )
                }
            }

            when (latestOrderDetail!!.status) {
                "completed" -> {
                    binding.lytLatestOrder.findViewById<MaterialDivider>(R.id.verticalDivider).dividerColor =
                        ContextCompat.getColor(
                            binding.lytLatestOrder.context,
                            R.color.black
                        )
                }

                "rejected" -> {
                    binding.lytLatestOrder.findViewById<MaterialDivider>(R.id.verticalDivider).dividerColor =
                        ContextCompat.getColor(
                            binding.lytLatestOrder.context,
                            R.color.redish
                        )
                }

                "cancelled" -> {
                    binding.lytLatestOrder.findViewById<MaterialDivider>(R.id.verticalDivider).dividerColor =
                        ContextCompat.getColor(
                            binding.lytLatestOrder.context,
                            R.color.grey
                        )
                }
            }

            // Set up child RecyclerView
            val childAdapter = ViewOrderedItemsAdapter()
            binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).layoutManager =
                LinearLayoutManager(binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).context) // Set layout manager
            binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems)
                .addItemDecoration(
                    DividerItemDecoration(
                        binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).context,
                        (binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).layoutManager as LinearLayoutManager).orientation
                    )
                )
            binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).adapter =
                childAdapter
            childAdapter.submitList(latestOrderDetail!!.items)

            binding.lytLatestOrder.findViewById<RelativeLayout>(R.id.lytBtnToggle)
                .setOnClickListener {
                    if (binding.lytLatestOrder.findViewById<ImageView>(R.id.btnArrowDown).visibility == VISIBLE) {
                        binding.lytLatestOrder.findViewById<ImageView>(R.id.btnArrowDown).visibility =
                            GONE
                        binding.lytLatestOrder.findViewById<ImageView>(R.id.btnArrowUp).visibility =
                            VISIBLE
                        binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).visibility =
                            VISIBLE
                    } else {
                        binding.lytLatestOrder.findViewById<ImageView>(R.id.btnArrowUp).visibility =
                            GONE
                        binding.lytLatestOrder.findViewById<ImageView>(R.id.btnArrowDown).visibility =
                            VISIBLE
                        binding.lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems).visibility =
                            GONE
                    }
                }

        }
    }

    override fun onResume() {
        super.onResume()
        context?.let { kamuDaSecurePreference.getCustomerID(it).toInt() }
            ?.let { viewModel.getLatestOrderOfCustomer(it) }
    }

    private fun showErrorPopup(kamuDaPopup: KamuDaPopup) {
        mainActivity.showErrorPopup(kamuDaPopup).show(childFragmentManager, "custom_dialog")
    }

    companion object {
        var kamuDaSecurePreference = KamuDaSecurePreference()
    }
}