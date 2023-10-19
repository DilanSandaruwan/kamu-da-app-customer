package com.dilan.kamuda.customerapp.views.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dilan.kamuda.customerapp.ActBase.Companion.kamuDaSecurePreference
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.databinding.FragmentViewOrderBinding
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.model.specific.KamuDaPopup
import com.dilan.kamuda.customerapp.util.ResponseHandlingDialogFragment
import com.dilan.kamuda.customerapp.viewmodels.order.ViewOrderViewModel
import com.dilan.kamuda.customerapp.views.activities.main.MainActivity
import com.dilan.kamuda.customerapp.views.adapters.ViewAllOrdersAdapter
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewOrderFragment : Fragment() {

    lateinit var binding: FragmentViewOrderBinding
    private val viewModel: ViewOrderViewModel by viewModels()
    private lateinit var adapter: ViewAllOrdersAdapter
    private lateinit var mainActivity: MainActivity

    override fun onResume() {
        super.onResume()
        callToGetListOfAllOrders()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_order, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewOrderVM = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _layoutManager = LinearLayoutManager(requireContext())
        val _dividerItemDecoration =
            DividerItemDecoration(requireContext(), _layoutManager.orientation)
        adapter = ViewAllOrdersAdapter(object :
            ViewAllOrdersAdapter.OnItemClickListener {

            override fun itemClick(itemId: Int, status: String) {
                viewModel.updateOrderWithStatus(itemId, status)
            }
        }, object : ViewAllOrdersAdapter.OnReorderClickListener {
            override fun reorderClick(item: OrderDetail) {
                goToReorderSelectedOrder(item)
            }

        })

        binding.rvViewOrderDetails.also {
            it.layoutManager = _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
            it.adapter = adapter
        }

        viewModel.ordersList.observe(viewLifecycleOwner) { listOfOrders ->
            if (listOfOrders.isNotEmpty()) {
                binding.tvNoOrdersYet.visibility = GONE
                binding.rvViewOrderDetails.visibility = VISIBLE
                viewModel.ongoingList.value =
                    listOfOrders.filter { it.status == "pending" || it.status == "accepted" }
                viewModel.pastOrdersList.value =
                    listOfOrders.filter { it.status != "pending" && it.status != "accepted" }
                when (viewModel.currentlySelectedGroup) {
                    "ongoing" -> {
                        adapter.submitList(viewModel.ongoingList.value)
                        binding.toggleButton.check(binding.btnOngoingOrders.id)
                    }

                    "past-orders" -> adapter.submitList(viewModel.pastOrdersList.value)
                    else -> adapter.submitList(viewModel.ongoingList.value)
                }
            } else {
                binding.rvViewOrderDetails.visibility = GONE
                binding.tvNoOrdersYet.visibility = VISIBLE
            }
        }

        viewModel.objectHasUpdated.observe(viewLifecycleOwner) {
            if (it != null) {
                val kamuDaPopup = KamuDaPopup(
                    "Success",
                    "Successfully updated the status",
                    "",
                    "Close",
                    1
                )
                showErrorPopup(kamuDaPopup)
                viewModel.getOrdersListOfCustomer(12)
            } else {
                val kamuDaPopup = KamuDaPopup(
                    "Error",
                    "Failed to update the status",
                    "",
                    "Close",
                    2
                )
                showErrorPopup(kamuDaPopup)
            }

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
                kamuDaSecurePreference.setLoadMyOrders(requireContext(), false)
            }
        }

        binding.lytCommonErrorScreenIncluded.findViewById<MaterialButton>(R.id.mbtnCommonErrorScreen)
            .setOnClickListener {
                mainActivity.binding.navView.visibility = VISIBLE
                callToGetListOfAllOrders()
                binding.lytCommonErrorScreenIncluded.visibility = GONE
            }

        binding.toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    binding.btnOngoingOrders.id -> {
                        viewModel.currentlySelectedGroup = "ongoing"
                        setColorAsSelected(binding.btnOngoingOrders)
                        setColorAsDeSelected(binding.btnPastOrders)
                        adapter.submitList(viewModel.ongoingList.value)
                    }

                    binding.btnPastOrders.id -> {
                        viewModel.currentlySelectedGroup = "past-orders"
                        setColorAsSelected(binding.btnPastOrders)
                        setColorAsDeSelected(binding.btnOngoingOrders)
                        adapter.submitList(viewModel.pastOrdersList.value)
                    }
                }
            }
        }
    }

    private fun setColorAsSelected(button: Button) {
        button.setBackgroundColor(resources.getColor(R.color.aqua, resources.newTheme()))
        button.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
    }

    private fun setColorAsDeSelected(button: Button) {
        button.setBackgroundColor(resources.getColor(R.color.white, resources.newTheme()))
        button.setTextColor(resources.getColor(R.color.black, resources.newTheme()))
    }

    private fun showErrorPopup(kamuDaPopup: KamuDaPopup) {
        val dialogFragment = ResponseHandlingDialogFragment.newInstance(
            title = kamuDaPopup.title,
            message = kamuDaPopup.message,
            positiveButtonText = kamuDaPopup.positiveButtonText,
            negativeButtonText = kamuDaPopup.negativeButtonText,
            type = kamuDaPopup.type,
        )
        dialogFragment.show(childFragmentManager, "custom_dialog")
    }

    fun goToReorderSelectedOrder(selectedOrderDetail: OrderDetail) {
        val action = ViewOrderFragmentDirections.actionViewOrderFragment2ToReorderFragment(
            selectedOrderDetail
        )
        view?.findNavController()?.navigate(action)

    }

    private fun showCommonErrorScreen() {
        mainActivity.binding.navView.visibility = GONE
        binding.lytCommonErrorScreenIncluded.visibility = VISIBLE
    }

    private fun callToGetListOfAllOrders() {
        if (kamuDaSecurePreference.isLoadMyOrders(requireContext())) {
            viewModel.getOrdersListOfCustomer(
                kamuDaSecurePreference.getCustomerID(requireContext()).toInt()
            )
        }
    }
}