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
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.model.order.OrderItem
import com.dilan.kamuda.customerapp.util.CustomDialogFragment
import com.dilan.kamuda.customerapp.viewmodels.order.CreateOrderViewModel
import com.dilan.kamuda.customerapp.views.adapters.CreateOrderAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class CreateOrderFragment : Fragment(), CreateOrderAdapter.CheckedItemListener {

    private lateinit var viewModel: CreateOrderViewModel
    private lateinit var binding: FragmentCreateOrderBinding
    private lateinit var adapter: CreateOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

            override fun itemClick(item: FoodMenu) {

            }
        }, this)

        binding.rvMakeOrderItem.also {
            it.layoutManager = _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
            it.adapter = adapter
        }

        binding.btnPlaceOrder.setOnClickListener {
            val checkedItems = adapter.getCheckedItemsList()
            val dialogFragment = CustomDialogFragment.newInstance(
                title = "Custom Dialog",
                message = "This is a custom dialog with checked items.",
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
            //binding.tvTotal.text = it.sumOf { it.price * it.itemCount }.toString()
            adapter.setCheckedItems(it)
        }

        viewModel.emptyOrder.observe(viewLifecycleOwner) {
            binding.btnPlaceOrder.isEnabled = !it
        }

    }

    override fun onItemChecked(item: FoodMenu, isChecked: Boolean) {
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

    private fun setOrderDetails(checkedItems: List<FoodMenu>) {
        var mutableList = mutableListOf<OrderItem>()
        for (i in checkedItems) {
            mutableList.add(OrderItem(i.id.toInt(), i.name, i.price, i.itemCount))
        }
        val list = mutableList
        val myOrder = OrderDetail(
            0,
            Date().toString(),
            "BREAKFAST",
            "PENDING",
            checkedItems.sumOf { it.price * it.itemCount }.toDouble(),
            list,
        )

        viewModel.saveData(myOrder)
    }

}