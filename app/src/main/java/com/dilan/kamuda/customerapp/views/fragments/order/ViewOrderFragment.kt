package com.dilan.kamuda.customerapp.views.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.NO_ID
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.databinding.FragmentViewOrderBinding
import com.dilan.kamuda.customerapp.viewmodels.order.ViewOrderViewModel
import com.dilan.kamuda.customerapp.views.activities.main.MainActivity
import com.dilan.kamuda.customerapp.views.adapters.ViewAllOrdersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewOrderFragment : Fragment() {

    lateinit var binding: FragmentViewOrderBinding
    private val viewModel: ViewOrderViewModel by viewModels()
    private lateinit var adapter: ViewAllOrdersAdapter
    private lateinit var mainActivity: MainActivity


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
                    "ongoing" -> adapter.submitList(viewModel.ongoingList.value)
                    "past-orders" -> adapter.submitList(viewModel.pastOrdersList.value)
                    else -> adapter.submitList(viewModel.ongoingList.value)
                }
            } else {
                binding.rvViewOrderDetails.visibility = GONE
                binding.tvNoOrdersYet.visibility = VISIBLE
            }
        }

        viewModel.objectHasUpdated.observe(viewLifecycleOwner) {
            if (it != null)
                viewModel.getOrdersListOfCustomer(12)
            else
                showErrorPopup()
        }

        viewModel.showLoader.observe(viewLifecycleOwner){
            mainActivity.showProgress(it)
        }

        binding.toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    binding.btnOngoingOrders.id -> {
                        viewModel.currentlySelectedGroup = "ongoing"
                        adapter.submitList(viewModel.ongoingList.value)
                    }

                    binding.btnPastOrders.id -> {
                        viewModel.currentlySelectedGroup = "past-orders"
                        adapter.submitList(viewModel.pastOrdersList.value)
                    }
                }
            } else {
                if (toggleButton.checkedButtonId == NO_ID) {
                    viewModel.currentlySelectedGroup = "ongoing"
                    adapter.submitList(viewModel.ongoingList.value)
                }
            }
        }
        binding.btnOngoingOrders.setOnClickListener {
            viewModel.currentlySelectedGroup = "ongoing"
            adapter.submitList(viewModel.ongoingList.value)
        }

        binding.btnPastOrders.setOnClickListener {
            viewModel.currentlySelectedGroup = "past-orders"
            adapter.submitList(viewModel.pastOrdersList.value)
        }
    }

    //    private fun setOrderDetails(checkedItems: List<OrderItemIntermediate>) {
//        var mutableList = mutableListOf<OrderItem>()
//        val custId = KamuDaSecurePreference().getCustomerID(requireContext()).toInt()
//
//        for (i in checkedItems) {
//            mutableList.add(OrderItem(i.name, i.price, i.quantity, i.status))
//        }
//        val list = mutableList
//        val myOrder =
//            OrderDetail(
//                -1,
//                custId,
//                checkedItems.sumOf { it.price * it.quantity }.toDouble(),
//                getThisDate(),
//                "pending",
//                getThisTime(),
//                list,
//            )
//
//        viewModel.saveData(myOrder)
//    }
//
//    private fun getThisTime(): String {
//        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
//        return sdf.format(Calendar.getInstance().time)
//    }
//
//    private fun getThisDate(): String {
//        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        return sdf.format(Calendar.getInstance().time)
//    }

    private fun showErrorPopup() {
        Toast.makeText(context, "Response is null!", Toast.LENGTH_LONG).show()
    }
}