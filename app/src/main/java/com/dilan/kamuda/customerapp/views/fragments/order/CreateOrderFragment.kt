package com.dilan.kamuda.customerapp.views.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dilan.kamuda.customerapp.databinding.FragmentCreateOrderBinding
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.viewmodels.order.CreateOrderViewModel
import com.dilan.kamuda.customerapp.views.adapters.CreateOrderAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateOrderFragment : Fragment() {

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
        val _dividerItemDecoration = DividerItemDecoration(requireContext(), _layoutManager.orientation)
        adapter = CreateOrderAdapter(object :
            CreateOrderAdapter.OnItemClickListener {

            override fun itemClick(item: FoodMenu) {

            }
        })

        binding.rvMakeOrderItem.also {
            it.layoutManager = _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
            it.adapter = adapter
        }

        viewModel.menuList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

    }

}