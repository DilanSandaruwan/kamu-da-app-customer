package com.dilan.kamuda.customerapp.views.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.databinding.FragmentReorderBinding
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.model.order.OrderItem
import com.dilan.kamuda.customerapp.model.order.OrderItemIntermediate
import com.dilan.kamuda.customerapp.model.specific.KamuDaPopup
import com.dilan.kamuda.customerapp.util.CustomDialogFragment
import com.dilan.kamuda.customerapp.util.KamuDaSecurePreference
import com.dilan.kamuda.customerapp.viewmodels.order.ReorderViewModel
import com.dilan.kamuda.customerapp.views.activities.main.MainActivity
import com.dilan.kamuda.customerapp.views.adapters.ReorderAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ReorderFragment : Fragment(), ReorderAdapter.CheckedItemListener,
    ReorderAdapter.OnItemQuantityChangeListener {

    private val viewModel: ReorderViewModel by viewModels()
    private lateinit var binding: FragmentReorderBinding
    private lateinit var adapter: ReorderAdapter
    private lateinit var mainActivity: MainActivity
    private var selectedOrderDetail: OrderDetail? = null

    // Get arguments using SafeArgs
    private val args: ReorderFragmentArgs by navArgs()

    override fun onStart() {
        super.onStart()
        selectedOrderDetail = args.selectedOrderDetail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReorderBinding.inflate(layoutInflater, container, false)
        //viewModel = ViewModelProvider(requireActivity())[ReorderViewModel::class.java]
        binding.reorderVM = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _layoutManager = LinearLayoutManager(requireContext())
        val _dividerItemDecoration =
            DividerItemDecoration(requireContext(), _layoutManager.orientation)
        adapter = ReorderAdapter(this, object :
            ReorderAdapter.OnItemClickListener {

            override fun itemClick(item: OrderItemIntermediate) {

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
                title = "Re-Order Confirmation",
                message = "Please press Confirm if you sure to confirm the order.",
                positiveButtonText = "Confirm",
                negativeButtonText = "Cancel",
                checkedItems = checkedItems
            )
            dialogFragment.setPositiveActionListener { setOrderDetails(checkedItems) }
            dialogFragment.show(childFragmentManager, "custom_dialog")

        }

        binding.btnArrowBack.setOnClickListener {
            goToViewOrderFromReorder()
        }

        viewModel.menuList.observe(viewLifecycleOwner) { latestList ->
            if (selectedOrderDetail != null) {
                adapter.submitList(
                    updateQuantityInOriginalItemsList(
                        selectedOrderDetail!!.items,
                        latestList
                    )
                )
            }
        }

        viewModel.checkedItems.observe(viewLifecycleOwner) { it ->
            if (it.size > 0) {
                binding.tvTotal.text = it.sumOf { it.price * it.quantity }.toString()
                binding.btnPlaceOrder.isEnabled = !it.any { it.quantity == 0 }
            } else {
                binding.tvTotal.text = "0.00"
                binding.btnPlaceOrder.isEnabled = false
            }
            adapter.setCheckedItems(it)
        }

        viewModel.emptyOrder.observe(viewLifecycleOwner) {
            binding.btnPlaceOrder.isEnabled = !it
        }

        viewModel.resetList.observe(viewLifecycleOwner) {
            if (it) {
                resetOrder()
            }
        }

        viewModel.savedSuccessfully.observe(viewLifecycleOwner) {
            if (it) {
                CreateOrderFragment.kamuDaSecurePreference.setLoadMenuForOrders(
                    requireContext(),
                    true
                )
                CreateOrderFragment.kamuDaSecurePreference.setLoadMyOrders(requireContext(), true)
                val kamuDaPopup = KamuDaPopup(
                    "Success",
                    "Successfully saved the order",
                    "",
                    "Close",
                    1
                )
                val dialogFragment = mainActivity.showErrorPopup(kamuDaPopup).apply {
                    setNegativeActionListener {
                        goToViewOrderFromReorder()
                    }
                }
                dialogFragment.show(childFragmentManager, "custom_dialog")
            }
        }

        viewModel.showErrorPopup.observe(viewLifecycleOwner) {
            showErrorPopup(it)
        }

        viewModel.showLoader.observe(viewLifecycleOwner) {
            mainActivity.showProgress(it)
        }

    }

    fun updateQuantityInOriginalItemsList(
        selectedItemsList: List<OrderItem>,
        originalItemsList: List<OrderItemIntermediate>
    ): List<OrderItemIntermediate> {

        var updatedItemsList = emptyList<OrderItemIntermediate>().toMutableList()

        for (selectedItem in selectedItemsList) {
            for (item in originalItemsList) {
                if (item.name == selectedItem.name) {
                    updatedItemsList.add(item)
                }
            }
        }
        selectedOrderDetail = null
        return updatedItemsList
    }

    override fun onItemChecked(item: OrderItemIntermediate, isChecked: Boolean) {
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

    private fun setOrderDetails(checkedItems: List<OrderItemIntermediate>) {
        var mutableList = mutableListOf<OrderItem>()
        val custId = kamuDaSecurePreference.getCustomerID(requireContext()).toInt()

        for (i in checkedItems) {
            mutableList.add(OrderItem(i.name, i.price, i.quantity))
        }
        val list = mutableList
        val myOrder =
            OrderDetail(
                -1,
                custId,
                checkedItems.sumOf { it.price * it.quantity }.toDouble(),
                getThisDate(),
                "pending",
                getThisTime(),
                list,
            )

        viewModel.saveData(myOrder)
    }

    private fun getThisTime(): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Calendar.getInstance().time)
    }

    private fun getThisDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Calendar.getInstance().time)
    }

    private fun resetOrder() {
        viewModel.setCheckedItemsList(mutableListOf())
    }

    private fun showErrorPopup(kamuDaPopup: KamuDaPopup) {
        mainActivity.showErrorPopup(kamuDaPopup).show(childFragmentManager, "custom_dialog")
    }

    fun goToViewOrderFromReorder() {
        ViewOrderFragment.kamuDaSecurePreference.setLoadMyOrders(requireContext(),true)
        val action = ReorderFragmentDirections.actionReorderFragmentToViewOrderFragment2()
        view?.findNavController()?.popBackStack(R.id.reorderFragment, false)
        view?.findNavController()?.navigate(action)

    }

    companion object {
        var kamuDaSecurePreference = KamuDaSecurePreference()
    }
}