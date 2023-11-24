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
import com.dilan.kamuda.customerapp.ActBase.Companion.kamuDaSecurePreference
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.databinding.FragmentFoodHouseBinding
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.model.specific.KamuDaPopup
import com.dilan.kamuda.customerapp.viewmodels.foodhouse.FoodHouseViewModel
import com.dilan.kamuda.customerapp.views.activities.LoginActivity
import com.dilan.kamuda.customerapp.views.activities.main.MainActivity
import com.dilan.kamuda.customerapp.views.adapters.ViewAllMealsAdapter
import com.dilan.kamuda.customerapp.views.adapters.ViewOrderedItemsAdapter
import com.google.android.material.button.MaterialButton
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

    override fun onResume() {
        super.onResume()
        viewModel.getMenuListForAll()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        binding.lytCommonErrorScreenIncluded.findViewById<MaterialButton>(R.id.mbtnCommonErrorScreen)
            .setOnClickListener {
                mainActivity.binding.navView.visibility = VISIBLE
                viewModel.getMenuListForAll()
                binding.lytCommonErrorScreenIncluded.visibility = GONE
            }

        binding.rvMeals.also {
            it.layoutManager = _layoutManager
            it.adapter = adapter
        }

        binding.imageCall.setOnClickListener {
            makePhoneCall()
        }

        binding.imageLogout.setOnClickListener {
            logOutFromApp()
        }

        viewModel.latestOrder.observe(viewLifecycleOwner) {
            latestOrderDetail = it
            manageLatestOrder()
        }

        viewModel.menuList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.showLoader.observe(viewLifecycleOwner) {
            if (it) {
                mainActivity.binding.navView.visibility = GONE
            } else {
                mainActivity.binding.navView.visibility = VISIBLE
            }
            mainActivity.showProgress(it)
        }

        viewModel.showErrorPopup.observe(viewLifecycleOwner) {
            showErrorPopup(it)
        }

        viewModel.showErrorPage.observe(viewLifecycleOwner) {
            if (it) {
                showCommonErrorScreen()
            }
        }

        viewModel.successfulRetrieve.observe(viewLifecycleOwner) { isSuccessful ->
            if (isSuccessful) {
                context?.let { kamuDaSecurePreference.getCustomerID(it).toInt() }
                    ?.let { viewModel.getLatestOrderOfCustomer(it) }
            }
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
                    getString(R.string.permission_denied_cannot_make_a_call),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun logOutFromApp() {
        kamuDaSecurePreference.clearSharedPrefKeys(requireContext())
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    /***
     * Manage the latest order of the customer
     */
    private fun manageLatestOrder() {
        latestOrderDetail?.let {
            with(binding) {
                lytLatestOrder.findViewById<LinearLayout>(R.id.lytBtnActions).visibility = GONE
                lytLatestOrderLoading.visibility = GONE
                lytLatestOrderNot.visibility = GONE
                lytLatestOrder.visibility = VISIBLE

                val orderDateTextView =
                    lytLatestOrder.findViewById<MaterialTextView>(R.id.mtvOrderDate)
                val orderTimeTextView =
                    lytLatestOrder.findViewById<MaterialTextView>(R.id.mtvOrderTime)
                val orderTotalTextView =
                    lytLatestOrder.findViewById<MaterialTextView>(R.id.mtvOrderTotal)
                val itemCountTextView =
                    lytLatestOrder.findViewById<MaterialTextView>(R.id.mtvOrderedItemCount)
                val orderStatusTextView = lytLatestOrder.findViewById<TextView>(R.id.tvOrderStatus)
                val verticalDivider =
                    lytLatestOrder.findViewById<MaterialDivider>(R.id.verticalDivider)
                val recyclerView = lytLatestOrder.findViewById<RecyclerView>(R.id.rvViewOrderItems)
                val arrowDownImageView = lytLatestOrder.findViewById<ImageView>(R.id.btnArrowDown)
                val arrowUpImageView = lytLatestOrder.findViewById<ImageView>(R.id.btnArrowUp)

                // Set up order details
                SimpleDateFormat("yyyy-MM-dd").parse(it.date)?.let { parsedDate ->
                    val formattedDate = SimpleDateFormat("yyyy MMM dd").format(parsedDate)
                    orderDateTextView.text = formattedDate
                }

                orderTimeTextView.text = it.createdAt
                orderTotalTextView.text = getString(
                    R.string.order_total_format,
                    getString(R.string.lkr),
                    it.total.toString() ?: 0.0
                )
                itemCountTextView.text =
                    resources.getQuantityString(R.plurals.item_count, it.items.size, it.items.size)
                orderStatusTextView.text = it.status.uppercase()

                // Set up vertical divider color based on order status
                verticalDivider.dividerColor = when (it.status) {
                    "pending" -> ContextCompat.getColor(root.context, R.color.yellow)
                    "completed" -> ContextCompat.getColor(root.context, R.color.black)
                    "rejected" -> ContextCompat.getColor(root.context, R.color.redish)
                    "cancelled" -> ContextCompat.getColor(root.context, R.color.grey)
                    else -> ContextCompat.getColor(root.context, R.color.white)
                }

                // Set up child RecyclerView
                val childAdapter = ViewOrderedItemsAdapter()
                recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
                recyclerView.addItemDecoration(
                    DividerItemDecoration(
                        recyclerView.context,
                        RecyclerView.VERTICAL
                    )
                )
                recyclerView.adapter = childAdapter
                childAdapter.submitList(it.items)

                // Set up click listener for toggle button
                lytLatestOrder.findViewById<RelativeLayout>(R.id.lytBtnToggle).setOnClickListener {
                    arrowDownImageView.visibility =
                        if (arrowDownImageView.visibility == VISIBLE) GONE else VISIBLE
                    arrowUpImageView.visibility =
                        if (arrowUpImageView.visibility == VISIBLE) GONE else VISIBLE
                    recyclerView.visibility =
                        if (arrowDownImageView.visibility == VISIBLE) VISIBLE else GONE
                }
            }
        } ?: run {
            with(binding) {
                lytLatestOrderLoading.visibility = GONE
                lytLatestOrderNot.visibility = VISIBLE
                lytLatestOrder.visibility = GONE
            }
        }
    }

    private fun showErrorPopup(kamuDaPopup: KamuDaPopup) {
        mainActivity.showErrorPopup(kamuDaPopup).show(childFragmentManager, "custom_dialog")
    }

    private fun showCommonErrorScreen() {
        mainActivity.binding.navView.visibility = GONE
        binding.lytCommonErrorScreenIncluded.visibility = VISIBLE
    }
}