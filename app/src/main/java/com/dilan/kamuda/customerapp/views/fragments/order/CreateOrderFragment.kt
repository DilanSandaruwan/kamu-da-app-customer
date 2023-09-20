package com.dilan.kamuda.customerapp.views.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dilan.kamuda.customerapp.databinding.FragmentCreateOrderBinding
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.model.order.OrderItem
import com.dilan.kamuda.customerapp.util.CustomDialogFragment
import com.dilan.kamuda.customerapp.viewmodels.order.CreateOrderViewModel
import com.dilan.kamuda.customerapp.views.adapters.CreateOrderAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class CreateOrderFragment : Fragment(), CreateOrderAdapter.CheckedItemListener,
    CreateOrderAdapter.OnItemQuantityChangeListener {

    private lateinit var viewModel: CreateOrderViewModel
    private lateinit var binding: FragmentCreateOrderBinding
    private lateinit var adapter: CreateOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCreateOrderBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[CreateOrderViewModel::class.java]
        binding.createOrderVM = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _layoutManager = LinearLayoutManager(requireContext())
        val _dividerItemDecoration =
            DividerItemDecoration(requireContext(), _layoutManager.orientation)
        adapter = CreateOrderAdapter(object :
            CreateOrderAdapter.OnItemClickListener {

            override fun itemClick(item: OrderItem) {

            }
        }, this, this)

        binding.rvMakeOrderItem.also {
            it.layoutManager = _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
            it.adapter = adapter
        }

        binding.btnPlaceOrder.setOnClickListener {
            val checkedItems = adapter.getCheckedItemsList()

            val dialogFragment = CustomDialogFragment.newInstance(
                title = "Order Confirmation",
                message = "Please press Confirm if you sure to confirm the order.",
                positiveButtonText = "Confirm",
                negativeButtonText = "Cancel",
                checkedItems = checkedItems
            )
            dialogFragment.setPositiveActionListener { setOrderDetails(checkedItems) }
            dialogFragment.show(childFragmentManager, "custom_dialog")

        }

        viewModel.menuList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.checkedItems.observe(viewLifecycleOwner) { it ->
            if (it.size > 0) {
                binding.tvTotal.text = it.sumOf { it.price * it.quantity }.toString()
            } else {
                binding.tvTotal.text = "0.00"
            }
            binding.btnPlaceOrder.isEnabled = !it.any { it.quantity == 0 }
            adapter.setCheckedItems(it)
        }

        viewModel.emptyOrder.observe(viewLifecycleOwner) {
            binding.btnPlaceOrder.isEnabled = !it
        }

        viewModel.resetList.observe(viewLifecycleOwner){
            resetOrder()
        }

    }

    override fun onItemChecked(item: OrderItem, isChecked: Boolean) {
        val updatedCheckedItems = viewModel.checkedItems.value?.toMutableList() ?: mutableListOf()
        if (isChecked) {
            if (!updatedCheckedItems.contains(item)) {
                updatedCheckedItems.add(item)
            }
        } else {
            updatedCheckedItems.remove(item)
        }
        viewModel.setCheckedItemsList(updatedCheckedItems)
    }

    override fun onItemQuantityChanged(isChanged: Boolean) {
        if (isChanged) {
            val it = viewModel.checkedItems.value?.toMutableList() ?: mutableListOf()
            if (it.size > 0) {
                binding.tvTotal.text = it.sumOf { it.price * it.quantity }.toString()
                binding.btnPlaceOrder.isEnabled = !it.any { it.quantity == 0 }
            } else {
                binding.tvTotal.text = "0.00"
            }
        }
    }

    private fun setOrderDetails(checkedItems: List<OrderItem>) {
        var mutableList = mutableListOf<OrderItem>()
        for (i in checkedItems) {
            mutableList.add(OrderItem(i.name, i.price, i.quantity))
        }
        val list = mutableList
        val myOrder =
            OrderDetail(
                -1,
                12,
                checkedItems.sumOf { it.price * it.quantity }.toDouble(),
                "2023-09-16",
                "pending",
                getThisTime(),
                list,
            )

        viewModel.saveData(myOrder)
    }

    private fun getThisTime():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDateAndTime = sdf.format(Calendar.getInstance().time)
        return currentDateAndTime
    }

    private fun resetOrder(){
        viewModel.setCheckedItemsList(mutableListOf())
        //viewModel.getMenuListForMeal("breakfast")
    }

}