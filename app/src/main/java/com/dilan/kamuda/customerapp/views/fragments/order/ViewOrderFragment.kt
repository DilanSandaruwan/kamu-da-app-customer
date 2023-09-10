package com.dilan.kamuda.customerapp.views.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.databinding.FragmentViewOrderBinding
import com.dilan.kamuda.customerapp.model.order.OrderDetail
import com.dilan.kamuda.customerapp.viewmodels.order.ViewOrderViewModel
import com.dilan.kamuda.customerapp.views.adapters.ViewAllOrdersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewOrderFragment : Fragment() {

    lateinit var binding: FragmentViewOrderBinding
    private val viewModel: ViewOrderViewModel by viewModels()
    private lateinit var adapter: ViewAllOrdersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

            override fun itemClick(item: OrderDetail) {

            }
        })

        binding.rvViewOrderDetails.also {
            it.layoutManager = _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
            it.adapter = adapter
        }

        viewModel.ordersList.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()){
                binding.tvNoOrdersYet.visibility = GONE
                binding.rvViewOrderDetails.visibility = VISIBLE
                adapter.submitList(it)
            } else {
                binding.rvViewOrderDetails.visibility = GONE
                binding.tvNoOrdersYet.visibility = VISIBLE
            }
        }
    }
}