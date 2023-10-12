package com.dilan.kamuda.customerapp.views.fragments.foodhouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.dilan.kamuda.customerapp.R
import com.dilan.kamuda.customerapp.databinding.FragmentCreateOrderBinding
import com.dilan.kamuda.customerapp.databinding.FragmentFoodHouseBinding
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.model.order.OrderItemIntermediate
import com.dilan.kamuda.customerapp.viewmodels.foodhouse.FoodHouseViewModel
import com.dilan.kamuda.customerapp.viewmodels.order.CreateOrderViewModel
import com.dilan.kamuda.customerapp.views.adapters.CreateOrderAdapter
import com.dilan.kamuda.customerapp.views.adapters.ViewAllMealsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodHouseFragment : Fragment() {

    private lateinit var viewModel: FoodHouseViewModel
    private lateinit var binding: FragmentFoodHouseBinding
    private lateinit var adapter: ViewAllMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        val _layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        adapter = ViewAllMealsAdapter(this, object :
            ViewAllMealsAdapter.OnItemClickListener {

            override fun itemClick(item: FoodMenu) {

            }
        })

        binding.rvMeals.also {
            it.layoutManager = _layoutManager
            it.adapter = adapter
        }

        viewModel.menuList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}